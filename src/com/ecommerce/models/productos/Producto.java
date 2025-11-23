
package com.ecommerce.models.productos;

import com.ecommerce.models.interfaces.IBuscable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Producto implements IBuscable<Producto>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagen;
    private boolean activo;
    private Categoria categoria;
    private Inventario inventario;
    
    public Producto() {
        this.activo = true;
    }
    
    public Producto(String nombre, String descripcion, double precio, Categoria categoria) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.inventario = new Inventario(this, 0);
    }
    
    public void actualizarPrecio(double nuevoPrecio) {
        if (nuevoPrecio > 0) {
            this.precio = nuevoPrecio;
        }
    }
    
    public void actualizarStock(int cantidad) {
        if (inventario != null) {
            inventario.agregarStock(cantidad);
        }
    }
    
    public boolean estaDisponible() {
        return activo && inventario != null && inventario.getStockActual() > 0;
    }
    
    @Override
    public List<Producto> buscar(String criterio) {
        List<Producto> resultado = new ArrayList<>();
        if (this.nombre.toLowerCase().contains(criterio.toLowerCase()) ||
            this.descripcion.toLowerCase().contains(criterio.toLowerCase())) {
            resultado.add(this);
        }
        return resultado;
    }
    
    @Override
    public List<Producto> filtrar(Map<String, Object> filtros) {
        List<Producto> resultado = new ArrayList<>();
        boolean cumpleFiltros = true;
        
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            switch (filtro.getKey()) {
                case "precioMin":
                    if (this.precio < (Double) filtro.getValue()) cumpleFiltros = false;
                    break;
                case "precioMax":
                    if (this.precio > (Double) filtro.getValue()) cumpleFiltros = false;
                    break;
            }
        }
        
        if (cumpleFiltros) resultado.add(this);
        return resultado;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Inventario getInventario() { return inventario; }
    public void setInventario(Inventario inventario) { this.inventario = inventario; }
}