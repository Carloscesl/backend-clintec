package com.terreneitors.backendclintec.shared.exception;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String recurso, String campo , Object valor){
        super(
                "RECURSO_NO_ENCONTRADO",
                String.format("No se encontró %s con %s: %s", recurso, campo, valor)
        );
    }
}
