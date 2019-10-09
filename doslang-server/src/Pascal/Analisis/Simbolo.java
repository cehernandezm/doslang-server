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
    Boolean constante;
    Boolean inicializada;
    int posStack;
    int posRelativa;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id 
     * @param constante 
     * @param inicializada
     * @param tipo 
     */
    public Simbolo(String id, Boolean constante, Boolean inicializada, Tipo tipo, int posStack, int posRelativa) {
        this.id = id;
        this.constante = constante;
        this.inicializada = inicializada;
        this.tipo = tipo;
        this.posStack = posStack;
        this.posRelativa = posRelativa;
    }

    

    /**
     * OBTENER EL ID DEL SIMBOLO
     * @return 
     */
    public String getId() {
        return id;
    }

    /**
     * SABER SI ES UNA CONSTANTE
     * @return 
     */
    public Boolean getConstante() {
        return constante;
    }

    /**
     * SABER SI YA FUE INICIALIZADA
     * @return 
     */
    public Boolean getInicializada() {
        return inicializada;
    }

    /**
     * DEVUELVE LA POSICION EN EL STACK
     * @return 
     */
    public int getPosStack() {
        return posStack;
    }
    
    /**
     * DEVUELVE SU POSICION RELATIVA
     * @return 
     */
    public int getPosRelativa() {
        return posRelativa;
    }
    
    


    

    
    

   
    
    
    
    
    
}
