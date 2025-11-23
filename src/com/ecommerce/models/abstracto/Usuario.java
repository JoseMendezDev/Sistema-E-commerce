/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.abstracto;

import com.ecommerce.interfaces.IAutenticable;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 *
 * @author USER
 */
public abstract class Usuario implements IAutenticable, Serializable {
    private static final long serialVersionUID = 1L;
    
    protected int id;
    protected String email;
    protected String password;
    protected String nombre;
    protected String telefono;
    protected LocalDateTime fechaRegistro;
    protected boolean autenticado;
    
    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
        this.autenticado = false;
    }
    
    public Usuario(String email, String password, String nombre, String telefono) {
        this();
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.telefono = telefono;
    }
    
    @Override
    public boolean autenticar(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.autenticado = true;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean estaAutenticado() {
        return autenticado;
    }
    
    public abstract void cerrarSesion();
    
    public void actualizarPerfil(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}   

