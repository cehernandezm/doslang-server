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
public class TipoDato {
    
    Tipo tipo;     
    /**
     * GET DE TIPO
     * @return Tipo
     */
    public Tipo getTipo() {
        return this.tipo;
    }
    
    
    
    
    
    
    
    
    public enum Tipo{
        INT,
        DOUBLE
    }
}
