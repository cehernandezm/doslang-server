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
import Pascal.Analisis.TablaSimbolos;
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
    String identificador;

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
        Nodo nodo = new Nodo();
        generarIdentificador();
        
        
        
        codigo = Generador.generarComentarioSimple("------------------------- INICIO FUNCION :" + identificador);
        codigo += "\nBegin,,," + identificador;
        
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
                
                
                Boolean existeIdentificador = ambito.addFuncion(new InfoFuncion(id.toLowerCase(), identificador, tipo, listaParametros, s.getPosRelativa()));
                if (!existeIdentificador) {
                    MessageError mensaje = new MessageError("Semantico", l, c, "Ya existe un identificador para la funcion: " + id);
                    ambito.addSalida(mensaje);
                    return mensaje;
                }
                ejecutar = 1;
                nuevo.setearListaFunciones(ambito.getListaFunciones());
            }
            Object o = i.ejecutar(nuevo);
            if( o instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return o;
            }
            
            Nodo temp = (Nodo)o;
            codigo += "\n" + temp.getCodigo3D();
            System.out.println("TAM: " + nuevo.getTam());
        }
        codigo += "\n" +  Generador.generarComentarioSimple("------------------------- FIN FUNCION :" + identificador);
        codigo += "\nEnd,,," + identificador;
        
        
        
        
        
        
        
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
    /**
     * GENERA UN IDENTIFICADOR PARA LA FUNCION ID + PARAMETROS
     * @param identificador 
     */
    private void generarIdentificador(){
        String identi =  id;
        for(Parametro p : listaParametros){
            for(String s : p.getLista()){
                identi += "_" + p.getTipo().getTipo();
            }
            
        }
        this.identificador = identi;
    }

  
        
        
        
    
}
