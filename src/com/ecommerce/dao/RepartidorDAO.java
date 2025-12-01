/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.usuarios.Repartidor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class RepartidorDAO extends BaseDAO<Repartidor> {

    @Override
    public Repartidor buscarPorId(int id) throws SQLException {
        // Implementación de SELECT por ID
        return null;
    }

    @Override
    public List<Repartidor> buscarTodos() throws SQLException {
        // Implementación de SELECT ALL
        return new ArrayList<>();
    }

    @Override
    public boolean insertar(Repartidor entidad) throws SQLException {
        // Implementación de INSERT
        return false;
    }

    @Override
    public boolean actualizar(Repartidor entidad) throws SQLException {
        // Implementación de UPDATE
        return false;
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        // Implementación de DELETE o UPDATE activo=false
        return false;
    }
    
    private Repartidor mapearRepartidor(ResultSet rs) throws SQLException {
        return new Repartidor();
    }

}
