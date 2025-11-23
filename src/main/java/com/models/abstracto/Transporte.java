/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.models.abstracto;

import java.io.Serializable;

/**
 *
 * @author USER
 */
public abstract class Transporte implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String matricula;
    protected String modelo;
    protected int capacidadKg;
    protected boolean disponible;

    public Transporte(String matricula, String modelo, int capacidadKg) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.capacidadKg = capacidadKg;
        this.disponible = true;
    }

    public boolean verificarDisponibilidad() {
        return disponible;
    }

    public abstract int calcularCapacidad();

    // Getters y Setters
    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public int getCapacidadKg() {
        return capacidadKg;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
