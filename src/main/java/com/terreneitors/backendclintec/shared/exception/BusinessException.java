package com.terreneitors.backendclintec.shared.exception;

public class BusinessException extends RuntimeException {

    private final String codigo;

    public BusinessException( String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
