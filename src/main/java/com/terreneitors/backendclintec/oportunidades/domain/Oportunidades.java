package com.terreneitors.backendclintec.oportunidades.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Oportunidades {
    private Long idOportunidades;
    private Long clienteId;
    private Long asesorId;
    private String descripcion;
    private BigDecimal valorEstimado;
    private EstadoOportunidad estado;
    private LocalDateTime fechaCreacion;

    public Oportunidades() {
    }

    public Long getIdOportunidades() {
        return idOportunidades;
    }

    public void setIdOportunidades(Long idOportunidades) {
        this.idOportunidades = idOportunidades;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getAsesorId() {
        return asesorId;
    }

    public void setAsesorId(Long asesorId) {
        this.asesorId = asesorId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public EstadoOportunidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoOportunidad estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
