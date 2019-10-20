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

    
    /**
     * METODO ENCARGADO DE ADMINISTRAR UN BOOLEAN (ID U OPERACION)
     * @param temporal
     * @param nodo
     * @return 
     */
    public static String generarBoolean(String temporal, Nodo nodo){
        
        String codigo = "";
        if (nodo.getEtiquetaV().size() > 0) {
            codigo += "\n" + nodo.getCodigo3D();
            nodo.setResultado(temporal);
            //-------------------------------------- ETIQUETAS VERDADERAS --------------------------------
            codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaV());
            
            codigo += "\n" + Generador.generarCuadruplo("=", "1", "", nodo.getResultado());
            String etiquetaTemp = Generador.generarEtiqueta();
            codigo += "\n" + Generador.saltoIncondicional(etiquetaTemp);
            //------------------------------------- ETIQUETA FALSA ----------------------------------------------
            codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaF());
            codigo += "\n" + Generador.generarCuadruplo("=", "0", "", nodo.getResultado());
            //--------------------------------------- ETIQUETA DE SALIDA ---------------------------------------
            codigo += "\n" + Generador.guardarEtiqueta(etiquetaTemp);
        }
        else codigo += "\n" + Generador.generarCuadruplo("=", nodo.getResultado(), "", temporal);
        
        return codigo;
    }
    
    
    /**
     * METODO ENCARGADOR DE TRADUCIR UN 0 A FALSE  Y UN 1 A TRUE
     * @param nodo
     * @return 
     */
    public static String concatenarBoolean(Nodo nodo){
        String codigo = "";
        if (nodo.getEtiquetaV().size() > 0) {
            codigo += "\n" + nodo.getCodigo3D();
            //-------------------------------------- ETIQUETAS VERDADERAS --------------------------------
            codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaV());
            codigo += getTraduccionBoolean(true);
            String etiquetaTemp = Generador.generarEtiqueta();
            codigo += "\n" + Generador.saltoIncondicional(etiquetaTemp);
            //------------------------------------- ETIQUETA FALSA ----------------------------------------------
            codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaF());
            codigo += getTraduccionBoolean(false);
            //--------------------------------------- ETIQUETA DE SALIDA ---------------------------------------
            codigo += "\n" + Generador.guardarEtiqueta(etiquetaTemp);
        }
        else {
            codigo += "\n" + nodo.getCodigo3D();
            String etiquetaV = Generador.generarEtiqueta();
            
            String etiquetaSalida = Generador.generarEtiqueta();
            codigo += "\n" + Generador.guardarCondicional(etiquetaV, "0",nodo.getResultado() ,"=");
            codigo += getTraduccionBoolean(true);
            codigo += "\n" + Generador.saltoIncondicional(etiquetaSalida);
            codigo += "\n" + Generador.guardarEtiqueta(etiquetaV);
            codigo += getTraduccionBoolean(false);
            codigo += "\n" + Generador.guardarEtiqueta(etiquetaSalida);
        }
        return codigo;
    }
    
    
    /**
     * METODO QUE DEVUELVE LA TRADUCCION DE UN TRUE O UN FALSE
     * @param estado
     * @return 
     */
    private static String getTraduccionBoolean(Boolean estado){
        String codigo = "";
        if(estado){
            codigo = "\n" + Generador.generarComentarioSimple("------------------ TRUE ---------------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "84", "Heap"); //---------- T
            codigo += "\n" + Generador.generarCuadruplo("+","H", "1", "H");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "114", "Heap"); //---------- r
            codigo += "\n" + Generador.generarCuadruplo("+","H", "1", "H");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "117", "Heap"); //---------- u
            codigo += "\n" + Generador.generarCuadruplo("+","H", "1", "H");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "101", "Heap"); //---------- e
            codigo += "\n" + Generador.generarCuadruplo("+","H", "1", "H");
            return codigo;
        }
        codigo = "\n" + Generador.generarComentarioSimple("------------------ FALSE ---------------------------");
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "70", "Heap"); //---------- F
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "97", "Heap"); //---------- a
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "108", "Heap"); //---------- l
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "115", "Heap"); //---------- s
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "101", "Heap"); //---------- e
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        return codigo;
    }
}
