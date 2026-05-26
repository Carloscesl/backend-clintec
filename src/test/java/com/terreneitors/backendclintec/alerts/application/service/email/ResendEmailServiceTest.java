package com.terreneitors.backendclintec.alerts.application.service.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.Emails;
import com.terreneitors.backendclintec.alerts.domain.Alert;
import com.terreneitors.backendclintec.alerts.domain.TypeAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResendEmailServiceTest {

    @Mock private Resend resend;
    @Mock private Emails emails;
    @Mock private TemplateEngine templateEngine;

    @InjectMocks
    private ResendEmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@clintec.com");
    }

    @ParameterizedTest
    @EnumSource(TypeAlert.class)
    @DisplayName("Debe enviar correctamente alertas para todos los tipos")
    void enviarAlertaConPlantilla_ShouldHandleAllTypes(TypeAlert tipo) throws ResendException {
        // Arrange
        Alert alerta = new Alert(); // Constructor vacío
        alerta.setTipo(tipo);
        alerta.setFechaVencimiento(LocalDateTime.now().plusDays(1));

        Map<String, Object> datos = Map.of("key", "value");

        when(resend.emails()).thenReturn(emails);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>Cuerpo</html>");

        // Act
        emailService.enviarAlertaConPlantilla("user@test.com", "Carlos", alerta, datos);

        // Assert
        verify(emails).send(any());
        verify(templateEngine).process(anyString(), any(Context.class));
    }

    @Test
    @DisplayName("Debe manejar excepciones de Resend sin interrumpir el flujo")
    void enviar_ShouldHandleResendException() throws ResendException {
        // Arrange
        when(resend.emails()).thenReturn(emails);
        when(emails.send(any())).thenThrow(new ResendException("Error de conexión"));

        // Act & Assert (No debe lanzar excepción)
        emailService.enviarAlerta("to@test.com", "Asunto", "Cuerpo");

        verify(emails).send(any());
    }
}