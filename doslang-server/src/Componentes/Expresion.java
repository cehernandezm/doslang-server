/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Componentes;

import Analisis.Instruccion;
import Analisis.TablaSimbolos;
import Analisis.TipoDato;

/**
 *
 * @author Carlos
 */
public class Expresion extends TipoDato implements Instruccion {

    Expresion op1;
    Expresion op2;
    String operacion;
    Tipo tipo;
    
    
    
    
    /**
     * CONSTRUCOR PARA OPERACIONES TERNARIAS EJEMPLO 3 + 2
     * @param op1
     * @param op2
     * @param operacion 
     */
    public Expresion(Expresion op1, Expresion op2, String operacion) {
        this.op1 = op1;
        this.op2 = op2;
        this.operacion = operacion;
    }

    
    /**
     * CONSTRUCOR PARA VALORES PRIMARIOS EJEMPLO 3,"HOLA",'C',ID
     * @param op1
     * @param tipo 
     */
    
    public Expresion(Expresion op1, Tipo tipo) {
        this.op1 = op1;
        this.tipo = tipo;
    }
    
    
    
    
    
    
    
    /**
     * METODO DE LA CLASE PADRE
     * @param ts
     * @return 
     */
    @Override
    public Object ejecutar(TablaSimbolos ts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
