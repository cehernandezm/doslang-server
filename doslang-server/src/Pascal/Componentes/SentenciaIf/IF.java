/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.SentenciaIf;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Expresion;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class IF implements Instruccion{
    Expresion condicion;
    int l;
    int c;
    LinkedList<Instruccion> cuerpo;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param condicion
     * @param l
     * @param c
     * @param cuerpo 
     */
    public IF(Expresion condicion, int l, int c, LinkedList<Instruccion> cuerpo) {
        this.condicion = condicion;
        this.l = l;
        this.c = c;
        this.cuerpo = cuerpo;
    }

    @Override
    public Object ejecutar(Ambito ambito) {
        Nodo nodo = new Nodo();
        String codigo = "";
        LinkedList<String> listadoFalsa = new LinkedList<>();
        Object nodoCondicion = (condicion == null) ? null : condicion.ejecutar(ambito);
        
        //-------------------------------------------- Si hay un error en la Expresion ------------------------------------------------------------
        if(nodoCondicion instanceof MessageError) return new MessageError("",l,c,"");
        
        if (nodoCondicion != null) {
            Nodo con = (Nodo) nodoCondicion;
            //---------------------------------------------- Si la expresion no es de tipo Boolean ----------------------------------------------------
            if (con.getTipo() != Tipo.BOOLEAN) {
                MessageError mensaje = new MessageError("Semantico", l, c, " la expresion tiene que ser de tipo Boolean no se reconoce: " + con.getTipo());
                ambito.addSalida(mensaje);
                return mensaje;
            }

            codigo = con.getCodigo3D();
            //-------------------------------------------- ES UN BOOLEAN NORMAL COMO TRUE O FALSE ---------------------------------------------------
            if (con.getEtiquetaV() == null) {
                String verdadera = Generador.generarEtiqueta();
                String falsa = Generador.generarEtiqueta();
                con.setEtiquetaV(new LinkedList<>());
                con.setEtiquetaF(new LinkedList<>());

                con.addEtiquetaV(verdadera);
                con.addEtiquetaF(falsa);
                codigo += "\n" + Generador.generarComentarioSimple("------------------------- CONDICION -------------------------------------------");
                codigo += "\n" + Generador.guardarCondicional(verdadera, con.getResultado(), "1", "=");
                codigo += "\n" + Generador.saltoIncondicional(falsa);
                codigo += "\n" + Generador.generarComentarioSimple("------------------------- FIN CONDICION -------------------------------------------");
            }
            codigo += "\n" + Generador.generarComentarioSimple("------------------------- ETIQUETA VERDADERA -------------------------------------------");
            codigo += "\n" + Generador.getAllEtiquetas(con.getEtiquetaV());
            listadoFalsa = con.getEtiquetaF();
        }
        
        
        
        Ambito nuevo = new Ambito(ambito.getId(),ambito,ambito.getArchivo());
        nuevo.addAllVariables(ambito.getListaVariables());
        nuevo.setearListaFunciones(ambito.getListaFunciones());
        for(Instruccion ins : cuerpo){
            Object o = ins.ejecutar(nuevo);
            if(o instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return new MessageError("",l,c,"");
            }
            Nodo temp = (Nodo)o;
            codigo += "\n" + temp.getCodigo3D();
        }
        
        
        if(nodoCondicion != null){
            String salto = Generador.generarEtiqueta();
            nodo.setSalto(salto);
            codigo += "\n" + Generador.saltoIncondicional(salto);
        }
        nodo.setCodigo3D(codigo);
        nodo.setEtiquetaF(listadoFalsa);
        return nodo;
    }

    /**
     * OBTENEMOS LA CONDICION DEL IF
     * @return 
     */
    public Expresion getCondicion() {
        return condicion;
    }
    
    
    
    
    
}
