/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

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
    public Boolean agregarVariable(Simbolo simbolo){
        this.addLast(simbolo);
        return true;
    }
    
    /**
     * METODO QUE OBTIENE UN SIMBOLO DE NUESTRA TABLA DE SIMBOLOS
     * @param id Nombre del simbolo a buscar
     * @return Simbolo simbolo | null no existe
     */
    public Simbolo getVariable(String id){
        for(int i = this.size() - 1; i >= 0; i--){
            if(this.get(i).getId().equalsIgnoreCase(id)) return this.get(i);
        }
        return null;
    }
    
    
   
    
}
