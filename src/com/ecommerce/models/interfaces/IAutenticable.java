/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.interfaces;

/**
 *
 * @author USER
 */
public interface IAutenticable {

    boolean autenticar(String email, String password);

    void cerrarSesion();

    boolean estaAutenticado();
}
