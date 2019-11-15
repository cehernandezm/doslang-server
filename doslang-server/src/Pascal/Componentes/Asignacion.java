/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato.Tipo;

/**
 *
 * @author Carlos
 */
public class Asignacion implements Instruccion{

    String id;
    Expresion expresion;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param expresion
     * @param l
     * @param c 
     */
    public Asignacion(String id, Expresion expresion, int l, int c) {
        this.id = id.toLowerCase();
        this.expresion = expresion;
        this.l = l;
        this.c = c;
    }


    
    
    
    
    
    
    /**
     * METODO PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        Object result = (expresion == null) ? null : expresion.ejecutar(ambito);
        if(result == null) return new MessageError("",l,c,"");
        if(result instanceof MessageError) return new MessageError("",l,c,"");
        
       
        
        
        Simbolo simbolo = ambito.getSimbolo(id);
        //------------------------------------------------- Si no existe ----------------------------------------
        if(simbolo == null){
           MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " no existe");
           ambito.addSalida(mensaje);
           return mensaje;
        }
        
        Nodo nodo = (Nodo)result;
        
        if(simbolo.getTipo() == Tipo.REGISTRO){
            if(nodo.getTipo() == Tipo.MALLOC) {
                simbolo.setInicializada(true);
                String codigo = "";
                String pos = Generador.generarTemporal();
                codigo = Generador.generarComentarioSimple("------------------------ Iniciando apartado de espacio para los atributos");
                codigo += nodo.getCodigo3D();
                codigo += "\n" + Generador.generarComentarioSimple("------------------------ FIN apartado de espacio para los atributos");
                if(!(simbolo.getAmbito().equalsIgnoreCase("global"))) codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(simbolo.getPosRelativa()), pos);
                else codigo += "\n" + Generador.generarCuadruplo("=", String.valueOf(simbolo.getPosStack()), "", pos);
                codigo += "\n" + Generador.generarCuadruplo("=", pos, nodo.getResultado(), "Stack");
                codigo += "  " + Generador.generarComentarioSimple("Almacenamos en la variable : " + simbolo.getId() + " la posicion del primer atributo" );
                Nodo temp = new Nodo();
                temp.setCodigo3D(codigo);
                temp.setResultado(nodo.getResultado());
                temp.setTipo(Tipo.MALLOC);
                return temp;
            }
            else if(nodo.getTipo() == Tipo.NULL){
                simbolo.setInicializada(true);
                String codigo = "";
                codigo = Generador.generarCuadruplo("=", String.valueOf(simbolo.getPosRelativa()), "-1", "Stack");
                codigo += "   " + Generador.generarComentarioSimple("--------- REGISTRO NULL " + simbolo.getId());
                Nodo temp = new Nodo();
                temp.setCodigo3D(codigo);
                return temp;
            }
            else if (nodo.getTipo() == Tipo.REGISTRO) {
                simbolo.setInicializada(true);
                String pos = Generador.generarTemporal();
                String codigo = nodo.getCodigo3D();
                if(!(simbolo.getAmbito().equalsIgnoreCase("global"))) codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(simbolo.getPosRelativa()), pos);
                else codigo += "\n" + Generador.generarCuadruplo("=", String.valueOf(simbolo.getPosStack()), "", pos);
                
                codigo += "\n" + Generador.generarCuadruplo("=", pos, nodo.getResultado(), "Stack");
                codigo += "   " + Generador.generarComentarioSimple("--------- REGISTRO Igual a otro registro " + simbolo.getId());
                Nodo temp = new Nodo();
                temp.setCodigo3D(codigo);
                return temp;
            }
            else{
                MessageError mensaje = new MessageError("Semantico", l, c, "No coinciden los tipos: " + simbolo.getTipo() + " con: " + nodo.getTipo());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
        }
        
        //------------------------------------------------- SI NO COINCIDEN LOS TIPOS ------------------------------------------------------------
        if(!(Declaracion.casteoImplicito(simbolo.getTipo(), nodo.getTipo()))){
           MessageError mensaje = new MessageError("Semantico",l,c,"No coinciden los tipos: " + simbolo.getTipo() + " con: " + nodo.getTipo());
           ambito.addSalida(mensaje);
           return mensaje;
        }
        //----------------------------------------------- ES CONSTANTE ---------------------------------------------------------------------------
        if(simbolo.getConstante() == true){
           MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " es constante");
           ambito.addSalida(mensaje);
           return mensaje;
        }
        String codigo = "";
        codigo = nodo.getCodigo3D();
        //------------------------------------------------------- UN ARRAY IGUAL A OTRO ARRAY ----------------------------------------------------------
        if(simbolo.getTipo() == Tipo.ARRAY && nodo.getTipo() == Tipo.ARRAY){
            //------------------------------------------------------------ SI NO COINCIDEN LAS CANTIDAD DE DIMENSIONES --------------------------------
            if(simbolo.getCantidadDimensiones() != nodo.getCantidadDimensiones()){
                MessageError mensaje = new MessageError("Semantico",l,c,"El arreglo a asignar es de : " + nodo.getCantidadDimensiones() + " dimensiones y el arreglo es de: " + simbolo.getCantidadDimensiones());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
            codigo += "\n" + nodo.getCodigo3D();
            codigo += "\n" + Generador.generarCuadruplo("=", String.valueOf(simbolo.getPosRelativa()), String.valueOf(nodo.getResultado()), "Stack");
            codigo += "   " + Generador.generarComentarioSimple("--------- SE HACE UNA INSTANCIA DEL ARREGLO: " + simbolo.getId());
            Nodo temp = new Nodo();
            temp.setCodigo3D(codigo);
            return temp;
        }
        
       
        
        simbolo.setInicializada(true);
        
        if (nodo.getTipo() == Tipo.BOOLEAN) {
            nodo.setResultado(Generador.generarTemporal());
            //-------------------------------------- ETIQUETAS VERDADERAS --------------------------------
            codigo += "\n"  + Generador.getAllEtiquetas(nodo.getEtiquetaV());
            codigo += "\n"  +Generador.generarCuadruplo("=", "1", "", nodo.getResultado());
            String etiquetaTemp = Generador.generarEtiqueta();
            codigo += "\n"  + Generador.saltoIncondicional(etiquetaTemp);
            //------------------------------------- ETIQUETA FALSA ----------------------------------------------
            codigo += "\n"  + Generador.getAllEtiquetas(nodo.getEtiquetaF());
            codigo += "\n"  + Generador.generarCuadruplo("=", "0", "", nodo.getResultado());
            //--------------------------------------- ETIQUETA DE SALIDA ---------------------------------------
            codigo += "\n"  + Generador.guardarEtiqueta(etiquetaTemp);
        }
        
        
        
        codigo += "\n"  + Generador.generarComentarioSimple("------------- Guardando la variable : " + id);

        String temporalP = Generador.generarTemporal();
        codigo += "\n"  + Generador.generarCuadruplo("+", "P", String.valueOf(simbolo.getPosRelativa()), temporalP);
        codigo += "\n"  + Generador.generarCuadruplo("=", temporalP, nodo.getResultado(), "Stack");

        codigo += "\n"  + Generador.generarComentarioSimple("-------------- FIN guardar variable : " + id);
        
        nodo = new Nodo();
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
    
    
    
}
