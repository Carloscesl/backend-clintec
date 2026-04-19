package com.terreneitors.backendclintec.shared.exception.handler;

import com.terreneitors.backendclintec.shared.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 Recurso no encontrado ────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse > handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.warn("[NOT_FOUND] {} | ruta: {}", ex.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .codigo(ex.getCodigo())
                        .mensaje(ex.getMessage())
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 409 Estado inválido de negocio ───────────────────────────
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(
            InvalidStateException ex,
            HttpServletRequest request) {

        log.warn("[INVALID_STATE] {} | ruta: {}", ex.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .codigo(ex.getCodigo())
                        .mensaje(ex.getMessage())
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 400 Validación de regla de negocio ───────────────────────
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex,
            HttpServletRequest request) {

        log.warn("[VALIDATION] {} | ruta: {}", ex.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .codigo(ex.getCodigo())
                        .mensaje(ex.getMessage())
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 400 Cualquier otra BusinessException ─────────────────────
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        log.warn("[BUSINESS] {} | codigo: {} | ruta: {}",
                ex.getMessage(), ex.getCodigo(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .codigo(ex.getCodigo())
                        .mensaje(ex.getMessage())
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 400 Campos inválidos (@Valid en controller) ──────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCamposInvalidos(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> "Campo '" + e.getField() + "': " + e.getDefaultMessage())
                .toList();

        log.warn("[CAMPOS_INVALIDOS] {} errores | ruta: {}",
                detalles.size(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .codigo("CAMPOS_INVALIDOS")
                        .mensaje("Hay errores en los campos enviados")
                        .ruta(request.getRequestURI())
                        .detalles(detalles)
                        .timestamp()
                        .build());
    }

    // ── 403 Sin permisos de rol ──────────────────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        log.warn("[ACCESO_DENEGADO] ruta: {} | usuario sin permisos", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .codigo("ACCESO_DENEGADO")
                        .mensaje("No tienes permisos para realizar esta acción")
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 401 Credenciales incorrectas (login) ─────────────────────
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        log.warn("[CREDENCIALES_INVALIDAS] intento fallido | ruta: {}", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .codigo("CREDENCIALES_INVALIDAS")
                        .mensaje("Email o contraseña incorrectos")
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 403 Usuario desactivado ──────────────────────────────────
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(
            DisabledException ex,
            HttpServletRequest request) {

        log.warn("[USUARIO_DESACTIVADO] intento de login | ruta: {}", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .codigo("USUARIO_DESACTIVADO")
                        .mensaje("Tu cuenta está desactivada. Contacta al administrador.")
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 400 Argumento ilegal del dominio ─────────────────────────
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("[ARGUMENTO_INVALIDO] {} | ruta: {}", ex.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .codigo("ARGUMENTO_INVALIDO")
                        .mensaje(ex.getMessage())
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

    // ── 500 Error inesperado — siempre al final ──────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        // ERROR porque es inesperado — incluye stack trace completo en el log
        log.error("[ERROR_INTERNO] ruta: {} | tipo: {} | mensaje: {}",
                request.getRequestURI(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex); // <-- el cuarto argumento imprime el stack trace en el archivo

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .codigo("ERROR_INTERNO")
                        .mensaje("Ocurrió un error inesperado. Contacta al administrador.")
                        .ruta(request.getRequestURI())
                        .timestamp()
                        .build());
    }

}
