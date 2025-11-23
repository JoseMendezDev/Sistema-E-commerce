/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public interface IBuscable<T> {

    List<T> buscar(String criterio);

    List<T> filtrar(Map<String, Object> filtros);
}
