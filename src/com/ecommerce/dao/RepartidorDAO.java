/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.envios.Automovil;
import com.ecommerce.models.pedidos.EstadoPedido;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.usuarios.Repartidor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class RepartidorDAO extends BaseDAO<Repartidor> {

    @Override
    public Repartidor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM repartidores WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearRepartidor(rs);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Repartidor> buscarTodos() throws SQLException {
        List<Repartidor> repartidores = new ArrayList<>();

    String sql = "SELECT * FROM repartidores";

    try(PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){

        while(rs.next()){
            repartidores.add(mapearRepartidor(rs));
        }
    }
    return repartidores;
    }

    @Override
    public boolean insertar(Repartidor entidad) throws SQLException {

        String sql = "INSERT INTO repartidores (email, password, nombre, telefono, vehiculo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entidad.getEmail());
            ps.setString(2, entidad.getPassword());
            ps.setString(3, entidad.getNombre());
            ps.setString(4, entidad.getTelefono());
            ps.setString(5, entidad.getVehiculo().getDescripcion());
            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean actualizar(Repartidor entidad) throws SQLException {

    String sql = "UPDATE repartidores SET nombre = ?, telefono = ?, vehiculo = ? WHERE id = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {

        ps.setString(1, entidad.getNombre());
        ps.setString(2, entidad.getTelefono());
        ps.setString(3, entidad.getVehiculo() != null
                ? entidad.getVehiculo().getDescripcion()
                : null);
        ps.setInt(4, entidad.getId());

        return ps.executeUpdate() > 0;
    }
}

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE repartidor SET activo = 0 WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
    
    public Repartidor buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM repartidores WHERE email = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearRepartidor(rs);
            }
        } finally {
            cerrarRecursos(rs, ps);
        }
        return null;
    }
    
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM repartidores WHERE email = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    public boolean reactivarCuenta(int id) throws SQLException {
        String sql = "UPDATE repartidores SET activo = 1 WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean ocupar(int idRepartidor) throws SQLException {
        String sql = "UPDATE repartidores SET disponible = 0 WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idRepartidor);
            return ps.executeUpdate() > 0;
        }
    }
    
    public Repartidor autenticar(String email, String password) throws SQLException {
        String sql = "SELECT * FROM repartidores WHERE email = ? AND password = ? AND activo = 1;";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearRepartidor(rs);
            }
        } finally {
            cerrarRecursos(rs, ps);
        }
        return null;
    }
    
    public boolean actualizarDisponibilidad(int id, boolean disponible) throws SQLException {
    String sql = "UPDATE repartidores SET disponible = ? WHERE id = ?";

    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setBoolean(1, disponible);
        ps.setInt(2, id);

        return ps.executeUpdate() > 0;
    }
}

    public boolean estaDisponible(int id) throws SQLException {
        String sql = "SELECT disponible FROM repartidores WHERE id = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("disponible");
        }
        return false;
    }
    
    public boolean activarDesactivarRepartidor(int id, boolean disponible) throws SQLException {
        String sql = "UPDATE repartidores SET activo = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setBoolean(1, disponible);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Pedido> verPedidosAsignados(Repartidor repartidor) {
        List<Pedido> pedidos = new ArrayList<>();

        String sql
                = "SELECT p.id AS pedido_id, p.estado AS pedido_estado "
                + "FROM envios e "
                + "INNER JOIN pedidos p ON e.pedido_id = p.id "
                + "WHERE e.repartidor_id = ? AND e.estado <> 'ENTREGADO'";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, repartidor.getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("→ pedido encontrado: " + rs.getInt("pedido_id") + " - " + rs.getString("pedido_estado"));
                Pedido p = new Pedido();
                p.setId(rs.getInt("pedido_id"));
                p.setEstado(EstadoPedido.valueOf(rs.getString("pedido_estado").toUpperCase()));
                pedidos.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error obteniendo pedidos asignados: " + e.getMessage());
        }

        return pedidos;
    }
    
    public void liberarRepartidor(int id) {
        String sql = "UPDATE repartidores SET disponible = 1 WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error liberando repartidor " + e.getMessage());
        }
    }

    private Repartidor mapearRepartidor(ResultSet rs) throws SQLException {
        Repartidor repartidor = new Repartidor();

        repartidor.setId(rs.getInt("id"));
        repartidor.setEmail(rs.getString("email"));
        repartidor.setPassword(rs.getString("password"));
        repartidor.setNombre(rs.getString("nombre"));
        repartidor.setTelefono(rs.getString("telefono"));

        String vehiculoNombre = rs.getString("vehiculo");
        if (vehiculoNombre != null && !vehiculoNombre.isEmpty()) {
            repartidor.setVehiculo(new Automovil(vehiculoNombre));
        }

        repartidor.setDisponible(rs.getBoolean("disponible"));
        repartidor.setActivo(rs.getBoolean("activo"));

        return repartidor;

    }
}
