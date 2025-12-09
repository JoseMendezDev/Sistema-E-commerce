/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce;

import com.ecommerce.database.ConexionBD;
import com.ecommerce.ui.MenuPrincipal;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        try {
            MenuPrincipal.mostrar();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            ConexionBD.closeConexion();
        }
    }
}