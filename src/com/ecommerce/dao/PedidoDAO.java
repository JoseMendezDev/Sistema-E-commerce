/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.pedidos.EstadoPedido;
import com.ecommerce.models.pedidos.DetallePedido;
import com.ecommerce.models.usuarios.Cliente;
import com.ecommerce.models.productos.Producto;
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
    private ProductoDAO productoDAO = new ProductoDAO();
    
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
    
    public List<Pedido> buscarPorEstado(String estado) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE estado = ? ORDER BY fecha_pedido DESC";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, estado);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        }
        return pedidos;
    }
    
    public List<Pedido> buscarPorEstado(EstadoPedido estado) throws SQLException {
        return buscarPorEstado(estado.toString());
    }
    
    @Override
    public boolean insertar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_id, direccion_envio, total, estado, fecha_pedido) VALUES (?, ?, ?, ?, GETDATE())";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getCliente().getId());
            ps.setString(2, pedido.getDireccionEnvio());
            ps.setDouble(3, pedido.calcularTotal());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    }
                }
                
                insertarDetalles(pedido);
                
                actualizarStockDespuesDePedido(pedido);
                return true;
            }
        }
        return false;
    }
    
    private void insertarDetalles(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, precio_unitario) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                ps.setInt(1, pedido.getId());
                ps.setInt(2, detalle.getProducto().getId());
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getPrecioUnitario());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    private void actualizarStockDespuesDePedido(Pedido pedido) throws SQLException {
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            int cantidadVendida = detalle.getCantidad();
            
            // Obtener stock actual
            Producto productoBD = productoDAO.buscarPorId(producto.getId());
            int stockActual = productoBD.getInventario().getStockActual();
            int nuevoStock = stockActual - cantidadVendida;
            
            // Actualizar stock
            productoDAO.actualizarStock(producto.getId(), nuevoStock);
        }
    }
    
    @Override
    public boolean actualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET cliente_id = ?, direccion_envio = ?, total = ?, estado = ? WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedido.getCliente().getId());
            ps.setString(2, pedido.getDireccionEnvio());
            ps.setDouble(3, pedido.getTotal());
            ps.setInt(4, pedido.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarEstado(int pedidoId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, pedidoId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizarEstado(int pedidoId, EstadoPedido nuevoEstado) throws SQLException {
        return actualizarEstado(pedidoId, nuevoEstado.toString());
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
        
        int clienteId = rs.getInt("cliente_id");
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        pedido.setCliente(cliente);
            
        pedido.setDireccionEnvio(rs.getString("direccion_envio"));
        pedido.setTotal(rs.getDouble("total"));

        String estadoStr = rs.getString("estado");
        
        try {
            pedido.setEstado(EstadoPedido.valueOf(estadoStr));
        } catch (IllegalArgumentException e) {
            System.err.println("Estado no reconocido: " + estadoStr + ". Usando CREADO como default.");
            pedido.setEstado(EstadoPedido.CREADO);
        }
        
        Timestamp ts = rs.getTimestamp("fecha_pedido");
        if (ts != null) {
             pedido.setFechaPedido(ts.toLocalDateTime());
        } else {
             pedido.setFechaPedido(LocalDateTime.now());
        }
        
        cargarDetalles(pedido);
        
        return pedido;
    }
    
    private void cargarDetalles(Pedido pedido) throws SQLException {
        String sql = "SELECT dp.*, p.nombre as producto_nombre, p.precio as producto_precio " +
                     "FROM detalles_pedido dp " +
                     "INNER JOIN productos p ON dp.producto_id = p.id " +
                     "WHERE dp.pedido_id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedido.getId());
            
            try (ResultSet rs = ps.executeQuery()) {
                List<DetallePedido> detalles = new ArrayList<>();
                
                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setId(rs.getInt("id"));
                    
                    // Obtener producto
                    int productoId = rs.getInt("producto_id");
                    Producto producto = productoDAO.buscarPorId(productoId);
                    
                    if (producto == null) {
                        // Si no existe, crear uno básico con los datos disponibles
                        producto = new Producto();
                        producto.setId(productoId);
                        producto.setNombre(rs.getString("producto_nombre"));
                        producto.setPrecio(rs.getDouble("producto_precio"));
                    }
                    
                    detalle.setProducto(producto);
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    
                    detalles.add(detalle);
                }
                
                pedido.setDetalles(detalles);
            }
        }
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
