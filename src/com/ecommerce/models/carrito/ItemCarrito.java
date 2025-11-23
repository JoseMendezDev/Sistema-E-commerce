/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.carrito;

import com.ecommerce.models.productos.Producto;
import java.io.Serializable;

/**
 *
 * @author USER
 */
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public ItemCarrito() {
    }

    public ItemCarrito(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }

    public void actualizarCantidad(int nuevaCantidad) {
        if (nuevaCantidad > 0) {
            this.cantidad = nuevaCantidad;
        }
    }

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
}
