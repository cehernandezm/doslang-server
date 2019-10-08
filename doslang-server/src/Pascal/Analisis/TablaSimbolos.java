/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import Pascal.Analisis.TipoDato.Tipo;
import java.util.HashMap;

/**
 *
 * @author Carlos
 */
public class TablaSimbolos extends HashMap<String,Simbolo>{

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
        Simbolo s = getVariable(simbolo.getId());
        if(s == null){
            this.put(simbolo.getId(), simbolo);
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
        if(existeVariable(id)) return this.get(id);
        return null;
    }
    
    
    /**
     * METODO QUE VERIFICA QUE EXISTA UN SIMBOLO
     * @param id
     * @return 
     */
    public Boolean existeVariable(String id){
        return this.containsKey(id);
    }
    
}
