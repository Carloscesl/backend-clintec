package com.terreneitors.backendclintec.shared.email;

import com.terreneitors.backendclintec.alerts.domain.Alert;

import java.util.Map;

public interface EmailPort {
    void enviarAlerta(String destinatario, String asunto, String cuerpo);
    void enviarAlertaConPlantilla(
            String destinatario,
            String nombreUsuario,
            Alert alerta,
            Map<String, Object> datosExtra
    );
}
