/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class ConexionBD {

    private static Connection conexion = null;

    private ConexionBD() {
        try {
            // Cargar el driver JDBC de SQL Server
            Class.forName(ConfigBD.DRIVER);
            System.out.println("Driver JDBC cargado.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de SQL Server no encontrado.");
            e.printStackTrace();
        }
    }

    // Método para obtener la conexión
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                conexion = DriverManager.getConnection(ConfigBD.URL, ConfigBD.USUARIO, ConfigBD.CLAVE);
                System.out.println("Conexión a la base de datos establecida.");
            } catch (SQLException e) {
                System.err.println("Error al establecer la conexión a la base de datos.");
                throw e;
            }
        }
        return conexion;
    }

    // Método para cerrar la conexión
    public static void closeConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                    System.out.println("Conexión a la base de datos cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
}
