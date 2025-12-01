/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.services;

import com.ecommerce.models.carrito.CarritoCompras;
import com.ecommerce.models.productos.Producto;
import com.ecommerce.dao.ProductoDAO;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class CarritoService {

    private ProductoDAO productoDAO;

    public CarritoService() {
        this.productoDAO = new ProductoDAO();
    }
    
    public boolean agregarProducto(CarritoCompras carrito, int productoId, int cantidad) {
        try {
            Producto producto = productoDAO.buscarPorId(productoId);
            
            if (producto == null) {
                System.err.println("Producto no encontrado");
                return false;
            }
            
            if (!producto.estaDisponible()) {
                System.err.println("Producto no disponible");
                return false;
            }
            
            if (!producto.getInventario().verificarDisponibilidad(cantidad)) {
                System.err.println("Stock insuficiente");
                return false;
            }
            
            carrito.agregarItem(producto, cantidad);
            System.out.println("Producto agregado al carrito");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error agregando producto: " + e.getMessage());
            return false;
        }
    }
    
    public void mostrarCarrito(CarritoCompras carrito) {
        System.out.println("\n=== CARRITO DE COMPRAS ===");
        carrito.getItems().forEach(item -> {
            System.out.printf("- %s x%d - $%.2f\n",
                item.getProducto().getNombre(),
                item.getCantidad(),
                item.calcularSubtotal());
        });
        System.out.printf("TOTAL: $%.2f\n", carrito.calcularTotal());
    }
}
