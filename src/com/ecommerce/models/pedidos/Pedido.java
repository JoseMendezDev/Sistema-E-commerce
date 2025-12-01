/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pedidos;

import com.ecommerce.models.usuarios.Cliente;
import com.ecommerce.models.productos.Producto;
import com.ecommerce.models.pagos.Pago;
import com.ecommerce.models.envios.Envio;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Cliente cliente;
    private String direccionEnvio;
    private List<DetallePedido> detalles;
    private double total;
    private EstadoPedido estado;
    private LocalDateTime fechaPedido;

    public Pedido() {
        this.detalles = new ArrayList<>();
        this.fechaPedido = LocalDateTime.now();
        this.estado = EstadoPedido.CREADO;
        calcularTotal();
    }

    public Pedido(Cliente cliente, String direccionEnvio) {
        this();
        this.cliente = cliente;
        this.direccionEnvio = direccionEnvio;
    }

    public void agregarDetalle(Producto producto, int cantidad, double precioUnitario) {
        DetallePedido detalle = new DetallePedido(producto, cantidad, precioUnitario);
        this.detalles.add(detalle);
        calcularTotal();
    }

    //Monto total del pedido sumando los subtotales de los detalles.
    public double calcularTotal() {
        this.total = detalles.stream()
                           .mapToDouble(DetallePedido::calcularSubtotal)
                           .sum();
        return this.total;
    }

    public void actualizarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // Getters y Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public Cliente getCliente(){
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
        
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
