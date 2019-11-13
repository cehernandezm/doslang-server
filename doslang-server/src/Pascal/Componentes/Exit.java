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
public class Exit implements Instruccion{
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param l
     * @param c 
     */
    public Exit(int l, int c) {
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
        String salto = Generador.generarEtiqueta();
        String codigo = Generador.saltoIncondicional(salto);
        ambito.addListadoExit(salto);
        codigo += "  " + Generador.generarComentarioSimple("   ETIQUETA DE SALIDA");
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
}
