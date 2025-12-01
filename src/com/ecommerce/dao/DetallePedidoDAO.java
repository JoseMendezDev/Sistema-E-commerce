/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.pedidos.DetallePedido;
import com.ecommerce.models.productos.Producto;
import com.ecommerce.models.pedidos.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class DetallePedidoDAO extends BaseDAO<DetallePedido> {

    public DetallePedidoDAO() {
        super();
    }

    @Override
    public DetallePedido buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM detalles_pedido WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDetallePedido(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<DetallePedido> buscarTodos() throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalles_pedido";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                detalles.add(mapearDetallePedido(rs));
            }
        }
        return detalles;
    }

    public List<DetallePedido> buscarPorPedido(int pedidoId) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalles_pedido WHERE pedido_id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearDetallePedido(rs));
                }
            }
        }
        return detalles;
    }

    @Override
    public boolean insertar(DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, detalle.getPedido().getId());
            ps.setInt(2, detalle.getProducto().getId());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecioUnitario());
            ps.setDouble(5, detalle.calcularSubtotal());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        detalle.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean actualizar(DetallePedido detalle) throws SQLException {
        String sql = "UPDATE detalles_pedido SET cantidad = ?, precio_unitario = ?, subtotal = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getPrecioUnitario());
            ps.setDouble(3, detalle.calcularSubtotal());
            ps.setInt(4, detalle.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM detalles_pedido WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private DetallePedido mapearDetallePedido(ResultSet rs) throws SQLException {
        DetallePedido detalle = new DetallePedido();

        detalle.setId(rs.getInt("id"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));

        return detalle;
    }
    
    public boolean insertarDetalles(int pedidoId, List<DetallePedido> detalles) throws SQLException {
        String sql = "INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            
            for (DetallePedido detalle : detalles) {
                ps.setInt(1, pedidoId);
                ps.setInt(2, detalle.getProducto().getId());
                ps.setInt(3, detalle.getCantidad());
                ps.setDouble(4, detalle.getPrecioUnitario());
                ps.setDouble(5, detalle.calcularSubtotal());
                
                ps.addBatch();
            }
            
            int[] resultados = ps.executeBatch();
            
            for (int resultado : resultados) {
                if (resultado <= 0 && resultado != Statement.SUCCESS_NO_INFO) {
                    return false;
                }
            }
            return true;
        }
    }
}
