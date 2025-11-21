
package main.java.com.models.interfaces;

import java.util.List;
import java.util.Map;

public interface IBuscable<T> {
    List<T> buscar(String criterio);
    List<T> filtrar(Map<String, Object> filtros);
}
