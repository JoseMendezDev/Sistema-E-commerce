/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.productos;

import java.time.LocalDateTime;

public class Inventario {

    private int id;
    private Producto producto;
    private int stockActual;
    private LocalDateTime ultimaActualizacion;
    
    //Sirve para: crear inventario vacío
    public Inventario() {
    }

    //Crear inventario con stock inicial
    public Inventario(Producto producto, int stockActual) {
        this.producto = producto;
        this.stockActual = stockActual;

        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void agregarStock(int cantidad) {
        if (cantidad > 0) {
            this.stockActual += cantidad;
            this.ultimaActualizacion = LocalDateTime.now();
        } else {
            System.err.println("Error: Cantidad inválida o excede el stock maximo.");
        }
    }

    public boolean reducirStock(int cantidad) {
        if (cantidad > 0 && stockActual >= cantidad) {
            this.stockActual -= cantidad;
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        System.err.println("Error: No hay suficiente stock para reducir.");
        return false;
    }

    public boolean verificarDisponibilidad(int cantidadRequerida) {
        return stockActual >= cantidadRequerida;
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
    
    public void setProducto(Producto producto){
        this.producto = producto;
    }

    public int getStockActual() {
        return stockActual;
    }
    
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
    
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
