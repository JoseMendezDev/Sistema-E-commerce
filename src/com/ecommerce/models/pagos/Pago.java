/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pagos;

import com.ecommerce.models.abstracto.MetodoPago;
import com.ecommerce.models.pedidos.Pedido;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author USER
 */
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Pedido pedido;
    private MetodoPago metodoPago;
    private double monto;
    private EstadoPago estado;
    private LocalDateTime fechaPago;
    private String numeroTransaccion;

    public Pago() {
        this.estado = EstadoPago.PENDIENTE;
    }

    public Pago(Pedido pedido, MetodoPago metodoPago, double monto) {
        this();
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.numeroTransaccion = generarNumeroTransaccion();
    }

    private String generarNumeroTransaccion() {
        return "TRANS-" + System.currentTimeMillis();
    }

    public boolean procesarPago() {
        if (metodoPago.validar() && metodoPago.procesar(monto)) {
            this.estado = EstadoPago.COMPLETADO;
            this.fechaPago = LocalDateTime.now();
            return true;
        }
        this.estado = EstadoPago.FALLIDO;
        return false;
    }

    public boolean validarPago() {
        return metodoPago.validar() && monto > 0;
    }

    public boolean reembolsar() {
        if (estado == EstadoPago.COMPLETADO) {
            this.estado = EstadoPago.REEMBOLSADO;
            System.out.println("Pago reembolsado: " + numeroTransaccion);
            return true;
        }
        return false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }
}