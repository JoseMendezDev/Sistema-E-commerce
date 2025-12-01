/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.services;

import com.ecommerce.dao.ClienteDAO;
import com.ecommerce.models.usuarios.Cliente;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public Cliente registrarCliente(Cliente cliente) throws SQLException {

        if (clienteDAO.existeEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya esta registrado.");
        }

        if (clienteDAO.insertar(cliente)) {
            System.out.println("Cliente " + cliente.getNombre() + " registrado con exito.");
            return cliente;
        }
        return null;
    }

    public Cliente autenticarCliente(String email, String password) throws SQLException {
        Cliente cliente = clienteDAO.autenticar(email, password);

        if (cliente != null) {
            System.out.println("Cliente " + cliente.getNombre() + " autenticado.");
        }
        return cliente;
    }
}
