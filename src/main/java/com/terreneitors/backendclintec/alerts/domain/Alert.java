package com.terreneitors.backendclintec.alerts.domain;

import java.time.LocalDateTime;

public class Alert {
    private Long id;
    private Long clienteId;
    private Long  usuarioId;
    private String descripcion;
    private TypeAlert tipo;
    private StateAlert estado;
    private LocalDateTime fechaVencimiento;
    private LocalDateTime fecha;
    private LocalDateTime fechaActualizacion;

    public Alert() {
        this.estado = StateAlert.PENDIENTE;
    }

    public void marcarComoVista() {
        this.estado = StateAlert.VISTA;
    }

    public void resolver() {
        this.estado = StateAlert.RESUELTA;
    }

    public void vencer() {
        this.estado = StateAlert.VENCIDA;
    }

    public boolean estaVencida() {
        return this.fechaVencimiento != null &&
                LocalDateTime.now().isAfter(this.fechaVencimiento) &&
                this.estado == StateAlert.PENDIENTE;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TypeAlert getTipo() {
        return tipo;
    }

    public void setTipo(TypeAlert tipo) {
        this.tipo = tipo;
    }

    public StateAlert getEstado() {
        return estado;
    }

    public void setEstado(StateAlert estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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
