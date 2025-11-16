
package main.java.com.models.interfaces;

public interface IPagable {
    boolean procesarPago();
    boolean validarPago();
    String obtenerEstadoPago();
}