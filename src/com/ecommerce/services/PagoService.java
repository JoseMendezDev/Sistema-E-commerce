/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.services;

import com.ecommerce.dao.PagoDAO;
import com.ecommerce.models.pagos.Pago;
import com.ecommerce.models.pagos.EstadoPago;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PagoService {

    private final PagoDAO pagoDAO;

    public PagoService() {
        this.pagoDAO = new PagoDAO();
    }

    public Pago obtenerPagoPorId(int id) {
        try {
            return pagoDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener el pago por ID: " + e.getMessage());
            return null;
        }
    }

    public List<Pago> obtenerTodosLosPagos() {
        try {
            return pagoDAO.buscarTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pagos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean registrarPago(Pago pago) {
        if (!pago.validarPago()) {
            System.err.println("Pago inválido. El método de pago y el monto no son correctos.");
            return false;
        }

        try {
            boolean pagoProcesado = pago.procesarPago();

            boolean insertado = pagoDAO.insertar(pago);

            if (pagoProcesado && insertado) {
                System.out.println("Pago exitoso y registrado: " + pago.getNumeroTransaccion());
                return true;
            } else if (pagoProcesado && !insertado) {
                System.err.println("Pago procesado, pero no se pudo registrar en la BD.");
            } else {
                System.err.println("El método de pago falló. Estado: " + pago.getEstado());
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error de BD al registrar/procesar el pago: " + e.getMessage());
            return false;
        }
    }

    public boolean reembolsarPago(int pagoId) {
        try {
            Pago pago = pagoDAO.buscarPorId(pagoId);
            if (pago == null) {
                System.err.println("Pago no encontrado con ID: " + pagoId);
                return false;
            }

            if (pago.reembolsar()) {
                return pagoDAO.actualizar(pago);
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al intentar reembolsar el pago: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEstadoPago(int pagoId, EstadoPago nuevoEstado) {
        try {
            Pago pago = pagoDAO.buscarPorId(pagoId);
            if (pago == null) {
                System.err.println("Pago no encontrado con ID: " + pagoId);
                return false;
            }

            pago.setEstado(nuevoEstado);
            return pagoDAO.actualizar(pago);

        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del pago: " + e.getMessage());
            return false;
        }
    }

}
