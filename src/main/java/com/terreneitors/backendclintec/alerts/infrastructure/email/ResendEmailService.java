package com.terreneitors.backendclintec.alerts.infrastructure.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.terreneitors.backendclintec.shared.email.EmailPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ResendEmailService implements EmailPort {

    private final Resend resend;

    @Value("${resend.from-email}")
    private String fromEmail;

    public ResendEmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    @Override
    public void enviarAlerta(String destinatario, String asunto, String cuerpo) {
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(destinatario)
                    .subject(asunto)
                    .html(cuerpo)
                    .build();

            resend.emails().send(options);
            log.info("[EMAIL_ENVIADO] destinatario={} | asunto={}", destinatario, asunto);

        } catch (ResendException e) {
            // No lanzamos excepción — el email falla silenciosamente
            // La alerta ya se guardó en BD
            log.error("[EMAIL_FALLIDO] destinatario={} | causa={}", destinatario, e.getMessage());
        }
    }
}
