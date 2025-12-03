/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce;

import com.ecommerce.models.usuarios.*;
import com.ecommerce.models.productos.*;
import com.ecommerce.models.pedidos.*;
import com.ecommerce.dao.*;
import com.ecommerce.database.ConexionBD;
import com.ecommerce.utils.*;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author USER
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static PedidoDAO pedidoDAO = new PedidoDAO();
    private static ClienteDAO clienteDAO = new ClienteDAO();

    public static void main(String[] args) throws SQLException {
        try {
            menuPrincipal();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            ConexionBD.closeConexion();
        }
    }

    public static void menuPrincipal() throws SQLException {
        int opcion = -1;
        System.out.println("SISTEMA DE E-COMMERCE");

        ConexionBD.getConexion();

        while (opcion != 0) {
            System.out.println("MENU PRINCIPAL");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Iniciar sesión (Cliente)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        iniciarSesion();
                        break;
                    case 0:
                        System.out.println("Gracias por su visita");
                        break;
                    default:
                        System.out.println("Opción invalida");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada no válida. Por favor, ingrese un numero.");
            }
        }

    }

    private static void registrarCliente() {
        System.out.println("\n=== REGISTRO DE CLIENTE ===");

        try {

            System.out.print("Ingrese Email: ");
            String email = scanner.nextLine();

            if (!Validador.esEmailValido(email) || clienteDAO.existeEmail(email)) {
                System.out.println("Email invalido");
                return;
            }

            System.out.print("Ingrese Contrasena: ");
            String password = scanner.nextLine();

            if (!Validador.validarPassword(password)) {
                System.out.println("Contraseña debe tener al menos 6 caracteres");
                return;
            }

            System.out.print("Ingrese Nombre completo: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese Telefono: ");
            String telefono = scanner.nextLine();

            System.out.print("Ingrese Direccion de envio: ");
            String direccion = scanner.nextLine();

            Cliente nuevoCliente = new Cliente(email, password, nombre, telefono, direccion);

            if (clienteDAO.insertar(nuevoCliente)) {
                System.out.println("   Cliente registrado exitosamente:");
                System.out.println("   ID: " + nuevoCliente.getId());
                System.out.println("   Nombre: " + nuevoCliente.getNombre());
                System.out.println("   Dirección: " + nuevoCliente.getDireccionEnvio());
            } else {
                System.err.println("Error: No se pudo insertar el cliente en la base de datos.");
            }

            System.out.println("Cliente registrado exitosamente");

        } catch (SQLException e) {
            System.err.println("Error de base de datos durante el registro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrio un error inesperado: " + e.getMessage());
        }
    }

    private static void iniciarSesion() {
        System.out.println("\n=== INICIO DE SESIÓN - CLIENTE ===");

        try {

            System.out.print("Introduce tu Email: ");
            String email = scanner.nextLine();

            System.out.print("Introduce tu Contrasena: ");
            String password = scanner.nextLine();

            System.out.println("\nIniciando Sesion");

            Cliente clienteLogeado = clienteDAO.autenticar(email, password);

            if (clienteLogeado != null) {
                System.out.println("¡Inicio de Sesión Exitoso!");
                System.out.println("Bienvenido/a: " + clienteLogeado.getNombre());
                System.out.println("Tu Direccion de Envío es: " + clienteLogeado.getDireccionEnvio());
                ejecutarMenuCliente(clienteLogeado);
            } else {
                if (clienteDAO.existeEmail(email)) {
                    Cliente clienteInactivo = clienteDAO.buscarPorEmail(email);

                    if (clienteInactivo != null) {
                        if (clienteDAO.verificarCredenciales(email, password)) {
                            System.out.println("\n¡Su cuenta se encuentra INACTIVA!");
                            System.out.print("¿Desea reactivar su cuenta? (s/n): ");
                            String respuesta = scanner.nextLine();

                            if (respuesta.equalsIgnoreCase("s")) {
                                if (clienteDAO.reactivarCuenta(clienteInactivo.getId())) {
                                    System.out.println("¡Cuenta reactivada exitosamente!");
                                    System.out.println("Bienvenido/a de nuevo: " + clienteInactivo.getNombre());
                                    ejecutarMenuCliente(clienteInactivo);
                                } else {
                                    System.out.println("Error al reactivar la cuenta. Contacte al administrador.");
                                }
                            } else {
                                System.out.println("La cuenta permanecerá inactiva.");
                            }
                        } else {
                            System.out.println("Error de Inicio de Sesion. Contraseña incorrecta.");
                        }
                    } else {
                        System.out.println("Error de Inicio de Sesion. Email o contraseña incorrectos.");
                    }
                } else {
                    System.out.println("Error de Inicio de Sesion. Email o contraseña incorrectos.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de conexion/operacion con la base de datos:");
            System.err.println("Código de Error: " + e.getSQLState());
            System.err.println("Mensaje: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrio un error inesperado durante el login: " + e.getMessage());
        }
    }

    private static void ejecutarMenuCliente(Cliente cliente) {
        int opcion = -1;
        boolean salirMenu = false;

        System.out.println("--- Sesión iniciada como Cliente: " + cliente.getNombre() + " ---");

        do {
            try {
                mostrarMenuCliente();
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        verCatalogo();
                        break;
                    case 2:
                        buscarProductos(cliente);
                        break;
                    case 3:
                        agregarAlCarrito(cliente);
                        break;
                    case 4:
                        verCarrito(cliente);
                        break;
                    case 5:
                        realizarPedido(cliente);
                        break;
                    case 6:
                        miPerfil(cliente);
                        break;
                    case 7:
                        if (eliminarCuenta(cliente)) {
                        salirMenu = true;
                        opcion = 0;
                    }
                        break;
                    case 0:
                        cliente.cerrarSesion();
                        System.out.println("Sesion cerrada. Volviendo al menu principal.");
                        break;
                    default:
                        System.out.println("Opcion no valida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no valida. Por favor, ingrese un numero.");
            } catch (IllegalStateException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocurrio un error inesperado: " + e.getMessage());
            }

            if (opcion != 0) {
                System.out.print("\nPresione Enter para continuar...");
                scanner.nextLine();
            }

        } while (opcion != 0);
    }

    private static void mostrarMenuCliente() {
        System.out.println("\n===== MENÚ CLIENTE =====");
        System.out.println("1. Ver catalogo");
        System.out.println("2. Buscar productos");
        System.out.println("3. Agregar al carrito");
        System.out.println("4. Ver carrito");
        System.out.println("5. Realizar pedido");
        System.out.println("6. Mi perfil");
        System.out.println("7. Eliminar cuenta");
        System.out.println("0. Cerrar sesion");
        System.out.print("Seleccione una opción: ");
    }

    private static void verCatalogo() throws SQLException {
        System.out.println("\nCATALOGO DE PRODUCTOS ---");
        List<Producto> productos = productoDAO.buscarTodos();
        if (productos.isEmpty()) {
            System.out.println("El catalogo está vacio.");
        } else {
            productos.forEach(p -> System.out.printf("ID: %d | Nombre: %s | Precio: S/.%.2f | Stock: %d\n",
                    p.getId(), p.getNombre(), p.getPrecio(),
                    p.getInventario() != null ? p.getInventario().getStockActual() : 0));
        }
    }

    private static void buscarProductos(Cliente cliente) throws SQLException {
        System.out.print("Ingrese el nombre o parte del nombre del producto a buscar: ");
        String criterio = scanner.nextLine();
        List<Producto> productos = productoDAO.buscarPorNombre(criterio);

        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos que coincidan con la búsqueda.");
        } else {
            System.out.println("\n--- RESULTADOS DE LA BÚSQUEDA ---");
            productos.forEach(p -> System.out.printf("ID: %d | Nombre: %s | Precio: S/.%.2f | Stock: %d\n",
                    p.getId(), p.getNombre(), p.getPrecio(),
                    p.getInventario() != null ? p.getInventario().getStockActual() : 0));
        }
    }
    
    private static void agregarAlCarrito(Cliente cliente) throws SQLException {
    try {
        System.out.print("Ingrese el ID del producto a agregar: ");
        String input = scanner.nextLine();
        
        if (input.isEmpty()) {
            System.out.println("Error: Debe ingresar un ID.");
            return;
        }
        
        int productoId;
        try {
            productoId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número.");
            return;
        }
        
        System.out.print("Ingrese la cantidad: ");
        input = scanner.nextLine();
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: La cantidad debe ser un número.");
            return;
        }
        
        if (cantidad <= 0) {
            System.out.println("Error: La cantidad debe ser mayor a 0.");
            return;
        }
        
        System.out.println("Buscando producto ID: " + productoId + "...");
        
        // Buscar producto
        Producto producto = productoDAO.buscarPorId(productoId);
        
        if (producto == null) {
            System.out.println("Error: Producto no encontrado.");
            System.out.println("Verifique que el ID sea correcto y el producto esté activo.");
            return;
        }
        
        System.out.println("Producto encontrado: " + producto.getNombre());
        
        // Verificar stock
        int stockDisponible = 0;
        if (producto.getInventario() != null) {
            stockDisponible = producto.getInventario().getStockActual();
        }
        
        if (stockDisponible < cantidad) {
            System.out.println("Error: Stock insuficiente.");
            System.out.println("Stock disponible: " + stockDisponible);
            System.out.println("Cantidad solicitada: " + cantidad);
            return;
        }
        
        // Agregar al carrito
        try {
            cliente.agregarAlCarrito(producto, cantidad);
            System.out.println("Producto agregado al carrito exitosamente!");
            System.out.println("Producto: " + producto.getNombre());
            System.out.println("Cantidad: " + cantidad);
            System.out.println("Precio unitario: S/." + producto.getPrecio());
            System.out.println("Subtotal: S/." + (producto.getPrecio() * cantidad));
            
        } catch (IllegalStateException e) {
            System.out.println("Error al agregar al carrito: " + e.getMessage());
        }
        
    } catch (SQLException e) {
        System.err.println("Error de base de datos:");
        System.err.println("Mensaje: " + e.getMessage());
        System.err.println("Por favor, intente nuevamente.");
    }
}

    /*private static void agregarAlCarrito(Cliente cliente) throws SQLException {
        System.out.print("Ingrese el ID del producto a agregar: ");
        int productoId = Integer.parseInt(scanner.nextLine());
        System.out.print("Ingrese la cantidad: ");
        int cantidad = Integer.parseInt(scanner.nextLine());

        Producto producto = productoDAO.buscarPorId(productoId);

        if (producto != null) {
            try {
                cliente.agregarAlCarrito(producto, cantidad);
                System.out.println("Producto agregado al carrito.");
            } catch (IllegalStateException e) {
                System.out.println("Error al agregar al carrito: " + e.getMessage());
            }
        } else {
            System.out.println("Producto no encontrado.");
        }
    }
*/

    private static void verCarrito(Cliente cliente) {
        System.out.println("\n--- MI CARRITO DE COMPRAS ---");
        cliente.getCarrito().getItems().forEach(item
                -> System.out.printf("ID Prod: %d | Nombre: %s | Cantidad: %d | Precio Unitario: $%.2f | Subtotal: $%.2f\n",
                        item.getProducto().getId(), item.getProducto().getNombre(), item.getCantidad(),
                        item.getPrecioUnitario(), item.calcularSubtotal()));

        System.out.printf("SUBTOTAL: $%.2f\n", cliente.getCarrito().calcularTotal());
    }

    private static void realizarPedido(Cliente cliente) throws SQLException {
        if (cliente.getCarrito().getItems().isEmpty()) {
            System.out.println("Su carrito esta vacio. Agregue productos antes de realizar un pedido.");
            return;
        }

        verCarrito(cliente);
        System.out.print("¿Confirmar pedido? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            Pedido nuevoPedido = cliente.realizarPedido();
            pedidoDAO.insertar(nuevoPedido);
            System.out.println("Pedido Creado Exitosamente!");
            System.out.println("El total de su pedido es: S/." + nuevoPedido.getTotal());
            System.out.println("Recuerde procesar el pago para iniciar el envío.");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    private static void miPerfil(Cliente cliente) {
        System.out.println("\n--- MI PERFIL ---");
        System.out.println("Nombre: " + cliente.getNombre());
        System.out.println("Email: " + cliente.getEmail());
        System.out.println("Telefono: " + cliente.getTelefono());
        System.out.println("Dirección de Envio: " + cliente.getDireccionEnvio());
        System.out.println("Fecha de Registro: " + cliente.getFechaRegistro());
    }

    private static boolean eliminarCuenta(Cliente cliente) {
        System.out.print("¿Está seguro que desea desactivar su cuenta? (s/n): ");
        String respuesta = scanner.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            try {
                if (clienteDAO.eliminar(cliente.getId())) {
                    System.out.println("Cuenta desactivada exitosamente. Vuelva pronto.");
                    System.out.println("Sesión cerrada.");
                    return true;
                } else {
                    System.out.println("Error al desactivar la cuenta.");
                }
            } catch (SQLException e) {
                System.err.println("Error de base de datos: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }

        return false;
    }
}
