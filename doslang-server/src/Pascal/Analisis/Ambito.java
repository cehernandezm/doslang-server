/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Funciones.Funcion;
import Pascal.Componentes.Funciones.InfoFuncion;
import Pascal.Componentes.Funciones.Parametro;
import Pascal.Componentes.UserTypes.Equivalencia;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Carlos
 */
public class Ambito {
    String id;
    Ambito old;
    String archivo;
    int tam;
    TablaSimbolos listaVariables;
    LinkedList<Object> salida;
    LinkedList<Equivalencia> equivalencias; 
    LinkedList<InfoFuncion> listaFunciones; 
    String codigo = "";
    int posInicio;
    Simbolo tempSimbolo;
    
    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id Ambito actual
     * @param old   Ambito anterior
     * @param archivo Archivo que estamos analizando
     */
    public Ambito(String id, Ambito old, String archivo) {
        this.id = id;
        this.old = old;
        this.archivo = archivo;
        this.listaVariables = new TablaSimbolos();
        this.tam = 0;
        this.salida = new LinkedList<>();
        this.codigo = "";
        this.posInicio = Generador.getStack();
        this.equivalencias = new LinkedList<>();
        this.listaFunciones = new LinkedList<>();
    }

    /**
     * OBTENER ID DE AMBITO ACTUAL
     * @param id 
     */
    public String getId() {
        return this.id;
    }

    
    /**
     * METODO QUE DEVUELVE EL AMBITO ANTERIOR
     * @param old 
     */
    public Ambito getOldAmbito(Ambito old) {
        return old;
    }

    /**
     * METODO QUE DEVUELVE EL PATH DEL ARCHIVO ACTUAL
     * @return 
     */
    public String getArchivo() {
        return archivo;
    }
    
    
    
    
    /**
     * METODO QUE BUSCA UN SIMBOLO EN EL ACTUAL AMBITO O ANTERIOR
     * @param id
     * @return 
     */
    public Simbolo getSimbolo(String id){
        return listaVariables.getVariable(id);
    }
    
    
    public Boolean addFuncion(InfoFuncion funcion){
        if(buscarIdentificador(funcion.getNombre().toLowerCase(), false)) return false;
        if(buscarFuncion(funcion.getIdentificador()) != null) return false;
        listaFunciones.addLast(funcion);
        return true;
    }
    
    
    /**
     * METODO QUE AGREGA SIMBOLOS AL AMBITO
     * @param id
     * @param tipo
     * @return 
     */
    public Boolean addSimbolo(Simbolo simbolo){
        tempSimbolo = simbolo;
        if(!buscarIdentificador(simbolo.getId(),true)){
            if(simbolo.getParametro()) simbolo.setInicializada(true);
            listaVariables.agregarVariable(simbolo);
            return true;
        }
        
        return false;
    }
    
    /**
     * AGREGAR LAS VARIABLES DE UNA TABLA PADRE ESTO OCURRIRRA CUANDO SE CREEN SUBAMBITOS
     * @param tabla 
     */
    public void addAllVariables(TablaSimbolos tabla){
        listaVariables.addAll(tabla);
    }

    /**
     * OBTENER EL CODIGO 3D
     * @return 
     */
    public String getCodigo() {
        return codigo;
    }
    
    /**
     * AGREGARA CODIGO 3D AL AMBITO
     * @param codigo3d 
     */
    public void addCodigo(String codigo3d){
        this.codigo += "\n" + codigo3d;
    }

    /**
     * OBTENER LA SALIDA 
     * @return 
     */
    public LinkedList<Object> getSalida() {
        return salida;
    }

    /**
     * AGREGAR UN MENSAJE DE ERROR O DE ACEPTACION
     * @param salida 
     */
    public void addSalida(Object salida) {
        this.salida.addLast(salida);
    }

    
    /**
     * METODO QUE DEVUELVE LAS ETIQUETAS EN FORMATO : ETIQUETA+ :
     * @param lista
     * @return 
     */
    public String generarEtiquetas(LinkedList<String> lista){
        String etiquetas = "";
        for(int i = 0; i < lista.size(); i++){
            etiquetas += lista.get(i) + ":\n";
        }
        return etiquetas;
    }
    
    
    /**
     * NOS DEVUELVE NUESTRA POSICION RELATIVA
     * @return 
     */
    public int getRelativa(){
        int temp = tam;
        tam++;
        return temp;
    }
    
    
    /**
     * METODO QUE AGREGA UNA NUEVA EQUIVALENCIA
     * SI YA EXISTE RETORNA UN FALSE
     * SI SE AGREGA REETORNA UN TRUE
     * @param equivalencia
     * @return 
     */
    public Boolean agregarEquivalencia(Equivalencia equivalencia){
        if(!buscarIdentificador(equivalencia.getNombre(),true)){
            equivalencias.addLast(equivalencia);
            return true;
        }
        return false;
    }
    
    
    /**
     * METODO QUE BUSCA UNA EQUIVALENCIA
     * BUSCA EN LA LISTA DE EQUIVALENCIAS
     * @param nombre
     * @return 
     */
    public Equivalencia getEquivalencia(String nombre){
        for(Equivalencia e : equivalencias){
            if(e.getNombre().equals(nombre)) return e;
        }
        return null;
    }
    
