/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Funciones;

import Pascal.Componentes.Type;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Parametro  {
    LinkedList<String> lista;
    Boolean referencia;
    Type tipo;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param referencia
     * @param tipo 
     */
    public Parametro(LinkedList<String> id, Boolean referencia, Type tipo) {
        this.lista = id;
        this.referencia = referencia;
        this.tipo = tipo;
    }

    /**
     * DEVUELVE EL TIPO DE PARAMETRO
     * @return 
     */
    public Type getTipo() {
        return tipo;
    }

    /**
     * DEVUELVE UNA LISTA DE ID
     * @return 
     */
    public LinkedList<String> getLista() {
        return lista;
    }

    /**
     * DEVUELVE SI ES UNA REFERENCIA
     * @return 
     */
    public Boolean getReferencia() {
        return referencia;
    }
    
    
    
    
    
    
}
