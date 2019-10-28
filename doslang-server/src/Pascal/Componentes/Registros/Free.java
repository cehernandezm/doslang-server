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
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato.Tipo;

/**
 *
 * @author Carlos
 */
public class Free implements Instruccion {
    String id;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param l
     * @param c 
     */
    public Free(String id, int l, int c) {
        this.id = id.toLowerCase();
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
        Nodo nodo = new Nodo();
        Simbolo simbolo = ambito.getSimbolo(id);
        
        //------------------------------------------- NO EXISTE LA VARIABLE ----------------------------------------------------------
        if(simbolo == null){
            MessageError mensaje = new MessageError("Semantico",l,c,"No existe la variable: " + id);
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        if(simbolo.getTipo() != Tipo.REGISTRO){
            MessageError mensaje = new MessageError("Semantico",l,c,"Free solo se puede aplicar a tipos REGISTRO no se reconoce: " + simbolo.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        if(simbolo.getInicializada() == false){
            MessageError mensaje = new MessageError("Semantico",l,c,"No se puede aplicar free en la variable" + id + " porque no esta inicializada");
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        simbolo.setInicializada(false);
        
        nodo.setCodigo3D("");
        
        return nodo;
    }
    
    
    
}
