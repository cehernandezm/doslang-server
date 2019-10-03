/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

/**
 *
 * @author Carlos
 */
public class Simbolo extends TipoDato {
    String id;

    /**
     * Constructor de la clase
     * @param id nombre de la variable
     * @param tipo  tipo de la variable
     */
    public Simbolo(String id, Tipo tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    /**
     * OBTENER EL ID DEL SIMBOLO
     * @return 
     */
    public String getId() {
        return id;
    }

    
    

   
    
    
    
    
    
}
