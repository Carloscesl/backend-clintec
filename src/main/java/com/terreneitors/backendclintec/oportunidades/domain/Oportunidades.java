package com.terreneitors.backendclintec.oportunidades.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Oportunidades {
    private Long idOportunidades;
    private Long clienteId;
    private Long asesorId;
    private String descripcion;
    private BigDecimal valorEstimado;
    private Integer probabilidad;
    private EstadoOportunidad estado;
    private EtapaOportunidad etapaOportunidad;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEstimadaCierre;

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

    public Integer getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(Integer probabilidad) {
        this.probabilidad = probabilidad;
    }

    public EtapaOportunidad getEtapaOportunidad() {
        return etapaOportunidad;
    }

    public void setEtapaOportunidad(EtapaOportunidad etapaOportunidad) {
        this.etapaOportunidad = etapaOportunidad;
    }

    public LocalDate getFechaEstimadaCierre() {
        return fechaEstimadaCierre;
    }

    public void setFechaEstimadaCierre(LocalDate fechaEstimadaCierre) {
        this.fechaEstimadaCierre = fechaEstimadaCierre;
    }
}
