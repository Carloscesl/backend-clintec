package com.terreneitors.backendclintec.interactions.domain;

import java.time.LocalDateTime;

public class Interaction {
    private Long            id;
    private Long            clienteId;
    private Long            usuarioId;
    private Long            oportunidadId;
    private TypeInteraction tipo;
    private String          nota;
    private LocalDateTime fecha;
    private LocalDateTime   fechaActualizacion;

    public Interaction() {
        this.tipo = TypeInteraction.LLAMADA;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getOportunidadId() {
        return oportunidadId;
    }

    public void setOportunidadId(Long oportunidadId) {
        this.oportunidadId = oportunidadId;
    }

    public TypeInteraction getTipo() {
        return tipo;
    }

    public void setTipo(TypeInteraction tipo) {
        this.tipo = tipo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
