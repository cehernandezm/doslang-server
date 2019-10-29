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
    Tipo tipoArreglo;
    int cantidadDimensiones;
    Boolean parametro;
    Boolean referencia;
    String ambito;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id 
     * @param constante 
     * @param inicializada
     * @param tipo 
     */
    public Simbolo(String id, Boolean constante, Boolean inicializada, Tipo tipo, int posStack, int posRelativa, String ambito) {
        this.id = id;
        this.constante = constante;
        this.inicializada = inicializada;
        this.tipo = tipo;
        this.posStack = posStack;
        this.posRelativa = posRelativa;
        this.parametro = false;
        this.referencia = false;
        this.ambito = ambito.toLowerCase();
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

    /**
     * SETEA DE NUEVO LA INICIALIZACION DE UNA VARIABLE
     * @param inicializada 
     */
    public void setInicializada(Boolean inicializada) {
        this.inicializada = inicializada;
    }

    /**
     * DEVUELVE EL TIPO DE ARREGLO
     * @return 
     */
    public Tipo getTipoArreglo() {
        return tipoArreglo;
    }

    /**
     * SETE EAL TIPO DE ARREGLO
     * @param tipoArreglo 
     */
    public void setTipoArreglo(Tipo tipoArreglo) {
        this.tipoArreglo = tipoArreglo;
    }

    /**
     * OBTENGO LA CANTIDAD DE DIMENSIONES PARA EL ARREGLO
     * @return 
     */
    public int getCantidadDimensiones() {
        return cantidadDimensiones;
    }
    
    /**
     * SETEO LA CANTIDAD DE DIMENSIONES
     * @param cantidadDimensiones 
     */
    public void setCantidadDimensiones(int cantidadDimensiones) {
        this.cantidadDimensiones = cantidadDimensiones;
    }

    /**
     * SETEA SI ES UN PARAMETRO
     * @param parametro 
     */
    public void setParametro(Boolean parametro) {
        this.parametro = parametro;
    }

    /**
     * SETEA SI ES UNA REFERENCIA
     * @param referencia 
     */
    public void setReferencia(Boolean referencia) {
        this.referencia = referencia;
    }

    /**
     * DEVUELVE SI ES UN PARAMETRO
     * @return 
     */
    public Boolean getParametro() {
        return parametro;
    }

    /** 
     * DEVUELVE SI ES UNA REFERENCIA
     * @return 
     */
    public Boolean getReferencia() {
        return referencia;
    }

    /**
     * obtengo de que ambito estoy obteniendo las variables
     * @return 
     */
    public String getAmbito() {
        return ambito;
    }
    
    
    
    
    
    
    


    

    
    

   
    
    
    
    
    
}
