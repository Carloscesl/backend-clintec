package com.terreneitors.backendclintec.alerts.application.service;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import com.terreneitors.backendclintec.clients.application.port.out.ClientRepositoryPort;
import com.terreneitors.backendclintec.opportunities.application.port.out.OpportunityRepositoryPort;
import com.terreneitors.backendclintec.opportunities.domain.Opportunity;
import com.terreneitors.backendclintec.opportunities.domain.StatusOpportunity;
import com.terreneitors.backendclintec.shared.email.EmailPort;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.Rol;
import com.terreneitors.backendclintec.users.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertSchedulerService {

    private final AlertRepositoryPort alertRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final OpportunityRepositoryPort opportunityRepositoryPort;
    private final ClientRepositoryPort clientRepositoryPort;
    private final EmailPort emailPort;

    private static final int DIAS_INACTIVIDAD = 15;

    // ── Corre todos los días a las 8:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void ejecutarTodasLasAlertas() {
        log.info("[SCHEDULER_INICIO] Ejecutando verificación de alertas automáticas");
        verificarInactividad();
        verificarOportunidadesVencidas();
        verificarOportunidadesEnNegociacion();
        verificarClientesSinOportunidades();
        log.info("[SCHEDULER_FIN] Verificación de alertas completada");
    }

    // ── 1. INACTIVIDAD → solo al asesor───
    private void verificarInactividad() {
        log.info("[SCHEDULER_INACTIVIDAD] Verificando clientes inactivos");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesInactivos = alertRepositoryPort
                .findClientesSinInteraccionesDesdeFecha(hace15Dias);

        for (Long clienteId : clientesInactivos) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.INACTIVIDAD)) continue;

            Long asesorId = obtenerAsesorDelCliente(clienteId);

            Alert alerta = construirAlerta(clienteId, asesorId,
                    TypeAlert.INACTIVIDAD,
                    "El cliente no ha tenido interacciones en los últimos "
                            + DIAS_INACTIVIDAD + " días.",
                    LocalDateTime.now().plusDays(3));

            Alert guardada = alertRepositoryPort.save(alerta);

            Map<String, Object> datos = construirDatosBase(clienteId);
            datos.put("diasSinContacto", DIAS_INACTIVIDAD);
            agregarDatosOportunidadActiva(clienteId, datos);

            enviarConPlantilla(guardada, asesorId, datos);

            log.info("[ALERTA_INACTIVIDAD_CREADA] clienteId={} | asesorId={}",
                    clienteId, asesorId);
        }
    }

    // ── 2. VENCIMIENTO → asesor + gerentes
    private void verificarOportunidadesVencidas() {
        log.info("[SCHEDULER_VENCIMIENTO] Verificando oportunidades vencidas");

        List<Long> clientes = alertRepositoryPort
                .findOportunidadesVencidasActivas(LocalDate.now());

        for (Long clienteId : clientes) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.VENCIMIENTO)) continue;

            Long asesorId = obtenerAsesorDelCliente(clienteId);

            Alert alerta = construirAlerta(clienteId, asesorId,
                    TypeAlert.VENCIMIENTO,
                    "Existe una oportunidad cuya fecha de cierre estimada "
                            + "ya venció y aún está activa.",
                    LocalDateTime.now().plusDays(2));

            Alert guardada = alertRepositoryPort.save(alerta);

            Map<String, Object> datos = construirDatosBase(clienteId);
            agregarDatosOportunidadVencida(clienteId, datos);

            // Al asesor y a todos los gerentes
            enviarConPlantilla(guardada, asesorId, datos);
            notificarGerentes(guardada, datos);

            log.info("[ALERTA_VENCIMIENTO_CREADA] clienteId={} | asesorId={}",
                    clienteId, asesorId);
        }
    }

    // ── 3. SEGUIMIENTO → asesor + gerentes
    private void verificarOportunidadesEnNegociacion() {
        log.info("[SCHEDULER_SEGUIMIENTO] Verificando oportunidades estancadas");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientes = alertRepositoryPort
                .findOportunidadesEnNegociacionSinCambios(hace15Dias);

        for (Long clienteId : clientes) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.SEGUIMIENTO)) continue;

            Long asesorId = obtenerAsesorDelCliente(clienteId);

            Alert alerta = construirAlerta(clienteId, asesorId,
                    TypeAlert.SEGUIMIENTO,
                    "Una oportunidad lleva más de " + DIAS_INACTIVIDAD
                            + " días en etapa NEGOCIACIÓN sin actualizaciones.",
                    LocalDateTime.now().plusDays(3));

            Alert guardada = alertRepositoryPort.save(alerta);

            Map<String, Object> datos = construirDatosBase(clienteId);
            datos.put("diasSinCambios", DIAS_INACTIVIDAD);
            agregarDatosOportunidadActiva(clienteId, datos);

            // Al asesor y a todos los gerentes
            enviarConPlantilla(guardada, asesorId, datos);
            notificarGerentes(guardada, datos);

            log.info("[ALERTA_SEGUIMIENTO_CREADA] clienteId={} | asesorId={}",
                    clienteId, asesorId);
        }
    }

    // ── 4. OPORTUNIDAD → solo administradores ────────────────────
    private void verificarClientesSinOportunidades() {
        log.info("[SCHEDULER_OPORTUNIDAD] Verificando clientes sin oportunidades");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientes = alertRepositoryPort
                .findClientesSinOportunidadesDesdeFecha(hace15Dias);

        for (Long clienteId : clientes) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.OPORTUNIDAD)) continue;

            Long adminId = obtenerAdministrador();

            Alert alerta = construirAlerta(clienteId, adminId,
                    TypeAlert.OPORTUNIDAD,
                    "El cliente no tiene oportunidades activas asignadas "
                            + "en los últimos " + DIAS_INACTIVIDAD + " días.",
                    LocalDateTime.now().plusDays(5));

            Alert guardada = alertRepositoryPort.save(alerta);

            Map<String, Object> datos = construirDatosBase(clienteId);
            datos.put("diasSinOportunidad", DIAS_INACTIVIDAD);
            datos.put("clasificacion", "FRIO");

            // Solo a los administradores
            notificarAdministradores(guardada, datos);

            log.info("[ALERTA_OPORTUNIDAD_CREADA] clienteId={} | adminId={}",
                    clienteId, adminId);
        }
    }

    // ── Métodos de envío por rol──────────

    private void enviarConPlantilla(Alert alerta, Long usuarioId,
                                    Map<String, Object> datos) {
        userRepositoryPort.findById(usuarioId).ifPresent(usuario ->
                emailPort.enviarAlertaConPlantilla(
                        usuario.getEmail(),
                        usuario.getNombreUser(),
                        alerta,
                        datos
                )
        );
    }

    private void notificarGerentes(Alert alerta, Map<String, Object> datos) {
        userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol() == Rol.GERENTE && u.getActivo())
                .forEach(gerente -> emailPort.enviarAlertaConPlantilla(
                        gerente.getEmail(),
                        gerente.getNombreUser(),
                        alerta,
                        datos
                ));
    }

    private void notificarAdministradores(Alert alerta, Map<String, Object> datos) {
        userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol() == Rol.ADMINISTRADOR && u.getActivo())
                .forEach(admin -> emailPort.enviarAlertaConPlantilla(
                        admin.getEmail(),
                        admin.getNombreUser(),
                        alerta,
                        datos
                ));
    }

    private Map<String, Object> construirDatosBase(Long clienteId) {
        Map<String, Object> datos = new HashMap<>();
        clientRepositoryPort.findById(clienteId).ifPresent(cliente -> {
            datos.put("nombreCliente", cliente.getNombreCliente());
            datos.put("empresa",       cliente.getEmpresa());
            datos.put("emailCliente",  cliente.getEmail());
            datos.put("telefono",      cliente.getTelefono());
        });
        return datos;
    }

    private void agregarDatosOportunidadActiva(Long clienteId,
                                               Map<String, Object> datos) {
        opportunityRepositoryPort.findByIdClient(clienteId).stream()
                .filter(o -> o.getEstado() == StatusOpportunity.ACTIVA)
                .max(Comparator.comparing(Opportunity::getFechaCreacion))
                .ifPresent(o -> {
                    datos.put("descripcionOportunidad",
                            o.getDescripcion() != null ? o.getDescripcion() : "Sin descripción");
                    datos.put("valorEstimado",       o.getValorEstimado());
                    datos.put("fechaCierreEstimada", o.getFechaEstimadaCierre());
                });
    }

    private void agregarDatosOportunidadVencida(Long clienteId,
                                                Map<String, Object> datos) {
        opportunityRepositoryPort.findByIdClient(clienteId).stream()
                .filter(o -> o.getEstado() == StatusOpportunity.ACTIVA
                        && o.getFechaEstimadaCierre() != null
                        && o.getFechaEstimadaCierre()
                        .isBefore(LocalDate.now()))
                .findFirst()
                .ifPresent(o -> {
                    datos.put("descripcionOportunidad",
                            o.getDescripcion() != null ? o.getDescripcion() : "Sin descripción");
                    datos.put("valorEstimado",       o.getValorEstimado());
                    datos.put("fechaCierreEstimada", o.getFechaEstimadaCierre());
                    datos.put("diasVencida",
                            ChronoUnit.DAYS.between(
                                    o.getFechaEstimadaCierre(), LocalDate.now()));
                });
    }

    // ── Construcción de alerta────────────

    private Long obtenerAsesorDelCliente(Long clienteId) {
        return opportunityRepositoryPort
                .findByIdClient(clienteId)
                .stream()
                .filter(o -> o.getEstado() == StatusOpportunity.ACTIVA)
                .max(Comparator.comparing(Opportunity::getFechaCreacion))
                .map(Opportunity::getAsesorId)
                .orElseGet(this::obtenerAdministrador);
    }

    private Long obtenerAdministrador() {
        return userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol() == Rol.ADMINISTRADOR && u.getActivo())
                .findFirst()
                .map(User::getId)
                .orElse(1L);
    }

    private Alert construirAlerta(Long clienteId, Long usuarioId,
                                  TypeAlert tipo, String descripcion,
                                  LocalDateTime fechaVencimiento) {
        Alert alerta = new Alert();
        alerta.setClienteId(clienteId);
        alerta.setUsuarioId(usuarioId);
        alerta.setTipo(tipo);
        alerta.setDescripcion(descripcion);
        alerta.setFechaVencimiento(fechaVencimiento);
        return alerta;
    }
}
