/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.productos.Inventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class InventarioDAO extends BaseDAO<Inventario>{
    
    private ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    public Inventario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInventario(rs);
                }
            }
        }
        return null;
    }
    
    public Inventario buscarPorProducto(int productoId) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE producto_id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInventario(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Inventario> buscarTodos() throws SQLException {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario";
        
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                inventarios.add(mapearInventario(rs));
            }
        }
        return inventarios;
    }
    
    @Override
    public boolean insertar(Inventario inventario) throws SQLException {
        String sql = "INSERT INTO inventario (producto_id, stock_actual, stock_minimo, stock_maximo, ultima_actualizacion) VALUES (?, ?, ?, ?, NOW())";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inventario.getProducto().getId());
            ps.setInt(2, inventario.getStockActual());
            ps.setInt(3, inventario.getStockMinimo());
            ps.setInt(4, inventario.getStockMaximo());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        inventario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean actualizar(Inventario inventario) throws SQLException {
        String sql = "UPDATE inventario SET stock_actual = ?, stock_minimo = ?, stock_maximo = ?, ultima_actualizacion = NOW() WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, inventario.getStockActual());
            ps.setInt(2, inventario.getStockMinimo());
            ps.setInt(3, inventario.getStockMaximo());
            ps.setInt(4, inventario.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarStock(int productoId, int cantidad) throws SQLException {
        String sql = "UPDATE inventario SET stock_actual = stock_actual + ?, ultima_actualizacion = NOW() WHERE producto_id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM inventario WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Inventario mapearInventario(ResultSet rs) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.setId(rs.getInt("id"));
        inventario.setProducto(productoDAO.buscarPorId(rs.getInt("producto_id")));
        inventario.setStockActual(rs.getInt("stock_actual"));
        inventario.setStockMinimo(rs.getInt("stock_minimo"));
        inventario.setStockMaximo(rs.getInt("stock_maximo"));
        inventario.setUltimaActualizacion(rs.getTimestamp("ultima_actualizacion").toLocalDateTime());
        
        return inventario;
    }
}
