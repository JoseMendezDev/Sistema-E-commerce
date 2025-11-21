
package main.java.com.models.productos;

import main.java.models.interfaces.IBuscable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Categoria implements IBuscable<Producto>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String nombre;
    private String descripcion;
    private Categoria categoriaPadre;
    private List<Categoria> subcategorias;
    private List<Producto> productos;
    
    public Categoria() {
        this.subcategorias = new ArrayList<>();
        this.productos = new ArrayList<>();
    }
    
    public Categoria(String nombre, String descripcion) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    public void agregarSubcategoria(Categoria subcategoria) {
        subcategoria.setCategoriaPadre(this);
        this.subcategorias.add(subcategoria);
    }
    
    public void agregarProducto(Producto producto) {
        if (!productos.contains(producto)) {
            productos.add(producto);
            producto.setCategoria(this);
        }
    }
    
    public List<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }
    
    @Override
    public List<Producto> buscar(String criterio) {
        return productos.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                        p.getDescripcion().toLowerCase().contains(criterio.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Producto> filtrar(Map<String, Object> filtros) {
        return productos.stream()
            .filter(p -> aplicarFiltros(p, filtros))
            .collect(Collectors.toList());
    }
    
    private boolean aplicarFiltros(Producto p, Map<String, Object> filtros) {
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            switch (filtro.getKey()) {
                case "precioMin":
                    if (p.getPrecio() < (Double) filtro.getValue()) return false;
                    break;
                case "precioMax":
                    if (p.getPrecio() > (Double) filtro.getValue()) return false;
                    break;
                