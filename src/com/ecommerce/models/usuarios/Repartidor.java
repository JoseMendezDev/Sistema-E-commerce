/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.usuarios;

import com.ecommerce.models.abstracto.Usuario;
import com.ecommerce.models.envios.Automovil;
import com.ecommerce.models.pedidos.Pedido;
import java.util.ArrayList;
import java.util.List;

public class Repartidor extends Usuario {

    private String licencia;
    private boolean disponible;
    private List<Pedido> pedidosAsignados;
    private Automovil vehiculo;
    private boolean activo = true;
    
    public Repartidor(int id){
        this.id = id;
        this.activo = true;
    }

    //Crear un repartidor sin datos completos.
    public Repartidor() {
        super();
        this.pedidosAsignados = new ArrayList<>();
        this.disponible = true;
        this.activo = true;
    }

    //Crear un repartidor listo para trabajar
    public Repartidor(String email, String password, String nombre, String telefono, Automovil vehiculo) {
        super(email, password, nombre, telefono);
        this.vehiculo = vehiculo;
        this.pedidosAsignados = new ArrayList<>();
        this.disponible = true;
        this.activo = true;
    }

    public void aceptarPedido(Pedido pedido) {
        if (disponible && vehiculo.verificarDisponibilidad()) {
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

    @Override
    public void cerrarSesion() {
        this.autenticado = false;
        this.disponible = true;
        System.out.println("Repartidor " + nombre + " ha cerrado sesión");
    }

    // Getters y Setters
    public String getLicencia() {
        return licencia;
    }

    public boolean isDisponible() {
        return this.disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    public List<Pedido> getPedidosAsignados() {
        return new ArrayList<>(pedidosAsignados);
    }

    public Automovil getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Automovil vehiculo) {
        this.vehiculo = vehiculo;
    }
}