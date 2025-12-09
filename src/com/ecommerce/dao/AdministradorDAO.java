/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.usuarios.Administrador;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class AdministradorDAO extends BaseDAO<Administrador> {
    
    public Administrador autenticar(String email, String password) throws SQLException {
        String sql = "SELECT * FROM administradores WHERE email = ? AND password = ? AND activo = 1;";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearAdministrador(rs);
            }
        } finally {
            cerrarRecursos(rs, ps);
        }
        return null;
    }
    
    public boolean verificarCredenciales(String email, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM administradores WHERE email = ? AND password = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM administradores WHERE email = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean reactivarCuenta(int adminId) throws SQLException {
        String sql = "UPDATE administradores SET activo = 1 WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            return ps.executeUpdate() > 0;
        }
    }
    
    public Administrador buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nombre, email, nivel_acceso, activo, fecha_registro" +
                     "FROM administradores WHERE email = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAdministrador(rs);
                }
            }
        }
        return null;
    }
    
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
        String sql = "UPDATE administradores SET activo = 0 WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        Administrador admin = new Administrador();
        admin.setId(rs.getInt("id"));
        admin.setNombre(rs.getString("nombre"));
        admin.setEmail(rs.getString("email"));
        admin.setActivo(rs.getBoolean("activo"));
        
        if (rs.getTimestamp("fecha_registro") != null) {
            admin.setFechaCreacion(rs.getTimestamp("fecha_registro").toLocalDateTime());
        }
        
        return admin;
    }
}
