package com.terreneitors.backendclintec.qualification.domain;

import java.time.LocalDateTime;

public class QualificationClient {
    private Long id;
    private Long clienteId;
    private int puntaje;
    private Qualification clasificacion;
    private LocalDateTime ultimaActualizacion;

    public QualificationClient() {
        this.puntaje = 0;
        this.clasificacion = Qualification.FRIO;
    }

    public void setPuntaje(int puntaje) {
        if (puntaje < 0 || puntaje > 100)
            throw new IllegalArgumentException(
                    "El puntaje debe estar entre 0 y 100. Valor recibido: " + puntaje);
        this.puntaje = puntaje;
        this.clasificacion = calcularClasificacion(puntaje);
    }

    private Qualification calcularClasificacion(int puntaje) {
        if (puntaje <= 25) return Qualification.FRIO;
        if (puntaje <= 50) return Qualification.TIBIO;
        if (puntaje <= 75) return Qualification.CALIENTE;
        return Qualification.VIP;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public Qualification getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Qualification clasificacion) {
        this.clasificacion = clasificacion;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
