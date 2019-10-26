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
import Pascal.Analisis.TipoDato;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class REPEAT implements Instruccion {
    Expresion condicon;
    LinkedList<Instruccion> cuerpo;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param condicon
     * @param cuerpo
     * @param l
     * @param c 
     */
    public REPEAT(Expresion condicon, LinkedList<Instruccion> cuerpo, int l, int c) {
        this.condicon = condicon;
        this.cuerpo = cuerpo;
        this.l = l;
        this.c = c;
    }
    
    
    /**
     * METODO DE LA CLASE PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        Nodo nodo = new Nodo();
        String codigo = "";
        Object res = condicon.ejecutar(ambito);
        String recursividad = Generador.generarEtiqueta();
        String etiquetaCondicion = Generador.generarEtiqueta();
        //--------------------------------------------------- SI HAY UN ERROR AL EVALUAR LA CONDICION -------------------------------------------------
        if(res instanceof MessageError) return new MessageError("",l,c,"");
        
        Nodo condicion = (Nodo)res;
        
        //------------------------------------------------- SI LA CONDICION NO ES BOOLEAN ------------------------------------------------------------
        if( condicion.getTipo() != TipoDato.Tipo.BOOLEAN){
            MessageError mensaje = new MessageError("Semantico",l,c,"Se esperaba una expresion Boolean no se reconoce: " + condicion.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        codigo = Generador.generarComentarioSimple("-------------------------------------- INICIO REPEAT -----------------------------------");
        codigo = Generador.saltoIncondicional(recursividad);
        codigo += "\n" + Generador.guardarEtiqueta(etiquetaCondicion);
        codigo += "   " + Generador.generarComentarioSimple(" Etiqueta que hace la recursividad del Repeat");
        codigo += "\n" + condicion.getCodigo3D();
        
        //-------------------------------------------------- SI LA CONDICION ES UN TRUE SIMPLE O UN FALSE ---------------------------------------
        if (condicion.getEtiquetaV() == null) {
            String verdadera = Generador.generarEtiqueta();
            String falsa = Generador.generarEtiqueta();
            condicion.setEtiquetaV(new LinkedList<>());
            condicion.setEtiquetaF(new LinkedList<>());

            condicion.addEtiquetaV(verdadera);
            condicion.addEtiquetaF(falsa);
            codigo += "\n" + Generador.generarComentarioSimple("------------------------- CONDICION -------------------------------------------");
            codigo += "\n" + Generador.guardarCondicional(verdadera, condicion.getResultado(), "1", "=");
            codigo += "\n" + Generador.saltoIncondicional(falsa);
            codigo += "\n" + Generador.generarComentarioSimple("------------------------- FIN CONDICION -------------------------------------------");
        }
        
        codigo += "\n" + Generador.getAllEtiquetas(condicion.getEtiquetaV());
        codigo += "  " + Generador.generarComentarioSimple(" Etiqueta Verdadera");
        codigo += "\n" + Generador.generarComentarioSimple("----------- CUERPO REPEAT ------------");
        codigo += "\n" + Generador.guardarEtiqueta(recursividad);
        Ambito nuevo = new Ambito("repeat",ambito,ambito.getArchivo());
        nuevo.addAllVariables(ambito.getListaVariables());
        for(Instruccion i : cuerpo){
            Object resultado = i.ejecutar(nuevo);
            if(resultado instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return new MessageError("",l,c,"");
            }
            Nodo cod = (Nodo)resultado;
            codigo += "\n" + cod.getCodigo3D();
        }
        
        codigo += "\n" + Generador.saltoIncondicional(etiquetaCondicion);
        codigo += "  " + Generador.generarComentarioSimple("Salto que nos lleva al inicio");
        codigo += "\n" + Generador.generarComentarioSimple("----------- FIN CUERPO REPEAT ------------");
        
        
        
        
        codigo += "\n" + Generador.getAllEtiquetas(condicion.getEtiquetaF());
        codigo += " " + Generador.generarComentarioSimple(" Etiquetas Falsas");
        
        
        
        codigo += "\n" + Generador.generarComentarioSimple("-------------------------------- FIN REPEAT --------------------------------------");
        
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
    
}
