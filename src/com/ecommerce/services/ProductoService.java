/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.services;

import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.dao.CategoriaDAO;
import com.ecommerce.models.productos.Producto;
import com.ecommerce.models.productos.Categoria;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author USER
 */
public class ProductoService {
    
    private final ProductoDAO productoDAO;
    private final CategoriaDAO categoriaDAO;
    
    public ProductoService() {
        this.productoDAO = new ProductoDAO();
        this.categoriaDAO = new CategoriaDAO();
    }
    
    public Producto crearProducto(Producto producto) throws SQLException {
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser positivo.");
        }
        
        if (productoDAO.insertar(producto)) {
            System.out.println("Producto " + producto.getNombre() + " creado.");
            return producto;
        }
        return null;
    }
    
    public List<Producto> buscarProductos(String criterio) throws SQLException {
        return productoDAO.buscarPorNombre(criterio);
    }
    
    public Categoria crearCategoria(Categoria categoria) throws SQLException {
        if (categoriaDAO.insertar(categoria)) {
            System.out.println("Categoria " + categoria.getNombre() + " creada.");
            return categoria;
        }
        return null;
    }
    
    public List<Categoria> obtenerTodasLasCategorias() throws SQLException {
        return categoriaDAO.buscarTodos();
    }
}
