/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.carrito;

import com.ecommerce.models.usuarios.Cliente;
import com.ecommerce.models.productos.Producto;
import com.ecommerce.models.pedidos.Pedido;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author USER
 */
public class CarritoCompras implements Serializable {

    private static final long serialVersionUID = 1L;

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
            throw new IllegalStateException("Stock insuficiente");
        }

        Optional<ItemCarrito> itemExistente = items.stream()
                .filter(i -> i.getProducto().getId() == producto.getId())
                .findFirst();

        if (itemExistente.isPresent()) {
            itemExistente.get().actualizarCantidad(
                    itemExistente.get().getCantidad() + cantidad
            );
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(producto, cantidad, producto.getPrecio());
            items.add(nuevoItem);
        }

        calcularTotal();
    }

    public void eliminarItem(int itemId) {
        items.removeIf(item -> item.getId() == itemId);
        calcularTotal();
    }

    public void actualizarCantidad(int itemId, int nuevaCantidad) {
        items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getProducto().getInventario().verificarDisponibilidad(nuevaCantidad)) {
                        item.actualizarCantidad(nuevaCantidad);
                        calcularTotal();
                    }
                });
    }

    public double calcularTotal() {
        this.subtotal = items.stream()
                .mapToDouble(ItemCarrito::calcularSubtotal)
                .sum();
        return subtotal;
    }

    public void vaciar() {
        items.clear();
        subtotal = 0.0;
    }

    public Pedido convertirAPedido() {
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        Pedido pedido = new Pedido(cliente, cliente.getDireccionEnvio());

        for (ItemCarrito item : items) {
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
