/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.Nodo;

/**
 *
 * @author Carlos
 */
public class Break implements Instruccion {
    int l;
    int c;
    /**
     * CONSTRUCTOR DE LA CLASE
     */
    public Break(int l, int c) {    
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
        String etiquetaBreak = Generador.generarEtiqueta();
        String codigo = Generador.saltoIncondicional(etiquetaBreak);
        codigo += "  " + Generador.generarComentarioSimple(" Salto de un break");
        nodo.setCodigo3D(codigo);
        
        ambito.addListadoBreak(etiquetaBreak);
        
        return nodo;
    }

    public int getL() {
        return l;
    }

    public int getC() {
        return c;
    }
    
    
    
    
}
