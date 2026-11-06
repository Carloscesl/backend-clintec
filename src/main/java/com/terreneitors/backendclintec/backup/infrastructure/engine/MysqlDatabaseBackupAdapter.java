package com.terreneitors.backendclintec.backup.infrastructure.engine;

import com.terreneitors.backendclintec.backup.application.port.out.DatabaseBackupPort;
import com.terreneitors.backendclintec.backup.domain.BackupException;
import com.terreneitors.backendclintec.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;

/**
 * Backup/restauración de MySQL usando solo JDBC.
 *
 * Se evita depender de `mysqldump` porque la imagen de runtime del backend
 * (eclipse-temurin JRE) no lo trae. El archivo generado es un .sql de texto donde
 * CADA sentencia ocupa exactamente una línea (los saltos de línea dentro de los
 * valores se escapan), lo que hace trivial restaurarlo línea por línea.
 */
@Slf4j
@Component
public class MysqlDatabaseBackupAdapter implements DatabaseBackupPort {

    static final String HEADER = "-- CLINTEC BACKUP v1";

    // El historial de backups no entra en el volcado ni se restaura: si se restaurara,
    // se perderían los registros de los backups creados después del que se restaura.
    private static final Set<String> EXCLUDED_TABLES = Set.of("backup_historial");

    private static final int ROWS_PER_INSERT = 100;

    private final DataSource dataSource;
    private final Path directory;

    public MysqlDatabaseBackupAdapter(DataSource dataSource,
                                      @Value("${app.backup.directory:backups}") String directory) {
        this.dataSource = dataSource;
        this.directory = Path.of(directory).toAbsolutePath().normalize();
    }

    // ── Puerto ───────────────────────────────────────────────────

    @Override
    public long createDump(String fileName) {
        Path target = resolve(fileName);
        try {
            Files.createDirectories(directory);
            try (Connection conn = dataSource.getConnection();
                 BufferedWriter out = Files.newBufferedWriter(
                         target, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW)) {
                try {
                    writeDump(conn, out);
                } finally {
                    conn.setAutoCommit(true);
                }
            }
            return Files.size(target);
        } catch (IOException | SQLException e) {
            deleteQuietly(target);
            throw new BackupException("No se pudo generar el backup de la base de datos", e);
        }
    }

