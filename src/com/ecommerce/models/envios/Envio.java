/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.envios;

import com.ecommerce.interfaces.IEnviable;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.usuarios.Repartidor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author USER
 */
public class Envio implements IEnviable, Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Pedido pedido;
    private Repartidor repartidor;
    private EstadoEnvio estado;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;

    public Envio() {
        this.estado = EstadoEnvio.PREPARANDO;
    }

    public Envio(Pedido pedido) {
        this();
        this.pedido = pedido;
    }

    // se guarda el repartidor se establece la fecha de envio
    public void asignarRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
        this.fechaEnvio = LocalDateTime.now();
        this.estado = EstadoEnvio.EN_TRANSITO;
    }

    @Override
    public void actualizarEstadoEnvio() {
        System.out.println("Estado de envio actualizado: " + estado);
    }

    @Override
    public int obtenerTiempoEstimado() {
        // Tiempo en horas según el estado
        switch (estado) {
            case PREPARANDO:
                return 24;
            case EN_TRANSITO:
                return 48;
            case EN_REPARTO:
                return 4;
            case ENTREGADO:
                return 0;
            default:
                return -1;
        }
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
    
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }  
}