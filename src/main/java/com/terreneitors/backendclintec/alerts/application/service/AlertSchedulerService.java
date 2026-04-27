package com.terreneitors.backendclintec.alerts.application.service;

import com.terreneitors.backendclintec.alerts.application.port.out.AlertRepositoryPort;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import com.terreneitors.backendclintec.shared.email.EmailPort;
import com.terreneitors.backendclintec.users.application.port.out.UserRepositoryPort;
import com.terreneitors.backendclintec.users.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertSchedulerService {
    private final AlertRepositoryPort alertRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final EmailPort emailPort;

    private static final int DIAS_INACTIVIDAD = 15;

    @Scheduled(cron = "0 0 8 * * *")
    public void ejecutarTodasLasAlerts() {
        log.info("[SCHEDULER_INICIO] Ejecutando verificación de alertas automáticas");
        verificarInactividad();
        verificarOportunidadesVencidas();
        verificarOportunidadesEnNegociacion();
        verificarClientesSinOportunidades();
        log.info("[SCHEDULER_FIN] Verificación de alertas completada");
    }

    // ── 1. Clientes sin interacciones en 15 días ──────────────────
    private void verificarInactividad() {
        log.info("[SCHEDULER_INACTIVIDAD] Verificando clientes inactivos");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesInactivos = alertRepositoryPort
                .findClientesSinInteraccionesDesdeFecha(hace15Dias);

        for (Long clienteId : clientesInactivos) {
            if (alertRepositoryPort.existeAlertPendiente(clienteId, TypeAlert.INACTIVIDAD)) {
                continue; // ya tiene alerta pendiente, no duplicar
            }

            Alert alerta = construirAlert(
                    clienteId,
                    TypeAlert.INACTIVIDAD,
                    "El cliente no ha tenido interacciones en los últimos "
                            + DIAS_INACTIVIDAD + " días.",
                    LocalDateTime.now().plusDays(3)
            );

            Alert guardada = alertRepositoryPort.save(alerta);
            enviarEmailAlert(guardada);
            log.info("[ALERTA_INACTIVIDAD_CREADA] clienteId={}", clienteId);
        }
    }

    // ── 2. Oportunidades con fecha de cierre vencida ──────────────
    private void verificarOportunidadesVencidas() {
        log.info("[SCHEDULER_VENCIMIENTO] Verificando oportunidades vencidas");

        List<Long> clientesConOportunidadVencida = alertRepositoryPort
                .findOportunidadesVencidasActivas(LocalDateTime.now());

        for (Long clienteId : clientesConOportunidadVencida) {
            if (alertRepositoryPort.existeAlertPendiente(clienteId, TypeAlert.VENCIMIENTO)) {
                continue;
            }

            Alert alerta = construirAlert(
                    clienteId,
                    TypeAlert.VENCIMIENTO,
                    "Existe una oportunidad cuya fecha de cierre estimada ya venció y aún está activa.",
                    LocalDateTime.now().plusDays(2)
            );

            Alert guardada = alertRepositoryPort.save(alerta);
            enviarEmailAlert(guardada);
            log.info("[ALERTA_VENCIMIENTO_CREADA] clienteId={}", clienteId);
        }
    }

    // ── 3. Oportunidades en NEGOCIACIÓN sin cambios en 15 días ────
    private void verificarOportunidadesEnNegociacion() {
        log.info("[SCHEDULER_SEGUIMIENTO] Verificando oportunidades estancadas en negociación");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesEstancados = alertRepositoryPort
                .findOportunidadesEnNegociacionSinCambios(hace15Dias);

        for (Long clienteId : clientesEstancados) {
            if (alertRepositoryPort.existeAlertPendiente(clienteId, TypeAlert.SEGUIMIENTO)) {
                continue;
            }

            Alert alerta = construirAlert(
                    clienteId,
                    TypeAlert.SEGUIMIENTO,
                    "Una oportunidad lleva más de " + DIAS_INACTIVIDAD
                            + " días en etapa NEGOCIACIÓN sin actualizaciones.",
                    LocalDateTime.now().plusDays(3)
            );

            Alert guardada = alertRepositoryPort.save(alerta);
            enviarEmailAlert(guardada);
            log.info("[ALERTA_SEGUIMIENTO_CREADA] clienteId={}", clienteId);
        }
    }

    // ── 4. Clientes sin oportunidades asignadas en 15 días ────────
    private void verificarClientesSinOportunidades() {
        log.info("[SCHEDULER_OPORTUNIDAD] Verificando clientes sin oportunidades");
        LocalDateTime hace15Dias = LocalDateTime.now().minusDays(DIAS_INACTIVIDAD);

        List<Long> clientesSinOportunidad = alertRepositoryPort
                .findClientesSinOportunidadesDesdeFecha(hace15Dias);

        for (Long clienteId : clientesSinOportunidad) {
            if (alertRepositoryPort.existeAlertPendiente(clienteId, TypeAlert.OPORTUNIDAD)) {
                continue;
            }

            Alert alerta = construirAlert(
                    clienteId,
                    TypeAlert.OPORTUNIDAD,
                    "El cliente no tiene oportunidades activas asignadas en los últimos "
                            + DIAS_INACTIVIDAD + " días.",
                    LocalDateTime.now().plusDays(5)
            );

            Alert guardada = alertRepositoryPort.save(alerta);
            enviarEmailAlert(guardada);
            log.info("[ALERTA_OPORTUNIDAD_CREADA] clienteId={}", clienteId);
        }
    }

    // ── Métodos privados de apoyo ─────────────────────────────────

    private Alert construirAlert(Long clienteId, TypeAlert tipo,
                                   String descripcion, LocalDateTime fechaVencimiento) {
        Alert alerta = new Alert();
        alerta.setClienteId(clienteId);
        alerta.setUsuarioId(obtenerAsesorDelCliente(clienteId));
        alerta.setTipo(tipo);
        alerta.setDescripcion(descripcion);
        alerta.setFechaVencimiento(fechaVencimiento);
        return alerta;
    }

    private Long obtenerAsesorDelCliente(Long clienteId) {
        // Busca el primer asesor activo — ajusta según tu lógica de negocio
        return userRepositoryPort.findAll().stream()
                .filter(u -> u.getRol().name().equals("ASESOR") && u.getActivo())
                .findFirst()
                .map(User::getId)
                .orElse(1L); // fallback al id 1 si no hay asesor
    }

    private void enviarEmailAlert(Alert alerta) {
        userRepositoryPort.findById(alerta.getUsuarioId()).ifPresent(usuario -> {
            String asunto = "[CRM Clintec] Nueva alerta: " + alerta.getTipo();
            String cuerpo = construirCuerpoEmail(alerta, usuario.getNombreUser());
            emailPort.enviarAlerta(usuario.getEmail(), asunto, cuerpo);
        });
    }

    private String construirCuerpoEmail(Alert alerta, String nombreUsuario) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                <h2 style="color: #3b82f6;">CRM Clintec — Alert Automática</h2>
                <p>Hola <strong>%s</strong>,</p>
                <p>Se ha generado una nueva alerta en el sistema:</p>
                <table style="width:100%%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 8px; background: #f1f5f9;"><strong>Tipo</strong></td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px; background: #f1f5f9;"><strong>Descripción</strong></td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px; background: #f1f5f9;"><strong>Cliente ID</strong></td>
                        <td style="padding: 8px;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px; background: #f1f5f9;"><strong>Vence el</strong></td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                </table>
                <p style="margin-top: 20px;">Ingresa al sistema para gestionar esta alerta.</p>
                <p style="color: #94a3b8; font-size: 12px;">CRM Clintec — Sistema automatizado</p>
            </div>
            """.formatted(
                nombreUsuario,
                alerta.getTipo(),
                alerta.getDescripcion(),
                alerta.getClienteId(),
                alerta.getFechaVencimiento()
        );
    }
}
