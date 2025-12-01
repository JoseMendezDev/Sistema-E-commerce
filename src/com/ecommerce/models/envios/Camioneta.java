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
public class Camioneta extends Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    private double capacidadCarga;
    private boolean tieneRefrigeracion;

    public Camioneta() {
        super("", "", 0);
    }

    public Camioneta(String matricula, String modelo, int capacidadKg, double capacidadCarga, boolean tieneRefrigeracion) {
        super(matricula, modelo, capacidadKg);
        this.capacidadCarga = capacidadCarga;
        this.tieneRefrigeracion = tieneRefrigeracion;
    }

    @Override
    //Multiplica la capacidad base por 1.5 → 50% más capacidad
    public int calcularCapacidad() {
        return (int) (capacidadKg * 1.5);
    }

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public boolean isTieneRefrigeracion() {
        return tieneRefrigeracion;
    }

    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public void setTieneRefrigeracion(boolean tieneRefrigeracion) {
        this.tieneRefrigeracion = tieneRefrigeracion;
    }
}
