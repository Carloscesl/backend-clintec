package com.terreneitors.backendclintec.opportunities.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Opportunity {
    private Long idOportunidad;
    private Long clienteId;
    private Long asesorId;
    private String descripcion;
    private BigDecimal valorEstimado;
    private Integer probabilidad;
    private StatusOpportunity estado;
    private StageOpportunity stageOpportunity;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEstimadaCierre;
    private LocalDateTime fechaActualizacion;

    public Opportunity() {
    }

    public Long getIdOportunidad() {
        return idOportunidad;
    }

    public void setIdOportunidad(Long idOportunidad) {
        this.idOportunidad = idOportunidad;
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

    public StatusOpportunity getEstado() {
        return estado;
    }

    public void setEstado(StatusOpportunity estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(Integer probabilidad) {
        int min = this.stageOpportunity.getMin();
        int max = this.stageOpportunity.getMax();
        if (probabilidad < min || probabilidad > max)
            throw new IllegalArgumentException(
                    "Para la etapa " + this.stageOpportunity +
                            " la probabilidad debe estar entre " + min + "% y " + max + "%.");
        this.probabilidad = probabilidad;
    }

    public StageOpportunity getEtapaOportunidad() {
        return stageOpportunity;
    }

    public void setEtapaOportunidad(StageOpportunity stageOpportunity) {
        this.stageOpportunity = stageOpportunity;
        this.probabilidad = stageOpportunity.getProbabilidadDefault();
    }

    public LocalDate getFechaEstimadaCierre() {
        return fechaEstimadaCierre;
    }

    public void setFechaEstimadaCierre(LocalDate fechaEstimadaCierre) {
        this.fechaEstimadaCierre = fechaEstimadaCierre;
    }

    public void cerrarComoGanada() {
        this.stageOpportunity = StageOpportunity.CIERRE_GANADO;
        this.estado       = StatusOpportunity.GANADA;
        this.probabilidad = 100;
    }

    public void cerrarComoPerdida() {
        this.stageOpportunity = StageOpportunity.CIERRE_PERDIDO;
        this.estado       = StatusOpportunity.PERDIDA;
        this.probabilidad = 0;
    }

    public boolean esPotencial() {
        return this.estado == StatusOpportunity.ACTIVA && this.probabilidad >= 50;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
