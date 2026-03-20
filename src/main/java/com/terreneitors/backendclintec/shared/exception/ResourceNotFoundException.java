package com.terreneitors.backendclintec.shared.exception;

public class ResourceNotFoundException extends RuntimeException { // El 'public' es clave
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
