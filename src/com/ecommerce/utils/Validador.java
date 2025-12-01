/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.utils;

/**
 *
 * @author USER
 */
public class Validador {

    public static boolean esCadenaValida(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean esEmailValido(String email) {
        if (!esCadenaValida(email)) {
            return false;
        }
        // Expresión regular para validar formato de email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        return email.matches(emailRegex);
    }

    public static boolean esPositivo(double valor) {
        return valor > 0;
    }

    public static boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("\\d{9}");
    }

    public static boolean validarPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
