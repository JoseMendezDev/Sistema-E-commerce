/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.abstracto;

/**
 *
 * @author USER
 */
public abstract class Transporte {

    protected String tipo;
    protected String modelo;
    
    public Transporte() {
        
    }
    //Crear un vehículo de reparto.
    public Transporte(String tipo, String modelo) {
        this.tipo = tipo;
        this.modelo = modelo;
    }
    
    public boolean verificarDisponibilidad(){
        return true;
    }
    
    public String getTipo() {
        return tipo;
    }

    public String getModelo() {
        return modelo;
    }
}