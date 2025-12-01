/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.usuarios.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class ClienteDAO extends BaseDAO<Cliente> {

    @Override
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearCliente(rs);
            }
        } finally {
            cerrarRecursos(rs, ps);
        }
        return null;
    }

    public Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE email = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearCliente(rs);
            }
        } finally {
            cerrarRecursos(rs, ps);
        }
        return null;
    }

    @Override
    public List<Cliente> buscarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        return clientes;
    }
    
    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (email, password, nombre, telefono, direccion_envio) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getEmail());
            ps.setString(2, cliente.getPassword());
            ps.setString(3, cliente.getNombre());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getDireccionEnvio());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Cliente autenticar(String email, String password) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE email = ? AND password = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearCliente(rs);
            }
        } finally {
            cerrarRecursos(rs, ps);
        }
        return null;
    }
    
    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET email = ?, password = ?, nombre = ?, telefono = ?, direccion_envio = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getEmail());
            ps.setString(2, cliente.getPassword());
            ps.setString(3, cliente.getNombre());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getDireccionEnvio());
            ps.setInt(6, cliente.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setEmail(rs.getString("email"));
        cliente.setPassword(rs.getString("password"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDireccionEnvio(rs.getString("direccion_envio"));

        return cliente;
    }
}
