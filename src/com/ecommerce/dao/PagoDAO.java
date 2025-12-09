/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import com.ecommerce.models.pagos.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PagoDAO extends BaseDAO<Pago> {

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

    public Pago buscarPorPedidoId(int pedidoId) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE pedido_id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);

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

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pagos.add(mapearPago(rs));
            }
        }
        return pagos;
    }

    @Override
    public boolean insertar(Pago pago) throws SQLException {
    String sql = "INSERT INTO pagos (pedido_id, metodo_pago, monto, estado, detalles, numero_transaccion) "
               + "OUTPUT inserted.id "
               + "VALUES (?, ?, ?, ?, ?, ?)";
    System.out.println("EJECUTANDO: PagoDao.insertar(Pago)");
    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, pago.getPedidoId());
        ps.setString(2, pago.getMetodoPago());
        ps.setDouble(3, pago.getMonto());
        ps.setString(4, pago.getEstado());
        ps.setString(5, pago.getDetalles() != null ? pago.getDetalles() : "");
        ps.setString(6, pago.getNumeroTransaccion() != null ? pago.getNumeroTransaccion()
                : "COD-" + System.currentTimeMillis());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                pago.setId(rs.getInt(1));
            }
        }
        actualizarEstadoPedido(pago.getPedidoId(), "PAGADO");
        return true;
    } catch (SQLException ex) {
        System.err.println("Error en pago: " + ex.getMessage());
        throw ex;
    }
}

    @Override
    public boolean actualizar(Pago pago) throws SQLException {
        String sql = "UPDATE pagos SET estado = ?, detalles = ?, numero_transaccion = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, pago.getEstado());
            ps.setString(2, pago.getDetalles());
            ps.setString(3, pago.getNumeroTransaccion());
            ps.setInt(4, pago.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(int pagoId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE pagos SET estado = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, pagoId);

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

    private void actualizarEstadoPedido(int pedidoId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, pedidoId);
            ps.executeUpdate();
        }
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setId(rs.getInt("id"));
        pago.setPedidoId(rs.getInt("pedido_id"));
        pago.setMetodoPago(rs.getString("metodo_pago"));
        pago.setMonto(rs.getDouble("monto"));
        pago.setEstado(rs.getString("estado"));
        pago.setDetalles(rs.getString("detalles"));
        pago.setNumeroTransaccion(rs.getString("numero_transaccion"));

        Timestamp ts = rs.getTimestamp("fecha_pago");
        if (ts != null) {
            pago.setFechaPago(ts.toLocalDateTime());
        }

        return pago;
    }

}
