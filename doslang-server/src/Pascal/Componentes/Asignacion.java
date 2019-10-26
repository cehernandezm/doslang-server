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
        if(result == null) return -1;
        if(result instanceof MessageError) return -1;
        
       
        
        
        Simbolo simbolo = ambito.getSimbolo(id);
        //------------------------------------------------- Si no existe ----------------------------------------
        if(simbolo == null){
           MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " no existe");
           ambito.addSalida(mensaje);
           return mensaje;
        }
        
        Nodo nodo = (Nodo)result;
        
        if(simbolo.getTipo() == Tipo.REGISTRO){
            if(nodo.getTipo() == Tipo.MALLOC) simbolo.setInicializada(true);
            else{
                MessageError mensaje = new MessageError("Semantico", l, c, "No coinciden los tipos: " + simbolo.getTipo() + " con: " + nodo.getTipo());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            return -1;
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
        simbolo.setInicializada(true);
        codigo = nodo.getCodigo3D();
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
