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
import Pascal.Analisis.TipoDato;
import Pascal.Analisis.TipoDato.Tipo;
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
    String identificador;
    Type tipo;
    LinkedList<Instruccion> cuerpo;
    int l;
    int c;
    int posRelativaRetorno;

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
        Boolean flagTipo =  (tipo.getTipo() == Tipo.VOID) ? true : false;
        
        
        codigo = (tipo.getTipo() == Tipo.VOID) ? Generador.generarComentarioSimple("------------------------- INICIO PROCEDURE :" + id) : Generador.generarComentarioSimple("------------------------- INICIO FUNCION :" + id);
        codigo += "\nBegin,,," + ambito.getId() + "_" + id.toLowerCase();
        
        Ambito nuevo = new Ambito(id,ambito,ambito.getArchivo());
        nuevo.addAllVariables(ambito.getListaVariables());
        nuevo.setearListaFunciones(ambito.getListaFunciones());
        nuevo.setEquivalencias(ambito.getEquivalencias());
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
        
         /**
             * PRIMER RECORRIDO BUSCAMOS FUNCIONES Y GUARDAMOS SU RETORNO
             */
            
            for(Instruccion ins : cuerpo){
                if(ins instanceof Funcion){
                    ((Funcion) ins).setIdentificador(nuevo.getId() + "_" + ((Funcion) ins).getId() );
                    int estado = ((Funcion)ins).primeraPasada();
                    if(estado != -1 && ((Funcion)ins).getTipo().getTipo() == Tipo.VOID ){
                        MessageError mensaje = new MessageError("Semantico",((Funcion) ins).getL(), ((Funcion) ins).getC()," Los Procedures no retornan ni un valor");
                        ambito.addSalida(mensaje);  
                        return mensaje;
                    }
                    else if(estado == -1 && ((Funcion) ins).getTipo().getTipo() != Tipo.VOID){
                        MessageError mensaje = new MessageError("Semantico",((Funcion) ins).getL(), ((Funcion) ins).getC()," Las funciones tienen que retornar un valor");
                        ambito.addSalida(mensaje);  
                        return mensaje;
                    }
                    else{
                        Boolean resul = nuevo.addFuncion((Funcion) ins);
                        if (!resul) {
                            MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), "La funcion: " + ((Funcion) ins).getId() + " ya existe");
                            ambito.addSalida(mensaje);  
                            return mensaje;
                        }
                    }
                    
                    
                }
            }
        
        
        
        int ejecutar = 0;
        for(Instruccion i : cuerpo){
            if (!(i instanceof Funcion)) {

                

                if (!(i instanceof Declaracion) && ejecutar == 0) {
                    Simbolo s = nuevo.getSimbolo(id);
                    //---------------------------------------- SI NO EXISTE UNA VARIABLE IGUAL PARA EL RETORNO -----------------------------------------
                    if (s == null && !flagTipo) {
                        MessageError mensaje = new MessageError("Semantico", l, c, "La funcion necesita un retorno");
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    if (s != null) {
                        //------------------------------------------- SI LO QUE SE RETORNA NO ES DEL MISMO TIPO DE LA FUNCION
                        if (s.getTipo() != tipo.getTipo()) {
                            MessageError mensaje = new MessageError("Semantico", l, c, "La funcion es de tipo: " + tipo.getTipo() + " y se esta retornando: " + s.getTipo());
                            ambito.addSalida(mensaje);
                            return mensaje;
                        }
                    }
                    System.out.println("Funcion: " + id + " tam: " + nuevo.getTam());
                    posRelativaRetorno = (!flagTipo) ? 0 : s.getPosRelativa();

                    ejecutar = 1;
                }
                
                Object o = i.ejecutar(nuevo);
                if (o instanceof MessageError) {
                    ambito.setSalida(nuevo.getSalida());
                    return o;
                }
                Nodo temp = (Nodo) o;
                if (i instanceof Funcion) {
                    codigoFuncion += "\n" + temp.getCodigo3D();
                } else {
                    codigo += "\n" + temp.getCodigo3D();
                }
            }
        }
        
        
        //---------------------------------------------------- AGREGAMOS EL CODIGO DE LAS FUNCIONES ----------------------------------------------------------
        for (Instruccion ins : cuerpo) {
            if (ins instanceof Funcion) {
                Object res = ins.ejecutar(nuevo);
                if (res instanceof MessageError) {
                    ambito.setSalida(nuevo.getSalida());
                    return res;
                } else {
                    Nodo temp = (Nodo) res;
                    ambito.addCodigoFuncion(((Funcion) ins).getIdentificador(), temp.getCodigo3D());
                }
            }
        }
        
        
        
        
        
        codigo += "\n" + ((tipo.getTipo() == Tipo.VOID) ? Generador.generarComentarioSimple("------------------------- FIN PROCEDURE :" + id) : Generador.generarComentarioSimple("------------------------- FIN FUNCION :" + id));
        codigo += "\nEnd,,," + id.toLowerCase();
        
        codigo += "\n" + codigoFuncion;
        
        

        
        
        nodo.setCodigo3D(codigo);
        return nodo;
    }

    /**
     * METODO PARA OBTENER LA LISTA DE PARAMETROS
     * @return 
     */
    public LinkedList<Parametro> getListaParametros() {
        return listaParametros;
    }

    /**
     * OBTENEMOS EL ID DE LA FUNCION
     * @return 
     */
    public String getId() {
        return id;
    }

    /**
     * OBTENEMOS UN IDENTIFICADOR
     * @return 
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * GUARDAMOS UN IDENTIFICADOR
     * @param identificador 
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    /**
     * OBTENEMOS LA LINEA
     * @return 
     */
    public int getL() {
        return l;
    }

    /**
     * OBTENEMOS LA COLUMNA
     * @return 
     */
    public int getC() {
        return c;
    }

    /**
     * DEVUELVE LA POSICION RELATIVA DEL RETORNO
     * @return 
     */
    public int getPosRelativaRetorno() {
        return posRelativaRetorno;
    }
    
    /**
     * OBTENEMOS EL TIPO DE LA FUNCION
     * @return 
     */
    public Type getTipo() {
        return tipo;
    }

    
    
    public int primeraPasada(){
        int index = listaParametros.size();
        for(Instruccion i : cuerpo){
            if(i instanceof Declaracion){
                
                Declaracion d = (Declaracion) i;
                LinkedList<String> lista = d.getLista();
                for(String s : lista){
                    if(s.equalsIgnoreCase(id)){
                        posRelativaRetorno = index;
                        return index;
                    }
                    index ++;
                }
            }
        }
        
        return -1;
    }
    
    
    

  
        
        
        
    
}
