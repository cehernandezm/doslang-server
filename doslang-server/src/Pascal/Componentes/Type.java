/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes;

import Pascal.Analisis.TipoDato;

/**
 *
 * @author Carlos
 */
public class Type extends TipoDato {
    String id;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param tipo 
     */
    public Type(String id,Tipo tipo) {
        this.id = id.toLowerCase();
        this.tipo = tipo;
    }

    /**
     * OBTENER EL ID
     * @return 
     */
    public String getId() {
        return id;
    }
    
    
}
