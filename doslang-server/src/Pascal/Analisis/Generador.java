/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
     * GUARDA LAS ETIQUETAS (SI HAY VARIAS ASI) L1: L2: L3: ...
     * @param etiquetas
     * @return 
     */
    public static String getAllEtiquetas(HashMap<String,String> etiquetas){
        String codigo = "";
        for(Map.Entry<String,String> entry: etiquetas.entrySet()){
            codigo += "\n" + entry.getValue() + ":";
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

    /**
     * METODO PARA GENERAR UNA FUNCION TRUNK OCUPARA DEL 0-1 EN LA POSICION DEL STACK
     * @return 
     */
    public static String funcionTrunk(){
        String pos = Generador.generarTemporal();
        String val = Generador.generarTemporal();
        String modulo = Generador.generarTemporal();
        String retorno = Generador.generarTemporal();
        String codigo = generarComentarioSimple("------------------------------ FUNCION TRUNK");
        codigo += "\n BEGIN,,,funcionTrunk"; 
        codigo += "\n" + Generador.generarCuadruplo("+", "P", "0", pos);
        codigo += "\n" + Generador.guardarAcceso(val, "Stack", pos);
        codigo += "  " + Generador.generarComentarioSimple(" Obtenemos el valor del double pasado a la funcion");
        codigo += "\n" + Generador.generarCuadruplo("%", val, "1", modulo);
        codigo += "  " + Generador.generarComentarioSimple(" Obtenemos el modulo del double (devuelve el residuo)");
        codigo += "\n" + Generador.generarCuadruplo("-", val, modulo, retorno);
        codigo += "\n" + Generador.generarCuadruplo("+", "P", "1", pos);
        codigo += "\n" + Generador.generarCuadruplo("=", pos, retorno, "Stack");
        codigo += "  " + Generador.generarComentarioSimple("Devolvemos el valor del stack");
        codigo += "\n END,,,funcionTrunk"; 
        codigo += "\n" + generarComentarioSimple("------------------------------ FIN FUNCION TRUNK");
        return codigo;
    }

    /**
     * FUNCION QUE TRADUCE UNA LLAMADA A UNA FUNCION
     * @param id
     * @return 
     */
    public static String llamarAFuncion(String id){
        return "call,,," + id;
    }
    /**
     * METODO QUE GENERA EL CODIGO 3D PARA CASTEAR CUALQUIER NUMERO A UN STRING
     * @return 
     */
    public static String numeroToCadena(){
        String pos = generarTemporal();
        String val = generarTemporal();
        String verdadero = generarEtiqueta();
        String verdadero2 = generarEtiqueta();
        String newVal = generarTemporal();
        String modulo = generarTemporal();
        String ascii = generarTemporal();
        String salto = generarEtiqueta();
        String div = generarTemporal();
        String codigo = generarComentarioSimple("--------------------------- FUNCION QUE CONVIERTE UN NUMERO A CADENA ----------------------------------");
        
        codigo += "\nBegin,,,numeroToCadena";
        codigo += "\n" + generarCuadruplo("+", "P", "0", pos);
        codigo += "\n" + guardarAcceso(val, "Stack", pos);
        codigo += "  " + generarComentarioSimple("   Obtengo el numero que pasare a cadena");
       
        codigo += "\n" +  guardarCondicional(verdadero, val, "0", ">=");
        codigo += "\n" + generarCuadruplo("*", val, "-1", val);
        codigo += "\n" + generarCuadruplo("=", "H", "45","Heap");
        codigo += "\n" + generarCuadruplo("+", "H", "1","H");
        codigo += "\n" + guardarEtiqueta(verdadero);
        codigo += "\n" +  guardarCondicional(verdadero2, val, "10", "<");
        //--------------------------------------------------------------EL VALOR ES MAYOR A 10 ENTONCES HAGO UN TRUNC AL NUMERO ---------------------------------
        codigo += "\n" + generarComentarioSimple("LLAMAMOS A LA FUNCION TRUNC PARA OBTENER EL VALOR ENTERO");
        codigo += "\n" + generarCuadruplo("+", "P", "1", "P");
        codigo += "  " + generarComentarioSimple("   Inicio simulacion de cambio de ambito");
        codigo += "\n" + generarCuadruplo("+", "P","0", pos);
        codigo += "\n" + generarCuadruplo("/", val, "10", div);
        codigo += "\n" + generarCuadruplo("=", pos, div, "Stack");
        codigo += "  " + generarComentarioSimple(" Pasamos: " + div + " como parametro");
        codigo += "\n" + llamarAFuncion("funcionTrunk");
        codigo += "\n" + generarCuadruplo("+", "P", "1", pos);
        codigo += "\n" + guardarAcceso(newVal, "Stack", pos);
        codigo += "  " + generarComentarioSimple("   Obtenemos el valor del return");
        codigo += "\n" + generarCuadruplo("-", "P", "1", "P");
        codigo += "  " + generarComentarioSimple("   Fin simulacion de cambio de ambito");
        codigo += "\n" + generarComentarioSimple("FIN LLAMAMOS A LA FUNCION TRUNC PARA OBTENER EL VALOR ENTERO");
        //-------------------------------------------------------------- LLAMAMOS A LA RECURSIVIDAD DE TOSTRING ---------------------------------------------
       
        codigo += "\n" + generarComentarioSimple("LLAMAMOS A LA FUNCION TOSTRING ");
        codigo += "\n" + generarCuadruplo("+", "P", "1", "P");
        codigo += "  " + generarComentarioSimple("   Inicio simulacion de cambio de ambito");
        codigo += "\n" + generarCuadruplo("+", "P", "0", pos);
        codigo += "\n" + generarCuadruplo("=", pos, newVal, "Stack");
        codigo += "  " + generarComentarioSimple(" Pasamos: " + newVal + " como parametro");
        codigo += "\n" + llamarAFuncion("numeroToCadena");
        codigo += "\n" + generarCuadruplo("-", "P", "1", "P");
        codigo += "  " + generarComentarioSimple("   Fin simulacion de cambio de ambito");
        codigo += "\n" + generarComentarioSimple("FIN LLAMAMOS A LA FUNCION TOSTRING ");
        
        codigo += "\n" + generarCuadruplo("%", val, "10", modulo);
        codigo += "  " + generarComentarioSimple("   Obtenemos el Modulo del valor ingresado");
        codigo += "\n" + generarCuadruplo("+", modulo, "48", ascii);
         codigo += "\n" + "Print(%e," + val + ")";
        codigo += "\n" + "Print(%c,10)";
        codigo += "  " + generarComentarioSimple("   Le sumamos 48 para convertilos a ascii");
        codigo += "\n" + generarCuadruplo("=", "H", ascii, "Heap");
        codigo += "  " + generarComentarioSimple("   Almacenamos el valor en el heap");
        codigo += "\n" + generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + saltoIncondicional(salto);
        
        codigo += "\n" + generarComentarioSimple(" SI EL NUMERO ES MENOR A 10");
        codigo += "\n" + guardarEtiqueta(verdadero2);
        codigo += "\n" + generarCuadruplo("=", val, "", modulo);
        codigo += "  " + generarComentarioSimple("   Obtenemos el Modulo del valor ingresado");
        codigo += "\n" + generarCuadruplo("+", modulo, "48", ascii);
        codigo += "  " + generarComentarioSimple("   Le sumamos 48 para convertilos a ascii");
        codigo += "\n" + generarCuadruplo("=", "H", ascii, "Heap");
        codigo += "  " + generarComentarioSimple("   Almacenamos el valor en el heap");
        codigo += "\n" + generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + guardarEtiqueta(salto);
        
        codigo += "\nEnd,,,numeroToCadena";
        codigo += "\n" + generarComentarioSimple("--------------------------- FIN FUNCION QUE CONVIERTE UN NUMERO A CADENA ----------------------------------");
        return codigo;
    }
    
    
    public static String funcionRound(){
        String posicion = generarTemporal();
        String numero = generarTemporal();
        String decimal = generarTemporal();
        String entero = generarTemporal();
        String retorno = generarTemporal();
        String etiquetaV = generarEtiqueta();
        String etiquetaS = generarEtiqueta();
        
        String codigo = generarComentarioSimple("--------------------------------------- INICIO FUNCION ROUND ------------------------------------------");
        
        
        codigo += "\nBegin,,,funcionRound";
        codigo += "\n" + generarCuadruplo("+", "P", "0", posicion);
        codigo += "\n" + guardarAcceso(numero, "Stack", posicion);
        codigo += "  " + generarComentarioSimple("  Accedemos al parametro");
        codigo += "\n" + generarCuadruplo("%", numero, "1", decimal);
        codigo += "\n" + guardarCondicional(etiquetaV, decimal, "0.5", ">");
        codigo += "\n" + generarCuadruplo("-", numero, decimal, retorno);
        codigo += "  " + generarComentarioSimple("  No es mayor a 0.5 solo se trunca");
        codigo += "\n" + saltoIncondicional(etiquetaS);
        codigo += "\n" + guardarEtiqueta(etiquetaV);
        codigo += "\n" + generarCuadruplo("-", numero, decimal, entero);
        codigo += "\n" + generarCuadruplo("+", entero, "1", retorno);
        codigo += "  " + generarComentarioSimple("  es mayor a 0.5 se aproxima");
        codigo += "\n" + guardarEtiqueta(etiquetaS);
        codigo += "\n" + generarCuadruplo("+", "P", "1", posicion);
        codigo += "\n" + generarCuadruplo("=", posicion, retorno, "Stack");
        codigo += "  " + generarComentarioSimple("Guardamos la variable en el retorno");
        codigo += "\nEnd,,,funcionRound";
        codigo += "\n" + generarComentarioSimple("--------------------------------------- FIN FUNCION ROUND ------------------------------------------");
        return codigo;
    }
}
