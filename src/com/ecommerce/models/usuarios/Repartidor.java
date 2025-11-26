/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.usuarios;

import com.ecommerce.models.abstracto.Usuario;
import com.ecommerce.models.abstracto.Transporte;
import com.ecommerce.models.pedidos.Pedido;
import java.util.ArrayList;
import java.util.List;

public class Repartidor extends Usuario {

    private static final long serialVersionUID = 1L;

    private String licencia;
    private boolean enServicio;
    private List<Pedido> pedidosAsignados;
    private Transporte vehiculo;
//Crear un repartidor sin datos completos.
    public Repartidor() {
        super();
        this.pedidosAsignados = new ArrayList<>();
        this.enServicio = false;
    }
//Crear un repartidor listo para trabajar
    public Repartidor(String email, String password, String nombre, String telefono, String licencia, Transporte vehiculo) {
        super(email, password, nombre, telefono);
        this.licencia = licencia;
        this.vehiculo = vehiculo;
        this.pedidosAsignados = new ArrayList<>();
        this.enServicio = false;
    }

    public void aceptarPedido(Pedido pedido) {
        if (enServicio && vehiculo.verificarDisponibilidad()) {
            pedidosAsignados.add(pedido);
            System.out.println("Pedido #" + pedido.getId() + " aceptado por " + nombre);
        }
    }

    public void completarEntrega(Pedido pedido) {
        if (pedidosAsignados.contains(pedido)) {
            pedidosAsignados.remove(pedido);
            System.out.println("Entrega completada para pedido #" + pedido.getId());
        }
    }

    public void actualizarUbicacion() {
        if (enServicio) {
            System.out.println("Ubicación actualizada para repartidor " + nombre);
        }
    }

    @Override
    public void cerrarSesion() {
        this.autenticado = false;
        this.enServicio = false;
        System.out.println("Repartidor " + nombre + " ha cerrado sesión");
    }

    // Getters y Setters
    public String getLicencia() {
        return licencia;
    }

    public boolean isEnServicio() {
        return enServicio;
    }

    public void setEnServicio(boolean enServicio) {
        this.enServicio = enServicio;
    }

    public List<Pedido> getPedidosAsignados() {
        return new ArrayList<>(pedidosAsignados);
    }

    public Transporte getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Transporte vehiculo) {
        this.vehiculo = vehiculo;
    }
}
