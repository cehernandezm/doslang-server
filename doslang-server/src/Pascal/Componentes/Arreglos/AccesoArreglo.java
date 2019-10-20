/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Arreglos;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Expresion;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class AccesoArreglo implements Instruccion {
    String id;
    Expresion valor;
    LinkedList<Expresion> dimensiones;
    int l;
    int c;
    Boolean accion;
    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param valor
     * @param dimensiones
     * @param l
     * @param c
     * @param accion TRUE = ACCESO | FALSE = ASIGNACION
     */
    public AccesoArreglo(String id, Expresion valor, LinkedList<Expresion> dimensiones, int l, int c) {
        this.id = id;
        this.valor = valor;
        this.dimensiones = dimensiones;
        this.l = l;
        this.c = c;
        this.accion = accion;
    }

    
    

    @Override
    public Object ejecutar(Ambito ambito) {

        id = id.toLowerCase();
        Simbolo sim = ambito.getSimbolo(id);
        //----------------------------------------------- No existe la variable -------------------------------
        if(sim == null){
            MessageError mensaje = new MessageError("Semantico",l,c, "La variable:" + id + " no existe");
            ambito.addSalida(mensaje);
            return mensaje;
        }
        //--------------------------------- SI LA VARIABLE NO ES DE TIPO ARREGLO -----------------------------------------------------
        if(!(sim.getTipo() == Tipo.ARRAY)){
            MessageError mensaje = new MessageError("Semantico",l,c, "La variable:" + id + " no es de tipo ARRAY: " + sim.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        Object result = (valor == null) ? null : valor.ejecutar(ambito);
        //--------------------------------- SI ES NULL O HUBO UN ERROR EN LA OPERACION----------------------------------------------------
        if(result == null) return new MessageError("",l,c,"");
        if(result instanceof MessageError) return new MessageError("",l,c,"");
        
        //---------------------------------- SI EL VALOR A GUARDAR NO ES IGUAL AL TIPO DEL ARRAY ----------------------------------------
        Nodo expresion = (Nodo)result;
        if(!Declaracion.casteoImplicito(sim.getTipoArreglo(),expresion.getTipo())){
            MessageError mensaje = new MessageError("Semantico",l,c, "El Array es de Tipo: " + sim.getTipoArreglo() + " y se desea asignar un valor: " + expresion.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        //------------------------------------------------------- SI LAS DIMENSIONES NO CONCUERDAN ----------------------------------------------------------------------
        if(!(sim.getCantidadDimensiones() == dimensiones.size())){
            MessageError mensaje = new MessageError("Semantico",l,c, "El Array tiene : " + sim.getCantidadDimensiones() + " dimensiones y se estan obteniendo " + dimensiones.size() + " dimensiones");
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        String codigo = "";
        String etiquetaSalto = Generador.generarEtiqueta();

         //---------------------------------------------------------------- SI ESTOY ALMACENANDO BOOLEANOS
        if (expresion.getTipo() == Tipo.BOOLEAN) {
            String temp = Generador.generarTemporal();
            expresion.setResultado(temp);
            codigo = Generador.generarBoolean(temp, expresion);
        }
        
        Object resultado = obtenerMapeoLexicoGrafico(sim, ambito, etiquetaSalto,dimensiones,l,c);
        if(resultado instanceof MessageError) return new MessageError("",l,c,"");
        
        Nodo nodo = (Nodo) resultado;
        codigo += "\n" + nodo.getCodigo3D();
       
        
        //------------------------------------------------------- EJECUTAMOS LA EXPRESION A LA QUE ESTA SIENDO IGUALADA --------------------------------------------------
       codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- EXPRESION A LA QUE ESTA SIENDO IGUALADA ------------------");
       codigo += "\n" + expresion.getCodigo3D();
       codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- FIN EXPRESION A LA QUE ESTA SIENDO IGUALADA ------------------"); 
       codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- ALMACENAMOS EL VALOR ------------------");
       codigo += "\n" + Generador.generarCuadruplo("=", nodo.getResultado(), expresion.getResultado(), "Heap");
       codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- FIN ALMACENAR EL VALOR ------------------");
       codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- SI EL INTERVALO NO EXISTE ------------------");
       codigo += "\n" + Generador.guardarEtiqueta(etiquetaSalto);
        
       ambito.addCodigo(codigo);
        
        
        
        
        
        return -1;
    }
    
    
    /**
     * METODO QUE SE ENCARGARA DE OBTENER EL MAPEO LEXICOGRAFICO DE UNA ARREGLO
     * @param sim
     * @param ambito
     * @param etiquetaSalto
     * @return 
     */
    public  static Object obtenerMapeoLexicoGrafico(Simbolo sim, Ambito ambito, String etiquetaSalto,LinkedList<Expresion> dimensiones2, int l , int c){
        String codigo = "";
        String posicion = Generador.generarTemporal();
        String posTemp = Generador.generarTemporal();
        LinkedList<String> tams = new LinkedList<>();
        LinkedList<String> valores = new LinkedList<>();
        LinkedList<String> limiteIf = new LinkedList<>();
        LinkedList<String> limiteSuep = new LinkedList<>();
        
        
       
        
        
        codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- ACCEDEMOS A LA VARIABLE :" + sim.getId() + " ------------------------");
        codigo += "\n" + Generador.guardarAcceso(posicion, "Stack",String.valueOf(sim.getPosRelativa()));
        codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- OBTENEMOS EL TAMANIO DE CADA DIMENSION ------------------");
        
        //---------------------------------------------------------------------- OBTENER EL TAMAÑO DESDE EL HEAP ----------------------------------------------------
        for(int i = 0; i < sim.getCantidadDimensiones(); i++){
            String tam = Generador.generarTemporal();
            String li = Generador.generarTemporal();
            String ls = Generador.generarTemporal();
            
            codigo += "\n" + Generador.guardarAcceso(li, "Heap", posicion);
            codigo += "   " + Generador.generarComentarioSimple("Limite inferior de la dimension" + i);
            limiteIf.addLast(li);
            codigo += "\n" + Generador.generarCuadruplo("+", posicion, "1", posicion);
            
            codigo += "\n" + Generador.guardarAcceso(ls, "Heap", posicion);
            codigo += "   " + Generador.generarComentarioSimple("Limite Superior de la dimension" + i);
            limiteSuep.addLast(ls);
            codigo += "\n" + Generador.generarCuadruplo("+", posicion, "1", posicion);
                    
            codigo += "\n" + Generador.guardarAcceso(tam, "Heap", posicion);
            codigo += "   " + Generador.generarComentarioSimple("Tam de dimension:" + i);
            tams.addLast(tam);
            codigo += "\n" + Generador.generarCuadruplo("+", posicion, "1", posicion);
        }
        
        //--------------------------------------------------------------------- OBTENEMOS LA POSICION INGRESADA -------------------------------------------------------
        int index = 0;
        for(Expresion exp: dimensiones2){
            Object resultado = (exp == null) ? null : exp.ejecutar(ambito);
            if(resultado == null) return new MessageError("",l,c,"");
            if(resultado instanceof MessageError) return new MessageError("",l,c,"");
            
            Nodo nodo = (Nodo)resultado;
            if(nodo.getTipo() != Tipo.INT){
                MessageError mensaje = new MessageError("Semantico",l,c,"Se necesita que el intervalo sea un entero");
                ambito.addSalida(mensaje);
                return mensaje;
            }
            codigo += "\n" + Generador.generarComentarioSimple("---------------- Valor de dimension: " + index);
            codigo += "\n" + nodo.getCodigo3D();
            
            codigo += "\n" + Generador.generarComentarioSimple("----------------FIN valor de dimension: " + index);
            valores.addLast(nodo.getResultado());
            index++;
        }
        
        codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- MAPEAMOS LEXICOGRAFICAMENTE FILA,COLUMNA,PROFUNDIDAD ------------------");
        String diferencia = Generador.generarTemporal();
        
        codigo += "\n" + Generador.guardarCondicional(etiquetaSalto, valores.get(0), limiteIf.get(0), "<");
        codigo += "   " + Generador.generarComentarioSimple("Si el indice es menor al limite inferior");
        codigo += "\n" + Generador.guardarCondicional(etiquetaSalto, valores.get(0), limiteSuep.get(0), ">");
        codigo += "   " + Generador.generarComentarioSimple("Si el indice es mayor al limite superior");
        codigo += "\n" + Generador.generarCuadruplo("-", valores.get(0), limiteIf.get(0), diferencia);
        
        posTemp = diferencia;
        for(int i = 1; i < valores.size(); i++){
            String multiplicacion = Generador.generarTemporal();
            String mover = Generador.generarTemporal();
            diferencia = Generador.generarTemporal();
            
            codigo += "\n" + Generador.guardarCondicional(etiquetaSalto, valores.get(i), limiteIf.get(i), "<");
            codigo += "   " + Generador.generarComentarioSimple("Si el indice es menor al limite inferior");
            codigo += "\n" + Generador.guardarCondicional(etiquetaSalto, valores.get(i), limiteSuep.get(i), ">");
            codigo += "   " + Generador.generarComentarioSimple("Si el indice es mayor al limite superior");
            
            codigo += "\n" + Generador.generarCuadruplo("*", posTemp, tams.get(i), multiplicacion);
            codigo += "   " + Generador.generarComentarioSimple("Nos movemos en una dimension");
            codigo += "\n" + Generador.generarCuadruplo("+", multiplicacion, valores.get(i), mover);
            codigo += "   " + Generador.generarComentarioSimple("Movemos el indice en la misma dimension");
            codigo += "\n" + Generador.generarCuadruplo("-", mover, limiteIf.get(i), diferencia);
            codigo += "   " + Generador.generarComentarioSimple("Simulamos que se inicia en 0");
            posTemp = diferencia;
            
        }
        codigo += "\n" + Generador.generarCuadruplo("+", posTemp, posicion, posTemp);
        codigo += "\n" + Generador.generarComentarioSimple("---------------------------------- FIN MAPEAMOS LEXICOGRAFICAMENTE FILA,COLUMNA,PROFUNDIDAD ------------------");
        Nodo nodo = new Nodo();
        nodo.setCodigo3D(codigo);
        nodo.setResultado(posTemp);
        
       return nodo;
    }
    
    
}
