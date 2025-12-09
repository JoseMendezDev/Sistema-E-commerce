/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.usuarios;

import com.ecommerce.models.abstracto.Usuario;
import java.time.LocalDateTime;

public class Administrador extends Usuario {

    private boolean activo;
    private LocalDateTime fechaCreacion;

    //Crear un administrador básico.
    public Administrador() {
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    @Override
    public void cerrarSesion() {
        this.autenticado = false;
        System.out.println("Administrador " + nombre + " ha cerrado sesión");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return String.format("Administrador{id=%d, nombre='%s', email='%s'}",
            id, nombre, email);
    }
}
