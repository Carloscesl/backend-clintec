package com.terreneitors.backendclintec.shared.exception;

public class InvalidStateException extends BusinessException {
    public InvalidStateException(String mensaje) {
        super("ESTADO_INVALIDO", mensaje);
    }
}
