/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pedidos;

import com.ecommerce.models.usuarios.Cliente;
import com.ecommerce.models.productos.Producto;
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

    public void agregarDetalle(Producto producto, int cantidad) {
        agregarDetalle(producto, cantidad, producto.getPrecio());
    }
    
    //Monto total del pedido sumando los subtotales de los detalles.
    public double calcularTotal() {
        this.total = 0;
        for (DetallePedido detalle : detalles) {
            this.total += detalle.calcularSubtotal();
        }
        return this.total;
    }

    public void actualizarEstado(EstadoPedido estadoEnum) {
        this.estado = estadoEnum;
    }
    
    // Métodos para JDBC
    public java.sql.Timestamp getFechaPedidoTimestamp() {
        if (fechaPedido == null) return null;
        return java.sql.Timestamp.valueOf(fechaPedido);
    }
    
    public void setFechaPedidoTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            this.fechaPedido = null;
        } else {
            this.fechaPedido = timestamp.toLocalDateTime();
        }
    }
    
    // Método para verificar si el pedido está vacío
    public boolean estaVacio() {
        return detalles == null || detalles.isEmpty();
    }
    
    // Método para obtener cantidad de items
    public int getCantidadItems() {
        if (detalles == null) return 0;
        return detalles.size();
    }
    
    // Método para obtener cantidad total de productos
    public int getCantidadTotalProductos() {
        if (detalles == null) return 0;
        int total = 0;
        for (DetallePedido detalle : detalles) {
            total += detalle.getCantidad();
        }
        return total;
    }
    
    // Método para mostrar resumen del pedido
    public String getResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(id).append("\n");
        sb.append("Cliente: ").append(cliente != null ? cliente.getNombre() : "N/A").append("\n");
        sb.append("Fecha: ").append(fechaPedido).append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Total: S/.").append(String.format("%.2f", total)).append("\n");
        sb.append("Items: ").append(getCantidadItems()).append("\n");
        return sb.toString();
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
    
    public List<DetallePedido> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        calcularTotal();
    }
    
    @Override
    public String toString() {
        return String.format("Pedido #%d - Cliente: %s - Total: S/.%.2f - Estado: %s",
                id, 
                cliente != null ? cliente.getNombre() : "N/A", 
                total, 
                estado);
    }
}
