/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Funciones;

import Pascal.Componentes.Type;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class InfoFuncion {
    String nombre;
    String identificador;
    Type tipo;
    LinkedList<Parametro> listaParametros;
    int posRelativaRetorno;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param nombre
     * @param identificador
     * @param tipo
     * @param listaParametros
     * @param posRelativaRetorno 
     */
    public InfoFuncion(String nombre, String identificador, Type tipo, LinkedList<Parametro> listaParametros, int posRelativaRetorno) {
        this.nombre = nombre;
        this.identificador = identificador;
        this.tipo = tipo;
        this.listaParametros = listaParametros;
        this.posRelativaRetorno = posRelativaRetorno;
    }

    

   

    /**
     * DEVUELVE EL NOMBRE DE LA FUNCION
     * @return 
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * OBTENER EL IDENTIFICADOR
     * @return 
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * OBTENER LA LISTA DE PARAMETRO
     * @return 
     */
    public LinkedList<Parametro> getListaParametros() {
        return listaParametros;
    }

    /**
     * OBTENEMOS LA POSICION RELATIVA DEL RETORNO
     * @return 
     */
    public int getPosRelativaRetorno() {
        return posRelativaRetorno;
    }

    /**
     * OBTENEMOS EL TIPO DE LA FUNCION
     * @return 
     */
    public Type getTipo() {
        return tipo;
    }
    
    
    
    
    
    
    
}
