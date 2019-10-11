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
public class Declaracion extends TipoDato implements Instruccion {
    String id;
    Expresion valor;
    int l;
    int c;
    Boolean constante;
    LinkedList<String> lista;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param valor
     * @param l
     * @param c
     * @param constante 
     */
    public Declaracion(String id, Expresion valor, int l, int c, Boolean constante, Tipo tipo) {
        this.id = id;
        this.valor = valor;
        this.l = l;
        this.c = c;
        this.constante = constante;
        this.tipo = tipo;
    }

    public Declaracion(Expresion valor, int l, int c, Boolean constante, Tipo tipo,LinkedList<String> lista) {
        this.valor = valor;
        this.l = l;
        this.c = c;
        this.constante = constante;
        this.tipo = tipo;
        this.lista = lista;
    }
    
    

    
    /**
     * CONSTRUCTOR DE LA CLASE PADRE
     * @param ambito
     * @return 
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        Object dato = (valor == null) ? null : valor.ejecutar(ambito);
        if(lista == null){
            lista = new LinkedList<String>();
            lista.addLast(id);
        }
        guardarListaVariables(ambito,dato);
        
        return -1;
    }

    
    
    private Object guardarListaVariables(Ambito ambito, Object dato) {
        Nodo nodo = new Nodo();
        if (!(dato instanceof MessageError)) {
            nodo = (Nodo) dato;
        } else {
            return -1;
        }
        ambito.addCodigo(nodo.getCodigo3D());
        if (nodo.getTipo() == Tipo.BOOLEAN) {

            nodo.setResultado(Generador.generarTemporal());
            //-------------------------------------- ETIQUETAS VERDADERAS --------------------------------
            ambito.addCodigo(Generador.getAllEtiquetas(nodo.getEtiquetaV()));
            ambito.addCodigo(Generador.generarCuadruplo("=", "1", "", nodo.getResultado()));
            String etiquetaTemp = Generador.generarEtiqueta();
            ambito.addCodigo(Generador.saltoIncondicional(etiquetaTemp));
            //------------------------------------- ETIQUETA FALSA ----------------------------------------------
            ambito.addCodigo(Generador.getAllEtiquetas(nodo.getEtiquetaF()));
            ambito.addCodigo(Generador.generarCuadruplo("=", "0", "", nodo.getResultado()));
            //--------------------------------------- ETIQUETA DE SALIDA ---------------------------------------
            ambito.addCodigo(Generador.guardarEtiqueta(etiquetaTemp));
        }
        for (String s : lista) {
            //----------------------------------- NO SE INICIALIZO LA VARIABLE ------------------------------------------------------
            if (dato == null) {
                Boolean resultado = ambito.addSimbolo(new Simbolo(s, constante, false, this.getTipo(), Generador.generarStack(), ambito.getRelativa()));
                if (!resultado) {
                    MessageError er = new MessageError("Semantico", l, c, "La variable : " + s + "ya existe");
                    ambito.addSalida(er);
                }
            } //-------------------------------- SI SE INICIALIZO ---------------------------------------------------------------------
            else {

                if (casteoImplicito(this.getTipo(),nodo.getTipo() )) {
                    Boolean resultado = ambito.addSimbolo(new Simbolo(s, constante, true, this.getTipo(), Generador.generarStack(), ambito.getRelativa()));

                    if (resultado) {

                        ambito.addCodigo(Generador.generarComentarioSimple("------------- Guardando la variable : " + s));
                        
                        String temporalP = Generador.generarTemporal();
                        Simbolo sim = ambito.getSimbolo(s);
                        ambito.addCodigo(Generador.generarCuadruplo("+", "P", String.valueOf(sim.getPosRelativa()), temporalP));
                        ambito.addCodigo(Generador.generarCuadruplo("=", temporalP, nodo.getResultado(), "Stack"));

                        ambito.addCodigo(Generador.generarComentarioSimple("-------------- FIN guardar variable : " + s));
                    } else {
                        MessageError er = new MessageError("Semantico", l, c, "La variable : " + s + "ya existe");
                        ambito.addSalida(er);
                    }

                } else {
                    MessageError er = new MessageError("Semantico", l, c, "No coinciden los datos: " + this.getTipo() + " con: " + nodo.getTipo());
                    ambito.addSalida(er);
                }

            }
        }
        return -1;
    }
    
    
    /**
     * METODO QUE SE ENCARGARA DE MANEJAR LOS CASTEOS IMPLICITOS
     * @param tipo1
     * @param tipo2
     * @return 
     */
    public Boolean casteoImplicito(Tipo tipo1, Tipo tipo2){
        if(tipo1 == Tipo.STRING && tipo2 == Tipo.WORD) return true;
        return tipo1 == tipo2;
    }
    

    
}
