/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Arreglos;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Arreglo extends TipoDato implements Instruccion {
    LinkedList<Dimension> dimensiones;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param dimensiones
     * @param tipo 
     */
    public Arreglo(LinkedList<Dimension> dimensiones, int l, int c, Tipo tipo) {
        this.dimensiones = dimensiones;
        this.l = l;
        this.c = c;
        this.tipo = tipo;
    }

    /**
     * CONSTRUCTOR DE LA CLASE PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        String codigo = "";
        LinkedList<String> temporales = new LinkedList<String>();
        String temporalPosicion = Generador.generarTemporal();
        String tamanio = Generador.generarTemporal();
        String etiquetaRetorno = Generador.generarEtiqueta();
        String contador = Generador.generarTemporal();

        codigo += "\n" + Generador.generarComentarioSimple("-------------------------------------- RESERVAR ESPACIO PARA EL ARREGLO -----------------------------");
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "0", temporalPosicion);
        codigo += "\n" + Generador.generarCuadruplo("=", "0", "", contador);
        
        codigo +=  "\n" + Generador.generarComentarioSimple("-------------------------------------- Guardando las dimensiones -----------------------------");
        
        //----------------------------------------------------- EJECUTAMOS Y OBTENEMOS EL CODIGO GENERADO POR CADA DIMENSION ---------------------------------
        for (Dimension d : dimensiones) {
            Object result = d.ejecutar(ambito);
            if (result instanceof MessageError) return  new MessageError("Semantico", l, c, "Error en las dimensiones");
            Nodo nodoTemp = (Nodo)result;
            codigo += "\n" + nodoTemp.getCodigo3D();
            temporales.addLast(nodoTemp.getResultado());

        }
        codigo += "\n" + Generador.generarComentarioSimple("-------------------------------------- Fin guardando las dimensiones -----------------------------");
        
        
        
        codigo +=  "\n" + Generador.generarComentarioSimple("-------------------------------------- OBTENIENDO TAMANIO -----------------------------");
        codigo += "\n" + Generador.generarCuadruplo("+", temporales.get(0), "0", tamanio);
        for(int i = 1; i < temporales.size(); i++){
            codigo += "\n" + Generador.generarCuadruplo("*", tamanio, temporales.get(i), tamanio);
        }
        codigo +=  "\n" + Generador.generarComentarioSimple("-------------------------------------- FIN OBTENIENDO TAMANIO -----------------------------");
       
        
        
        
        
        codigo +=   Generador.generarComentarioSimple("//-------------------------------------- VALORES 0 PARA EL ARREGLO -----------------------------");
        codigo += "\n" + Generador.guardarEtiqueta(etiquetaRetorno);
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "0", "Heap");
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarCuadruplo("+", contador, "1", contador);
        codigo += "\n" + Generador.guardarCondicional(etiquetaRetorno, contador, tamanio, "<");
        
        codigo += "\n" + Generador.generarComentarioSimple("-------------------------------------- FIN RESERVAR ESPACIO PARA EL ARREGLO -----------------------------");
        
        Nodo nodo = new Nodo();
        nodo.setTipo(this.getTipo());
        nodo.setResultado(temporalPosicion);
        nodo.setCodigo3D(codigo);
        nodo.setCantidadDimensiones(dimensiones.size());
        return nodo;
    }

    /**
     * DEVUELVE LAS DIMESIONES DEL ARREGLO
     * @return 
     */
    public LinkedList<Dimension> getDimensiones() {
        return dimensiones;
    }
    
    
    
    
    
    
}
