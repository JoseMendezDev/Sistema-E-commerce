/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pagos;

import com.ecommerce.models.abstracto.MetodoPago;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.pedidos.EstadoPedido;
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
        this.estado = EstadoPago.PROCESANDO;
        if (metodoPago.validar() && metodoPago.procesar(monto)) {
            this.estado = EstadoPago.COMPLETADO;
            this.fechaPago = LocalDateTime.now();
            if (this.pedido != null) {
                this.pedido.actualizarEstado(EstadoPedido.PAGADO);
            }
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
            if (this.pedido != null) {
                this.pedido.actualizarEstado(EstadoPedido.CANCELADO); // Asumiendo que el reembolso cancela el pedido
            }
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
    
    public void setMonto(double monto) {
        this.monto = monto;
    }

    public EstadoPago getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }
    
    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }
    
    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }
}
