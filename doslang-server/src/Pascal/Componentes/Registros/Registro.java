/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Registros;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato;
import Pascal.Analisis.TipoDato.Tipo;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Registro implements Instruccion {
    LinkedList<Atributo> atributos;
    int l;
    int c;

    
    public Registro(LinkedList<Atributo> atributos, int l, int c) {
        this.atributos = atributos;
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
        String codigo = "";
        String primero = Generador.generarTemporal();
        codigo += Generador.generarCuadruplo("=", "H", "", primero);
        //-------------------------------------------------------------- RECORREMOS LA LISTA DE ATRIBUTOS GENERANDO SU CODIGO 3D ------------------------------------------
        for(Atributo a : atributos){
            if(a.getTipo().getTipo() == Tipo.ARRAY){
                MessageError mensaje = new MessageError("Semantico",l,c,"Un array no puede estar dentro de un Registro");
                ambito.addSalida(mensaje);
                return mensaje;
            }
            codigo += "\n" + Generador.generarCuadruplo("=", "H", "0", "Heap");
            codigo += "   " + Generador.generarComentarioSimple("Se reserva el espacio para el atributo: " + a.getId());
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        }
        Nodo nodo = new Nodo();
        nodo.setTipo(Tipo.REGISTRO);
        nodo.setCodigo3D(codigo);
        nodo.setResultado(primero);
        nodo.setValor(atributos);
        return nodo;
        
        
    }

    /**
     * DEVUELVE LA LISTA DE ATRIBUTOS
     * @return 
     */
    public LinkedList<Atributo> getAtributos() {
        return atributos;
    }
    
    
    
    
    
}
