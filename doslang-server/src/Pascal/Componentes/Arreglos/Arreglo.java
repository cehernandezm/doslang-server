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
        
        Object res = dimensiones.get(0).ejecutar(ambito);
        if (res instanceof MessageError) {
            return new MessageError("", l, c, "");
        }
        
        String codigo = "";
        String temporalPosicion = Generador.generarTemporal();
        String contador = Generador.generarTemporal();
        String cantidadRepeticiones = Generador.generarTemporal();
        String tamHijos = Generador.generarTemporal();
        String posDinamica = Generador.generarTemporal();
        
        
        codigo += "\n" + Generador.generarComentarioSimple("-------------------------------------- RESERVAR ESPACIO PARA EL ARREGLO -----------------------------");
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "0", temporalPosicion);
        codigo += "\n" + Generador.generarCuadruplo("=", "0", "", contador);
        
        codigo +=  "\n" + Generador.generarComentarioSimple("-------------------------------------- Guardando las dimensiones -----------------------------");
        
        //----------------------------------------------------- EJECUTAMOS Y OBTENEMOS EL CODIGO GENERADO POR CADA DIMENSION ---------------------------------
        
        codigo += "\n" + Generador.generarComentarioSimple("-------------- DIMENSION 1 -------------------------------------");
        String falsa = Generador.generarEtiqueta();
        String salto = Generador.generarEtiqueta();
        Nodo di = (Nodo) res;
        codigo += "\n" + di.getCodigo3D();

        codigo += "\n" + Generador.guardarEtiqueta(salto);
        codigo += "\n" + Generador.guardarCondicional(falsa, contador, di.getResultado(), ">=");
        codigo += "\n" + Generador.generarCuadruplo("=", "H", "-1", "Heap");
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarCuadruplo("+", contador, "1", contador);
        codigo += "\n" + Generador.saltoIncondicional(salto);
        codigo += "\n" + Generador.guardarEtiqueta(falsa);
        codigo += "\n" + Generador.generarComentarioSimple("-------------- FIN DIMENSION 1 -------------------------------------");
        

        codigo += "\n" + Generador.generarCuadruplo("=", "1", "", cantidadRepeticiones);
        codigo += "\n" + Generador.generarCuadruplo("=", di.getResultado(), "", tamHijos);
        codigo += "\n" + Generador.generarCuadruplo("+", temporalPosicion, "3", posDinamica);
        for (int i = 1; i < dimensiones.size(); i++) {
            
            
            String fori = Generador.generarTemporal();
            String falsai = Generador.generarEtiqueta();
            String saltoi = Generador.generarEtiqueta();
            
            String forh = Generador.generarTemporal();
            String falsah = Generador.generarEtiqueta();
            String saltoh = Generador.generarEtiqueta();
            
            Object result = dimensiones.get(i).ejecutar(ambito);
            Nodo temp = (Nodo)result;
            
            String forb = Generador.generarTemporal();
            String falsab = Generador.generarEtiqueta();
            String saltob = Generador.generarEtiqueta();
            String posActual = Generador.generarTemporal();
            
            
            //------------------------------------  ------------------------ FOR DE LA CANTIDAD DE REPETICIONES A HACER -----------------------------------------------------
            codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- CUANTOS HIJOS TIENE EL ARREGLO ---------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "0", "", fori);
            codigo += "\n" + Generador.guardarEtiqueta(saltoi);
            codigo += "\n" + Generador.guardarCondicional(falsai, fori,cantidadRepeticiones, ">=");
            
            //------------------------------------  ------------------------ FOR DE LAS POSICIONES  A RECORRER -----------------------------------------------------
            codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- RELACIONAR LOS PADRES CON LOS HIJOS ---------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "0", "", forh);
            codigo += "\n" + Generador.guardarEtiqueta(saltoh);
            codigo += "\n" + Generador.guardarCondicional(falsah, forh,tamHijos, ">=");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "", posActual);
            //------------------------------------  ------------------------ CODIGO DE LA DIMENSION I -----------------------------------------------------
            codigo += "\n" + temp.getCodigo3D();
            //------------------------------------  ------------------------ CODIGO DE LA DIMENSION I -----------------------------------------------------
            
            //------------------------------------  ------------------------ FOR PARA INICIALIZAR LOS VALORES -----------------------------------------------------
            codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- DIMENSION : " + (i + 1) +  "---------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "0", "", forb);
            
            codigo += "\n" + Generador.guardarEtiqueta(saltob);
            codigo += "\n" + Generador.guardarCondicional(falsab, forb,temp.getResultado(), ">=");
            
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "-1", "Heap");
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
            codigo += "\n " + Generador.generarCuadruplo("+", forb, "1", forb);
            codigo += "\n" + Generador.saltoIncondicional(saltob);
            codigo += "\n" + Generador.guardarEtiqueta(falsab);
            codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- FIN DIMENSION" + (i + 1) + " ---------------------");
            //------------------------------------  ------------------------ FIN FOR PARA INICIALIZAR LOS VALORES-----------------------------------------------------
            
            
            codigo += "\n" + Generador.generarCuadruplo("=", posDinamica, posActual, "HEAP");
            codigo += "\n " + Generador.generarCuadruplo("+", posDinamica, "1", posDinamica);
            codigo += "\n " + Generador.generarCuadruplo("+", forh, "1", forh);
            codigo += "\n" + Generador.saltoIncondicional(saltoh);
            codigo += "\n" + Generador.guardarEtiqueta(falsah);
            codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- FIN RELACIONAR LOS PADRES CON LOS HIJOS ---------------------");
            //------------------------------------  ------------------------ FIN FOR DE LAS POSICIONES  A RECORRER-----------------------------------------------------
            
            
            codigo += "\n" + Generador.generarCuadruplo("+", posDinamica, "3", posDinamica);
            codigo += "\n " + Generador.generarCuadruplo("+", fori, "1", fori);
            codigo += "\n" + Generador.saltoIncondicional(saltoi);
            codigo += "\n" + Generador.guardarEtiqueta(falsai);
            
            codigo += "\n" + Generador.generarCuadruplo("=", temp.getResultado(), "", tamHijos);
            codigo += "   " + Generador.generarComentarioSimple("Guardamos el nuevo tamanio del arreglo");
            codigo += "\n" + Generador.generarCuadruplo("*", cantidadRepeticiones, tamHijos, cantidadRepeticiones); 
            codigo += "   " + Generador.generarComentarioSimple("Almacenamos la nueva cantidad a recorrer");
            codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- FIN CUANTOS HIJOS TIENE EL ARREGLO ---------------------");
            //------------------------------------  ------------------------ FIN DE LA CANTIDAD DE REPETICIONES A HACER -----------------------------------------------------
            
           
        }
        

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
