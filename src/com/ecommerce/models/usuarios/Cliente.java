/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.usuarios;

import com.ecommerce.models.abstracto.Usuario;
import com.ecommerce.models.carrito.CarritoCompras;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.productos.Producto;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    private String direccionEnvio;
    private List<Pedido> historialPedidos;
    private CarritoCompras carrito;

    //Crear un cliente con carrito y lista de pedidos.
    public Cliente() {
        super();
        this.historialPedidos = new ArrayList<>();
        this.carrito = new CarritoCompras(this);
    }

    //Crear un cliente completo.
    public Cliente(String email, String password, String nombre, String telefono, String direccionEnvio) {
        super(email, password, nombre, telefono);
        this.direccionEnvio = direccionEnvio;
        this.historialPedidos = new ArrayList<>();
        this.carrito = new CarritoCompras(this);
    }

    //Esta parte verifica si el carrito está vacío antes de crear el pedido.
    public Pedido realizarPedido() {
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        Pedido pedido = carrito.convertirAPedido();
        historialPedidos.add(pedido);
        return pedido;
    }

    public void agregarAlCarrito(Producto producto, int cantidad) {
        carrito.agregarItem(producto, cantidad);
    }

    public List<Pedido> verHistorial() {
        return new ArrayList<>(historialPedidos);
    }

    @Override
    public void cerrarSesion() {
        this.autenticado = false;
        System.out.println("Cliente " + nombre + " ha cerrado sesión");
    }

    // Getters y Setters
    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public CarritoCompras getCarrito() {
        return carrito;
    }

    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }
}
