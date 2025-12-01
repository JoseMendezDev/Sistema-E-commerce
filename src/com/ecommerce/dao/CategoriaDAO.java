/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.productos.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class CategoriaDAO extends BaseDAO<Categoria> {

    @Override
    public Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Categoria> buscarTodos() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE activo = true";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }
        return categorias;
    }

    public List<Categoria> buscarPrincipales() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE categoria_padre_id IS NULL AND activo = true";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }
        return categorias;
    }

    public List<Categoria> buscarSubcategorias(int categoriaPadreId) throws SQLException {
        List<Categoria> subcategorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE categoria_padre_id = ? AND activo = true";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, categoriaPadreId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subcategorias.add(mapearCategoria(rs));
                }
            }
        }
        return subcategorias;
    }

    @Override
    public boolean insertar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre, descripcion, categoria_padre_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());

            if (categoria.getCategoriaPadre() != null) {
                ps.setInt(3, categoria.getCategoriaPadre().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean actualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ?, categoria_padre_id = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());

            if (categoria.getCategoriaPadre() != null) {
                ps.setInt(3, categoria.getCategoriaPadre().getId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setInt(4, categoria.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE categorias SET activo = false WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setDescripcion(rs.getString("descripcion"));
        
        int categoriaPadreId = rs.getInt("categoria_padre_id");
        if (!rs.wasNull()) {
            Categoria padre = buscarPorId(categoriaPadreId);
            categoria.setCategoriaPadre(padre); 
        }
        
        return categoria;
    }
}
