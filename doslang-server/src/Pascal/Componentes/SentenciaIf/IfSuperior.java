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
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class IfSuperior implements Instruccion{
    LinkedList<IF> listado;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param listado
     * @param l
     * @param c 
     */
    public IfSuperior(LinkedList<IF> listado, int l, int c) {
        this.listado = listado;
        this.l = l;
        this.c = c;
    }

    @Override
    public Object ejecutar(Ambito ambito) {
        Nodo nodo = new Nodo();
        String codigo = "";
        LinkedList<String> listadoSalto = new LinkedList<>();
        for(IF i : listado){
            Object o = i.ejecutar(ambito);
            if(o instanceof MessageError) return new MessageError("",l,c,"");
            codigo += "\n" + Generador.generarComentarioSimple(" ------------------------ INICIO IF --------------------------------------------");
            Nodo temp = (Nodo)o;
            
            codigo += "\n" + temp.getCodigo3D();
            codigo += "\n" + Generador.generarComentarioSimple(" ------------------------  FIN IF --------------------------------------------");
            
            if(((IF)i).getCondicion() != null){
                codigo += "\n" + Generador.generarComentarioSimple("------------------------- ETIQUETA FALSA -------------------------------------------");
                codigo += "\n" + Generador.getAllEtiquetas(temp.getEtiquetaF());
                listadoSalto.addLast(temp.getSalto());
            } 
        }
        codigo += "\n" + Generador.getAllEtiquetas(listadoSalto);
        nodo.setCodigo3D(codigo);
        return nodo;
    }

    /**
     * OBTENER TODOS LOS SUBIFS
     * @return 
     */
    public LinkedList<IF> getListado() {
        return listado;
    }

    
    
    
    
    
}
