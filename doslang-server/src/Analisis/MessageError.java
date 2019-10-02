/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analisis;

/**
 *
 * @author Carlos
 */
public class MessageError {
    String tipo;
    int l;
    int c;
    String detalle;

    /**
     * CONSTRUCTOR DE LA CLASE PARA EL MANEJO DE ERRORES
     * @param tipo
     * @param l
     * @param c
     * @param detalle 
     */
    public MessageError(String tipo, int l, int c, String detalle) {
        this.tipo = tipo;
        this.l = l;
        this.c = c;
        this.detalle = detalle;
    }
    
    
}
