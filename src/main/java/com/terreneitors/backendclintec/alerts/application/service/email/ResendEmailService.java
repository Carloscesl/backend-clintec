package com.terreneitors.backendclintec.alerts.application.service.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.shared.email.EmailPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class ResendEmailService implements EmailPort {

    private final Resend resend;
    private final TemplateEngine templateEngine;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Override
    public void enviarAlerta(String destinatario, String asunto, String cuerpo) {
        enviar(destinatario, asunto, cuerpo);
    }

    @Override
    public void enviarAlertaConPlantilla(
            String destinatario,
            String nombreUsuario,
            Alert alerta,
            Map<String, Object> datosExtra) {

        String plantilla = switch (alerta.getTipo()) {
            case INACTIVIDAD -> "emails/alerta-inactividad";
            case VENCIMIENTO -> "emails/alerta-vencimiento";
            case SEGUIMIENTO -> "emails/alerta-seguimiento";
            case OPORTUNIDAD -> "emails/alerta-oportunidad";
        };

        String asunto = switch (alerta.getTipo()) {
            case INACTIVIDAD -> "[CRM Clintec] ⚠️ Cliente sin actividad";
            case VENCIMIENTO -> "[CRM Clintec] 🚨 Oportunidad vencida";
            case SEGUIMIENTO -> "[CRM Clintec] 📋 Seguimiento requerido";
            case OPORTUNIDAD -> "[CRM Clintec] 💼 Cliente sin oportunidad";
        };

        // ── Construye contexto Thymeleaf ──────────────────────────
        Context context = new Context();
        context.setVariable("nombreUsuario", nombreUsuario);
        context.setVariable("fechaVencimiento",
                alerta.getFechaVencimiento().toLocalDate().toString());

        // Agrega datos extra del cliente y oportunidad
        datosExtra.forEach(context::setVariable);

        String cuerpoHtml = templateEngine.process(plantilla, context);

        enviar(destinatario, asunto, cuerpoHtml);
    }

    private void enviar(String destinatario, String asunto, String cuerpo) {
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(destinatario)
                    .subject(asunto)
                    .html(cuerpo)
                    .build();

            resend.emails().send(options);
            log.info("[EMAIL_ENVIADO] destinatario={} | asunto={}",
                    destinatario, asunto);

        } catch (ResendException e) {
            log.error("[EMAIL_FALLIDO] destinatario={} | causa={}",
                    destinatario, e.getMessage());
        }
    }

}
