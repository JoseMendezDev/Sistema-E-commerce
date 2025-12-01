/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pagos;

import java.time.LocalDate;
import com.ecommerce.models.abstracto.MetodoPago;

/**
 *
 * Por subir github antes de presentación
 */
public class TarjetaCredito extends MetodoPago {

    private static final long serialVersionUID = 1L;

    private String numeroTarjeta;
    private String titular;
    private String cvv;
    private LocalDate fechaExpiracion;

    public TarjetaCredito() {
        super("TARJETA_CREDITO");
    }

    public TarjetaCredito(String numeroTarjeta, String titular, String cvv, LocalDate fechaExpiracion) {
        super("TARJETA_CREDITO");
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
        this.cvv = cvv;
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    //validacion de la tarjeta
    public boolean validar() {
        return numeroTarjeta != null
                && numeroTarjeta.length() == 16
                && cvv != null
                && cvv.length() == 3
                && fechaExpiracion.isAfter(LocalDate.now());
    }

    @Override
    //verifica que la tarjeta sea valida
    public boolean procesar(double monto) {
        if (validar() && monto > 0) {
            System.out.println("Procesando pago con tarjeta: " + enmascarar(numeroTarjeta));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean validarPago() {
        return validar();
    }
//enmascara el numero de la tarjeta
    private String enmascarar(String numero) {
        return "**** **** **** " + numero.substring(12);
    }

    @Override
    //muestra el estado de pago
    public String obtenerEstadoPago() {
        return "Tarjeta valida hasta: " + fechaExpiracion;
    }

    // Getters y Setters
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public String getTitular() {
        return titular;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
