/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.pagos.Pago;
import com.ecommerce.models.pagos.EstadoPago;
import com.ecommerce.models.abstracto.MetodoPago;
import com.ecommerce.models.pagos.TarjetaCredito; 
import com.ecommerce.models.pedidos.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PagoDAO extends BaseDAO<Pago>{
    
    @Override
    public Pago buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<Pago> buscarTodos() throws SQLException {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pagos ORDER BY fecha_pago DESC";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pagos.add(mapearPago(rs));
            }
        }
        return pagos;
    }
    
    @Override
    public boolean insertar(Pago pago) throws SQLException {
        String sql = "INSERT INTO pagos (pedido_id, metodo_pago_id, monto, estado, fecha_pago, numero_transaccion) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pago.getPedido().getId()); 
            ps.setInt(2, pago.getMetodoPago().getId()); 
            ps.setDouble(3, pago.getMonto());
            ps.setString(4, pago.getEstado().name());
            ps.setTimestamp(5, pago.getFechaPago() != null ? Timestamp.valueOf(pago.getFechaPago()) : null);
            ps.setString(6, pago.getNumeroTransaccion());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pago.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean actualizar(Pago pago) throws SQLException {
        String sql = "UPDATE pagos SET pedido_id = ?, metodo_pago_id = ?, monto = ?, estado = ?, fecha_pago = ?, numero_transaccion = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pago.getPedido().getId());
            ps.setInt(2, pago.getMetodoPago().getId());
            ps.setDouble(3, pago.getMonto());
            ps.setString(4, pago.getEstado().name());
            ps.setTimestamp(5, pago.getFechaPago() != null ? Timestamp.valueOf(pago.getFechaPago()) : null);
            ps.setString(6, pago.getNumeroTransaccion());
            ps.setInt(7, pago.getId());

            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM pagos WHERE id = ?";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setId(rs.getInt("id"));
        pago.setMonto(rs.getDouble("monto"));
        pago.setEstado(EstadoPago.valueOf(rs.getString("estado")));
        pago.setNumeroTransaccion(rs.getString("numero_transaccion"));

        Timestamp fechaPagoTs = rs.getTimestamp("fecha_pago");
        if (fechaPagoTs != null) {
            pago.setFechaPago(fechaPagoTs.toLocalDateTime());
        }

        return pago;
    }
    
}
