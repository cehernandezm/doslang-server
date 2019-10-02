/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Componentes;

import Analisis.Ambito;
import Analisis.Generador;
import Analisis.Instruccion;
import Analisis.MessageError;
import Analisis.Nodo;
import Analisis.TipoDato;

/**
 *
 * @author Carlos
 */
public class Expresion extends TipoDato implements Instruccion{

    Expresion izq;
    Expresion der;
    Tipo tipo;
    Operacion operacion;
    int l;
    int c;
    Object valor;

    /**
     * CONSTRUCTOR PARA OPERACIONES TERNARIAS NUM  + NUM
     * @param izq
     * @param der
     * @param operacion
     * @param l
     * @param c 
     */
    public Expresion(Expresion izq, Expresion der, Operacion operacion, int l, int c) {
        this.izq = izq;
        this.der = der;
        this.operacion = operacion;
        this.l = l;
        this.c = c;
    }

    /**
     * CONSTRUCTOR PARA VALORES PRIMITIVOS
     * @param izq
     * @param tipo
     * @param l
     * @param c 
     */
    public Expresion(Object valor, Tipo tipo, int l, int c) {
        this.valor = valor;
        this.tipo = tipo;
        this.l = l;
        this.c = c;
        this.operacion = null;
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * CONSTRUCTOR DEL METODO PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        MessageError error = new MessageError("", l, c, "");
        Object op1 = (izq == null) ? null : izq.ejecutar(ambito);
        Object op2 = (der == null) ? null : der.ejecutar(ambito);
        String codigo = "";
        if(operacion != null){
            switch(operacion){
                case SUMA:
                    codigo = "";
                   if(op1 instanceof MessageError || op2 instanceof MessageError){
                       if(op1 instanceof MessageError) error = (MessageError) op1;
                       else if(op2 instanceof MessageError) error = (MessageError) op2;
                   }
                   else{
                       Nodo nodoIzq = (Nodo)op1;
                       Nodo nodoDer = (Nodo)op2;
                       if(nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.INT){
                           String temp = Generador.generarTemporal();
                           codigo = "+," + nodoIzq.getResultado() + "," + nodoDer.getResultado() + "," + temp;
                           ambito.addCodigo(codigo);
                           Nodo resultado = new Nodo();
                           resultado.setTipo(Tipo.INT);
                           resultado.setResultado(temp);
                           return resultado;
                       } 
                   }
            }
        }
        //------------------------------------------ VALORES PRIMARIOS -----------------------------------------------------------------------------
        else{
            switch(tipo){
                case INT:
                    Nodo nodo = new Nodo();
                    nodo.setResultado(valor.toString());
                    nodo.setTipo(tipo);
                    return nodo;
            }
        }
        return error;
    }
    
}
