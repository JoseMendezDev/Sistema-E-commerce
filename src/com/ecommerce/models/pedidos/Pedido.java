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
    private List<DetallePedido> detalles;
    private EstadoPedido estado;
    private double total;
    private LocalDateTime fechaPedido;
    private String direccionEnvio;
    private Pago pago;
    private Envio envio;

    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaPedido = LocalDateTime.now();
    }

    public Pedido(Cliente cliente, String direccionEnvio) {
        this();
        this.cliente = cliente;
        this.direccionEnvio = direccionEnvio;
    }

    public void agregarDetalle(Producto producto, int cantidad, double precioUnitario) {
        DetallePedido detalle = new DetallePedido(producto, cantidad, precioUnitario);
        detalles.add(detalle);
        calcularTotal();
    }

    public double calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(DetallePedido::calcularSubtotal)
                .sum();

        if (envio != null) {
            this.total += envio.getCostoEnvio();
        }

        return total;
    }

    public void actualizarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("Pedido #" + id + " actualizado a: " + nuevoEstado.getDescripcion());
    }

    public boolean cancelar() {
        if (estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.CONFIRMADO) {
            this.estado = EstadoPedido.CANCELADO;

            // Devolver stock al inventario
            for (DetallePedido detalle : detalles) {
                detalle.getProducto().getInventario().agregarStock(detalle.getCantidad());
            }

            return true;
        }
        return false;
    }

    public void confirmar() {
        if (estado == EstadoPedido.PENDIENTE) {
            // Reducir stock del inventario
            for (DetallePedido detalle : detalles) {
                detalle.getProducto().getInventario().reducirStock(detalle.getCantidad());
            }

            this.estado = EstadoPedido.CONFIRMADO;
        }
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

    public List<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
        calcularTotal();
    }
}
