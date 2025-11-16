
package main.java.com.models.interfaces;

    public interface IAutenticable {
    boolean autenticar(String email, String password);
    void cerrarSesion();
    boolean estaAutenticado();
}

