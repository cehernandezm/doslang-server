/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Registros;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.UserTypes.Equivalencia;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Registro implements Instruccion {
    LinkedList<Atributo> atributos;
    int l;
    int c;

    
    public Registro(LinkedList<Atributo> atributos, int l, int c) {
        this.atributos = atributos;
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
        String codigo = "";
        //-------------------------------------------------------------- RECORREMOS LA LISTA DE ATRIBUTOS GENERANDO SU CODIGO 3D ------------------------------------------
        for(Atributo a : atributos){
            if(a.getTipo().getTipo() == Tipo.ID){
                Equivalencia equi = ambito.getEquivalencia(a.getTipo().getId().toString());
                if(equi != null) {
                    a.getTipo().setTipo(equi.getTipo().getTipo());
                    a.getTipo().setValor(equi.getTipo().getValor());
                }
                else a.getTipo().setTipo(Tipo.REGISTRO);
            }
            else if(a.getTipo().getTipo() == Tipo.ARRAY){
                MessageError mensaje = new MessageError("Semantico",l,c,"No pueden haber arreglos dentro de registros");
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
        }
        Nodo nodo = new Nodo();
        nodo.setTipo(Tipo.REGISTRO);
        nodo.setCodigo3D(codigo);
        nodo.setResultado("");
        nodo.setValor(atributos);
        return nodo;
        
        
    }

    /**
     * DEVUELVE LA LISTA DE ATRIBUTOS
     * @return 
     */
    public LinkedList<Atributo> getAtributos() {
        return atributos;
    }
    
    
    
    
    
}
