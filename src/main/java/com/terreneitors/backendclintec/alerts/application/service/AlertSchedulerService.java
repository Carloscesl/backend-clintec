package com.terreneitors.backendclintec.alerts.application.service;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertSchedulerService {

    private final AlertRepositoryPort alertRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final OpportunityRepositoryPort opportunityRepositoryPort;
    private final EmailPort emailPort;

    private static final int DIAS_INACTIVIDAD = 15;

    // ── Corre todos los días a las 8:00 AM ────────────────────────
    @Scheduled(cron = "0 0 8 * * *")
    public void ejecutarTodasLasAlertas() {
        log.info("[SCHEDULER_INICIO] Ejecutando verificación de alertas automáticas");
        verificarInactividad();
        verificarOportunidadesVencidas();
        verificarOportunidadesEnNegociacion();
        verificarClientesSinOportunidades();
        log.info("[SCHEDULER_FIN] Verificación de alertas completada");
    }

    // ── 1. INACTIVIDAD → solo al asesor ───────────────────────────
    private void verificarInactividad() {
        log.info("[SCHEDULER_INACTIVIDAD] Verificando clientes inactivos");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesInactivos = alertRepositoryPort
                .findClientesSinInteraccionesDesdeFecha(hace15Dias);

        for (Long clienteId : clientesInactivos) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.INACTIVIDAD)) continue;

            Long asesorId = obtenerAsesorDelCliente(clienteId);

            Alert alerta = construirAlerta(
                    clienteId,
                    asesorId,
                    TypeAlert.INACTIVIDAD,
                    "El cliente no ha tenido interacciones en los últimos "
                            + DIAS_INACTIVIDAD + " días.",
                    LocalDateTime.now().plusDays(3)
            );

            Alert guardada = alertRepositoryPort.save(alerta);

            // Solo al asesor
            enviarAUsuario(guardada, asesorId);

            log.info("[ALERTA_INACTIVIDAD_CREADA] clienteId={} | asesorId={}",
                    clienteId, asesorId);
        }
    }

    // ── 2. VENCIMIENTO → asesor + gerentes ────────────────────────
    private void verificarOportunidadesVencidas() {
        log.info("[SCHEDULER_VENCIMIENTO] Verificando oportunidades vencidas");

        List<Long> clientesConOportunidadVencida = alertRepositoryPort
                .findOportunidadesVencidasActivas(LocalDateTime.now());

        for (Long clienteId : clientesConOportunidadVencida) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.VENCIMIENTO)) continue;

            Long asesorId = obtenerAsesorDelCliente(clienteId);

            Alert alerta = construirAlerta(
                    clienteId,
                    asesorId,
                    TypeAlert.VENCIMIENTO,
                    "Existe una oportunidad cuya fecha de cierre estimada "
                            + "ya venció y aún está activa.",
                    LocalDateTime.now().plusDays(2)
            );

            Alert guardada = alertRepositoryPort.save(alerta);

            // Al asesor y a todos los gerentes
            enviarAUsuario(guardada, asesorId);
            notificarGerentes(guardada);

            log.info("[ALERTA_VENCIMIENTO_CREADA] clienteId={} | asesorId={}",
                    clienteId, asesorId);
        }
    }

    // ── 3. SEGUIMIENTO → asesor + gerentes ────────────────────────
    private void verificarOportunidadesEnNegociacion() {
        log.info("[SCHEDULER_SEGUIMIENTO] Verificando oportunidades estancadas");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesEstancados = alertRepositoryPort
                .findOportunidadesEnNegociacionSinCambios(hace15Dias);

        for (Long clienteId : clientesEstancados) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.SEGUIMIENTO)) continue;

            Long asesorId = obtenerAsesorDelCliente(clienteId);

            Alert alerta = construirAlerta(
                    clienteId,
                    asesorId,
                    TypeAlert.SEGUIMIENTO,
                    "Una oportunidad lleva más de " + DIAS_INACTIVIDAD
                            + " días en etapa NEGOCIACIÓN sin actualizaciones.",
                    LocalDateTime.now().plusDays(3)
            );

            Alert guardada = alertRepositoryPort.save(alerta);

            // Al asesor y a todos los gerentes
            enviarAUsuario(guardada, asesorId);
            notificarGerentes(guardada);

            log.info("[ALERTA_SEGUIMIENTO_CREADA] clienteId={} | asesorId={}",
                    clienteId, asesorId);
        }
    }

    // ── 4. OPORTUNIDAD → solo administradores ────────────────────
    private void verificarClientesSinOportunidades() {
        log.info("[SCHEDULER_OPORTUNIDAD] Verificando clientes sin oportunidades");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesSinOportunidad = alertRepositoryPort
                .findClientesSinOportunidadesDesdeFecha(hace15Dias);

        for (Long clienteId : clientesSinOportunidad) {
            if (alertRepositoryPort.existeAlertPendiente(
                    clienteId, TypeAlert.OPORTUNIDAD)) continue;

            // No hay asesor asignado — va al administrador
            Long adminId = obtenerAdministrador();

            Alert alerta = construirAlerta(
                    clienteId,
                    adminId,
                    TypeAlert.OPORTUNIDAD,
                    "El cliente no tiene oportunidades activas asignadas "
                            + "en los últimos " + DIAS_INACTIVIDAD + " días.",
                    LocalDateTime.now().plusDays(5)
            );

            Alert guardada = alertRepositoryPort.save(alerta);

            // Solo a los administradores
            notificarAdministradores(guardada);

            log.info("[ALERTA_OPORTUNIDAD_CREADA] clienteId={} | adminId={}",
                    clienteId, adminId);
        }
    }

    // ── Métodos de envío por rol ──────────────────────────────────

    private void enviarAUsuario(Alert alerta, Long usuarioId) {
        userRepositoryPort.findById(usuarioId).ifPresent(usuario ->
                enviarEmail(alerta, usuario));
    }

    private void notificarGerentes(Alert alerta) {
        userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol() == Rol.GERENTE && u.getActivo())
                .forEach(gerente -> enviarEmail(alerta, gerente));
    }

    private void notificarAdministradores(Alert alerta) {
        userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol() == Rol.ADMINISTRADOR && u.getActivo())
                .forEach(admin -> enviarEmail(alerta, admin));
    }

    private void enviarEmail(Alert alerta, User usuario) {
        String asunto = "[CRM Clintec] Nueva alerta: " + alerta.getTipo();
        String cuerpo = construirCuerpoEmail(alerta, usuario.getNombreUser());
        emailPort.enviarAlerta(usuario.getEmail(), asunto, cuerpo);
        log.info("[EMAIL_ALERTA_ENVIADO] tipo={} | destinatario={}",
                alerta.getTipo(), usuario.getEmail());
    }

    // ── Lógica de negocio — obtener responsable ───────────────────

    private Long obtenerAsesorDelCliente(Long clienteId) {
        // Busca el asesor de la oportunidad activa más reciente del cliente
        return opportunityRepositoryPort
                .findByIdClient(clienteId)
                .stream()
                .filter(o -> o.getEstado() == StatusOpportunity.ACTIVA)
                .max(Comparator.comparing(Opportunity::getFechaCreacion))
                .map(Opportunity::getAsesorId)
                // Si no tiene oportunidades activas va al administrador
                .orElseGet(this::obtenerAdministrador);
    }

    private Long obtenerAdministrador() {
        return userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol() == Rol.ADMINISTRADOR && u.getActivo())
                .findFirst()
                .map(User::getId)
                .orElse(1L);
    }

    // ── Construcción de alerta ────────────────────────────────────

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

    private String construirCuerpoEmail(Alert alerta, String nombreUsuario) {
        String colorBadge = switch (alerta.getTipo()) {
            case INACTIVIDAD  -> "#fef3c7; color: #92400e";
            case VENCIMIENTO  -> "#fee2e2; color: #991b1b";
            case SEGUIMIENTO  -> "#dbeafe; color: #1e40af";
            case OPORTUNIDAD  -> "#d1fae5; color: #065f46";
        };

        String destinatarioLabel = switch (alerta.getTipo()) {
            case INACTIVIDAD, SEGUIMIENTO -> "Asesor responsable";
            case VENCIMIENTO              -> "Asesor y Gerencia";
            case OPORTUNIDAD              -> "Administración";
        };

        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px;
                        margin: auto; background: #f8fafc; padding: 20px;">
                <div style="background: white; border-radius: 12px;
                            overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.08);">

                    <div style="background: #3b82f6; padding: 24px; text-align: center;">
                        <h1 style="color: white; margin: 0; font-size: 20px;">
                            🔔 CRM Clintec — Alerta Automática
                        </h1>
                    </div>

                    <div style="padding: 32px;">
                        <p>Hola <strong>%s</strong>,</p>
                        <p>Se ha generado una nueva alerta que requiere tu atención:</p>

                        <div style="display: inline-block; padding: 6px 16px;
                                    border-radius: 20px; font-size: 13px;
                                    font-weight: bold; background: %s;
                                    margin-bottom: 20px;">
                            %s
                        </div>

                        <table style="width: 100%%; border-collapse: collapse; margin: 16px 0;">
                            <tr style="border-bottom: 1px solid #f1f5f9;">
                                <td style="padding: 10px; color: #64748b;
                                           font-weight: 600; width: 40%%;">
                                    Descripción
                                </td>
                                <td style="padding: 10px;">%s</td>
                            </tr>
                            <tr style="border-bottom: 1px solid #f1f5f9;">
                                <td style="padding: 10px; color: #64748b; font-weight: 600;">
                                    Cliente ID
                                </td>
                                <td style="padding: 10px;">%d</td>
                            </tr>
                            <tr style="border-bottom: 1px solid #f1f5f9;">
                                <td style="padding: 10px; color: #64748b; font-weight: 600;">
                                    Vence el
                                </td>
                                <td style="padding: 10px;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; color: #64748b; font-weight: 600;">
                                    Notificado a
                                </td>
                                <td style="padding: 10px;">%s</td>
                            </tr>
                        </table>

                        <div style="text-align: center; margin-top: 24px;">
                            <a href="http://localhost:4200/admin"
                               style="background: #3b82f6; color: white;
                                      padding: 12px 28px; border-radius: 8px;
                                      text-decoration: none; font-weight: bold;">
                                Ir al CRM
                            </a>
                        </div>
                    </div>

                    <div style="background: #f8fafc; padding: 16px;
                                text-align: center; color: #94a3b8; font-size: 12px;">
                        <p>Este es un mensaje automático del CRM Clintec.</p>
                        <p>Por favor no respondas a este correo.</p>
                    </div>
                </div>
            </div>
            """.formatted(
                nombreUsuario,
                colorBadge,
                alerta.getTipo(),
                alerta.getDescripcion(),
                alerta.getClienteId(),
                alerta.getFechaVencimiento().toLocalDate().toString(),
                destinatarioLabel
        );
    }
}
