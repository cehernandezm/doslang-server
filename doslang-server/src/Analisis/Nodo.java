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
public class Nodo extends TipoDato {
    String resultado;
    Tipo tipo;

    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public Nodo() {
    }

   
    /**
     * TIPO DE RESULTADO
     * @param tipo 
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public Tipo getTipo() {
        return tipo;
    }
    
    
    /**
     * ETIQUETA,ID,VALOR PRIMITIVO
     * @param tipo 
     */
    public String getResultado() {
        return resultado;
    }
    
    /**
     * ETIQUETA,ID,VALOR PRIMITIVO
     * @param tipo 
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    
    
    
}
