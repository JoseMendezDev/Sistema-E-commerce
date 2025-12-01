/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.dao;

import java.sql.*;
import java.util.List;
import com.ecommerce.database.ConexionBD;

/**
 *
 * @author USER
 */
public abstract class BaseDAO<T> {

    protected Connection conexion;

    public BaseDAO() {
        try {
            this.conexion = ConexionBD.getConexion();
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión en DAO");
            e.printStackTrace();
        }

    }

    public abstract T buscarPorId(int id) throws SQLException;

    public abstract List<T> buscarTodos() throws SQLException;

    public abstract boolean insertar(T entidad) throws SQLException;

    public abstract boolean actualizar(T entidad) throws SQLException;

    public abstract boolean eliminar(int id) throws SQLException;

    protected void cerrarRecursos(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos");
            e.printStackTrace();
        }
    }

    protected void iniciarTransaccion() throws SQLException {
        if (conexion != null) {
            conexion.setAutoCommit(false);
        }
    }

    protected void confirmarTransaccion() throws SQLException {
        if (conexion != null) {
            conexion.commit();
            conexion.setAutoCommit(true);
        }
    }

    protected void revertirTransaccion() {
        try {
            if (conexion != null) {
                conexion.rollback();
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al revertir transacción");
            e.printStackTrace();
        }
    }
}
