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
public class Motocicleta extends Transporte {

    private static final long serialVersionUID = 1L;

    private int cilindrada;

    public Motocicleta() {
        super("", "", 0);
    }

    public Motocicleta(String matricula, String modelo, int capacidadKg, int cilindrada) {
        super(matricula, modelo, capacidadKg);
        this.cilindrada = cilindrada;
    }

    @Override
    public int calcularCapacidad() {
        // Motocicletas tienen capacidad reducida
        return capacidadKg / 2;
    }
    
    //Getters y Setters
    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }
}
