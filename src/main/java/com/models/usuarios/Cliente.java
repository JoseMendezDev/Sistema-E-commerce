
package main.java.com.models.usuarios;

import main.java.com.models.abstracto.Usuario;
import main.java.com.models.carrito.CarritoCompras;
import main.java.com.models.pedidos.Pedido;
import main.java.com.models.productos.Producto;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String direccionEnvio;
    private List<Pedido> historialPedidos;
    private CarritoCompras carrito;
    
    public Cliente() {
        super();
        this.historialPedidos = new ArrayList<>();
        this.carrito = new CarritoCompras(this);
    }
    
    public Cliente(String email, String password, String nombre, String telefono, String direccionEnvio) {
        super(email, password, nombre, telefono);
        this.direccionEnvio = direccionEnvio;
        this.historialPedidos = new ArrayList<>();
        this.carrito = new CarritoCompras(this);
    }
    
    public Pedido realizarPedido() {
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        Pedido pedido = carrito.convertirAPedido();
        historialPedidos.add(pedido);
        return pedido;
    }
    
    public void agregarAlCarrito(Producto producto, int cantidad) {
        carrito.agregarItem(producto, cantidad);
    }
    
    public List<Pedido> verHistorial() {
        return new ArrayList<>(historialPedidos);
    }
    
    @Override
    public void cerrarSesion() {
        this.autenticado = false;
        System.out.println("Cliente " + nombre + " ha cerrado sesión");
    }
    
    // Getters y Setters
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public CarritoCompras getCarrito() { return carrito; }
    public List<Pedido> getHistorialPedidos() { return historialPedidos; }
}