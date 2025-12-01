/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author USER
 */
public class ConfigBD {

        public static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ECOMMERCE_DB;encrypt=true;trustServerCertificate=true;";
        public static final String USUARIO = "administrador";
        public static final String CLAVE = "ecommerce";
        public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        
        private ConfigBD() {
    }   
}
