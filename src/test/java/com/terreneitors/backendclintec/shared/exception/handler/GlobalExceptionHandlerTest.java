package com.terreneitors.backendclintec.shared.exception.handler;

import com.terreneitors.backendclintec.shared.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(GlobalExceptionHandlerTest.TestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @Configuration
    static class TestController {
        // Estas excepciones aceptan parámetros específicos según tus archivos
        @GetMapping("/test-not-found")
        public void t1() { throw new ResourceNotFoundException("Cliente", "id", 1L); }

        @GetMapping("/test-invalid-state")
        public void t2() { throw new InvalidStateException("Estado no permitido"); }

        @GetMapping("/test-validation")
        public void t3() { throw new ValidationException("Error en regla"); }

        @GetMapping("/test-business")
        public void t4() { throw new BusinessException("ERROR_NEGOCIO", "Mensaje de negocio"); }

        @GetMapping("/test-access")
        public void t5() throws AccessDeniedException { throw new AccessDeniedException("Acceso denegado"); }

        @GetMapping("/test-bad-cred")
        public void t6() { throw new BadCredentialsException("Credenciales malas"); }

        @GetMapping("/test-disabled")
        public void t7() { throw new DisabledException("Usuario desactivado"); }

        @GetMapping("/test-illegal")
        public void t8() { throw new IllegalArgumentException("Argumento ilegal"); }

        @GetMapping("/test-generic")
        public void t9() { throw new RuntimeException("Error inesperado"); }
    }

    @Test
    @DisplayName("Cobertura 100% - Excepciones individuales")
    void testAllExceptions() throws Exception {
        mockMvc.perform(get("/test-not-found")).andExpect(status().isNotFound());
        mockMvc.perform(get("/test-invalid-state")).andExpect(status().isConflict());
        mockMvc.perform(get("/test-validation")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/test-business")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/test-access")).andExpect(status().isForbidden());
        mockMvc.perform(get("/test-bad-cred")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/test-disabled")).andExpect(status().isForbidden());
        mockMvc.perform(get("/test-illegal")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/test-generic")).andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Cobertura 100% - MethodArgumentNotValidException")
    void handleMethodArgumentNotValidException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        BeanPropertyBindingResult result =
                new BeanPropertyBindingResult(new Object(), "target");

        result.addError(new FieldError("target", "email", "es requerido"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        new MethodParameter(this.getClass().getDeclaredMethods()[0], -1),
                        result
                );

        var response =
                handler.handleCamposInvalidos(ex, new MockHttpServletRequest());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCodigo()).isEqualTo("CAMPOS_INVALIDOS");
        assertThat(response.getBody().getDetalles()).isNotEmpty();
    }
}