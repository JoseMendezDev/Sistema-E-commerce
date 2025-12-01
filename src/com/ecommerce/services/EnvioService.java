/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.services;

import com.ecommerce.dao.EnvioDAO;
import com.ecommerce.models.envios.Envio;
import com.ecommerce.models.envios.EstadoEnvio;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.usuarios.Repartidor;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author USER
 */
public class EnvioService {

    private final EnvioDAO envioDAO;

    public EnvioService() {
        this.envioDAO = new EnvioDAO();
    }

    public Envio crearEnvio(Pedido pedido) {
        try {
            Envio envio = new Envio(pedido);

            if (envioDAO.insertar(envio)) {
                return envio;
            }
            return null;

        } catch (SQLException e) {
            System.err.println("Error al crear el envio: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos al crear el envio", e);
        }
    }

    public Envio buscarEnvio(int id) {
        try {
            return envioDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar el envío: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos al buscar el envío", e);
        }
    }

    public List<Envio> obtenerTodosLosEnvios() {
        try {
            return envioDAO.buscarTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los envíos: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos al obtener envíos", e);
        }
    }

    public boolean asignarRepartidor(int envioId, Repartidor repartidor) {
        try {
            Envio envio = envioDAO.buscarPorId(envioId);
            if (envio != null && repartidor != null) {
                envio.asignarRepartidor(repartidor);
                envio.setEstado(EstadoEnvio.EN_TRANSITO);
                envio.setFechaEnvio(LocalDateTime.now());

                return envioDAO.actualizar(envio);
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al asignar repartidor: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos al asignar repartidor", e);
        }
    }
    
    public boolean actualizarEstadoEnvio(int envioId, EstadoEnvio nuevoEstado) {
        try {
            boolean exito = envioDAO.actualizarEstado(envioId, nuevoEstado);
            
            if (exito && nuevoEstado == EstadoEnvio.ENTREGADO) {
                Envio envio = envioDAO.buscarPorId(envioId);
                if (envio != null) {
                    envio.setFechaEntrega(LocalDateTime.now());
                    return envioDAO.actualizar(envio);
                }
            }
            return exito;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del envío: " + e.getMessage());
            throw new RuntimeException("Error en la base de datos al actualizar estado", e);
        }
    }
    
    public int obtenerTiempoEstimado(int envioId) {
        Envio envio = buscarEnvio(envioId);
        if (envio != null) {
            return envio.obtenerTiempoEstimado();
        }
        return -1;
    }
}
