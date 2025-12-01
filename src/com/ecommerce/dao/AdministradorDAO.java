/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.usuarios.Administrador;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class AdministradorDAO extends BaseDAO<Administrador> {
    
    @Override
    public Administrador buscarPorId(int id) throws SQLException {
        return null;
    }
    
    @Override
    public List<Administrador> buscarTodos() throws SQLException {
        return new ArrayList<>();
    }
    
    @Override
    public boolean insertar(Administrador entidad) throws SQLException {

        return false;
    }
    
    @Override
    public boolean actualizar(Administrador entidad) throws SQLException {
        return false;
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        return false;
    }
    
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        return new Administrador();
    }
}
