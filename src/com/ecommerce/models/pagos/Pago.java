/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pagos;

import java.time.LocalDateTime;

/**
 *
 * @author USER
 */
public class Pago {

    private int id;
    private int pedidoId;
    private String metodoPago;
    private double monto;
    private String estado;
    private LocalDateTime fechaPago;
    private String detalles;
    private String numeroTransaccion;

    public Pago() {
        this.fechaPago = LocalDateTime.now();
        this.estado = "PEDIENTE";
    }

    public Pago(int pedido, String metodoPago, double monto) {
        this();
        this.pedidoId = pedidoId;
        this.metodoPago = metodoPago;
        this.monto = monto;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    @Override
    public String toString() {
        return String.format("Pago #%d - Pedido: #%d - Monto: S/.%.2f - Estado %s", id, pedidoId, monto, estado);
    }
}