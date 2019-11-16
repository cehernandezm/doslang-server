/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Funciones.Funcion;
import Pascal.Componentes.Funciones.Parametro;
import Pascal.Componentes.UserTypes.Equivalencia;
import java.util.HashMap;
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
    LinkedList<Funcion> listaFunciones;
    HashMap<String,String> listaCodigoFunciones;
    String codigo = "";
    int posInicio;
    Simbolo tempSimbolo;
    HashMap<String,String> listadoBreak;
    HashMap<String,String> listadoContinue;
    HashMap<String,String> listadoExit;
    String posPadre = "";
    
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
        this.listaCodigoFunciones = new HashMap<>();
        this.listadoBreak = new HashMap<>();
        this.listadoContinue = new HashMap<>();
        this.listadoExit = new HashMap<>();
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
    
    
    
    public Boolean addFuncion(Funcion f){
        if(buscarIdentificador(f.getId().toLowerCase(), false)) return false;
        if(buscarFuncion(f.getIdentificador(),f.getListaParametros()) != null) return false;
        listaFunciones.addLast(f);
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
            if(!tempSimbolo.getParametro()) return true;
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
    private Funcion buscarFuncion(String nombre){
        nombre = nombre.toLowerCase();
        for(Funcion f : listaFunciones){
            if(f.getId().equalsIgnoreCase(nombre)){
                if(nombre.equalsIgnoreCase(id)) return null;
                return f;
            }
        }
        return null;
    }
    
    /**
     * DEVUELVE UNA FUNCION
     * @param nombre
     * @return 
     */
    private Funcion buscarFuncion(String nombre,LinkedList<Parametro> parametros){
        nombre = nombre.toLowerCase();
        Boolean flag = false;
        for(Funcion f : listaFunciones){
            flag = false;
            if(f.getIdentificador().equalsIgnoreCase(nombre)){
                if(parametros.size() == f.getListaParametros().size()){
                    flag = true;
                    for(int i = 0; i < parametros.size(); i++){
                        Parametro para1 = parametros.get(i);
                        Parametro para2 = f.getListaParametros().get(i);
                        if(!(Declaracion.casteoImplicito(para2.getTipo().getTipo(), para1.getTipo().getTipo()))) flag = false;
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
    public Funcion buscarFuncionLlamada(String nombre,LinkedList<Nodo> parametros){
        nombre = nombre.toLowerCase();
        Boolean flag = false;
        for(int k = listaFunciones.size() - 1 ; k >= 0 ; k--){
           Funcion f = listaFunciones.get(k);
            
           flag = false;

            if(f.getId().equalsIgnoreCase(nombre)){
                
                if(parametros.size() == f.getTamanioTotalParametro()){
                    flag = true;
                    int contador = 0;
                    for (int i = 0; i < f.getListaParametros().size(); i++) {
                        Parametro para2 = f.getListaParametros().get(i);
                        for (int j = 0; j < f.getListaParametros().get(i).getLista().size(); j++) {
                            Nodo para1 = parametros.get(contador);

                            if (para2.getTipo().getTipo() == Tipo.ID) {
                                Equivalencia equi = getEquivalencia((String.valueOf(para2.getTipo().getId())).toLowerCase());

                                if (equi != null) {
                                    if (!(Declaracion.casteoImplicito(equi.getTipo().getTipo(), para1.getTipo()))) {
                                        flag = false;
                                    }
                                } else {
                                    flag = false;
                                }
                            } else {
                                if (!(Declaracion.casteoImplicito(para2.getTipo().getTipo(), para1.getTipo()))) {
                                    flag = false;
                                }
                            }

                            contador++;
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
    public void setearListaFunciones(LinkedList<Funcion> funciones){
        this.listaFunciones.addAll(funciones);
    }

    /**
     * SETEA UNA NUEVA LISTA DE FUNCIONES
     * @param funciones 
     */
    public void setearListaFunciones(Funcion funcion){
        this.listaFunciones.addLast(funcion);
    }
    
    /** 
     * OBTENEMOS LA LISTA DE FUNCIONES
     * @return 
     */
    public LinkedList<Funcion> getListaFunciones() {
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
    
    /**
     * METODO PARA VERIFICAR SI EXISTE YA EL CODIGO DE LA FUNCION
     * @param id
     * @return 
     */
    public Boolean existeCodigoFuncion(String id){
        System.out.println(listaCodigoFunciones.containsKey(id) + "_" + id);
        return listaCodigoFunciones.containsKey(id);
    }
    
    /**
     * AGREGAMOS EL CODIGO DE UNA FUNCION A NUESTRO HASHMAP
     * @param id
     * @param codigo 
     */
    public void addCodigoFuncion(String id, String codigo){
        listaCodigoFunciones.put(id, codigo);
    }
    
  
    /**
     * OBTENEMOS EL CODIGO GUARDADO DE TODAS LAS FUNCIONES
     * @return 
     */
    public String getCodigoAllFunciones(){
        String codigo = "";
        for(Map.Entry<String,String> entry: listaCodigoFunciones.entrySet()){
            codigo += "\n" + entry.getValue();
        }
        return codigo;
    }

    /**
     * RETORNAMOS NUESTRO LISTADO DE FUNCIONES
     * @return 
     */
    public HashMap<String, String> getListaCodigoFunciones() {
        return listaCodigoFunciones;
    }

    /**
     * SETEAMOS EL TAMANIO DE UN AMBITO
     * @param tam 
     */
    public void setTam(int tam) {
        this.tam = tam;
    }

    /**
     * OBTENEMOS EL LISTADO DE BREAKS
     * @return 
     */
    public HashMap<String,String> getListadoBreak() {
        return listadoBreak;
    }
    
    /**
     * SETEAMOS UN NUEVO LISTADO DE BREAK
     * @param listadoBreak 
     */
    public void addListadoBreak(HashMap<String,String> listadoBreak) {
        this.listadoBreak.putAll(listadoBreak);
    }
    
    /**
     * SETEAMOS UN NUEVO LISTADO DE BREAK
     * @param listadoBreak 
     */
    public void addListadoBreak(String listadoBreak) {
        this.listadoBreak.put(listadoBreak, listadoBreak);
    }

    /**
     * METODO PARA REINICIAR BREAK
     */
    public void reiniciarBreak(){
        listadoBreak.clear();
    }
    
    
    
    
    /**
     * OBTENEMOS EL LISTADO DE CONTINUES
     * @return 
     */
    public HashMap<String, String> getListadoContinue() {
        return listadoContinue;
    }
    
    /**
     * GUARDAMOS LAS LISTAS DE CONTINUE
     * @param listado 
     */
    public void addListadoContinue(HashMap<String,String> listado){
        listadoContinue.putAll(listado);
    }
    
    /**
     * GUARDAMOS UN NUEVO CONTINUE
     * @param listado 
     */
    public void addListadoContinue(String listado){
        listadoContinue.put(listado, listado);
    }
    
    /**
     * REINICIA EL LISTADO CONTINUE
     */
    public void reiniciarContinue(){
        listadoContinue.clear();
    }

    /**
     * OBTENGO LA LISTA DE SALIDA
     * @return 
     */
    public HashMap<String, String> getListadoExit() {
        return listadoExit;
    }
    
    /**
     * GUARDAMOS UN LISTADO DE SALIDAS
     * @param listado 
     */
    public void addListadoExit(HashMap<String,String> listado){
        this.listadoExit.putAll(listado);
    }
    
    /**
     * GUARDAMOS UNA SOLA LISTA
     * @param listado 
     */
    public void addListadoExit(String listado){
        this.listadoExit.put(listado, listado);
    }
    
    /**
     * METODO ENCARGADO DE REINICIAR LAS ETIQUETAS DE EXIT
     */
    public void reiniciarExit(){
        this.listadoExit.clear();
    }

    /**
     * OBTENEMOS LA POSICION DEL REGISTRO PADRE
     * @return 
     */
    public String getPosPadre() {
        return posPadre;
    }

    /**
     * ALMACENAMOS LA POSICION DEL REGISTRO PADRE
     * @param posPadre 
     */
    public void setPosPadre(String posPadre) {
        this.posPadre = posPadre;
    }

    
}
