/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import Pascal.Analisis.TipoDato.Tipo;
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
    String codigo = "";
    
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
        this.tam = 1;
        this.salida = new LinkedList<>();
        this.codigo = "";
    }

    /**
     * OBTENER ID DE AMBITO ACTUAL
     * @param id 
     */
    public String getId(String id) {
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
        Ambito aux = this;
        while(aux != null){
            if(aux.listaVariables.existeVariable(id)) return aux.listaVariables.getVariable(id);
            aux = aux.old;
        }
        return null;
    }
    
    
    
    /**
     * METODO QUE AGREGA SIMBOLOS AL AMBITO
     * @param id
     * @param tipo
     * @return 
     */
    public Boolean addSimbolo(String id, Tipo tipo){
        if(this.listaVariables.agregarVariable(id, tipo)){
            tam++;
            return true;
        }
        return false;
    }
    
    /**
     * AGREGAR LAS VARIABLES DE UNA TABLA PADRE ESTO OCURRIRRA CUANDO SE CREEN SUBAMBITOS
     * @param tabla 
     */
    public void addAllVariables(TablaSimbolos tabla){
        for(Map.Entry<String,Simbolo> entry: tabla.entrySet()){
            Simbolo s = entry.getValue();
            if(this.getSimbolo(s.getId()) != null) this.addSimbolo(s.getId(), s.tipo);
        }
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
    
    
    
    
    
    
    
    
}