    @Override
    public void restore(String fileName) {
        Path source = resolve(fileName);
        if (!Files.isRegularFile(source)) {
            throw new BackupException("El archivo de backup no existe: " + fileName);
        }

        try (Connection conn = dataSource.getConnection();
             BufferedReader in = Files.newBufferedReader(source, StandardCharsets.UTF_8);
             Statement st = conn.createStatement()) {

            String first = in.readLine();
            if (first == null || !first.startsWith(HEADER)) {
                throw new BackupException("El archivo no es un backup válido de Clintec");
            }

            conn.setAutoCommit(true);
            int executed = 0;
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    String sql = line.trim();
                    if (sql.isEmpty() || sql.startsWith("--")) {
                        continue;
                    }
                    // Todas las sentencias del archivo terminan en ';'
                    st.execute(sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql);
                    executed++;
                }
            } finally {
                // La conexión vuelve al pool: nunca debe quedar con las FK desactivadas
                try {
                    st.execute("SET FOREIGN_KEY_CHECKS=1");
                } catch (SQLException ex) {
                    log.error("[BACKUP_RESTAURAR] no se pudo reactivar FOREIGN_KEY_CHECKS", ex);
                }
            }
            log.info("[BACKUP_RESTAURAR] {} sentencias ejecutadas desde {}", executed, fileName);

        } catch (IOException | SQLException e) {
            throw new BackupException(
                    "Falló la restauración. La base de datos puede estar incompleta: restaura el backup de seguridad más reciente", e);
        }
    }

    @Override
    public boolean exists(String fileName) {
        return Files.isRegularFile(resolve(fileName));
    }

    @Override
    public Resource load(String fileName) {
        Path file = resolve(fileName);
        if (!Files.isRegularFile(file)) {
            throw new ResourceNotFoundException("Archivo de backup", "nombre", fileName);
        }
        return new FileSystemResource(file);
    }

    // ── Volcado ──────────────────────────────────────────────────

    private void writeDump(Connection conn, BufferedWriter out) throws SQLException, IOException {
        // Una sola transacción => todas las tablas se leen con el mismo "snapshot" consistente
        conn.setAutoCommit(false);

        List<String> tables = listTables(conn);

        writeLine(out, HEADER);
        writeLine(out, "-- Generado: " + LocalDateTime.now());
        writeLine(out, "-- Base de datos: " + conn.getCatalog());
        writeLine(out, "-- Tablas: " + tables.size());
        writeLine(out, "SET NAMES utf8mb4;");
        writeLine(out, "SET FOREIGN_KEY_CHECKS=0;");

        for (String table : tables) {
            dumpTable(conn, table, out);
        }

        writeLine(out, "SET FOREIGN_KEY_CHECKS=1;");
        conn.rollback(); // solo lecturas; cierra la transacción
    }

    private List<String> listTables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SHOW FULL TABLES WHERE Table_type = 'BASE TABLE'")) {
            while (rs.next()) {
                String name = rs.getString(1);
                if (!EXCLUDED_TABLES.contains(name.toLowerCase())) {
                    tables.add(name);
                }
            }
        }
        return tables;
    }

    private void dumpTable(Connection conn, String table, BufferedWriter out) throws SQLException, IOException {
        String quoted = quoteIdentifier(table);

        writeLine(out, "-- Tabla: " + table);
        writeLine(out, "DROP TABLE IF EXISTS " + quoted + ";");

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SHOW CREATE TABLE " + quoted)) {
            if (rs.next()) {
                // Una sentencia = una línea
                String ddl = rs.getString(2).replace("\r", " ").replace("\n", " ");
                writeLine(out, ddl + ";");
            }
        }

        try (Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            st.setFetchSize(Integer.MIN_VALUE); // streaming: no carga la tabla completa en memoria
            try (ResultSet rs = st.executeQuery("SELECT * FROM " + quoted)) {
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                String prefix = buildInsertPrefix(quoted, meta, cols);

                int rowsInStatement = 0;
                while (rs.next()) {
                    out.write(rowsInStatement == 0 ? prefix : ",");
                    out.write(formatRow(rs, meta, cols));
                    if (++rowsInStatement == ROWS_PER_INSERT) {
                        out.write(";");
                        out.newLine();
                        rowsInStatement = 0;
                    }
                }
                if (rowsInStatement > 0) {
                    out.write(";");
                    out.newLine();
                }
            }
        }
    }

    private String buildInsertPrefix(String quotedTable, ResultSetMetaData meta, int cols) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(quotedTable).append(" (");
        for (int i = 1; i <= cols; i++) {
            if (i > 1) {
                sb.append(',');
            }
            sb.append(quoteIdentifier(meta.getColumnName(i)));
        }
        return sb.append(") VALUES ").toString();
    }

    private String formatRow(ResultSet rs, ResultSetMetaData meta, int cols) throws SQLException {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 1; i <= cols; i++) {
            if (i > 1) {
                sb.append(',');
            }
            sb.append(formatValue(rs, meta.getColumnType(i), i));
        }
        return sb.append(')').toString();
    }

    private String formatValue(ResultSet rs, int sqlType, int index) throws SQLException {
        switch (sqlType) {
            case Types.BIT, Types.BOOLEAN -> {
                // Hibernate mapea Boolean a bit(1) / tinyint(1)
                boolean value = rs.getBoolean(index);
                return rs.wasNull() ? "NULL" : (value ? "1" : "0");
            }
            case Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT,
                 Types.DECIMAL, Types.NUMERIC, Types.FLOAT, Types.REAL, Types.DOUBLE -> {
                String value = rs.getString(index);
                return value == null ? "NULL" : value;
            }
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> {
                byte[] value = rs.getBytes(index);
                if (value == null) {
                    return "NULL";
                }
                return value.length == 0 ? "''" : "0x" + HexFormat.of().formatHex(value);
            }
            default -> {
                // Textos y fechas (con Statement el driver devuelve la fecha tal cual está en la BD)
                String value = rs.getString(index);
                return value == null ? "NULL" : quoteString(value);
            }
        }
    }

    // ── Utilidades ───────────────────────────────────────────────

    private static String quoteIdentifier(String name) {
        return "`" + name.replace("`", "``") + "`";
    }

    private static String quoteString(String value) {
        StringBuilder sb = new StringBuilder(value.length() + 2).append('\'');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\\' -> sb.append("\\\\");
                case '\'' -> sb.append("\\'");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\0' -> sb.append("\\0");
                case '\u001a' -> sb.append("\\Z");
                default -> sb.append(c);
            }
        }
        return sb.append('\'').toString();
    }

    private static void writeLine(BufferedWriter out, String line) throws IOException {
        out.write(line);
        out.newLine();
    }

    /** Evita path traversal: el archivo siempre debe quedar directamente dentro del directorio de backups. */
    private Path resolve(String fileName) {
        Path file = directory.resolve(fileName).normalize();
        if (!file.startsWith(directory) || !file.getFileName().toString().equals(fileName)) {
            throw new IllegalArgumentException("Nombre de archivo de backup inválido");
        }
        return file;
    }

    private void deleteQuietly(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.warn("[BACKUP] no se pudo borrar el archivo incompleto {}", file, e);
        }
    }
}
