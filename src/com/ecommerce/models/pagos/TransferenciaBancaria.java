/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pagos;

import com.ecommerce.models.abstracto.MetodoPago;

/**
 *
 * @autor USER
 */
public class TransferenciaBancaria extends MetodoPago {

    private static final long serialVersionUID = 1L;

    private String numeroCuenta;
    private String banco;
    private String codigoTransferencia;

    public TransferenciaBancaria() {
        super("TRANSFERENCIA_BANCARIA");
    }

    public TransferenciaBancaria(String numeroCuenta, String banco) {
        super("TRANSFERENCIA_BANCARIA");
        this.numeroCuenta = numeroCuenta;
        this.banco = banco;
    }

    @Override
    //validacion
    public boolean validar() {
        return numeroCuenta != null
                && numeroCuenta.length() >= 10
                && banco != null
                && !banco.isEmpty();
    }

    @Override
    //revisa si los datos bancarios son validso
    public boolean procesar(double monto) {
        if (validar() && monto > 0) {
            this.codigoTransferencia = "TRANS-" + System.currentTimeMillis();
            System.out.println("Transferencia bancaria iniciada: " + codigoTransferencia);
            return true;
        }
        return false;
    }

    @Override
    public boolean validarPago() {
        return validar();
    }

    @Override
    public String obtenerEstadoPago() {
        return "Banco: " + banco + " - Codigo: " + codigoTransferencia;
    }

    // Getters y Setters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getBanco() {
        return banco;
    }

    public String getCodigoTransferencia() {
        return codigoTransferencia;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}
