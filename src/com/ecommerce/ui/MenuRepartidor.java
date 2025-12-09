/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ui;

import com.ecommerce.dao.EnvioDAO;
import com.ecommerce.dao.PedidoDAO;
import com.ecommerce.dao.RepartidorDAO;
import com.ecommerce.models.envios.Automovil;
import com.ecommerce.models.pedidos.EstadoPedido;
import com.ecommerce.models.pedidos.Pedido;
import com.ecommerce.models.usuarios.Repartidor;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class MenuRepartidor {
    
    private static Scanner scanner = new Scanner(System.in);
    private static RepartidorDAO repartidorDAO = new RepartidorDAO();
    private static EnvioDAO envioDAO = new EnvioDAO();
    private static PedidoDAO pedidoDAO = new PedidoDAO();
    
    
    public static void mostrar(Repartidor repartidor) {
        boolean salir = false;
        
        while(!salir){
            mostrarEncabezado();
            
            try {
                String input = scanner.nextLine();
                int opcion = Integer.parseInt(input);
                switch (opcion) {
                    case 1:
                        verPedidosAsignados(repartidor);
                        break;
                  case 2:
                       finalizarEnvio(repartidor);
                      break;
                    case 3:
                        verMiInformacion(repartidor);
                       break;
                    case 4:
                        actualizarDatos(repartidor);
                       break;
                    case 5:
                       cambiarDisponibilidad(repartidor);
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
        }
    }
    
    private static void mostrarEncabezado() {
        System.out.println("\nENVIOS:");
        System.out.println("1. Ver pedidos asignados");
        System.out.println("2. Finalizar envío");
        System.out.println("3. Ver mi información");
        System.out.println("4. Cambiar disponibilidad");

        System.out.println("\n0. Cerrar sesión");
        System.out.print("Seleccione opción: ");
    }
    
    public static void verPedidosAsignados(Repartidor repartidor) {
        List<Pedido> pedidos = repartidorDAO.verPedidosAsignados(repartidor);

        if (pedidos.isEmpty()) {
            System.out.println("No tienes pedidos asignados");
            return;
        }

        System.out.println("\n===== PEDIDOS ASIGNADOS =====");
        for (Pedido p : pedidos) {
            System.out.println("Pedido #" + p.getId() + " | Estado: " + p.getEstado());
        }
    }

    private static void finalizarEnvio(Repartidor repartidor) {

        System.out.print("Ingrese ID del envío: ");
        int id = Integer.parseInt(scanner.nextLine());

        int pedidoId = envioDAO.obtenerPedidoPorEnvio(id);

        if (pedidoId <= 0) {
            System.out.println("No existe el envío.");
            return;
        }

        try {
            boolean ok1 = envioDAO.finalizarEnvio(id);
            boolean ok2 = pedidoDAO.actualizarEstado(pedidoId, EstadoPedido.ENTREGADO);

            envioDAO.liberarRepartidor(repartidor.getId());

            if (ok1 && ok2) {
                System.out.println("Envío finalizado.");
            } else {
                System.out.println("No se pudo finalizar.");
            }

        } catch (SQLException e) {
            System.out.println("Error al finalizar el envío: " + e.getMessage());
        }
    }

    public static void verMiInformacion(Repartidor repartidor) {
        System.out.println("\n--- INFORMACIÓN DEL REPARTIDOR ---");
        System.out.println("ID           : " + repartidor.getId());
        System.out.println("Nombre       : " + repartidor.getNombre());
        System.out.println("Email        : " + repartidor.getEmail());
        System.out.println("Teléfono     : " + repartidor.getTelefono());
        if (repartidor.getVehiculo() != null) {
        System.out.println("Vehículo     : " + repartidor.getVehiculo());
    } else {
        System.out.println("Vehículo     : Sin vehículo");
    }
        System.out.println("Activo       : " + ((repartidor.isActivo()) ? "Sí" : "No"));
        System.out.println("En servicio  : " + (repartidor.isDisponible() ? "No" : "Sí"));
    }

    public static void actualizarDatos(Repartidor repartidor) {

        System.out.println("\n=== ACTUALIZAR MIS DATOS ===");

        try {
            System.out.print("Nuevo nombre (enter para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty()) {
                repartidor.setNombre(nuevoNombre);
            }

            System.out.print("Nuevo teléfono (enter para mantener): ");
            String nuevoTelefono = scanner.nextLine().trim();
            if (!nuevoTelefono.isEmpty()) {
                repartidor.setTelefono(nuevoTelefono);
            }

            System.out.print("Nuevo vehículo (enter para mantener): ");
            String nuevoVehiculo = scanner.nextLine().trim();
            if (!nuevoVehiculo.isEmpty()) {
                repartidor.setVehiculo(new Automovil(nuevoVehiculo));
            }

            if (repartidorDAO.actualizar(repartidor)) {
                System.out.println("Datos actualizados correctamente");
            } else {
                System.out.println("No se pudo actualizar los datos.");
            }

        } catch (Exception e) {
            System.out.println("Error actualizando datos: " + e.getMessage());
        }
    }

    public static void cambiarDisponibilidad(Repartidor repartidor) {
        boolean actual = repartidor.isDisponible();

        System.out.println("Tu disponibilidad actual es: " + (actual ? "Disponible" : "No disponible"));
        System.out.print("¿Quieres cambiarla? (S/N): ");

        String rpta = scanner.nextLine();

        if (rpta.equalsIgnoreCase("S")) {

            boolean nueva = !actual;
            repartidor.setDisponible(nueva);

            try {
                repartidorDAO.actualizarDisponibilidad(repartidor.getId(), nueva);
                System.out.println("Disponibilidad actualizada a: " + (nueva ? "Disponible" : "No disponible"));
            } catch (Exception e) {
                System.out.println("Error al actualizar disponibilidad: " + e.getMessage());
            }

        } else {
            System.out.println("No se realizaron cambios.");
        }
    }
}   
