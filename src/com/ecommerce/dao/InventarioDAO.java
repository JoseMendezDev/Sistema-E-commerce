/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.productos.Inventario;
import com.ecommerce.models.productos.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class InventarioDAO extends BaseDAO<Inventario> {

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

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                inventarios.add(mapearInventario(rs));
            }
        }
        return inventarios;
    }

    @Override
    public boolean insertar(Inventario inventario) throws SQLException {
        String sql = "UPDATE inventario SET stock_actual = stock_actual ? WHERE producto_id = ? ";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, inventario.getProducto().getId());
            ps.setInt(2, inventario.getStockActual());

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

    public boolean agregar(int productoId, int cantidad) throws SQLException {
        String sqlSelect = "SELECT stock_actual FROM inventario WHERE producto_id = ?";
        PreparedStatement ps1 = conexion.prepareStatement(sqlSelect);
        ps1.setInt(1, productoId);

        ResultSet rs = ps1.executeQuery();
        if (!rs.next()) {
            return false;
        }

        int stockActual = rs.getInt("stock_actual");
        int nuevoStock = stockActual + cantidad;

        String sqlUpdate = "UPDATE inventario SET stock_actual = ? WHERE producto_id = ?";
        PreparedStatement ps2 = conexion.prepareStatement(sqlUpdate);
        ps2.setInt(1, nuevoStock);
        ps2.setInt(2, productoId);

        return ps2.executeUpdate() > 0;
    }
    
    public boolean reducir(int productoId, int cantidad) throws SQLException {
        String sqlSelect = "SELECT stock_actual FROM inventario WHERE producto_id = ?";
        PreparedStatement ps1 = conexion.prepareStatement(sqlSelect);
        ps1.setInt(1, productoId);

        ResultSet rs = ps1.executeQuery();
        if (!rs.next()) {
            return false;
        }

        int stockActual = rs.getInt("stock_actual");
        int nuevoStock = stockActual - cantidad;

        String sqlUpdate = "UPDATE inventario SET stock_actual = ? WHERE producto_id = ?";
        PreparedStatement ps2 = conexion.prepareStatement(sqlUpdate);
        ps2.setInt(1, nuevoStock);
        ps2.setInt(2, productoId);

        return ps2.executeUpdate() > 0;
    }

    @Override
    public boolean actualizar(Inventario inventario) throws SQLException {
        String sql = "UPDATE inventario SET stock_actual = ? WHERE producto_id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, inventario.getStockActual());
            ps.setInt(2, inventario.getProducto().getId());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Inventario> obtenerStockGeneral() throws SQLException {
        List<Inventario> lista = new ArrayList<>();

        String sql = "SELECT i.producto_id, p.nombre, i.stock_actual "
                + "FROM inventario i "
                + "INNER JOIN productos p ON p.id = i.producto_id";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Producto producto = new Producto();
            producto.setId(rs.getInt("producto_id"));
            producto.setNombre(rs.getString("nombre"));

            Inventario inv = new Inventario();
            inv.setProducto(producto);
            inv.setStockActual(rs.getInt("stock_actual"));

            lista.add(inv);
        }

        return lista;
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
        inventario.setUltimaActualizacion(rs.getTimestamp("ultima_actualizacion").toLocalDateTime());

        return inventario;
    }
}
