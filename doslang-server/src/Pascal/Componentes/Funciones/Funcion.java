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
import Pascal.Componentes.Break;
import Pascal.Componentes.Continue;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Registros.Registro;
import Pascal.Componentes.Type;
import Pascal.Componentes.UserTypes.Equivalencia;
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
        
        
        codigo = (tipo.getTipo() == Tipo.VOID) ? Generador.generarComentarioSimple("------------------------- INICIO PROCEDURE :" + id) : Generador.generarComentarioSimple("------------------------- INICIO FUNCION :" + id);
        codigo += "\nBegin,,," + this.getIdentificador();
        
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
                    ((Funcion) ins).setIdentificador(nuevo.getId() + "_" + ((Funcion) ins).getId() + ((Funcion) ins).getIdentificadorParametros() );
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
                       if(((Funcion) ins).getTipo().getTipo() != Tipo.VOID) ((Funcion)ins).setPosRelativaRetorno(estado);
                        if (((Funcion) ins).getTipo().getTipo() == TipoDato.Tipo.ID) {
                            String id = ((Funcion) ins).getTipo().getId().toLowerCase();
                            Equivalencia equi = ambito.getEquivalencia(id);
                            if (equi != null) {
                                ((Funcion) ins).getTipo().setTipo(equi.getTipo().getTipo());
                            }

                        }
                        Boolean resul = nuevo.addFuncion((Funcion) ins);
                        //System.out.println(((Funcion) ins).getId() + "_" + resul);
                        if (!resul) {
                            MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), "La funcion: " + ((Funcion) ins).getId() + " ya existe");
                            ambito.addSalida(mensaje);
                        }
                    }
                    
                    
                }
            }
        
        
        
        int ejecutar = 0;
        for(Instruccion i : cuerpo){
            if (!(i instanceof Funcion)) {
                
                //-------------------------------------------- SI ES UN BREAK ------------------------------------------------------------------------------------
                if (i instanceof Break) {
                    MessageError mensaje = new MessageError("Semantico", ((Break) i).getL(), ((Break) i).getC(), "La sentencia BREAK solo puede venir en ciclos");
                    ambito.addSalida(mensaje);
                    return mensaje;

                }
                
                //-------------------------------------------- SI ES UN CCONTINUE ------------------------------------------------------------------------------------
                if (i instanceof Continue) {
                    MessageError mensaje = new MessageError("Semantico", ((Continue) i).getL(), ((Continue) i).getC(), "La sentencia CONTINUE solo puede venir en ciclos");
                    ambito.addSalida(mensaje);
                    return mensaje;
                }


                if (!(i instanceof Declaracion) && ejecutar == 0) {
                    Simbolo s = new Simbolo(id.toLowerCase(), false, true, tipo.getTipo(), Generador.generarStack(), posRelativaRetorno, id);
                    s.setParametro(true);
                    s.setReferencia(false);
                    s.setInicializada(true);
                    if(tipo.getTipo() == Tipo.REGISTRO){
                        Equivalencia equi = ambito.getEquivalencia(tipo.getId().toLowerCase());
                        if(equi == null){
                            MessageError mensaje = new MessageError("Semantico",l,c,"No existe el registro: " + tipo.getId());
                            ambito.addSalida(mensaje);
                            return mensaje;
                        }
                        tipo = equi.getTipo();
                        s.setTipo(Tipo.REGISTRO);
                        Registro r = (Registro)equi.getTipo().getValor();
                        s.setValor(r.getAtributos());
                    }
                    nuevo.addSimbolo(s);
                    
                    

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
                    ambito.getListaCodigoFunciones().putAll(nuevo.getListaCodigoFunciones());
                    ambito.addCodigoFuncion(((Funcion) ins).getIdentificador(), temp.getCodigo3D());
                }
            }
        }
        
        
        
        
        
        codigo += "\n" + ((tipo.getTipo() == Tipo.VOID) ? Generador.generarComentarioSimple("------------------------- FIN PROCEDURE :" + id) : Generador.generarComentarioSimple("------------------------- FIN FUNCION :" + id));
        codigo += "\n" + Generador.generarComentarioSimple("--------------------------- LISTADO DE EXIT ----------------- ");
        codigo += "\n" + Generador.getAllEtiquetas(nuevo.getListadoExit());
        codigo += "\nEnd,,," + this.getIdentificador();
        
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

    
    /**
     * RECORRE LA FUNCION Y NOS DEVULVE LA POSICION EN STACK DONDE ESTARA SU RETURN
     * @return 
     */
    public int primeraPasada(){
        if(tipo.getTipo() == Tipo.VOID) return -1;
        int index = getTamanioTotalParametro() - 1;
        for(Instruccion i : cuerpo){
            if(i instanceof Declaracion)index++; 
        }
        
        return index + 1;
    }

    /**
     * SETEMOS LA POSICION DEL RETURN
     * @param posRelativaRetorno 
     */
    public void setPosRelativaRetorno(int posRelativaRetorno) {
        this.posRelativaRetorno = posRelativaRetorno;
    }
    
    
    /**
     * GENERAMOS UN STRING CON LOS TIPOS DE DATOS EJEMPLO: INT_INT_STRING (INT,INT,STRING)
     * @return 
     */
    public String getIdentificadorParametros(){
        String identificador = "";
        
        for(Parametro p : listaParametros)identificador += "_" + p.getTipo().getTipo();
        
        
        return identificador;
    }

  
    /**
     * METODO PARA BUSCAR LA CANTIDAD TOTAL DE PARAMETROS
     * @return 
     */
    public int getTamanioTotalParametro(){
        int contador = 0;
        for(Parametro p: listaParametros){
            for(String s: p.getLista()){
                contador ++;
            }
        }
        return contador;
    }
        
        
        
    
}
