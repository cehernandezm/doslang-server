/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.UserTypes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.TipoDato;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Type;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class TypeDeclaration implements Instruccion {
    
    LinkedList<String> listaEquivalencias;
    int l;
    int c;
    Type tipo;
    /**
     * CONSTRUCTOR DE LA CLASE
     * @param listaEquivalencias
     * @param l
     * @param c
     * @param tipo 
     */
    public TypeDeclaration(LinkedList<String> listaEquivalencias, int l, int c, Type tipo) {
        this.listaEquivalencias = listaEquivalencias;
        this.l = l;
        this.c = c;
        this.tipo = tipo;
    }
    
    
    

    @Override
    public Object ejecutar(Ambito ambito) {
        return guardarEquivalencias(ambito);
    }
    
    /**
     * METODO QUE SE ENCARGARA DE RECORRER EL LISTADO DE EQUIVALENCIAS Y ALMACENARLAS
     * @param ambito
     * @return 
     */
    private Object guardarEquivalencias(Ambito ambito){
        for(String e : listaEquivalencias){
            e = e.toLowerCase();
            if(tipo.getTipo() == Tipo.ID){
                MessageError mensajeError = new MessageError("Semantico",l,c," un Type no puede hacer referencia a otro Type");
                ambito.addSalida(mensajeError);
                return mensajeError;
            }
            Boolean resultado = ambito.agregarEquivalencia(new Equivalencia(e,new Type("",tipo.getTipo())));
            if(!resultado){
                MessageError mensajeError = new MessageError("Semantico",l,c,"Ya existe el identificador: " + e);
                ambito.addSalida(mensajeError);
                return mensajeError;
            }
        }
        return -1;
    }
    
}
