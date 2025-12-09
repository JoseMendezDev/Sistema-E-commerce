/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ui;

import com.ecommerce.dao.*;
import com.ecommerce.models.envios.*;
import com.ecommerce.models.pedidos.*;
import com.ecommerce.models.productos.*;
import com.ecommerce.models.usuarios.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class MenuAdministrador {

    private static Scanner scanner = new Scanner(System.in);
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static InventarioDAO inventarioDAO = new InventarioDAO();
    private static PedidoDAO pedidoDAO = new PedidoDAO();
    private static EnvioDAO envioDAO = new EnvioDAO();
    private static RepartidorDAO repartidorDAO = new RepartidorDAO();
    private static ClienteDAO clienteDAO = new ClienteDAO();
    

    public static void mostrar(Administrador admin) {
        boolean salir = false;

        while (!salir) {
            mostrarEncabezado();

            try {
                String input = scanner.nextLine();

                if (input == null || input.trim().isEmpty()) {
                    System.out.println("Error: No se ingresó ninguna opción.");
                    continue;
                }
                int opcion = Integer.parseInt(input);

                switch (opcion) {
                    case 1:
                        submenuProductos();
                        break;
                    case 2:
                        submenuCategorias();
                        break;
                    case 3:
                        submenuInventario();
                        break;
                    case 4:
                        submenuPedidos();
                        break;
                    case 5:
                        submenuUsuarios();
                        break;
                    case 6:
                        submenuRepartidores();
                        break;
                    case 0:
                        System.out.println("Sesión cerrada.");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                System.out.println("Por favor intente nuevamente.");
            }
        }
    }

    private static void mostrarEncabezado() {
        System.out.println("       MENÚ ADMINISTRADOR");

        System.out.println("\n1. GESTIÓN DE PRODUCTOS");
        System.out.println("2. GESTIÓN DE CATEGORIAS");
        System.out.println("3. GESTIÓN DE INVENTARIO");
        System.out.println("4. GESTIÓN DE PEDIDOS");
        System.out.println("5. GESTIÓN DE USUARIOS");
        System.out.println("6. GESTIÓN DE REPARTIDORES");

        System.out.println("\n0. Cerrar sesión");
        System.out.print("Seleccione opción: ");

    }

    private static void submenuProductos() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n     GESTIÓN DE PRODUCTOS:");
            System.out.println("1. Ver todos los productos");
            System.out.println("2. Agregar nuevo producto");
            System.out.println("3. Editar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("\n0. Volver al menú principal");

            System.out.print("Opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verProductos();
                        break;
                    case 2:
                        agregarProducto();
                        break;
                    case 3:
                        editarProducto();
                        break;
                    case 4:
                        eliminarProducto();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número.");
            }

        }
    }

    public static void verProductos() {
        System.out.println("\n=== LISTA DE PRODUCTOS ===");

        try {
            List<Producto> productos = productoDAO.buscarTodosAdministrador();

            if (productos.isEmpty()) {
                System.out.println("No hay productos registrados.");
                return;
            }

            System.out.printf("| %-4s | %-20s | %-30s | %-10s | %-8s | %-15s |\n",
                    "ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría");

            for (Producto producto : productos) {
                String nombre = producto.getNombre();
                if (nombre.length() > 20) {
                    nombre = nombre.substring(0, 17) + "...";
                }

                String descripcion = producto.getDescripcion();
                if (descripcion.length() > 30) {
                    descripcion = descripcion.substring(0, 27) + "...";
                }

                String categoriaNombre = "Sin categoría";
                if (producto.getCategoria() != null && producto.getCategoria().getNombre() != null) {
                    categoriaNombre = producto.getCategoria().getNombre();
                    if (categoriaNombre.length() > 15) {
                        categoriaNombre = categoriaNombre.substring(0, 12) + "...";
                    }
                }

                int stock = 0;
                if (producto.getInventario() != null) {
                    stock = producto.getInventario().getStockActual();
                }

                System.out.printf("| %-4d | %-20s | %-30s | S/.%-9.2f | %-8d | %-15s |\n",
                        producto.getId(),
                        nombre,
                        descripcion,
                        producto.getPrecio(),
                        stock,
                        categoriaNombre);
            }

            System.out.println("\nTotal de productos: " + productos.size());

        } catch (SQLException e) {
            System.out.println("Error al obtener los productos: " + e.getMessage());
        }

    }

    public static void agregarProducto() {
        System.out.println("\n=== AGREGAR NUEVO PRODUCTO ===");

        try {
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("Error: Nombre requerido.");
                return;
            }

            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine().trim();
            if (descripcion.isEmpty()) {
                System.out.println("Error: Descripción requerida.");
                return;
            }

            double precio = obtenerPrecioValido();
            if (precio <= 0) {
                return;
            }

            Categoria categoria = seleccionarCategoria();
            if (categoria == null) {
                return;
            }

            int stock = obtenerStockValido();

            mostrarResumenProducto(nombre, descripcion, precio, categoria, stock);
            if (!confirmarOperacion("¿Crear producto?")) {
                return;
            }

            Producto nuevoProducto = new Producto(nombre, descripcion, precio, categoria);

            if (productoDAO.insertar(nuevoProducto)) {
                if (stock > 0) {
                    productoDAO.actualizarStock(nuevoProducto.getId(), stock);
                }
                System.out.println("Producto creado. ID: " + nuevoProducto.getId());
            } else {
                System.out.println("Error al guardar.");
            }

        } catch (SQLException e) {
            System.out.println("Error BD: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static double obtenerPrecioValido() {
        while (true) {
            try {
                System.out.print("Precio (S/.): ");
                double precio = Double.parseDouble(scanner.nextLine());
                if (precio > 0) {
                    return precio;
                }
                System.out.println("Precio debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private static Categoria seleccionarCategoria() throws SQLException {
        List<Categoria> categorias = categoriaDAO.buscarCategorias();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías. Cree una desde el menú categorías.");
            return null;
        }

        System.out.println("\n--- CATEGORÍAS ---");
        for (Categoria cat : categorias) {
            System.out.println(cat.getId() + ". " + cat.getNombre());
        }

        while (true) {
            try {
                System.out.print("ID de categoría: ");
                int id = Integer.parseInt(scanner.nextLine());

                for (Categoria cat : categorias) {
                    if (cat.getId() == id) {
                        return cat;
                    }
                }
                System.out.println("ID no válido. Intente nuevamente.");

            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número.");
            }
        }
    }

    private static int obtenerStockValido() {
        while (true) {
            try {
                System.out.print("Stock inicial: ");
                int stock = Integer.parseInt(scanner.nextLine());
                if (stock >= 0) {
                    return stock;
                }
                System.out.println("Stock no puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número.");
            }
        }
    }

    private static void mostrarResumenProducto(String nombre, String descripcion,
            double precio, Categoria categoria, int stock) {
        System.out.println("\n--- RESUMEN ---");
        System.out.println("Nombre: " + nombre);
        System.out.println("Descripción: " + descripcion);
        System.out.printf("Precio: S/.%.2f\n", precio);
        System.out.println("Categoría: " + categoria.getNombre());
        System.out.println("Stock: " + stock);
    }

    private static boolean confirmarOperacion(String mensaje) {
        System.out.print("\n" + mensaje + " (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S") || respuesta.equals("SI");
    }

    public static void editarProducto() {
        System.out.println("\n=== EDITAR PRODUCTO ===");

        try {
            System.out.println("\nProductos disponibles:");
            System.out.println("-----------------------");

            List<Producto> productos = productoDAO.buscarTodosAdministrador();

            if (productos.isEmpty()) {
                System.out.println("No hay productos para editar.");
                return;
            }

            for (Producto producto : productos) {
                System.out.println(producto.getId() + ". " + producto.getNombre()
                        + " - S/." + producto.getPrecio());
            }

            System.out.print("\nIngrese el ID del producto a editar (0 para cancelar): ");
            int idProducto;

            try {
                idProducto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                return;
            }

            if (idProducto == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                System.out.println("Producto no encontrado.");
                return;
            }

            System.out.println("\n=== EDITANDO PRODUCTO ===");
            System.out.println("Producto: " + producto.getNombre() + " (ID: " + producto.getId() + ")");
            System.out.println("-----------------------------------");

            System.out.println("\nDATOS ACTUALES:");
            System.out.println("1. Nombre: " + producto.getNombre());
            System.out.println("2. Descripción: " + producto.getDescripcion());
            System.out.printf("3. Precio: S/.%.2f\n", producto.getPrecio());

            boolean seguirEditando = true;

            while (seguirEditando) {
                System.out.println("\n¿Qué desea editar?");
                System.out.println("1. Cambiar nombre");
                System.out.println("2. Cambiar descripción");
                System.out.println("3. Cambiar precio");
                System.out.println("\n4. Guardar cambios y salir");
                System.out.println("5. Cancelar sin guardar");

                System.out.print("\nSeleccione opción: ");

                try {
                    int opcion = Integer.parseInt(scanner.nextLine());

                    switch (opcion) {
                        case 1:
                            System.out.println("\n--- CAMBIAR NOMBRE ---");
                            System.out.println("Nombre actual: " + producto.getNombre());
                            System.out.print("Nuevo nombre: ");
                            String nuevoNombre = scanner.nextLine().trim();

                            if (nuevoNombre.isEmpty()) {
                                System.out.println("El nombre no puede estar vacío.");
                            } else {
                                producto.setNombre(nuevoNombre);
                                System.out.println("Nombre actualizado.");
                            }
                            break;

                        case 2:
                            System.out.println("\n--- CAMBIAR DESCRIPCIÓN ---");
                            System.out.println("Descripción actual: " + producto.getDescripcion());
                            System.out.print("Nueva descripción: ");
                            String nuevaDescripcion = scanner.nextLine().trim();

                            if (nuevaDescripcion.isEmpty()) {
                                System.out.println("La descripción no puede estar vacía.");
                            } else {
                                producto.setDescripcion(nuevaDescripcion);
                                System.out.println("Descripción actualizada.");
                            }
                            break;

                        case 3:
                            System.out.println("\n--- CAMBIAR PRECIO ---");
                            System.out.printf("Precio actual: S/.%.2f\n", producto.getPrecio());

                            boolean precioValido = false;

                            while (!precioValido) {
                                try {
                                    System.out.print("Nuevo precio: S/.");
                                    double nuevoPrecio = Double.parseDouble(scanner.nextLine());

                                    if (nuevoPrecio <= 0) {
                                        System.out.println("El precio debe ser mayor a 0.");
                                        continue;
                                    }

                                    producto.setPrecio(nuevoPrecio);
                                    System.out.printf("Precio actualizado a S/.%.2f\n", nuevoPrecio);
                                    precioValido = true;

                                } catch (NumberFormatException e) {
                                    System.out.println("Ingrese un precio válido.");
                                }
                            }
                            break;

                        case 4:
                            try {
                                if (productoDAO.actualizar(producto)) {
                                    System.out.println("Cambios guardados exitosamente.");
                                } else {
                                    System.out.println("Error al guardar los cambios.");
                                }
                            } catch (SQLException e) {
                                System.out.println("Error al guardar: " + e.getMessage());
                            }
                            seguirEditando = false;
                            break;

                        case 5:
                            System.out.println("Edición cancelada. No se guardaron los cambios.");
                            return;

                        default:
                            System.out.println("Opción inválida.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void eliminarProducto() {
        try {
            System.out.println("=== ELIMINAR PRODUCTO ===");
            System.out.print("Ingrese ID del producto a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Producto producto = productoDAO.buscarPorId(id);

            if (producto == null) {
                System.out.println("No existe un producto con ese ID.");
                return;
            }

            System.out.println("\n¿Está seguro que desea eliminar?");
            System.out.println("ID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("(S/N): ");
            String confirmacion = scanner.nextLine();

            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean eliminado = productoDAO.eliminar(id);

            if (eliminado) {
                System.out.println("Producto eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar el producto.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: debe ingresar un número válido.");
        } catch (SQLException e) {
            System.out.println("Error de BD: " + e.getMessage());
        }
    }

    private static void submenuCategorias() {
        boolean volver = false;

        while (!volver) {
            System.out.println("         GESTIÓN DE CATEGORÍAS");

            System.out.println("\n1. Ver todas las categorías");
            System.out.println("2. Agregar nueva categoría");
            System.out.println("3. Editar categoría");
            System.out.println("4. Eliminar categoría");

            System.out.println("\n0. Volver al menú principal");
            System.out.print("Opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verTodasCategorias();
                        break;
                    case 2:
                        agregarCategoria();
                        break;
                    case 3:
                        editarCategoria();
                        break;
                    case 4:
                        eliminarCategoria();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println(" Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Ingrese un número.");
            }

        }
    }

    private static void verTodasCategorias() {
        try {
            System.out.println("\n=== LISTA DE CATEGORÍAS ===");

            List<Categoria> categorias = categoriaDAO.buscarTodos();

            if (categorias.isEmpty()) {
                System.out.println("No existen categorías registradas.");
                return;
            }

            for (Categoria c : categorias) {
                System.out.println("ID: " + c.getId() + " | " + c.getNombre());
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error al obtener categorías: " + e.getMessage());
        }
    }

    private static void agregarCategoria() {
        try {
            System.out.println("\n=== AGREGAR NUEVA CATEGORIA ===");

            System.out.print("Nombre de la categoría: ");
            String nombre = scanner.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
                return;
            }

            System.out.print("Descripción de la categoría: ");
            String descripcion = scanner.nextLine().trim();

            if (descripcion.isEmpty()) {
                System.out.println("La descripción no puede estar vacía.");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);

            boolean creada = categoriaDAO.insertar(categoria);

            if (creada) {
                System.out.println("Categoría creada exitosamente.");
            } else {
                System.out.println("No se pudo crear la categoría.");
            }

        } catch (SQLException e) {
            System.out.println("Error en base de datos: " + e.getMessage());
        }
    }

    private static void editarCategoria() {
        try {
            System.out.println("\n=== EDITAR CATEGORIA ===");

            System.out.print("Ingrese ID de la categoría: ");
            String idInput = scanner.nextLine();

            int id;
            try {
                id = Integer.parseInt(idInput);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
                return;
            }

            Categoria categoria = categoriaDAO.buscarPorId(id);

            if (categoria == null) {
                System.out.println("Categoria no encontrada.");
                return;
            }

            System.out.println("\nCategoria actual:");
            System.out.println("Nombre: " + categoria.getNombre());
            System.out.println("Descripción: " + categoria.getDescripcion());

            System.out.println("\n--- NUEVOS DATOS ---");

            System.out.print("Nuevo nombre (enter para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty()) {
                categoria.setNombre(nuevoNombre);
            }

            System.out.print("Nueva descripción (enter para mantener): ");
            String nuevaDesc = scanner.nextLine().trim();
            if (!nuevaDesc.isEmpty()) {
                categoria.setDescripcion(nuevaDesc);
            }

            boolean ok = categoriaDAO.actualizar(categoria);

            if (ok) {
                System.out.println("Categoría actualizada correctamente.");
            } else {
                System.out.println("No se pudo actualizar la categoría.");
            }

        } catch (SQLException e) {
            System.out.println("Error BD: " + e.getMessage());
        }
    }

    private static void eliminarCategoria() {
        try {
            System.out.println("\n=== ELIMINAR CATEGORÍA ===");
            System.out.print("Ingrese ID de la categoría: ");

            int id = Integer.parseInt(scanner.nextLine());

            boolean eliminado = categoriaDAO.eliminar(id);

            if (eliminado) {
                System.out.println("Categoría eliminada correctamente");
            } else {
                System.out.println("No se pudo eliminar. Verifique el ID.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número válido.");
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private static void submenuInventario() {
        boolean volver = false;

        while (!volver) {
            System.out.println("       GESTIÓN DE INVENTARIO");

            System.out.println("\n1. Ver stock actual general");
            System.out.println("2. Agregar stock a producto");
            System.out.println("3. Reducir stock de producto");

            System.out.println("\n0.  Volver al menú principal");
            System.out.print("Opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verStockGeneral();
                        break;
                    case 2:
                        agregarStock();
                        break;
                    case 3:
                        reducirStock();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número.");
            }
        }
    }

    private static void verStockGeneral() {
        try {

            List<Inventario> inventarios = inventarioDAO.obtenerStockGeneral();

            System.out.println("\n=== STOCK ACTUAL ===");

            for (Inventario inv : inventarios) {
                System.out.println(
                        "Producto: " + inv.getProducto().getNombre()
                        + " → Stock actual: " + inv.getStockActual()
                );
            }

        } catch (SQLException e) {
            System.out.println("Error BD: " + e.getMessage());
        }
    }

    private static void agregarStock() {
        try {
            System.out.println("\n=== AGREGAR STOCK A PRODUCTO ===");

            System.out.print("Ingrese ID del producto: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Cantidad a agregar: ");
            int cantidad = Integer.parseInt(scanner.nextLine());

            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor que 0.");
                return;
            }

            boolean actualizado = inventarioDAO.agregar(id, cantidad);

            if (actualizado) {
                System.out.println("Stock actualizado correctamente.");
            } else {
                System.out.println("No se pudo actualizar (¿Producto inexistente?).");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ingrese valores válidos.");
        } catch (SQLException e) {
            System.out.println("Error BD: " + e.getMessage());
        }
    }

    public static void reducirStock() {
        try {
            System.out.println("\n=== AGREGAR STOCK A PRODUCTO ===");

            System.out.print("Ingrese ID del producto: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Cantidad a reducir: ");
            int cantidad = Integer.parseInt(scanner.nextLine());

            if (cantidad <= 0) {
                System.out.println("La cantidad debe ser mayor que 0.");
                return;
            }

            boolean actualizado = inventarioDAO.reducir(id, cantidad);

            if (actualizado) {
                System.out.println("Stock actualizado correctamente.");
            } else {
                System.out.println("No se pudo actualizar (¿Producto inexistente?).");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ingrese valores válidos.");
        } catch (SQLException e) {
            System.out.println("Error BD: " + e.getMessage());
        }
    }

    private static void submenuPedidos() {
        boolean volver = false;

        while (!volver) {
            System.out.println("GESTIÓN DE PEDIDOS");

            System.out.println("\n1. Ver todos los pedidos");
            System.out.println("2. Asignar repartidor a pedido");
            System.out.println("3. Actualizar estado de pedido");
            System.out.println("4. Cancelar pedido");

            System.out.println("\n0. Volver al menú principal");
            System.out.print("Opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verTodosPedidos();
                        break;
                    case 2:
                        asignarRepartidorPedido();
                        break;
                    case 3:
                        actualizarEstadoPedido();
                        break;
                    case 4:
                        cancelarPedido();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println(" Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Ingrese un número.");
            }
        }
    }

    private static void verTodosPedidos() {
        try {
            List<Pedido> lista = pedidoDAO.buscarTodos();

            System.out.println("\n=== LISTA DE PEDIDOS ===");
            for (Pedido p : lista) {
                System.out.println("ID: " + p.getId()
                        + " | Cliente: " + p.getCliente().getNombre()
                        + " | Estado: " + p.getEstado());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void asignarRepartidorPedido() {
        try {
            System.out.print("Ingrese ID del pedido: ");
            int idPedido = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese ID del repartidor: ");
            int idRepartidor = Integer.parseInt(scanner.nextLine());

            Envio envio = new Envio();
            envio.setPedido(new Pedido(idPedido));
            envio.setRepartidor(new Repartidor(idRepartidor));
            envio.setEstado(EstadoEnvio.EN_TRANSITO);

            
            boolean ok = envioDAO.insertar(envio);

            if (ok) {
                repartidorDAO.ocupar(idRepartidor);
                pedidoDAO.actualizarEstado(idPedido, EstadoPedido.ENVIADO);

                System.out.println("Repartidor asignado con éxito.");
            } else {
                System.out.println("No fue posible asignar repartidor.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void actualizarEstadoPedido() {
        try {
            System.out.print("Ingrese ID del pedido: ");
            int idPedido = Integer.parseInt(scanner.nextLine());

            System.out.println("Nuevo estado:");
            System.out.println("1. CREADO");
            System.out.println("2. PAGADO");
            System.out.println("3. EN_PROCESO");
            System.out.println("4. ENVIADO");
            System.out.println("5. ENTREGADO");
            System.out.println("6. CANCELADO");
            System.out.print("Opción: ");
            int op = Integer.parseInt(scanner.nextLine());

            EstadoPedido nuevoEstado = null;

            switch (op) {
                case 1:
                    nuevoEstado = EstadoPedido.CREADO;
                    break;
                case 2:
                    nuevoEstado = EstadoPedido.PAGADO;
                    break;
                case 3:
                    nuevoEstado = EstadoPedido.EN_PROCESO;
                    break;
                case 4:
                    nuevoEstado = EstadoPedido.ENVIADO;
                    break;
                case 5:
                    nuevoEstado = EstadoPedido.ENTREGADO;
                    break;
                case 6:
                    nuevoEstado = EstadoPedido.CANCELADO;
                    break;
                default:
                    System.out.println("Estado inválido");
                    return;
            }

            PedidoDAO pedidoDAO = new PedidoDAO();
            boolean ok = pedidoDAO.actualizarEstado(idPedido, nuevoEstado);

            if (ok) {
                System.out.println("Estado actualizado.");
            } else {
                System.out.println("No fue posible actualizar.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelarPedido() {
        try {
            System.out.print("Ingrese ID del pedido: ");
            int idPedido = Integer.parseInt(scanner.nextLine());

            boolean ok = pedidoDAO.actualizarEstado(idPedido, EstadoPedido.CANCELADO);

            if (ok) {
                System.out.println("Pedido cancelado correctamente.");
            } else {
                System.out.println("No fue posible cancelar el pedido.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void submenuUsuarios() {
        boolean volver = false;

        while (!volver) {
            System.out.println("        GESTIÓN DE USUARIOS");

            System.out.println("\n1. Ver todos los clientes");
            System.out.println("2. Activar/Desactivar cliente");

            System.out.println("\n3. Ver todos los repartidores");
            System.out.println("4. Activar/Desactivar repartidor");

            System.out.println("\n0. Volver al menú principal");
            System.out.print(" Opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verTodosClientes();
                        break;
                    case 2:
                        activarDesactivarCliente();
                        break;
                    case 3:
                        verTodosRepartidores();
                        break;
                    case 4:
                        activarDesactivarRepartidor();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número.");
            }
        }
    }

    public static void verTodosClientes() {
        try {
        List<Cliente> clientes = clienteDAO.buscarTodos();

        System.out.println("\n=== CLIENTES REGISTRADOS ===");

        for (Cliente c : clientes) {
            System.out.println(
                "ID: " + c.getId() +
                " | Nombre: " + c.getNombre() +
                " | Email: " + c.getEmail() +
                " | Estado: " + (c.isActivo() ? "ACTIVO" : "INACTIVO")
            );
        }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
    }

    public static void activarDesactivarCliente() {
    
        try {
            System.out.print("ID cliente: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.println("1) Activar");
            System.out.println("2) Desactivar");
            System.out.print("Opción: ");
            int op = Integer.parseInt(scanner.nextLine());

            boolean estado = (op == 1);

            ClienteDAO dao = new ClienteDAO();
            boolean ok = dao.activarDesactivarCliente(id, estado);

            System.out.println(ok ? "Actualizado correctamente" : "No se pudo actualizar");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void verTodosRepartidores(){
    try {
        List<Repartidor> lista = repartidorDAO.buscarTodos();

        System.out.println("=== Repartidores ===");

        for(Repartidor r : lista){
            System.out.println(
                "ID: " + r.getId() +
                " | " + r.getNombre() +
                " | " + r.getEmail() +
                " | Disponible: " + (r.isDisponible()? "SI" : "NO") +
                " | Estado: " + (r.isActivo()? "ACTIVO" : "INACTIVO")
            );
        }

    } catch (Exception e){
        System.out.println("Error: " + e.getMessage());
    }
    }

    public static void activarDesactivarRepartidor() {

        try {
            System.out.print("ID repartidor: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.println("1) Activar");
            System.out.println("2) Desactivar");
            System.out.print("Opción: ");
            int op = Integer.parseInt(scanner.nextLine());

            boolean estado = (op == 1);

            boolean ok = repartidorDAO.activarDesactivarRepartidor(id, estado);

            System.out.println(ok ? "Actualizado correctamente" : "No se pudo actualizar");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void submenuRepartidores() {
        boolean volver = false;

        while (!volver) {
            System.out.println("GESTIÓN DE REPARTIDORES");

            System.out.println("\n1. Ver repartidores disponibles");
            System.out.println("2. Ver información detallada");

            System.out.println("\n0. ️  Volver al menú principal");
            System.out.print(" Opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verRepartidoresDisponibles();
                        break;
                    case 2:
                        verInfoRepartidorDetallada();
                        break;
                    case 0:
                        volver = true;
                        break;
                    default:
                        System.out.println(" Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Ingrese un número.");
            }
        }
    }

    public static void verRepartidoresDisponibles() {
        try {
            List<Repartidor> lista = repartidorDAO.buscarTodos();

            System.out.println("--- REPARTIDORES DISPONIBLES ---");

            for (Repartidor r : lista) {
                if (r.isDisponible() && r.isActivo()) {
                    System.out.println("ID: " + r.getId()
                            + " | " + r.getNombre()
                            + " | " + r.getEmail());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void verInfoRepartidorDetallada() {
        try {
            System.out.print("Ingrese ID del repartidor: ");
            int id = Integer.parseInt(scanner.nextLine());

            Repartidor r = repartidorDAO.buscarPorId(id);

            if (r == null) {
                System.out.println("No encontrado.");
                return;
            }

            System.out.println("\n=== INFORMACIÓN DEL REPARTIDOR ===");
            System.out.println("ID: " + r.getId());
            System.out.println("Nombre: " + r.getNombre());
            System.out.println("Email: " + r.getEmail());
            System.out.println("Teléfono: " + r.getTelefono());
            System.out.println("Disponible: " + (r.isDisponible() ? "SI" : "NO"));
            System.out.println("Estado: " + (r.isActivo() ? "ACTIVO" : "INACTIVO"));

            if (r.getVehiculo() != null) {
                System.out.println("Vehículo: " + r.getVehiculo().getTipo());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
