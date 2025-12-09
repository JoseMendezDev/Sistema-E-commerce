/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.envios;

import com.ecommerce.models.abstracto.Transporte;

/**
 *
 * @author USER
 */
public class Automovil extends Transporte {

    private String descripcion;

    public Automovil() {
        super();
    }

    public Automovil(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean verificarDisponibilidad() {
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}