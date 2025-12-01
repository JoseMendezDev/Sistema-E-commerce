/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.pedidos.EstadoPedido;
import com.ecommerce.models.pedidos.DetallePedido;
import com.ecommerce.models.usuarios.Cliente;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PedidoDAO extends BaseDAO<Pedido>  {
    
    private ClienteDAO clienteDAO = new ClienteDAO();
    private DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
    
    @Override
    public Pedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Pedido> buscarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha_pedido DESC";
        
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        }
        return pedidos;
    }
    
    public List<Pedido> buscarPorCliente(int clienteId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE cliente_id = ? ORDER BY fecha_pedido DESC";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        }
        return pedidos;
    }
    
    public List<Pedido> buscarPorEstado(EstadoPedido estado) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE estado = ? ORDER BY fecha_pedido DESC";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        }
        return pedidos;
    }
    
    @Override
    public boolean insertar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_id, direccion_envio, total, estado, fecha_pedido) VALUES (?, ?, ?, ?, NOW())";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getCliente().getId());
            ps.setString(2, pedido.getDireccionEnvio());
            ps.setDouble(3, pedido.calcularTotal());
            ps.setString(4, pedido.getEstado().name());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean actualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET cliente_id = ?, direccion_envio = ?, total = ?, estado = ? WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedido.getCliente().getId());
            ps.setString(2, pedido.getDireccionEnvio());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getEstado().name());
            ps.setInt(5, pedido.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarEstado(int pedidoId, EstadoPedido nuevoEstado) throws SQLException {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, pedidoId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "UPDATE pedidos SET estado = 'CANCELADO' WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
            
        pedido.setDireccionEnvio(rs.getString("direccion_envio"));
        pedido.setTotal(rs.getDouble("total"));
        pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
        
        Timestamp ts = rs.getTimestamp("fecha_pedido");
        if (ts != null) {
             pedido.setFechaPedido(ts.toLocalDateTime());
        } else {
             pedido.setFechaPedido(LocalDateTime.MIN); // Valor por defecto
        }
        
        return pedido;
    }
    
    public List<DetallePedido> buscarDetallesPorPedido(int pedidoId) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedidos WHERE pedido_id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setId(rs.getInt("id"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }
}
