/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.usuarios;

import com.ecommerce.models.abstracto.Usuario;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String nivel;
    private List<String> permisos;
    
    public Administrador() {
        super();
        this.permisos = new ArrayList<>();
    }
    
    public Administrador(String email, String password, String nombre, String telefono, String nivel) {
        super(email, password, nombre, telefono);
        this.nivel = nivel;
        this.permisos = new ArrayList<>();
        inicializarPermisos();
    }
    
    private void inicializarPermisos() {
        permisos.add("GESTIONAR_PRODUCTOS");
        permisos.add("GESTIONAR_INVENTARIO");
        permisos.add("VER_REPORTES");
        permisos.add("GESTIONAR_USUARIOS");
    }
    
    public void gestionarProductos() {
        if (tienePermiso("GESTIONAR_PRODUCTOS")) {
            System.out.println("Gestionando productos...");
        }
    }
    
    public void gestionarInventario() {
        if (tienePermiso("GESTIONAR_INVENTARIO")) {
            System.out.println("Gestionando inventario...");
        }
    }
    
    public void verReportes() {
        if (tienePermiso("VER_REPORTES")) {
            System.out.println("Visualizando reportes...");
        }
    }
    
    public void gestionarUsuarios() {
        if (tienePermiso("GESTIONAR_USUARIOS")) {
            System.out.println("Gestionando usuarios...");
        }
    }
    
    private boolean tienePermiso(String permiso) {
        return permisos.contains(permiso);
    }
    
    @Override
    public void cerrarSesion() {
        this.autenticado = false;
        System.out.println("Administrador " + nombre + " ha cerrado sesión");
    }
    
    // Getters y Setters
    public String getNivel() { return nivel; }
    public List<String> getPermisos() { return new ArrayList<>(permisos); }
}