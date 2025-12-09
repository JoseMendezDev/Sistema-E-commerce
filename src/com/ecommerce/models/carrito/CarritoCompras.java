/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.carrito;

import com.ecommerce.models.usuarios.Cliente;
import com.ecommerce.models.productos.Producto;
import com.ecommerce.models.pedidos.Pedido;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author USER
 */
public class CarritoCompras {

    private int id;
    private Cliente cliente;
    private List<ItemCarrito> items;
    private double subtotal;
    private LocalDateTime fechaCreacion;

    public CarritoCompras() {
        this.items = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.subtotal = 0.0;
    }

    public CarritoCompras(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public void agregarItem(Producto producto, int cantidad) {
        if (!producto.estaDisponible()) {
            throw new IllegalStateException("Producto no disponible");
        }

        if (!producto.getInventario().verificarDisponibilidad(cantidad)) {
            throw new IllegalArgumentException("Cantidad solicitada excede el stock disponible: " + producto.getInventario().getStockActual());
        }

        Optional<ItemCarrito> existingItem = items.stream()
                .filter(item -> item.getProducto().getId() == producto.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            ItemCarrito item = existingItem.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            if (producto.getInventario().verificarDisponibilidad(nuevaCantidad)) {
                item.actualizarCantidad(nuevaCantidad);
            } else {
                throw new IllegalArgumentException("La cantidad total excede el stock disponible.");
            }
        } else {
            items.add(new ItemCarrito(producto, cantidad, producto.getPrecio()));
        }

        calcularTotal();
    }
    //Elimina cualquier item cuyo id coincida. Luego recalcula el total del carrito
    public void removerItem(int productoId) {
        items.removeIf(item -> item.getProducto().getId() == productoId);
        calcularTotal();
    }
    //busca item del producto, luego verifica si el nuevo stock esta disponible si se puede actualiza la cantidad
    public void actualizarCantidad(int productoId, int nuevaCantidad) {
        items.stream()
                .filter(item -> item.getProducto().getId() == productoId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getProducto().getInventario().verificarDisponibilidad(nuevaCantidad)) {
                        item.actualizarCantidad(nuevaCantidad);
                        calcularTotal();
                    } else {
                        System.err.println("No se puede actualizar la cantidad: stock insuficiente.");
                    }
                });
    }
    //suma todos los subtotales, actauliza el atributo subtotal y devuelve el tota del carrito
    public double calcularTotal() {
        this.subtotal = items.stream()
                .mapToDouble(ItemCarrito::calcularSubtotal)
                .sum();
        return subtotal;
    }
    //limpia el carrito eliminando todos los items
    public void vaciar() {
        items.clear();
        subtotal = 0.0;
    }
    //convierte el carrito en un pedido
    public Pedido convertirAPedido() {
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        Pedido pedido = new Pedido(cliente, cliente.getDireccionEnvio());

        for (ItemCarrito item : items) {
            //convierte cada item carrito en un detalle del pedido
            pedido.agregarDetalle(item.getProducto(), item.getCantidad(), item.getPrecioUnitario());
        }

        vaciar();
        return pedido;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}