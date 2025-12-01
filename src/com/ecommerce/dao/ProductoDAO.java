/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.productos.Producto;
import com.ecommerce.models.productos.Categoria;
import com.ecommerce.models.productos.Inventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class ProductoDAO extends BaseDAO<Producto> {

    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? AND activo = 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        }
        return productos;
    }

    @Override
    public Producto buscarPorId(int id) throws SQLException {
        String sql = "EXEC sp_buscar_producto_por_id";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscarTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "EXEC sp_productos";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }
        return productos;
    }

    public List<Producto> buscarPorCategoria(int categoriaId) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as categoria_nombre, c.descripcion as categoria_desc "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.categoria_id = c.id "
                + "WHERE p.categoria_id = ? AND p.disponible = 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
            rs.close();
        }
        return productos;
    }

    @Override
    public boolean insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, categoria_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getCategoria().getId());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, activo = ?, categoria_id = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setBoolean(4, producto.isActivo());
            ps.setInt(5, producto.getCategoria().getId());
            ps.setInt(6, producto.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE productos SET activo = false WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setActivo(rs.getBoolean("activo"));
        int stockActual = rs.getInt("stock_actual");

        try {
            int categoriaId = rs.getInt("categoria_id");
            String categoriaNombre = rs.getString("categoria_nombre");
            String categoriaDesc = rs.getString("categoria_desc");

            if (!rs.wasNull() && categoriaNombre != null) {
                Categoria categoria = new Categoria(categoriaNombre, categoriaDesc);
                categoria.setId(categoriaId);
                producto.setCategoria(categoria);
            }
        } catch (SQLException e) {

        }

        Inventario inventario = new Inventario();
        inventario.setStockActual(stockActual);

        producto.setInventario(inventario);

        return producto;
    }
}
