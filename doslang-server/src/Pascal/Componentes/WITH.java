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
import Pascal.Componentes.Registros.Atributo;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class WITH implements Instruccion {
    Expresion id;
    LinkedList<Instruccion> cuerpo;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param cuerpo
     * @param l
     * @param c 
     */
    public WITH(Expresion id, LinkedList<Instruccion> cuerpo, int l, int c) {
        this.id = id;
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
        Object o = id.ejecutar(ambito);
        if(o instanceof MessageError) return o;
        
        Nodo registro = (Nodo)o;
        if(registro.getTipo() != Tipo.REGISTRO){
            MessageError mensaje = new MessageError("Semantico",l,c,"La sentencia WITH solo puede ser utilizada en registros: no se reconoce: " + registro.getTipo() );
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        String codigo = "";        
        codigo = registro.getCodigo3D();
        codigo += "\n" + Generador.generarComentarioSimple("----------------------- INICIO SENTENCIA WITH ----------------------------------");
        
        Ambito nuevo = new Ambito(ambito.getId(),ambito,ambito.getArchivo());
        nuevo.setEquivalencias(ambito.getEquivalencias());
        nuevo.setearListaFunciones(ambito.getListaFunciones());
        nuevo.addAllVariables(ambito.getListaVariables());
        nuevo.setPosPadre(registro.getResultado());
        LinkedList<Atributo> listaAtributo = (LinkedList<Atributo>)registro.getValor();
        //----------------------------------------------------------- ALMACENAMOS CADA ATRIBUTO COMO SI FUERA UNA VARIABLE -------------------------------------------
        for(int i = 0; i < listaAtributo.size(); i++){
            Atributo a = listaAtributo.get(i);
            Simbolo s = new Simbolo(a.getId().toLowerCase(),false,true,a.getTipo().getTipo(),i,i,nuevo.getId());
            s.setParametro(true);
            s.setIsAtributo(true);
            nuevo.addSimbolo(s);
        }
        nuevo.setTam(ambito.getTam() + (listaAtributo.size() - 1));
        
        for(Instruccion i : cuerpo){
            o = i.ejecutar(nuevo);
            if(o instanceof MessageError){
                ambito.addSalida(nuevo.getSalida());
                return o;
            }
            Nodo temp = (Nodo)o;
            codigo += "\n" + temp.getCodigo3D();
        }
        
        codigo += "\n" + Generador.generarComentarioSimple("----------------------- FIN SENTENCIA WITH ----------------------------------");
        Nodo nodo = new Nodo();
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
    
}