    /**
     * METODO QUE BUSCA EL IDENTIFICADOR
     * LO BUSCA EN:
     * VARIABLES
     * EQUIVALENCIAS
     * @param nombre
     * @return 
     */
    public Boolean buscarIdentificador(String nombre, Boolean flag){
        //--------------------------- BUSQUEDA EN EQUIVALENCIAS --------------------------------------------------
        if(getEquivalencia(nombre) != null) return true;
        //--------------------------- BUSQUEDA EN VARIABLES ------------------------------------------------------
        if(getSimbolo(nombre) != null){
            if(! tempSimbolo.getParametro()) return true;
        }
        //--------------------------- BUSQUEDA FUNCIONES --------------------------------------------------------
        if(buscarFuncion(nombre) != null && flag) return true;
        return false;
    }

    /**
     * DEVUELVE EL TAMANIO DEL AMBITO
     * @return 
     */
    public int getTam() {
        return tam;
    }

    /**
     * DEVUELVE LA LISTA DE VARIABLES
     * @return 
     */
    public TablaSimbolos getListaVariables() {
        return listaVariables;
    }

    /**
     * SETEA UNA LISTA DE SALIDA
     * @param salida 
     */
    public void setSalida(LinkedList<Object> salida) {
        this.salida = salida;
    }
    
    /**
     * DEVUELVE UNA FUNCION
     * @param nombre
     * @return 
     */
    private InfoFuncion buscarFuncion(String nombre){
        nombre = nombre.toLowerCase();
        for(InfoFuncion f : listaFunciones){
            if(f.getNombre().equalsIgnoreCase(nombre)) return f;
        }
        return null;
    }
    
    /**
     * DEVUELVE UNA FUNCION
     * @param nombre
     * @return 
     */
    private InfoFuncion buscarFuncion(String nombre,LinkedList<Parametro> parametros){
        nombre = nombre.toLowerCase();
        Boolean flag = true;
        for(InfoFuncion f : listaFunciones){
            flag = true;
            if(f.getNombre().equalsIgnoreCase(nombre)){
                if(parametros.size() == f.getListaParametros().size()){
                    for(int i = 0; i < parametros.size(); i++){
                        Parametro para1 = parametros.get(i);
                        Parametro para2 = f.getListaParametros().get(i);
                        if(para1.getTipo().getTipo() != para2.getTipo().getTipo()){
                            if(para1.getTipo().getTipo() == Tipo.WORD && para2.getTipo().getTipo() == Tipo.STRING){}
                            else {
                                flag = false;
                                break;
                            }
                        }
                    }
                }
                if(flag) return f;
            }
        }
        return null;
    }
    
       /**
     * DEVUELVE UNA FUNCION
     * @param nombre
     * @return 
     */
    public InfoFuncion buscarFuncionLlamada(String nombre,LinkedList<Nodo> parametros){
        nombre = nombre.toLowerCase();
        Boolean flag = true;
        for(InfoFuncion f : listaFunciones){
            flag = true;
            if(f.getNombre().equalsIgnoreCase(nombre)){
                if(parametros.size() == f.getListaParametros().size()){
                    for(int i = 0; i < parametros.size(); i++){
                        Nodo para1 = parametros.get(i);
                        Parametro para2 = f.getListaParametros().get(i);
                        if(para1.getTipo() != para2.getTipo().getTipo()){
                            if(para1.getTipo() == Tipo.WORD && para2.getTipo().getTipo() == Tipo.STRING){}
                            else {
                                flag = false;
                                break;
                            }
                        }
                    }
                }
                if(flag) return f;
            }
        }
        return null;
    }
    
    
    
    /**
     * SETEA UNA NUEVA LISTA DE FUNCIONES
     * @param funciones 
     */
    public void setearListaFunciones(LinkedList<InfoFuncion> funciones){
        this.listaFunciones.addAll(funciones);
    }

    /**
     * SETEA UNA NUEVA LISTA DE FUNCIONES
     * @param funciones 
     */
    public void setearListaFunciones(InfoFuncion funcion){
        this.listaFunciones.addLast(funcion);
    }
    
    /** 
     * OBTENEMOS LA LISTA DE FUNCIONES
     * @return 
     */
    public LinkedList<InfoFuncion> getListaFunciones() {
        return listaFunciones;
    }

    public Ambito getOld() {
        return old;
    }

    public LinkedList<Equivalencia> getEquivalencias() {
        return equivalencias;
    }

    public void setEquivalencias(LinkedList<Equivalencia> equivalencias) {
        this.equivalencias = equivalencias;
    }
    
    
    
    
}
