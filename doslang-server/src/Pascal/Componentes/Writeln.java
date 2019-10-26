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
import Pascal.Analisis.TipoDato.Tipo;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Writeln implements Instruccion {
    
    Expresion expresion;
    Boolean salto;

    /**
     * CONSTRUCTOR DE LA CLASE 
     * @param expresion
     * @param salto 
     */
    public Writeln(Expresion expresion, Boolean salto) {
        this.expresion = expresion;
        this.salto = salto;
    }
    
    
    
    
    /**
     * METODO DE LA CLASE PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        String codigo = "";
        Object resultado = (expresion == null) ? null : expresion.ejecutar(ambito);
        if(resultado == null) return -1;
        else if(resultado instanceof MessageError) return -1;
        
        Nodo nodo = (Nodo) resultado;
        codigo = nodo.getCodigo3D();
        if(nodo.getTipo() == Tipo.INT) codigo += "\nPrint(%e," + nodo.getResultado() + ")";
        else if(nodo.getTipo() == Tipo.DOUBLE) codigo += "\nPrint(%d," + nodo.getResultado() + ")";
        else if(nodo.getTipo() == Tipo.CHAR) codigo += "\nPrint(%c," + nodo.getResultado() + ")";
        else if(nodo.getTipo() == Tipo.BOOLEAN){
           if(nodo.getEtiquetaV() != null){
               codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaV());
               String etiquetaSalto = Generador.generarEtiqueta();
               codigo += "\n Print(%c," + "84" + ")"; // -------------------- T
               codigo += "\n Print(%c," + "114" + ")"; // -------------------- r
               codigo += "\n Print(%c," + "117" + ")"; // -------------------- u
               codigo += "\n Print(%c," + "101" + ")"; // -------------------- e
               codigo += "\n" + Generador.saltoIncondicional(etiquetaSalto);
               codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaF());
               codigo += "\n Print(%c," + "70" + ")"; // -------------------- F
               codigo += "\n Print(%c," + "97" + ")"; // -------------------- a
               codigo += "\n Print(%c," + "108" + ")"; // -------------------- l
               codigo += "\n Print(%c," + "115" + ")"; // -------------------- s
               codigo += "\n Print(%c," + "101" + ")"; // -------------------- e
               codigo += "\n" + Generador.guardarEtiqueta(etiquetaSalto);
             
               
           }
           else{
               String falsa = Generador.generarEtiqueta();
               String etiquetaSalto = Generador.generarEtiqueta();
               codigo += "\n" +  Generador.guardarCondicional(falsa, nodo.getResultado(), "0", "=");
               codigo += "\n Print(%c," + "84" + ")"; // -------------------- T
               codigo += "\n Print(%c," + "114" + ")"; // -------------------- r
               codigo += "\n Print(%c," + "117" + ")"; // -------------------- u
               codigo += "\n Print(%c," + "101" + ")"; // -------------------- e
               codigo += "\n" + Generador.saltoIncondicional(etiquetaSalto);
               codigo += "\n" + Generador.guardarEtiqueta(falsa);
               codigo += "\n Print(%c," + "70" + ")"; // -------------------- F
               codigo += "\n Print(%c," + "97" + ")"; // -------------------- a
               codigo += "\n Print(%c," + "108" + ")"; // -------------------- l
               codigo += "\n Print(%c," + "115" + ")"; // -------------------- s
               codigo += "\n Print(%c," + "101" + ")"; // -------------------- e
               codigo += "\n" + Generador.guardarEtiqueta(etiquetaSalto);
              
           }
           
        }
        else if(nodo.getTipo() == Tipo.STRING || nodo.getTipo() == Tipo.WORD){
            
            String etiquetaRecursiva = Generador.generarEtiqueta();
            //------------------------------------------------------- POSICION ACTUAL EN EL HEAP Y SU VALOR
            String pos = Generador.generarTemporal();
            codigo += "\n" + Generador.generarCuadruplo("+", nodo.getResultado(), "0", pos);
            //------------------------------------------------------- La etiqueta encargada de crear un loop

            codigo += "\n" + Generador.guardarEtiqueta(etiquetaRecursiva);

            String valor = Generador.generarTemporal();
            codigo += "\n" + Generador.guardarAcceso(valor, "Heap", pos);
            //------------------------------------------------------ CONDICIONES DE FIN DE CADENA -----------------------------------------------------------
            String finVerdadera = Generador.generarEtiqueta();
            String numeroVerdadera = Generador.generarEtiqueta();
            String saltoFalsa = Generador.generarEtiqueta();
            
            codigo += "\n" + Generador.guardarCondicional(finVerdadera, valor, "0", "=");
            
            codigo += "\n Print(%c," + valor + ")";     
            codigo += "\n" + Generador.generarCuadruplo("+", pos, "1", pos);
            codigo += "\n" + Generador.saltoIncondicional(etiquetaRecursiva);
            //---------------------------------------------------- SALIDA -----------------------------------------------------------------------------
            codigo += "\n" + Generador.guardarEtiqueta(finVerdadera);
            
        }
        
        if (salto) {
            codigo += "\nPrint(%c,10)";
        }
        Nodo temp = new Nodo();
        temp.setCodigo3D(codigo);
        return temp;
        
    }
    
}
