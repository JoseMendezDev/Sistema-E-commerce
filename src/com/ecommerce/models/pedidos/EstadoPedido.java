/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.models.pedidos;

/**
 *
 * @author USER
 */
public enum EstadoPedido {

    CREADO("CREADO"),
    PAGADO("PAGADO"),
    EN_PROCESO("EN_PROCESO"),
    ENVIADO("ENVIADO"),
    ENTREGADO("ENTREGADO"),
    CANCELADO("CANCELADO");
    
    private final String valor;
    
    EstadoPedido(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    @Override
    public String toString() {
        return valor;
    }
    
    public static EstadoPedido fromString(String texto) {
        for (EstadoPedido estado : EstadoPedido.values()) {
            if (estado.valor.equalsIgnoreCase(texto)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + texto);
    }
}