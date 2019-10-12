/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.UserTypes;

import Pascal.Analisis.TipoDato;
import Pascal.Componentes.Type;

/**
 *
 * @author Carlos
 */
public class Equivalencia extends TipoDato {
    String nombre;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param nombre
     * @param tipo 
     */
    public Equivalencia(String nombre, Type tipo) {
        this.nombre = nombre;
        this.tipo = tipo.getTipo();
    }

    /**
     * GET NOMBRE
     * @return 
     */
    public String getNombre() {
        return nombre;
    }
    
    
    
    
}
