/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ui;

import com.ecommerce.dao.*;
import com.ecommerce.models.pagos.Pago;
import com.ecommerce.models.pedidos.*;
import com.ecommerce.models.productos.*;
import com.ecommerce.models.usuarios.Cliente;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class MenuCliente {

    private static Scanner scanner = new Scanner(System.in);
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static PedidoDAO pedidoDAO = new PedidoDAO();
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static PagoDAO pagoDAO = new PagoDAO();

    public static void mostrar(Cliente cliente) {
        boolean salir = false;
        
        while (!salir) {
            mostrarEncabezado();

            try {
                String input = scanner.nextLine();
                int opcion = Integer.parseInt(input);
                switch (opcion) {
                    case 1:
                        verCatalogo();
                        break;
                    case 2:
                        buscarProducto();
                        break;
                    case 3:
                        productosPorCategoria();
                        break;
                    case 4:
                        agregarAlCarrito(cliente);
                        break;
                    case 5:
                        verCarrito(cliente);
                        break;
                    case 6:
                        verPedidos(cliente);
                        break;
                    case 7:
                        realizarPedido(cliente);
                        break;
                    case 8:
                        verPerfil(cliente);
                        break;
                    case 9:
                        editarDatos(cliente);
                        break;
                    case 10:
                        eliminarCuenta(cliente);
                        break;
                    case 0:
                        System.out.println("Sesión cerrada.");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }
            }catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                System.out.println("Por favor intente nuevamente.");
            } 
            catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error de base de datos: " + e.getMessage());
                System.err.println("Por favor intente nuevamente");
            }
        }

    }

    private static void mostrarEncabezado() {
        System.out.println("\nCOMPRAS:");
        System.out.println("1. Ver catálogo de productos");
        System.out.println("2. Buscar producto");
        System.out.println("3. Ver productos por categoría");

        System.out.println("\nCARRITO:");
        System.out.println("4. Agregar producto al carrito");
        System.out.println("5. Ver mi carrito");

        System.out.println("\nMIS PEDIDOS:");
        System.out.println("6. Ver mis pedidos");
        System.out.println("7. Realizar nuevo pedido");

        System.out.println("\nMI CUENTA:");
        System.out.println("8. Ver mi perfil");
        System.out.println("9. Editar mis datos");
        System.out.println("10. Eliminar cuenta");

        System.out.println("\n0. Cerrar sesión");
        System.out.print("Seleccione opción: ");
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

    private static void buscarProducto() throws SQLException {
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

    private static void productosPorCategoria() throws SQLException {
        List<Categoria> categorias = productoDAO.obtenerCategorias();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías disponibles.");
            return;
        }

        System.out.println("\nSeleccione una categoría:");

        for (int i = 0; i < categorias.size(); i++) {
            System.out.printf("%2d. %s\n", i + 1, categorias.get(i).getNombre());
        }

        System.out.println(" 0. Cancelar");
        System.out.print("\nOpción: ");

        try {
            String input = scanner.nextLine();
            int opcion = Integer.parseInt(input);

            if (opcion == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            if (opcion < 1 || opcion > categorias.size()) {
                System.out.println("Opción inválida.");
                return;
            }

            Categoria categoria = categorias.get(opcion - 1);
            List<Producto> productos = productoDAO.buscarPorCategoria(categoria.getId());

            if (productos.isEmpty()) {
                System.out.println("\nNo hay productos en esta categoría.");
            } else {
                System.out.println("\nPRODUCTOS EN " + categoria.getNombre().toUpperCase() + ":");

                for (Producto p : productos) {
                    System.out.printf("• %s - S/.%.2f\n", p.getNombre(), p.getPrecio());
                }
                System.out.println("Total: " + productos.size() + " productos");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
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

    private static void verCarrito(Cliente cliente) {
        System.out.println("\n--- MI CARRITO DE COMPRAS ---");
        cliente.getCarrito().getItems().forEach(item
                -> System.out.printf("ID Prod: %d | Nombre: %s | Cantidad: %d | Precio Unitario: S/.%.2f | Subtotal: S/.%.2f\n",
                        item.getProducto().getId(), item.getProducto().getNombre(), item.getCantidad(),
                        item.getPrecioUnitario(), item.calcularSubtotal()));

        System.out.printf("SUBTOTAL: S/.%.2f\n", cliente.getCarrito().calcularTotal());
    }

    private static void realizarPedido(Cliente cliente) throws SQLException {
        if (cliente.getCarrito().getItems().isEmpty()) {
            System.out.println("Su carrito esta vacio. Agregue productos antes de realizar un pedido.");
            return;
        }

        verCarrito(cliente);
        System.out.print("¿Confirmar pedido? (s/n): ");
        if (!scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Pedido cancelado.");
            return;
        }

        try {
            Pedido nuevoPedido = cliente.realizarPedido();

            boolean pedidoGuardado = pedidoDAO.insertar(nuevoPedido);

            if (!pedidoGuardado) {
                System.out.println("Error al guardar el pedido.");
                return;
            }

            System.out.println("\n¡PEDIDO CREADO EXITOSAMENTE!");
            System.out.println("Número de pedido: #" + nuevoPedido.getId());
            System.out.println("Total a pagar: S/." + nuevoPedido.getTotal());

            System.out.println("PAGO DEL PEDIDO");

            boolean pagoExitoso = false;

            while (!pagoExitoso) {
                System.out.println("\nSELECCIONE METODO DE PAGO:");
                System.out.println("1. Tarjeta de Credito/Debito");
                System.out.println("2. Transferencia Bancaria");
                System.out.print("Opcion: ");

                String opcionStr = scanner.nextLine();

                try {
                    int opcion = Integer.parseInt(opcionStr);

                    if (opcion == 1) {
                        pagoExitoso = procesarPagoTarjeta(nuevoPedido);
                    } else if (opcion == 2) {
                        pagoExitoso = procesarPagoTransferencia(nuevoPedido);
                    } else {
                        System.out.println("Opcion invalida.");
                    }

                    if (!pagoExitoso) {
                        System.out.println("\nDebe completar el pago para finalizar su compra.");
                        System.out.print("¿Intentar con otro metodo? (s/n): ");
                        String intentarOtraVez = scanner.nextLine();

                        if (!intentarOtraVez.equalsIgnoreCase("s")) {
                            System.out.println("Pedido #" + nuevoPedido.getId() + " cancelado por falta de pago.");
                            cancelarPedidoSinPago(nuevoPedido);
                            return;
                        }
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida. Ingrese 1 o 2.");
                }
            }

        } catch (SQLException e) {
            System.err.println("\nError al procesar el pedido: " + e.getMessage());
            System.err.println("Por favor, intente nuevamente.");
        } catch (Exception e) {
            System.err.println("\nError inesperado: " + e.getMessage());
        }

        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    private static boolean procesarPagoTarjeta(Pedido pedido) {
        System.out.println("\nPAGO CON TARJETA");
        System.out.println("Pedido: #" + pedido.getId() + " | Total: S/." + pedido.getTotal());

        try {
            System.out.print("Numero de tarjeta (16 digitos): ");
            String tarjeta = scanner.nextLine();

            System.out.print("Nombre del titular: ");
            String titular = scanner.nextLine();

            System.out.print("CVV (3 digitos): ");
            String cvv = scanner.nextLine();

            if (tarjeta.length() != 16 || !esNumero(tarjeta)) {
                System.out.println("Numero de tarjeta invalido.");
                return false;
            }

            if (cvv.length() != 3 || !esNumero(cvv)) {
                System.out.println("CVV invalido.");
                return false;
            }

            System.out.println("\nProcesando pago con tarjeta...");

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }

            Pago pago = new Pago();
            pago.setPedidoId(pedido.getId());
            pago.setMetodoPago("TARJETA");
            pago.setMonto(pedido.getTotal());
            pago.setEstado("APROBADO");
            pago.setNumeroTransaccion("TARJ-" + System.currentTimeMillis());

            String tarjetaEnmascarada = "**** **** **** " + tarjeta.substring(12);
            pago.setDetalles("Tarjeta: " + tarjetaEnmascarada + " | Titular: " + titular);

 
            boolean pagoGuardado = pagoDAO.insertar(pago);

            if (pagoGuardado) {
                pedido.setEstado(EstadoPedido.PAGADO);

                System.out.println("\nPAGO APROBADO!");
                System.out.println("Codigo de transaccion: " + pago.getNumeroTransaccion());
                System.out.println("Su pedido #" + pedido.getId() + " sera procesado para envio.");
                return true;
            } else {
                System.out.println("Error al registrar el pago.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error en pago: " + e.getMessage());
            return false;
        }
    }

    private static boolean esNumero(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean procesarPagoTransferencia(Pedido pedido) {
        System.out.println("\nTRANSFERENCIA BANCARIA");
        System.out.println("Pedido: #" + pedido.getId() + " | Total: S/." + pedido.getTotal());

        try {
            System.out.println("\nDATOS PARA TRANSFERENCIA:");
            System.out.println("----------------------------------------");
            System.out.println("Banco: BANCO DE COMERCIO");
            System.out.println("Titular: E-COMMERCE S.A.");
            System.out.println("Cuenta: 0011-0234-56789012");
            System.out.println("CCI: 011-234-000123456789012-45");
            System.out.println("Monto: S/." + pedido.getTotal());
            System.out.println("Concepto: PEDIDO-" + pedido.getId());
            System.out.println("----------------------------------------");

            System.out.println("\nINSTRUCCIONES:");
            System.out.println("1. Realice la transferencia");
            System.out.println("2. Guarde el comprobante");
            System.out.println("3. Ingrese el codigo de operacion");

            System.out.print("\n¿Ya realizo la transferencia? (s/n): ");
            String respuesta = scanner.nextLine();

            if (!respuesta.equalsIgnoreCase("s")) {
                System.out.println("Debe realizar la transferencia para completar la compra.");
                return false;
            }

            System.out.print("Ingrese codigo de operación: ");
            String codigoOperacion = scanner.nextLine();

            if (codigoOperacion.trim().isEmpty()) {
                System.out.println("Codigo de operacion requerido.");
                return false;
            }

            Pago pago = new Pago();
            pago.setPedidoId(pedido.getId());
            pago.setMetodoPago("TRANSFERENCIA");
            pago.setMonto(pedido.getTotal());
            pago.setEstado("PENDIENTE");
            pago.setNumeroTransaccion("TRANS-" + System.currentTimeMillis());
            pago.setDetalles("Codigo operacion: " + codigoOperacion);

            PagoDAO pagoDAO = new PagoDAO();
            boolean pagoGuardado = pagoDAO.insertar(pago);

            if (pagoGuardado) {
                System.out.println("\nPAGO REGISTRADO COMO PENDIENTE");
                System.out.println("Codigo de seguimiento: " + pago.getNumeroTransaccion());
                System.out.println("Su pedido sera procesado una vez confirmemos la transferencia.");
                return true;
            } else {
                System.out.println("Error al registrar el pago.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private static void cancelarPedidoSinPago(Pedido pedido) {
        try {
            pedido.setEstado(EstadoPedido.CANCELADO);

            pedidoDAO.actualizarEstado(pedido.getId(), EstadoPedido.CANCELADO);

        } catch (SQLException e) {
            System.err.println("Error al cancelar pedido: " + e.getMessage());
        }
    }

    private static void verPedidos(Cliente cliente) throws SQLException {
        System.out.println("\n=== MIS PEDIDOS ===\n");

        List<Pedido> pedidos = pedidoDAO.buscarPorCliente(cliente.getId());

        if (pedidos.isEmpty()) {
            System.out.println("No tienes pedidos registrados.");
            System.out.println("¡Realiza tu primer pedido!");
            return;
        }

        System.out.printf("Tienes %d pedido(s):\n\n", pedidos.size());

        System.out.println("|  ID  |      Fecha       |   Estado   |   Total    |     Dirección        |");

        pedidos.forEach(p -> {
            String fechaFormateada = p.getFechaPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            String direccionCorta = p.getDireccionEnvio().length() > 20
                    ? p.getDireccionEnvio().substring(0, 17) + "..."
                    : p.getDireccionEnvio();

            System.out.printf("| %4d | %16s | %-10s | S/.%7.2f | %-20s |\n",
                    p.getId(),
                    fechaFormateada,
                    p.getEstado().toString(),
                    p.getTotal(),
                    direccionCorta
            );
        });
    }

    private static void verPerfil(Cliente cliente) {
        System.out.println("\n--- MI PERFIL ---");
        System.out.println("Nombre: " + cliente.getNombre());
        System.out.println("Email: " + cliente.getEmail());
        System.out.println("Telefono: " + cliente.getTelefono());
        System.out.println("Dirección de Envio: " + cliente.getDireccionEnvio());
        System.out.println("Fecha de Registro: " + cliente.getFechaRegistro());
    }

    private static void editarDatos(Cliente cliente) {
        System.out.println("\n INGRESA LOS NUEVOS DATOS:");

        try {

            System.out.print("Nuevo nombre [" + cliente.getNombre() + "]: ");
            String nuevoNombre = scanner.nextLine().trim();
            if (nuevoNombre.isEmpty()) {
                nuevoNombre = cliente.getNombre();
            }

            System.out.print("Nuevo teléfono [" + (cliente.getTelefono() != null ? cliente.getTelefono() : "") + "]: ");
            String nuevoTelefono = scanner.nextLine().trim();
            if (nuevoTelefono.isEmpty()) {
                nuevoTelefono = cliente.getTelefono();
            }

            System.out.print("Nueva dirección [" + (cliente.getDireccionEnvio() != null ? cliente.getDireccionEnvio() : "") + "]: ");
            String nuevaDireccion = scanner.nextLine().trim();
            if (nuevaDireccion.isEmpty()) {
                nuevaDireccion = cliente.getDireccionEnvio();
            }

            System.out.println("\n¿Guardar estos cambios? (S/N)");
            System.out.println("Nombre: " + nuevoNombre);
            System.out.println("Teléfono: " + nuevoTelefono);
            System.out.println("Dirección: " + nuevaDireccion);
            System.out.print("\nOpción: ");

            String confirmar = scanner.nextLine().trim().toUpperCase();

            if (confirmar.equals("S") || confirmar.equals("SI")) {
                Cliente clienteActualizado = new Cliente();
                clienteActualizado.setId(cliente.getId());
                clienteActualizado.setNombre(nuevoNombre);
                clienteActualizado.setTelefono(nuevoTelefono);
                clienteActualizado.setDireccionEnvio(nuevaDireccion);
                clienteActualizado.setEmail(cliente.getEmail());
                clienteActualizado.setPassword(cliente.getPassword());

                boolean exito = clienteDAO.actualizar(clienteActualizado);

                if (exito) {
                    System.out.println("\n¡Datos actualizados exitosamente!");

                    cliente.setNombre(nuevoNombre);
                    cliente.setTelefono(nuevoTelefono);
                    cliente.setDireccionEnvio(nuevaDireccion);
                } else {
                    System.out.println("\nError al actualizar los datos.");
                }
            } else {
                System.out.println("\nCambios cancelados.");
            }

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }

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
