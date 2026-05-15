// qualification/domain/QualificationHistory.java
package com.terreneitors.backendclintec.qualification.domain;

import java.time.LocalDateTime;

public class QualificationHistory {
    private Long id;
    private Long clienteId;
    private int puntajeAnterior;
    private int puntajeNuevo;
    private String motivo;
    private LocalDateTime fecha;

    public QualificationHistory() {}

    public QualificationHistory(Long clienteId, int puntajeAnterior,
                                int puntajeNuevo, String motivo) {
        this.clienteId      = clienteId;
        this.puntajeAnterior = puntajeAnterior;
        this.puntajeNuevo   = puntajeNuevo;
        this.motivo         = motivo;
        this.fecha          = LocalDateTime.now();
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public int getPuntajeAnterior() { return puntajeAnterior; }
    public void setPuntajeAnterior(int puntajeAnterior) { this.puntajeAnterior = puntajeAnterior; }
    public int getPuntajeNuevo() { return puntajeNuevo; }
    public void setPuntajeNuevo(int puntajeNuevo) { this.puntajeNuevo = puntajeNuevo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}