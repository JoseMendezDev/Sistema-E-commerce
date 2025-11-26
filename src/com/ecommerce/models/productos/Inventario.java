/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.productos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Producto producto;
    private int stockActual;
    private int stockMinimo;
    private int stockMaximo;
    private LocalDateTime ultimaActualizacion;
//Sirve para: crear inventario vacío
    public Inventario() {
    }
//Crear inventario con stock inicial
    public Inventario(Producto producto, int stockInicial) {
        this.producto = producto;
        this.stockActual = stockInicial;
        this.stockMinimo = 10;
        this.stockMaximo = 1000;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void agregarStock(int cantidad) {
        if (cantidad > 0 && stockActual + cantidad <= stockMaximo) {
            this.stockActual += cantidad;
            this.ultimaActualizacion = LocalDateTime.now();
        }
    }

    public boolean reducirStock(int cantidad) {
        if (cantidad > 0 && stockActual >= cantidad) {
            this.stockActual -= cantidad;
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean necesitaReposicion() {
        return stockActual <= stockMinimo;
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

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
}
