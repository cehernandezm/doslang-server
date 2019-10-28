/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Funciones;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Type;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Funcion implements Instruccion{
    LinkedList<Parametro> listaParametros;
    String id;
    Type tipo;
    LinkedList<Instruccion> cuerpo;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param listaParametros
     * @param id
     * @param tipo
     * @param cuerpo
     * @param l
     * @param c 
     */
    public Funcion(LinkedList<Parametro> listaParametros, String id, Type tipo, LinkedList<Instruccion> cuerpo, int l, int c) {
        this.listaParametros = listaParametros;
        this.id = id.toLowerCase();
        this.tipo = tipo;
        this.cuerpo = cuerpo;
        this.l = l;
        this.c = c;
    }
    
    
    

    @Override
    public Object ejecutar(Ambito ambito) {
        String codigo = "";
        String codigoFuncion = "";
        Nodo nodo = new Nodo();
        
        
        
        
        codigo = Generador.generarComentarioSimple("------------------------- INICIO FUNCION :" + id);
        codigo += "\nBegin,,," + ambito.getId() + "_" + id.toLowerCase();
        
        Ambito nuevo = new Ambito(id,ambito,ambito.getArchivo());
        nuevo.addAllVariables(ambito.getListaVariables());
        nuevo.setearListaFunciones(ambito.getListaFunciones());
        
        //--------------------------------------------------- PARAMETROS ------------------------------------------------------
        for(Parametro p: listaParametros){
            Declaracion d = new Declaracion(null,l,c,false,p.getTipo(),p.lista);
            
            d.setParametro(true);
            d.setReferencia(p.getReferencia());
            Object o = d.ejecutar(nuevo);
            if( o instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return o;
            }
        }
        
        int ejecutar = 0;
        
        for(Instruccion i : cuerpo){
            //-------------------------------------------------- DESPUES DE ALMACENAR SUS VARIABLES Y FUNCIONES/PROCEDIMIENTOS SE ALMACENA LA FUNCION ------------------
            if (!(i instanceof Declaracion || i instanceof Funcion) && ejecutar == 0) {
                Simbolo s = nuevo.getSimbolo(id);
                //---------------------------------------- SI NO EXISTE UNA VARIABLE IGUAL PARA EL RETORNO -----------------------------------------
                if (s == null) {
                    MessageError mensaje = new MessageError("Semantico", l, c, "La funcion necesita un retorno");
                    ambito.addSalida(mensaje);
                    return mensaje;
                }
                //------------------------------------------- SI LO QUE SE RETORNA NO ES DEL MISMO TIPO DE LA FUNCION
                if (s.getTipo() != tipo.getTipo()) {
                    MessageError mensaje = new MessageError("Semantico", l, c, "La funcion es de tipo: " + tipo.getTipo() + " y se esta retornando: " + s.getTipo());
                    ambito.addSalida(mensaje);
                    return mensaje;
                }
                
                InfoFuncion f = new InfoFuncion(id.toLowerCase(), ambito.getId(), tipo, listaParametros, s.getPosRelativa());
                Boolean existeIdentificador = ambito.addFuncion(f);
                if (!existeIdentificador) {
                    MessageError mensaje = new MessageError("Semantico", l, c, "Ya existe un identificador para la funcion: " + id);
                    ambito.addSalida(mensaje);
                    return mensaje;
                }
                ejecutar = 1;
                nuevo.setearListaFunciones(f);
            }
            Object o = i.ejecutar(nuevo);
            if( o instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return o;
            }
            
            Nodo temp = (Nodo)o;
            if(i instanceof Funcion) codigoFuncion += "\n" + temp.getCodigo3D();
            else codigo += "\n" + temp.getCodigo3D();
        }
        codigo += "\n" +  Generador.generarComentarioSimple("------------------------- FIN FUNCION :" + id);
        codigo += "\nEnd,,," + id.toLowerCase();
        
        codigo += "\n" + codigoFuncion;
        
        
        
        
        
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    

  
        
        
        
    
}
