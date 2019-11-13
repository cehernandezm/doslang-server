/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Enumerador extends TipoDato implements Instruccion  {
    LinkedList<String> listado;
    int l;
    int c;
    Boolean constante;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param listado
     * @param l
     * @param c
     * @param constante
     */
    public Enumerador(LinkedList<String> listado, int l, int c, Boolean constante) {
        this.listado = listado;
        this.l = l;
        this.c = c;
        this.constante = constante;
    }
    
    
    
    
    /**
     * METODO PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        
        String codigo = "";
        LinkedList<String> temporales = new LinkedList<>();
        String primer = "";
        int index = 0;
        //---------------------------------------------- GUARDAR LAS CADENAS ------------------------------------------------
        for(String s : listado){
            s = s.toLowerCase();
            Nodo nodo = Expresion.guardarCadena3D(s, Tipo.WORD, ambito);
            codigo += "\n" + nodo.getCodigo3D();
            temporales.addLast(nodo.getResultado());
        }
        //----------------------------------------------GUARDAR LA POSICION DE LOS TEMPORALES
        codigo += "\n" + Generador.generarComentarioSimple("------------------------- Guardando Los valores del Enum");

        for(String s : temporales){
            String temp = Generador.generarTemporal();
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "0", temp);
            codigo += "\n" + Generador.generarCuadruplo("=", temp, s, "Heap");
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
            if(index == 0) primer = temp;
            index++;
        }
        codigo += "\n" + Generador.generarComentarioSimple("------------------------- FIN Guardando Los valores del Enum");
        
        Nodo temp = new Nodo();
        temp.setCodigo3D(codigo);
        temp.setValor(listado);
        temp.setResultado(primer);
        temp.setTipo(Tipo.ENUM);
        return temp;
    }
    
}
