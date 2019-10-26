/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.SentenciaSwitch;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Componentes.Expresion;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class SWITCH implements Instruccion{
    LinkedList<CASE> lista;
    int l;
    int c;
    Expresion condicion;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param lista
     * @param l
     * @param c
     * @param condicion 
     */
    public SWITCH(LinkedList<CASE> lista, int l, int c, Expresion condicion) {
        this.lista = lista;
        this.l = l;
        this.c = c;
        this.condicion = condicion;
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
        
        
        LinkedList<String> listadoSalto = new LinkedList<>();
        for(CASE i : lista){
            i.setCondicion(condicion);
            Object o = i.ejecutar(ambito);
            if(o instanceof MessageError) return new MessageError("",l,c,"");
            codigo += "\n" + Generador.generarComentarioSimple(" ------------------------ INICIO CASE --------------------------------------------");
            Nodo temp = (Nodo)o;
            
            codigo += "\n" + temp.getCodigo3D();
            codigo += "\n" + Generador.generarComentarioSimple(" ------------------------  FIN CASE --------------------------------------------");
            
            if(i.getListaCondicion() != null){
                codigo += "\n" + Generador.generarComentarioSimple("------------------------- ETIQUETA FALSA -------------------------------------------");
                codigo += "\n" + Generador.getAllEtiquetas(temp.getEtiquetaF());
                listadoSalto.addLast(temp.getSalto());
            } 
        }
        codigo += "\n" + Generador.getAllEtiquetas(listadoSalto);
        nodo.setCodigo3D(codigo);
        return nodo;
        
    }
    
    
   
    
    
}
