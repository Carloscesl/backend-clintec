package com.terreneitors.backendclintec.shared.exception;

public class ValidationException extends BusinessException {
    public ValidationException(String mensaje) {
        super("ERROR_VALIDACION", mensaje);
    }
}
