/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.envios.Envio;
import com.ecommerce.models.envios.EstadoEnvio;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.usuarios.Repartidor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class EnvioDAO extends BaseDAO<Envio>{
    
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;
    
    public EnvioDAO() {
        super();
        this.pedidoDAO = new PedidoDAO();
        this.repartidorDAO = new RepartidorDAO();
    }
    
    private Envio mapearEnvio(ResultSet rs) throws SQLException {
        Envio envio = new Envio();
        envio.setId(rs.getInt("id"));
        
        // Cargar Pedido
        int pedidoId = rs.getInt("pedido_id");
        Pedido pedido = null;
        try {
             pedido = pedidoDAO.buscarPorId(pedidoId);
        } catch (SQLException e) {
            System.err.println("Error al cargar Pedido para Envio: " + e.getMessage());
        }
        envio.setPedido(pedido);

        // Cargar Repartidor
        int repartidorId = rs.getInt("repartidor_id");
        if (!rs.wasNull()) { // Solo si hay un ID de repartidor
            Repartidor repartidor = null;
            try {
                repartidor = repartidorDAO.buscarPorId(repartidorId);
            } catch (SQLException e) {
                System.err.println("Error al cargar Repartidor para Envio: " + e.getMessage());
            }
            envio.asignarRepartidor(repartidor);
        }

        envio.setEstado(EstadoEnvio.valueOf(rs.getString("estado")));
        envio.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());
        
        Timestamp fechaEntregaTS = rs.getTimestamp("fecha_entrega");
        if (fechaEntregaTS != null) {
            envio.setFechaEntrega(fechaEntregaTS.toLocalDateTime());
        }
        
        envio.setCostoEnvio(rs.getDouble("costo_envio"));
        envio.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));

        return envio;
    }
    
    @Override
    public Envio buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM envios WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEnvio(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Envio> buscarTodos() throws SQLException {
        List<Envio> envios = new ArrayList<>();
        String sql = "SELECT * FROM envios ORDER BY fecha_envio DESC";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                envios.add(mapearEnvio(rs));
            }
        }
        return envios;
    }
    
    @Override
    public boolean insertar(Envio envio) throws SQLException {
        // La fechaEntrega puede ser nula inicialmente
        String sql = "INSERT INTO envios (pedido_id, repartidor_id, estado, fecha_envio, costo_envio, codigo_seguimiento) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, envio.getPedido().getId());
            if (envio.getRepartidor() != null) {
                 ps.setInt(2, envio.getRepartidor().getId());
            } else {
                 ps.setNull(2, java.sql.Types.INTEGER);
            }
            
            ps.setString(3, envio.getEstado().name());
            ps.setTimestamp(4, Timestamp.valueOf(envio.getFechaEnvio()));
            ps.setDouble(5, envio.getCostoEnvio());
            ps.setString(6, envio.getCodigoSeguimiento());

            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        envio.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean actualizar(Envio envio) throws SQLException {
        String sql = "UPDATE envios SET repartidor_id = ?, estado = ?, fecha_entrega = ?, costo_envio = ? "
                   + "WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (envio.getRepartidor() != null) {
                 ps.setInt(1, envio.getRepartidor().getId());
            } else {
                 ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, envio.getEstado().name());
            
            if (envio.getFechaEntrega() != null) {
                ps.setTimestamp(3, Timestamp.valueOf(envio.getFechaEntrega()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }
            
            ps.setDouble(4, envio.getCostoEnvio());
            ps.setInt(5, envio.getId());

            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarEstado(int id, EstadoEnvio estado) throws SQLException {
        String sql = "UPDATE envios SET estado = ? WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, id);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean asignarRepartidor(int id, Repartidor repartidor) throws SQLException {
        String sql = "UPDATE envios SET repartidor_id = ? WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (repartidor != null) {
                 ps.setInt(1, repartidor.getId());
            } else {
                 ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setInt(2, id);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM envios WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }   
}
