/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ui;

import com.ecommerce.dao.AdministradorDAO;
import com.ecommerce.dao.ClienteDAO;
import com.ecommerce.dao.RepartidorDAO;
import com.ecommerce.models.abstracto.Transporte;
import com.ecommerce.models.envios.Automovil;
import com.ecommerce.models.usuarios.Administrador;
import com.ecommerce.models.usuarios.Cliente;
import com.ecommerce.models.usuarios.Repartidor;
import com.ecommerce.utils.Validador;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class MenuPrincipal {

    private static Scanner scanner = new Scanner(System.in);
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static AdministradorDAO administradorDAO = new AdministradorDAO();
    private static RepartidorDAO repartidorDAO = new RepartidorDAO();

    public static void mostrar() {
        while (true) {
            System.out.println("       SISTEMA E-COMMERCE");
            System.out.println("INICIAR SESIÓN COMO:");
            System.out.println("1.Cliente");
            System.out.println("2.Administrador");
            System.out.println("3.Repartidor");
            System.out.println();
            System.out.println("REGISTRARSE COMO:");
            System.out.println("4. Nuevo Cliente");
            System.out.println("5. Nuevo Repartidor");
            System.out.println("0. Salir del sistema");
            System.out.print("Seleccione opción: ");

            try {
                String input = scanner.nextLine();
                int opcion = Integer.parseInt(input);

                switch (opcion) {
                    case 1:
                        iniciarSesionCliente();
                        break;
                    case 2:
                        iniciarSesionAdministrador();
                        break;
                    case 3:
                        iniciarSesionRepartidor();
                        break;
                    case 4:
                        registrarCliente();
                        break;
                    case 5:
                        registrarRepartidor();
                        break;
                    case 0:
                        System.out.println("¡Gracias por usar nuestro sistema!");
                        return;
                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    private static void iniciarSesionCliente() {
        System.out.println("\n=== INICIO DE SESIÓN - CLIENTE ===");

        try {

            System.out.print("Introduce tu Email: ");
            String email = scanner.nextLine();

            System.out.print("Introduce tu Contrasena: ");
            String password = scanner.nextLine();

            System.out.println("\nIniciando Sesión...");

            Cliente clienteLogueado = clienteDAO.autenticar(email, password);

            if (clienteLogueado != null) {
                System.out.println("¡Inicio de Sesión Exitoso!");
                System.out.println("Bienvenido/a: " + clienteLogueado.getNombre());
                System.out.println("Tu Direccion de Envío es: " + clienteLogueado.getDireccionEnvio());
                MenuCliente.mostrar(clienteLogueado);
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
                                    MenuCliente.mostrar(clienteInactivo);
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

    private static void iniciarSesionAdministrador() {
        System.out.println("\n=== INICIO DE SESIÓN - ADMINISTRADOR ===");

        try {

            System.out.print("Introduce tu Email: ");
            String email = scanner.nextLine();

            System.out.print("Introduce tu Contrasena: ");
            String password = scanner.nextLine();

            System.out.println("\nIniciando Sesión...");

            Administrador adminLogueado = administradorDAO.autenticar(email, password);

            if (adminLogueado != null) {
                System.out.println("¡Inicio de Sesión Exitoso!");
                System.out.println("Bienvenido/a: " + adminLogueado.getNombre());
                MenuAdministrador.mostrar(adminLogueado);
            } else {
                if (administradorDAO.existeEmail(email)) {
                    Administrador adminInactivo = administradorDAO.buscarPorEmail(email);

                    if (adminInactivo != null) {
                        if (administradorDAO.verificarCredenciales(email, password)) {
                            System.out.println("\n¡Su cuenta se encuentra INACTIVA!");
                            System.out.print("¿Desea reactivar su cuenta? (s/n): ");
                            String respuesta = scanner.nextLine();

                            if (respuesta.equalsIgnoreCase("s")) {
                                if (administradorDAO.reactivarCuenta(adminInactivo.getId())) {
                                    System.out.println("¡Cuenta reactivada exitosamente!");
                                    System.out.println("Bienvenido/a de nuevo: " + adminInactivo.getNombre());
                                    MenuAdministrador.mostrar(adminInactivo);
                                } else {
                                    System.out.println("Error al reactivar la cuenta. Contacte al super administrador.");
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

    private static void iniciarSesionRepartidor() {
        System.out.println("\n=== INICIO DE SESIÓN - REPARTIDOR ===");

        try {

            System.out.print("Introduce tu Email: ");
            String email = scanner.nextLine();

            System.out.print("Introduce tu Contrasena: ");
            String password = scanner.nextLine();

            System.out.println("\nIniciando Sesión...");

            Repartidor repartidorLogueado = repartidorDAO.autenticar(email, password);

            if (repartidorLogueado != null) {
                System.out.println("¡Inicio de Sesión Exitoso!");
                System.out.println("Bienvenido/a: " + repartidorLogueado.getNombre());
                MenuRepartidor.mostrar(repartidorLogueado);
            } else {
                if (repartidorDAO.existeEmail(email)) {
                    Repartidor repartidorInactivo = repartidorDAO.buscarPorEmail(email);

                    if (repartidorInactivo != null) {
                        if (clienteDAO.verificarCredenciales(email, password)) {
                            System.out.println("\n¡Su cuenta se encuentra INACTIVA!");
                            System.out.print("¿Desea reactivar su cuenta? (s/n): ");
                            String respuesta = scanner.nextLine();

                            if (respuesta.equalsIgnoreCase("s")) {
                                if (repartidorDAO.reactivarCuenta(repartidorInactivo.getId())) {
                                    System.out.println("¡Cuenta reactivada exitosamente!");
                                    System.out.println("Bienvenido/a de nuevo: " + repartidorInactivo.getNombre());
                                    MenuRepartidor.mostrar(repartidorInactivo);
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

        } catch (SQLException e) {
            System.err.println("Error de base de datos durante el registro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrio un error inesperado: " + e.getMessage());
        }
        System.out.println("Cliente registrado exitosamente");
    }

    public static void registrarRepartidor() {
        System.out.println("\n=== REGISTRO DE REPARTIDOR ===");

        try {

            System.out.print("Ingrese Email: ");
            String email = scanner.nextLine();

            if (!Validador.esEmailValido(email) || repartidorDAO.existeEmail(email)) {
                System.out.println("Email invalido");
                return;
            }

            System.out.print("Ingrese Contraseña: ");
            String password = scanner.nextLine();

            if (!Validador.validarPassword(password)) {
                System.out.println("Contraseña debe tener al menos 6 caracteres");
                return;
            }

            System.out.print("Ingrese Nombre completo: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese Telefono: ");
            String telefono = scanner.nextLine();

            System.out.print("Ingrese Vehículo: ");
            String Tipovehiculo = scanner.nextLine().toLowerCase();
            Automovil vehiculo = new Automovil(Tipovehiculo);

            Repartidor nuevoRepartidor = new Repartidor(email, password, nombre, telefono, vehiculo);

            if (repartidorDAO.insertar(nuevoRepartidor)) {
                System.out.println("   ID: " + nuevoRepartidor.getId());
                System.out.println("   Nombre: " + nuevoRepartidor.getNombre());
                System.out.println("   Vehiculo: " + nuevoRepartidor.getVehiculo());
            } else {
                System.err.println("Error: No se pudo insertar el cliente en la base de datos.");
            }

        } catch (SQLException e) {
            System.err.println("Error de base de datos durante el registro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrio un error inesperado: " + e.getMessage());
        }
        System.out.println("Repartidor registrado exitosamente");
    }

}
