package com.terreneitors.backendclintec.sales.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {
    private Long idVenta;
    private Long idOportunidad;
    private BigDecimal valorVenta;
    private Long idAsesor;
    private LocalDateTime fechaVenta;
    private LocalDateTime fechaActualizacion;
    private String notas;
    private PaymentMethod paymentMethod;

    public Sale() {
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El valor de la venta debe ser mayor a cero.");
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Long getIdOportunidad() {
        return idOportunidad;
    }

    public void setIdOportunidad(Long idOportunidad) {
        this.idOportunidad = idOportunidad;
    }

    public BigDecimal getValorVenta() {
        return valorVenta;
    }

    public void setValorVenta(BigDecimal valorVenta) {
        this.valorVenta = valorVenta;
    }

    public Long getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(Long idAsesor) {
        this.idAsesor = idAsesor;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public PaymentMethod getMetodoPago() {
        return paymentMethod;
    }

    public void setMetodoPago(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
