package com.terreneitors.backendclintec.oportunidades.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Oportunidad {
    private Long idOportunidad;
    private Long clienteId;
    private Long asesorId;
    private String descripcion;
    private BigDecimal valorEstimado;
    private Integer probabilidad;
    private EstadoOportunidad estado;
    private EtapaOportunidad etapaOportunidad;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEstimadaCierre;
    private LocalDateTime fechaActualizacion;

    public Oportunidad() {
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

    public Integer getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(Integer probabilidad) {
        int min = this.etapaOportunidad.getMin();
        int max = this.etapaOportunidad.getMax();
        if (probabilidad < min || probabilidad > max)
            throw new IllegalArgumentException(
                    "Para la etapa " + this.etapaOportunidad +
                            " la probabilidad debe estar entre " + min + "% y " + max + "%.");
        this.probabilidad = probabilidad;
    }

    public EtapaOportunidad getEtapaOportunidad() {
        return etapaOportunidad;
    }

    public void setEtapaOportunidad(EtapaOportunidad etapaOportunidad) {
        this.etapaOportunidad = etapaOportunidad;
        this.probabilidad = etapaOportunidad.getProbabilidadDefault();
    }

    public LocalDate getFechaEstimadaCierre() {
        return fechaEstimadaCierre;
    }

    public void setFechaEstimadaCierre(LocalDate fechaEstimadaCierre) {
        this.fechaEstimadaCierre = fechaEstimadaCierre;
    }

    public void cerrarComoGanada() {
        this.etapaOportunidad        = EtapaOportunidad.CIERRE_GANADO;
        this.estado       = EstadoOportunidad.GANADA;
        this.probabilidad = 100;
    }

    public void cerrarComoPerdida() {
        this.etapaOportunidad        = EtapaOportunidad.CIERRE_PERDIDO;
        this.estado       = EstadoOportunidad.PERDIDA;
        this.probabilidad = 0;
    }

    public boolean esPotencial() {
        return this.estado == EstadoOportunidad.ACTIVA && this.probabilidad >= 50;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
