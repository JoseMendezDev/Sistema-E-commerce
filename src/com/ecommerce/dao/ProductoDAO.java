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
        String sql = "SELECT p.*, i.stock_actual FROM productos p INNER JOIN inventario i ON p.id = producto_id WHERE nombre LIKE ? AND activo = 1";

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
        String sql = "EXEC sp_buscar_producto_por_id @id_producto = ?";

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

    public List<Producto> buscarTodosAdministrador() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "EXEC sp_productos_administrador";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }
        return productos;
    }

    public List<Producto> buscarPorCategoria(int categoriaId) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT DISTINCT p.*, c.nombre as categoria_nombre, c.descripcion as categoria_desc "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.categoria_id = c.id "
                + "WHERE p.categoria_id = ? AND p.activo = 1"
                + "ORDER BY p.nombre";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }
        }
        return productos;
    }

    public List<Categoria> obtenerCategorias() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT DISTINCT id, nombre, descripcion FROM categorias WHERE activo = 1 ORDER BY nombre";

        try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categorias.add(categoria);
            }
        }

        return categorias;
    }

    @Override
    public boolean insertar(Producto producto) throws SQLException {
        String sqlProducto = "INSERT INTO productos (nombre, descripcion, precio, categoria_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psProducto = conexion.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS)) {
            psProducto.setString(1, producto.getNombre());
            psProducto.setString(2, producto.getDescripcion());
            psProducto.setDouble(3, producto.getPrecio());
            psProducto.setInt(4, producto.getCategoria().getId());

            int filasAfectadas = psProducto.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = psProducto.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));

                        String sqlInventario = "INSERT INTO inventario (producto_id, stock_actual) VALUES (?,?)";
                        try (PreparedStatement psInv = conexion.prepareStatement(sqlInventario)) {
                            psInv.setInt(1, rs.getInt(1));
                            psInv.setInt(2, producto.getInventario().getStockActual());
                            psInv.executeUpdate();
                        }

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
            if (producto.getCategoria() != null) {
                ps.setInt(5, producto.getCategoria().getId());
            } else {
                throw new SQLException("La categoría no puede ser nula");
            }
            ps.setInt(6, producto.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE productos SET activo = 0 WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarStock(int productoId, int nuevoStock) throws SQLException {
        String sql = "EXEC sp_actualizar_stock @producto_id = ?, @nuevo_stock = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ps.setInt(2, nuevoStock);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            String sqlDirecto = "UPDATE inventario SET stock_actual = ? WHERE producto_id = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sqlDirecto)) {
                ps.setInt(1, nuevoStock);
                ps.setInt(2, productoId);

                return ps.executeUpdate() > 0;
            }
        }
    }

    public boolean reducirStock(int productoId, int cantidad) throws SQLException {
        String sql = "EXEC sp_reducir_stock @producto_id = ?, @cantidad = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            ps.setInt(2, cantidad);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            String sqlDirecto = "UPDATE inventario SET stock_actual = stock_actual - ? "
                    + "WHERE producto_id = ? AND stock_actual >= ?";

            try (PreparedStatement ps = conexion.prepareStatement(sqlDirecto)) {
                ps.setInt(1, cantidad);
                ps.setInt(2, productoId);
                ps.setInt(3, cantidad);

                return ps.executeUpdate() > 0;
            }
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setActivo(rs.getBoolean("activo"));

        try {
            int stockActual = rs.getInt("stock_actual");
            if (!rs.wasNull()) {
                Inventario inventario = new Inventario();
                inventario.setStockActual(stockActual);
                producto.setInventario(inventario);
            }
        } catch (SQLException e) {
        }

        try {
            Object catIdObj = rs.getObject("categoria_id");
            Object catNombreObj = rs.getObject("categoria_nombre");

            if (catIdObj != null && catNombreObj != null) {
                int categoriaId = Integer.parseInt(catIdObj.toString());
                String categoriaNombre = catNombreObj.toString();

                if (categoriaId > 0 && !categoriaNombre.isEmpty()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(categoriaId);
                    categoria.setNombre(categoriaNombre);
                    producto.setCategoria(categoria);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en categoría: " + e.getMessage());
        }
        return producto;
    }
}
