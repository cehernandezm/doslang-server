/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Registros;

import Pascal.Analisis.TipoDato;
import Pascal.Componentes.Type;

/**
 *
 * @author Carlos
 */
public class Atributo {
    String id;
    Type tipo;
    
    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param tipo 
     */
    public Atributo(String id, Type tipo) {
        this.id = id.toLowerCase();
        this.tipo = tipo;
    }

    /**
     * OBTENGO EL ID DEL ATRIBUTO
     * @return 
     */
    public String getId() {
        return id;
    }

    /**
     * OBTENGO EL TIPO DE ATRIBUTO
     * @return 
     */
    public Type getTipo() {
        return tipo;
    }

    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }
    
    
    
    
    
    
    
}
