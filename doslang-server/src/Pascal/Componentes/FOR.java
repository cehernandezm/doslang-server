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
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class FOR implements Instruccion {
    String id;
    Expresion valor;
    Expresion op1;
    LinkedList<Instruccion> cuerpo;
    int l;
    int c;
    Boolean tipo;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param valor
     * @param op1
     * @param op2
     * @param cuerpo
     * @param l
     * @param c
     * @param tipo TRUE (INCREMENTO) | FALSE (DECREMENTO)
     */
    public FOR(String id, Expresion valor, Expresion op1, LinkedList<Instruccion> cuerpo, int l, int c, Boolean tipo) {
        this.id = id.toLowerCase();
        this.valor = valor;
        this.op1 = op1;
        this.cuerpo = cuerpo;
        this.l = l;
        this.c = c;
        this.tipo = tipo;
    }

    @Override
    public Object ejecutar(Ambito ambito) {
        Nodo nodo = new Nodo();
        Simbolo simbolo = ambito.getSimbolo(id);
        Object asigna = valor.ejecutar(ambito);
        Object lim = op1.ejecutar(ambito);
        String recursividad = Generador.generarEtiqueta();
        String codigo = "";
        String pos = Generador.generarTemporal();
        String verdadera = Generador.generarEtiqueta();
        String falsa = Generador.generarEtiqueta();
        String valor = Generador.generarTemporal();
        
        //--------------------------------------------- No existe la variable ------------------------------------------------------------
        if(simbolo == null){
            MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " no existe");
            ambito.addSalida(mensaje);
            return mensaje;
        }
        //------------------------------------------------- LA VARIABLE TIENE QUE SER DE TIPO ENTERO --------------------------------------------------
        if(simbolo.getTipo() != Tipo.INT){
            MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " tiene que ser de tipo INT no se reconoce: " + simbolo.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        //------------------------------------------- HUBO ALGUN ERROR EN LA EXPRESION DE ASIGNACION --------------------------------------
        if(asigna instanceof MessageError) return new MessageError("",l,c,"");
        if(lim instanceof MessageError) return new MessageError("",l,c,"");
        
        Nodo asignacion = (Nodo)asigna;
        Nodo limite = (Nodo)lim;
        //--------------------------------------------- SOLO PUEDE SER DE TIPO ENTERO -----------------------------------------------------
        if(asignacion.getTipo() != Tipo.INT){
            MessageError mensaje = new MessageError("Semantico",l,c,"La asignacion tiene que ser de tipo INT no se reconoce: " + asignacion.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        simbolo.setInicializada(true);
        codigo = Generador.generarComentarioSimple("-------------------------------------------- INICIO FOR ----------------------------------------------------");
        
        codigo += "\n" + asignacion.getCodigo3D();
        codigo += "\n" + limite.getCodigo3D();
        codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(simbolo.getPosRelativa()), pos);
        codigo += "\n " + Generador.generarCuadruplo("=", pos, asignacion.getResultado(), "Stack");
        codigo += "  " + Generador.generarComentarioSimple("Guardamos el valor en la variable : " + simbolo.getId());
        codigo += "\n" + limite.getCodigo3D();
        
        codigo += "\n" + Generador.guardarEtiqueta(recursividad);
        codigo += "  " + Generador.generarComentarioSimple(" Etiqueta encargada de la recursividad");
        codigo += "\n" + Generador.guardarAcceso(valor,"Stack", pos);
        //--------------------------------------------- ES UN INCREMENTO | DECREMENTO ----------------------------------------------------------
        codigo += "\n" + ((tipo == true) ? Generador.guardarCondicional(verdadera,valor,limite.getResultado(), "<=") : Generador.guardarCondicional(verdadera, valor, limite.getResultado(), ">=")) ;
        codigo += "\n" + Generador.saltoIncondicional(falsa);
        
        codigo += "\n" + Generador.guardarEtiqueta(verdadera);
        codigo += " " + Generador.generarComentarioSimple(" Etiqueta Verdadera");
        codigo += "\n" + Generador.generarComentarioSimple("-------------------------- CUERPO FOR --------------------");
        
        Ambito nuevo = new Ambito(ambito.getId(),ambito,ambito.getArchivo());
        nuevo.addAllVariables(ambito.getListaVariables());
        nuevo.setearListaFunciones(ambito.getListaFunciones());
        nuevo.setTam(ambito.getTam());
        nuevo.setEquivalencias(ambito.getEquivalencias());
        for(Instruccion i : cuerpo){
            Object resultado = i.ejecutar(nuevo);
            if(resultado instanceof MessageError) {
                ambito.setSalida(nuevo.getSalida());
                return new MessageError("",l,c,"");
            }
            
            ambito.addListadoBreak(nuevo.getListadoBreak());
            ambito.addListadoContinue(nuevo.getListadoContinue());
            ambito.addListadoExit(nuevo.getListadoExit());
            
            Nodo cod = (Nodo)resultado;
            codigo += "\n" + cod.getCodigo3D();
        }
        codigo += "\n" + Generador.generarComentarioSimple("------- SALTOS DE CONTINUE ------");
        codigo += "\n" + Generador.getAllEtiquetas(ambito.getListadoContinue());
        codigo += "\n" + ((tipo == true) ? Generador.generarCuadruplo("+", valor, "1", valor) : Generador.generarCuadruplo("-", valor, "1", valor));
        codigo += "\n" + Generador.generarCuadruplo("=",pos, valor, "Stack");
        
        codigo += "\n" + Generador.generarComentarioSimple("-----------------------FIN CUERPO FOR ----------------------");

        codigo += "\n" + Generador.saltoIncondicional(recursividad);
        codigo += " " + Generador.generarComentarioSimple(" SALTO A LA RECURSIVIDAD");
        codigo += "\n" + Generador.generarComentarioSimple("------- SALTOS DE BREAKS ------");
        codigo += "\n" + Generador.getAllEtiquetas(ambito.getListadoBreak());
        codigo += "\n" + Generador.guardarEtiqueta(falsa);
        codigo += " " + Generador.generarComentarioSimple(" Etiqueta Falsa");
        codigo += "\n" + Generador.generarComentarioSimple("---------------------------------------------FIN FOR ---------------------------------------------");
        nodo.setCodigo3D(codigo);
        
        ambito.reiniciarBreak();
        ambito.reiniciarContinue();
        
        return nodo;
    }
    
    
}
