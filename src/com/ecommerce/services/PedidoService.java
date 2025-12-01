/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.services;

import com.ecommerce.dao.PedidoDAO;
import com.ecommerce.models.pedidos.EstadoPedido;
import com.ecommerce.models.pedidos.Pedido;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
    }

    public Pedido buscarPedidoPorId(int id) throws SQLException {
        return pedidoDAO.buscarPorId(id);
    }

    public boolean actualizarEstadoPedido(int pedidoId, EstadoPedido nuevoEstado) {
        try {
            Pedido pedido = pedidoDAO.buscarPorId(pedidoId);
            if (pedido != null) {
                pedido.actualizarEstado(nuevoEstado);
                return pedidoDAO.actualizar(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del pedido: " + e.getMessage());
        }
        return false;
    }
}
