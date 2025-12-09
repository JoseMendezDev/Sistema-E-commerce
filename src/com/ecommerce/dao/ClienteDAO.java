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
        String sql = "SELECT * FROM clientes WHERE email = ? AND password = ? AND activo = 1;";
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

    public boolean verificarCredenciales(String email, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ? AND password = ?";

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

    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, direccion_envio = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getDireccionEnvio());
            ps.setInt(4, cliente.getId());

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
        String sql = "UPDATE clientes SET activo = 0 WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean reactivarCuenta(int id) throws SQLException {
        String sql = "UPDATE clientes SET activo = 1 WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean activarDesactivarCliente(int idCliente, boolean activo) throws SQLException {
    String sql = "UPDATE clientes SET activo = ? WHERE id = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setBoolean(1, activo);
        ps.setInt(2, idCliente);

        return ps.executeUpdate() > 0;
    }
}


    public static void verTodosRepartidores() {
    }

    public static void activarDesactivarRepartidor() {
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setEmail(rs.getString("email"));
        cliente.setPassword(rs.getString("password"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDireccionEnvio(rs.getString("direccion_envio"));

        try {
            cliente.setActivo(rs.getBoolean("activo"));
        } catch (SQLException e) {
            cliente.setActivo(true);
        }

        return cliente;
    }
}
