
package main.java.com.models.abstracto;

import main.java.com.models.interfaces.IPagable;
import java.io.Serializable;

public abstract class MetodoPago implements IPagable, Serializable {
    private static final long serialVersionUID = 1L;
    
    protected int id;
    protected String tipo;
    protected boolean activo;
    
    public MetodoPago(String tipo) {
        this.tipo = tipo;
        this.activo = true;
    }
    
    public abstract boolean validar();
    public abstract boolean procesar(double monto);
    
    @Override
    public boolean procesarPago() {
        return activo && validar();
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTipo() { return tipo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}