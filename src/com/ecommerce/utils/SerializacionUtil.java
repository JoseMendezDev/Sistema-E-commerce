/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.utils;

import java.io.*;

/**
 *
 * @author USER
 */
public class SerializacionUtil {

    public static <T> void serializar(T objeto, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(objeto);
            System.out.println("Objeto serializado: " + archivo);
        } catch (IOException e) {
            System.err.println("Error serializando: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserializar(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error deserializando: " + e.getMessage());
            return null;
        }
    }
}
