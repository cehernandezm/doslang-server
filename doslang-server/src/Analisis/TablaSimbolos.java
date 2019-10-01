/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analisis;

import Analisis.TipoDato.Tipo;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class TablaSimbolos extends LinkedList<Simbolo>{

    /**
     * CONSTRUCTOR DE LA CLASE PADRE
     */
    public TablaSimbolos() {
        super();
    }
    
    
    /**
     * METODOQ QUE AGREGA UN NUEVO SIMBOLO A NUESTRA TABLA
     * @param id NOMBRE DEL NUEVO SIMBOLO
     * @param tipo TIPO DEL NUEVO SIMBOLO
     * @return True agregada | false ya existe
     */
    public Boolean agregarVariable(String id, Tipo tipo){
        Simbolo s = getVariable(id);
        if(s == null){
            this.addLast(new Simbolo(id,tipo));
            return true;
        }
        return false;
    }
    
    /**
     * METODO QUE OBTIENE UN SIMBOLO DE NUESTRA TABLA DE SIMBOLOS
     * @param id Nombre del simbolo a buscar
     * @return Simbolo simbolo | null no existe
     */
    public Simbolo getVariable(String id){
        for(Simbolo s: this){
            if(s.getId().equals(id)) return s;
        }
        return null;
    }
    
}
