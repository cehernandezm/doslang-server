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
public class Generador {
    static int temporal = 0;
    int etiqueta = 0;
    
    public static String generarTemporal(){
       String  temp = "t" + temporal;
        temporal++;
        return temp;
    }
}
