/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.envios;

import com.ecommerce.models.abstracto.Transporte;
import java.io.Serializable;

/**
 *
 * @author USER
 */
public class Automovil extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numeroPuertas;
    private boolean tieneMaletero;

    public Automovil() {
        super("", "", 0);
    }

    public Automovil(String matricula, String modelo, int capacidadKg, int numeroPuertas, boolean tieneMaletero) {
        super(matricula, modelo, capacidadKg);
        this.numeroPuertas = numeroPuertas;
        this.tieneMaletero = tieneMaletero;
    }

    @Override
    public int calcularCapacidad() {
        int capacidad = capacidadKg;
        if (tieneMaletero) {
            capacidad += 50; // Bonus por maletero
        }
        return capacidad;
    }

    public int getNumeroPuertas() {
        return numeroPuertas;
    }

    public boolean isTieneMaletero() {
        return tieneMaletero;
    }

    public void setNumeroPuertas(int numeroPuertas) {
        this.numeroPuertas = numeroPuertas;
    }

    public void setTieneMaletero(boolean tieneMaletero) {
        this.tieneMaletero = tieneMaletero;
    }
}
