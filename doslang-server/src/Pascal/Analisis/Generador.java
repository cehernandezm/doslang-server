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
public class Generador {
    static int temporal = 0;
    static int etiqueta = 0;
    static int stack  = 0;
    
    /**
     * METODO QUE DEVULVE UN TEMPORAL
     * @return 
     */
    public static String generarTemporal(){
       String  temp = "t" + temporal;
        temporal++;
        return temp;
    }
    
    /**
     * METODO QUE GENERAR UNA ETIQUETA
     * @return 
     */
    public static String generarEtiqueta(){
        String temp = "L" + etiqueta;
        etiqueta++;
        return temp;
    }
    
    /**
     * GENERA UN CUADRUPLO EJEMPLO +,2,3,T0
     * @param operador
     * @param operando1
     * @param operando2
     * @param temporal
     * @return 
     */
    public static String generarCuadruplo(String operador, String operando1, String operando2, String temporal){
        return operador +  "," + operando1 + "," + operando2 + "," + temporal;
    }
    /**
     * GENERAR COMENTARIO SIMPLE EJEMPLO // HOLA
     * @param texto
     * @return 
     */
    public static String generarComentarioSimple(String texto){
        return "//" + texto ;
    }
    
    /**
     * METODO QUE GUARDA EN STACK O EN HEAP EJEMPLO =,0,2,STACK
     * @param estructura
     * @param pos
     * @param valor
     * @return 
     */
    public static String guardarEnPosicion(String estructura, String pos, String valor){
        return "=," + pos + "," + valor + "," + estructura;
    }
    
    /**
     * DEVUELVE UNA ETIQUETA EN EL FORMARTO ETIQUETA :
     * @param codigo
     * @return 
     */
    public static String guardarEtiqueta(String codigo){
        return codigo + ":";
    }
    
    /**
     * DEVUELVE UN GET ASI : =,HEAP,2,T0
     * @param temporal
     * @param estructura
     * @param pos
     * @return 
     */
    public static String guardarAcceso(String temporal, String estructura, String pos){
        return "=," + estructura + "," + pos + "," + temporal;
    }
    
    /**
     * GENERA UN SALTO CONDICIONAL
     * @param etiquetaF
     * @param operadorIzq
     * @param operadorDer
     * @param operador
     * @return 
     */
    public static String guardarCondicional(String etiquetaF,String operadorIzq,String operadorDer,String operador){
        String simbolo = "";
        if(operador == "=") simbolo = "Je";
        else if(operador == "<>") simbolo = "Jne";
        else if(operador == ">") simbolo = "Jg";
        else if(operador == "<") simbolo = "Jl";
        else if(operador == ">=") simbolo = "Jge";
        else simbolo = "Jle";
        return simbolo + "," + operadorIzq + "," + operadorDer + "," + etiquetaF;
    }
    
    /**
     * GENERA UN SALTO INCONDICIONAL
     * @param etiquetaF
     * @return 
     */
    public static String saltoIncondicional(String etiquetaF){
        return "Jmp,,," + etiquetaF;
    }
    
    /**
     * GUARDA LAS ETIQUETAS (SI HAY VARIAS ASI) L1: L2: L3: ...
     * @param etiquetas
     * @return 
     */
    public static String getAllEtiquetas(LinkedList<String> etiquetas){
        String codigo = "";
        for(String s : etiquetas){
            codigo += "\n" + s + ":" ;
        }
        return codigo;
    }
    
    /**
     * METODO QUE GENERA UNA NUEVA POSICION EN EL STACK
     * @return 
     */
    public static int generarStack(){
        int temp = stack;
        stack++;
        return temp;
    }
    
    /**
     * METODO QUE DEVUELVE LA POSICION ACTUAL DEL STACK
     * @return 
     */
    public static int getStack(){
        return stack;
    }
}
