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
import Pascal.Analisis.TipoDato.Tipo;

/**
 *
 * @author Carlos
 */
public class Read implements Instruccion {
    String id;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param l
     * @param c 
     */
    public Read(String id, int l, int c) {
        this.id = id.toLowerCase();
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
        String codigo = "";
        Simbolo simbolo = ambito.getSimbolo(id);
        //--------------------------------------------- Si no existe la variable ----------------------------------------------------------------------
        if(simbolo == null){
            MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " no existe");
            ambito.addSalida(mensaje);
            return mensaje;
        }
        codigo = Generador.generarComentarioSimple("---------------- FUNCION READ ---------------------------" );
        String pos = Generador.generarTemporal();
        
        codigo += "\n" + Generador.generarComentarioSimple("---------------- Obtenemos la posicion de la variable:  " + simbolo.getId()  );
        codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(simbolo.getPosRelativa()),pos);
        
        if(simbolo.getTipo() == Tipo.STRING || simbolo.getTipo() == Tipo.WORD){
            simbolo.setInicializada(true);
            String posHeap = Generador.generarTemporal();
            codigo += "\n" + Generador.generarCuadruplo("=","H", "", posHeap);
            codigo += "\ncall,," + pos + "," + posHeap;
        }
        else if(simbolo.getTipo() == Tipo.INT || simbolo.getTipo() == Tipo.DOUBLE || simbolo.getTipo() == Tipo.BOOLEAN || simbolo.getTipo() == Tipo.CHAR) {
            simbolo.setInicializada(true);
            codigo += "\ncall,," + pos + ",";
        }    
        else{
            MessageError mensaje = new MessageError("Semantico",l,c,"La funcion READ solo acepta valores Primitivos no se reconoce: " + simbolo.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        codigo += "\n" + Generador.generarComentarioSimple("---------------- FIN FUNCION READ ---------------------------" );
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
    
}
