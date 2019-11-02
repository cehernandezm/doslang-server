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
import Pascal.Analisis.TipoDato;
import Pascal.Analisis.TipoDato.Operacion;
import Pascal.Componentes.Break;
import Pascal.Componentes.Expresion;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class CASE implements Instruccion {
    LinkedList<Expresion> listaCondicion;
    LinkedList<Instruccion> cuerpo;
    Expresion condicion;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE PADRE
     * @param listaCondicion
     * @param cuerpo
     * @param l
     * @param c 
     */
    public CASE(LinkedList<Expresion> listaCondicion, LinkedList<Instruccion> cuerpo, int l, int c) {
        this.listaCondicion = listaCondicion;
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
        LinkedList<String> listadoFalsa = new LinkedList<>();
        LinkedList<String> listaVerdadera = new LinkedList<>();
        //------------------------------------ ES UN CASE NORMAL Y NO UN DEFAULT --------------------------------------------
        if(listaCondicion != null){
            String etiquetaV = Generador.generarEtiqueta();
            String etiquetaF = Generador.generarEtiqueta();
            
            
            
           
            
            for(Expresion e : listaCondicion){
                Expresion verificar = new Expresion(condicion,e,Operacion.IGUAL,l,c);
                Object resultado = verificar.ejecutar(ambito);
                
                //------------------------------------ SI SE ENCONTRO UN ERROR EN LA COMPARACION ------------------------------------------------
                if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                Nodo res = (Nodo) resultado;
                codigo += "\n" + res.getCodigo3D();

                listaVerdadera.addAll(res.getEtiquetaV());
                listadoFalsa.addAll(res.getEtiquetaF());
                
                
                
            }
           
            
           
            
            
        }
        
        codigo += "\n" + Generador.generarComentarioSimple("------------------------- ETIQUETA VERDADERA -------------------------------------------");
        codigo += "\n" + Generador.getAllEtiquetas(listaVerdadera);

        Ambito nuevo = new Ambito(ambito.getId(), ambito, ambito.getArchivo());
        nuevo.addAllVariables(ambito.getListaVariables());
        nuevo.setearListaFunciones(ambito.getListaFunciones());
        nuevo.setTam(ambito.getTam());
        nuevo.setEquivalencias(ambito.getEquivalencias());
        for (Instruccion ins : cuerpo) {
            Object o = ins.ejecutar(nuevo);
            if (o instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return new MessageError("", l, c, "");
            }
            ambito.addListadoBreak(nuevo.getListadoBreak());
            ambito.addListadoContinue(nuevo.getListadoContinue());
            
            Nodo temp = (Nodo) o;
            codigo += "\n" + temp.getCodigo3D();
        }

        if (listaCondicion != null) {
            String salto = Generador.generarEtiqueta();
            nodo.setSalto(salto);
            codigo += "\n" + Generador.saltoIncondicional(salto);
        }
        
        nodo.setEtiquetaF(listadoFalsa);
        nodo.setCodigo3D(codigo);
        return nodo;
    }

    /**
     * METODO DONDE OBTENDREMOS LA CONDICION GENERAL
     * @param condicion 
     */
    public void setCondicion(Expresion condicion) {
        this.condicion = condicion;
    }

    /**
     * DEVUELVE EL LISTADO DE EXPRESIONES A EVALUAR
     * @return 
     */
    public LinkedList<Expresion> getListaCondicion() {
        return listaCondicion;
    }
    
    
    
    
    
    
    
}
