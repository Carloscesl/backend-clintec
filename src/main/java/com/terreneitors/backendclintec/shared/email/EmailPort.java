package com.terreneitors.backendclintec.shared.email;

public interface EmailPort {
    void enviarAlerta(String destinatario, String asunto, String cuerpo);
}
