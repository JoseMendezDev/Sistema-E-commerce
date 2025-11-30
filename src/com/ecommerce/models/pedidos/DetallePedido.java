/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pedidos;

import com.ecommerce.models.productos.Producto;
import java.io.Serializable;

/**
 *
 * @author USER
 */
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    //Crea un detalle vacio
    public DetallePedido() {
    }

    //Crea un detalle de pedido ya con información
    public DetallePedido(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = calcularSubtotal();
    }

    public double calcularSubtotal() {
        this.subtotal = cantidad * precioUnitario;
        return subtotal;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
