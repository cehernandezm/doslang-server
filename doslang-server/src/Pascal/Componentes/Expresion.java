/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import static Pascal.Analisis.Generador.generarComentarioSimple;
import static Pascal.Analisis.Generador.generarCuadruplo;
import static Pascal.Analisis.Generador.guardarAcceso;
import static Pascal.Analisis.Generador.llamarAFuncion;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato;
import Pascal.Componentes.Arreglos.AccesoArreglo;
import Pascal.Componentes.Funciones.Funcion;
import Pascal.Componentes.Funciones.Parametro;
import Pascal.Componentes.Registros.Atributo;
import Pascal.Componentes.Registros.Registro;
import Pascal.Componentes.UserTypes.Equivalencia;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Expresion extends TipoDato implements Instruccion {

    Expresion izq;
    Expresion der;
    Tipo tipo;
    Operacion operacion;
    int l;
    int c;
    Object valor;
    String id;
    
    LinkedList<Expresion> listaExpresiones;
    
    

    /**
     * CONSTRUCTOR PARA OPERACIONES TERNARIAS NUM + NUM
     *
     * @param izq
     * @param der
     * @param operacion
     * @param l
     * @param c
     */
    public Expresion(Expresion izq, Expresion der, Operacion operacion, int l, int c) {
        this.izq = izq;
        this.der = der;
        this.operacion = operacion;
        this.l = l;
        this.c = c;
    }

    /**
     * CONSTRUCTOR PARA VALORES PRIMITIVOS
     *
     * @param izq
     * @param tipo
     * @param l
     * @param c
     */
    public Expresion(Object valor, Tipo tipo, int l, int c) {
        this.valor = valor;
        this.tipo = tipo;
        this.l = l;
        this.c = c;
        this.operacion = null;
    }

    /**
     * CONSTRUCOR PARA EXPRESION . ID
     * @param izq
     * @param der
     * @param operacion
     * @param l
     * @param c
     * @param id 
     */
    public Expresion(Expresion izq, Expresion der, Operacion operacion, int l, int c, String id) {
        this.izq = izq;
        this.der = der;
        this.operacion = operacion;
        this.l = l;
        this.c = c;
        this.id = id;
    }

    /**
     * 
     * @param operacion
     * @param l
     * @param c
     * @param id
     * @param listaExpresiones 
     */
    public Expresion(Operacion operacion, int l, int c, String id, LinkedList<Expresion> listaExpresiones) {
        this.operacion = operacion;
        this.l = l;
        this.c = c;
        this.id = id.toLowerCase();
        this.listaExpresiones = listaExpresiones;
    }

    /**
     * CONSTRUCTOR PARA ACCEDER  AUN ARREGLO
     * @param izq
     * @param operacion
     * @param l
     * @param c
     * @param listaExpresiones 
     */
    public Expresion(Expresion izq, Operacion operacion, int l, int c, LinkedList<Expresion> listaExpresiones) {
        this.izq = izq;
        this.operacion = operacion;
        this.l = l;
        this.c = c;
        this.listaExpresiones = listaExpresiones;
    }

    
    
    
    
    
    
    /**
     * CONSTRUCTOR DEL METODO PADRE
     *
     * @param ambito
     * @return
     */
    @Override
    public Object ejecutar(Ambito ambito) {
        MessageError error = new MessageError("", l, c, "");
        Object op1 = (izq == null) ? null : izq.ejecutar(ambito);
        Object op2 = (der == null) ? null : der.ejecutar(ambito);
        String codigo = "";
        if (operacion != null) {
            if (op1 instanceof MessageError || op2 instanceof MessageError) {
                if (op1 instanceof MessageError) {
                    error = (MessageError) op1;
                } else if (op2 instanceof MessageError) {
                    error = (MessageError) op2;
                }
                return error;
            }
            Nodo nodoIzq = (Nodo) op1;
            Nodo nodoDer = (op2 == null) ? null :(Nodo) op2;
            codigo = "";
            switch (operacion) {
                //<editor-fold defaultstate="collapsed" desc="SUMA">
                case SUMA:
                    //-------------------------------- INT + INT || CHAR + INT || INT + CHAR || CHAR + CHAR --------------------------------------------------------
                    if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.CHAR
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        codigo = nodoIzq.getCodigo3D();
                        codigo += nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        resultado.setCodigo3D(codigo);
                        return resultado;
                    } //------------------------------- INT DOUBLE | DOUBLE INT | DOUBLE DOUBLE || CHAR DOUBLE || DOUBLE CHAR-----------------------------------
                    else if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.DOUBLE || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.DOUBLE
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.DOUBLE
                            || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        codigo = nodoIzq.getCodigo3D();
                        codigo += nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.DOUBLE);
                        resultado.setResultado(temp);
                        resultado.setCodigo3D(codigo);
                        return resultado;
                    } //-------------------------- WORD X | X WORD | X STRING | STRING X -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.WORD || nodoDer.getTipo() == Tipo.WORD || nodoIzq.getTipo() == Tipo.STRING || nodoDer.getTipo() == Tipo.STRING) {
                        Tipo sendTipo = (nodoIzq.getTipo() == Tipo.STRING && nodoDer.getTipo() == Tipo.STRING) ? Tipo.STRING : Tipo.WORD;
                        Nodo resultado = new Nodo();
                        resultado.setTipo(sendTipo);
                        String temp = Generador.generarTemporal();
                        resultado.setResultado(temp);
                        if(nodoIzq.getTipo() != Tipo.BOOLEAN) codigo = nodoIzq.getCodigo3D();
                        if(nodoDer.getTipo() != Tipo.BOOLEAN)codigo += nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarComentarioSimple("INICIO Concatenacion: ");
                        codigo += "\n" + Generador.generarCuadruplo("+", "H", "0", temp);
                        codigo += "\n" + Generador.generarComentarioSimple("Reservando nuevo espacio: ");
                        
                        if (nodoIzq.getTipo() == Tipo.BOOLEAN) codigo += "\n" + Generador.concatenarBoolean(nodoIzq);
                        else codigo += "\n" + concatenar(nodoIzq.getResultado(), nodoIzq.getTipo(),ambito);
                        
                        if (nodoDer.getTipo() == Tipo.BOOLEAN) codigo += "\n" + Generador.concatenarBoolean(nodoDer);
                        else codigo += "\n" + concatenar(nodoDer.getResultado(), nodoDer.getTipo(),ambito);
                        
                        codigo += "\n" + Generador.generarComentarioSimple("FIN Concatenacion: ");
                        codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", "0");
                        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                        resultado.setCodigo3D(codigo);
                        return resultado;

                    }
                    else{
                        MessageError mensajeError = new MessageError("Semantico",l,c,"No se pueden sumar : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
//</editor-fold>

                    

                //<editor-fold defaultstate="collapsed" desc="RESTA,MULTIPLICACION,DIVISION,MODULO">
                case RESTA:
                case MULTIPLICACION:
                case DIVISION:
                case MODULO:
                    //------------------------------------ INT DOUBLE | DOUBLE INT | DOUBLE DOUBLE || CHAR DOUBLE || DOUBLE CHAR------------
                    if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.DOUBLE || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.DOUBLE
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.DOUBLE
                            || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        String simbolo = "";
                        if (operacion == Operacion.RESTA) simbolo = "-";
                        else if (operacion == Operacion.MULTIPLICACION)simbolo = "*";
                        else if(operacion == Operacion.MODULO) simbolo = "%";
                        else simbolo = "/";
                        codigo = nodoIzq.getCodigo3D();
                        codigo += "\n" +  nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarCuadruplo(simbolo, nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.DOUBLE);
                        resultado.setResultado(temp);
                        resultado.setCodigo3D(codigo);
                        return resultado;
                    }
                    // --------------------------------------- INT  INT || INT CHAR || CHAR INT || CHAR CHAR-------------------------------
                    else if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.INT 
                            || nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.CHAR
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        String simbolo = "";
                        if (operacion == Operacion.RESTA) simbolo = "-";
                        else if (operacion == Operacion.MULTIPLICACION)simbolo = "*";
                        else if(operacion == Operacion.MODULO) simbolo = "%";
                        else simbolo = "/";
                        codigo = nodoIzq.getCodigo3D();
                        codigo +=  "\n" + nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarCuadruplo(simbolo, nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        
                        if (operacion == Operacion.DIVISION) {
                            String pos = Generador.generarTemporal();
                            //--------------------------------------------------------------EL VALOR ES MAYOR A 10 ENTONCES HAGO UN TRUNC AL NUMERO ---------------------------------
                            codigo += "\n" + generarComentarioSimple("LLAMAMOS A LA FUNCION TRUNC PARA OBTENER EL VALOR ENTERO");
                            codigo += "\n" + generarCuadruplo("+", "P", String.valueOf(ambito.getTam()), "P");
                            codigo += "  " + generarComentarioSimple("   Inicio simulacion de cambio de ambito");
                            codigo += "\n" + generarCuadruplo("+", "P", "0", pos);
                            codigo += "\n" + generarCuadruplo("=", pos, temp, "Stack");
                            codigo += "  " + generarComentarioSimple(" Pasamos: " + temp + " como parametro");
                            codigo += "\n" + llamarAFuncion("funcionTrunk");
                            codigo += "\n" + generarCuadruplo("+", "P", "1", pos);
                            codigo += "\n" + guardarAcceso(temp, "Stack", pos);
                            codigo += "  " + generarComentarioSimple("   Obtenemos el valor del return");
                            codigo += "\n" + generarCuadruplo("-", "P", String.valueOf(ambito.getTam()), "P");
                            codigo += "  " + generarComentarioSimple("   Fin simulacion de cambio de ambito");
                            codigo += "\n" + generarComentarioSimple("FIN LLAMAMOS A LA FUNCION TRUNC PARA OBTENER EL VALOR ENTERO");
                            //-------------------------------------------------------------- LLAMAMOS A LA RECURSIVIDAD DE TOSTRING ---------------------------------------------
                        }
                        
                        
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        resultado.setCodigo3D(codigo);
                        return resultado;
                    }
                    else{
                        MessageError mensajeError = new MessageError("Semantico",l,c,"No se pueden operar : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
//</editor-fold>
                    
                //<editor-fold defaultstate="collapsed" desc="POTENCIA">
                case POTENCIA:
                    //----------------------------------------- INT INT || CHAR INT || INT CHAR || CHAR CHAR-----------------------------------------------------------------------------
                    if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.CHAR
                            || nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        codigo = nodoIzq.getCodigo3D();
                        codigo += nodoDer.getCodigo3D();
                        codigo +=  "\n" +  Generador.generarCuadruplo("^", nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setCodigo3D(codigo);
                        resultado.setResultado(temp);
                        return resultado;
                    } else {
                        MessageError mensajeError = new MessageError("Semantico", l, c, "No se pueden obtener la potencia de : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                //</editor-fold>
                
                    
                //<editor-fold defaultstate="collapsed" desc="RELACIONALES">
                case MAYOR:
                    return generarRelacional(">","mayor",nodoIzq,nodoDer,ambito);
                case MENOR:
                    return generarRelacional("<","menor",nodoIzq,nodoDer,ambito);
                case MAYORIGUAL:
                    return generarRelacional(">=","mayorigual",nodoIzq,nodoDer,ambito);
                case MENORIGUAL:
                    return generarRelacional("<=","menorigual",nodoIzq,nodoDer,ambito);
                case IGUAL:
                    return generarRelacionalEspecial("=","igual",nodoIzq,nodoDer,ambito);
                case DIFERENTE:
                    return generarRelacionalEspecial("<>","diferente",nodoIzq,nodoDer,ambito);
                //</editor-fold>
                    
            
                
                //<editor-fold defaultstate="collapsed" desc="AND">
                case AND:
                    if(nodoIzq.getTipo() == Tipo.BOOLEAN && nodoDer.getTipo() == Tipo.BOOLEAN){
                        
                        //------------------------------------------- CORTO CIRCUITO ---------------------------------------------
                        //---------------------------------- SI ES FALSE SE SALE DE UNA VEZ --------------------------------------
                        //---------------------------------- SI ES VERDADERO SE VA A LA SEGUNDA CONDICION ------------------------
                        Nodo nodo = new Nodo();
                        nodo.addEtiquetaF(nodoIzq.getEtiquetaF());
                        nodo.addEtiquetaF(nodoDer.getEtiquetaF());
                        nodo.setEtiquetaV(nodoDer.getEtiquetaV());
                        
                        
                        String code = "";
                        if(nodoIzq.getCodigo3D() != null ) code = nodoIzq.getCodigo3D();
                        code += "\n" + Generador.getAllEtiquetas(nodoIzq.getEtiquetaV());
                        code += nodoDer.getCodigo3D();
                        
                        nodo.setCodigo3D(code);
                        nodo.setTipo(Tipo.BOOLEAN);
                        
                        return nodo;
                    }
                    else {
                        MessageError mensajeError = new MessageError("Semantico", l, c, "No se puede aplicar AND en los tipos : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                //</editor-fold>
                    
                    
                    
                //<editor-fold defaultstate="collapsed" desc="OR">
                case OR:
                    if(nodoIzq.getTipo() == Tipo.BOOLEAN && nodoDer.getTipo() == Tipo.BOOLEAN){
                        
                        //------------------------------------------- CORTO CIRCUITO ---------------------------------------------
                        //---------------------------------- SI ES FALSE SE VA A LA SEGUNDA CODNICON --------------------------------------
                        //---------------------------------- SI ES VERDADERO SE SALE ------------------------
                        Nodo nodo = new Nodo();
                        nodo.addEtiquetaF(nodoDer.getEtiquetaF());
                        nodo.setEtiquetaV(nodoIzq.getEtiquetaV());
                        nodo.setEtiquetaV(nodoDer.getEtiquetaV());
                        
                        
                        String code = "";
                        if(nodoIzq.getCodigo3D() != null ) code = nodoIzq.getCodigo3D();
                        code += "\n" + Generador.getAllEtiquetas(nodoIzq.getEtiquetaF());
                        code += nodoDer.getCodigo3D();
                        
                        nodo.setCodigo3D(code);
                        nodo.setTipo(Tipo.BOOLEAN);
                        
                        return nodo;
                    }
                    else {
                        MessageError mensajeError = new MessageError("Semantico", l, c, "No se puede aplicar OR en los tipos : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                //</editor-fold>
                    
            
            
                //<editor-fold defaultstate="collapsed" desc="NAND">
                case NAND:
                    if(nodoIzq.getTipo() == Tipo.BOOLEAN && nodoDer.getTipo() == Tipo.BOOLEAN){
                        
                        //------------------------------------------- CORTO CIRCUITO ---------------------------------------------
                        //---------------------------------- SI ES FALSE SE SALE --------------------------------------
                        //---------------------------------- SI ES VERDADERO SE VA A LA SEGUNDA OPCION ------------------------
                        Nodo nodo = new Nodo();
                        nodo.addEtiquetaF(nodoDer.getEtiquetaV());
                        nodo.addEtiquetaV(nodoIzq.getEtiquetaF());
                        nodo.addEtiquetaV(nodoDer.getEtiquetaF());
                        
                        
                        String code = "";
                        if(nodoIzq.getCodigo3D() != null ) code = nodoIzq.getCodigo3D();
                        code += "\n" + Generador.getAllEtiquetas(nodoIzq.getEtiquetaV());
                        code += nodoDer.getCodigo3D();
                        
                        nodo.setCodigo3D(code);
                        nodo.setTipo(Tipo.BOOLEAN);
                        
                        return nodo;
                    }
                    else {
                        MessageError mensajeError = new MessageError("Semantico", l, c, "No se puede aplicar NAND en los tipos : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                //</editor-fold>
            
            
                //<editor-fold defaultstate="collapsed" desc="NOR">
                case NOR:
                    if(nodoIzq.getTipo() == Tipo.BOOLEAN && nodoDer.getTipo() == Tipo.BOOLEAN){
                        
                        //------------------------------------------- CORTO CIRCUITO ---------------------------------------------
                        //---------------------------------- SI ES VERDADERO SE SALE (FALSO) --------------------------------------
                        //---------------------------------- SI ES FALSO SE VA A LA SEGUNDA OPCION ------------------------
                        Nodo nodo = new Nodo();
                        nodo.addEtiquetaF(nodoIzq.getEtiquetaV());
                        nodo.addEtiquetaF(nodoDer.getEtiquetaV());
                        nodo.addEtiquetaV(nodoDer.getEtiquetaF());
                        
                        
                        String code = "";
                        if(nodoIzq.getCodigo3D() != null ) code = nodoIzq.getCodigo3D();
                        code += "\n" + Generador.getAllEtiquetas(nodoIzq.getEtiquetaF());
                        code += nodoDer.getCodigo3D();
                        
                        nodo.setCodigo3D(code);
                        nodo.setTipo(Tipo.BOOLEAN);
                        
                        return nodo;
                    }
                    else {
                        MessageError mensajeError = new MessageError("Semantico", l, c, "No se puede aplicar NOR en los tipos : " + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                //</editor-fold>
                    
                    
                    
                //<editor-fold defaultstate="collapsed" desc="NOT">
                case NOT:
                    if(nodoIzq.getTipo() == Tipo.BOOLEAN){
                        
                        //------------------------------------------- CORTO CIRCUITO ---------------------------------------------
                        //---------------------------------- VERDADERO = FALSO) --------------------------------------
                        //---------------------------------- FALSO = VERDADERO ------------------------
                        Nodo nodo = new Nodo();
                        nodo.addEtiquetaF(nodoIzq.getEtiquetaV());
                        nodo.addEtiquetaV(nodoIzq.getEtiquetaF());

                        nodo.setTipo(Tipo.BOOLEAN);
                        nodo.setCodigo3D("");
                        return nodo;
                    }
                    else {
                        MessageError mensajeError = new MessageError("Semantico", l, c, "No se puede aplicar NOT en el tipo : " + nodoIzq.getTipo());
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                //</editor-fold>
                    
                    
                    
                //<editor-fold defaultstate="collapsed" desc="CHARAT">
                case CHARAT:
                    if((nodoIzq.getTipo() == Tipo.STRING || nodoIzq.getTipo() == Tipo.WORD) && nodoDer.getTipo() == Tipo.INT ){
                        String tempValor = Generador.generarTemporal();
                        String tempPos = nodoIzq.getResultado();
                        String contador = Generador.generarTemporal();
                        String etiquetaRecursiva = Generador.generarEtiqueta();
                        String etiquetaVerdadera = Generador.generarEtiqueta();
                        String etiquetaFalsa = Generador.generarEtiqueta();
                        String etiquetaSalida = Generador.generarEtiqueta();
                        String res = Generador.generarTemporal();
                        
                        codigo = nodoIzq.getCodigo3D();
                        codigo += "\n" + nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ CHAR AT ----------------------------");
                        codigo += "\n" + Generador.generarCuadruplo("=", "0", "", contador);
                        codigo += "\n" + Generador.guardarEtiqueta(etiquetaRecursiva);
                        codigo += "\n" + Generador.guardarAcceso(tempValor, "Heap", tempPos);
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ Validaciones ----------------------------");
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ Estamos en la posicion correcta ----------------------------");
                        codigo += "\n" + Generador.guardarCondicional(etiquetaVerdadera, nodoDer.getResultado(), contador, "=");
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ Estamos al final de la cadena ----------------------------");
                        codigo += "\n" + Generador.guardarCondicional(etiquetaFalsa, tempValor, "0", "=");
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ No se cumplieron ----------------------------");
                        codigo += "\n" + Generador.generarCuadruplo("+", "1", tempPos, tempPos);
                        codigo += "\n" + Generador.generarCuadruplo("+", "1", contador, contador);
                        codigo += "\n" + Generador.saltoIncondicional(etiquetaRecursiva);
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ Estamos en la posicion Correcta ----------------------------");
                        codigo += "\n" + Generador.guardarEtiqueta(etiquetaVerdadera);
                        codigo += "\n" + Generador.generarCuadruplo("=", tempValor, "", res);
                        codigo += "\n" + Generador.saltoIncondicional(etiquetaSalida);
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ Llegamos al final de la cadena ----------------------------");
                        codigo += "\n" + Generador.guardarEtiqueta(etiquetaFalsa);
                        codigo += "\n" + Generador.generarCuadruplo("=", "0", "", res);
                        codigo += "\n" + Generador.generarComentarioSimple("------------------------ Salida ----------------------------");
                        codigo += "\n" + Generador.guardarEtiqueta(etiquetaSalida);
                        
                        Nodo resultado = new Nodo();
                        resultado.setCodigo3D(codigo);
                        resultado.setTipo(Tipo.CHAR);
                        resultado.setResultado(res);
                        return resultado;
                        
                        
                    }
                    else ambito.addSalida(new MessageError("Semantico",l,c,"charAt solo Acepta una Cadena y un Entero como indice"));
                    break;
                //</editor-fold>
                    
                    
                //<editor-fold defaultstate="collapsed" desc="EXP . ID">
                case ACCESOID:
                    codigo = nodoIzq.getCodigo3D();
                    //----------------------------------- SI ES UN ENUM ------------------------------------------------------------
                    //<editor-fold defaultstate="collapsed" desc="ENUM">
                    if(nodoIzq.getTipo() == Tipo.ENUM){
                        Object valor = nodoIzq.getValor();
                        if(!(valor instanceof MessageError)){
                            if(valor != null){
                                LinkedList<String> lista = (LinkedList<String>)valor;
                                id = id.toLowerCase();
                                int index = 0;
                                for(String s : lista){
                                    s = s.toLowerCase();
                                    if(s.equals(id)){
                                        String pos = Generador.generarTemporal();
                                        codigo += "\n" + Generador.generarComentarioSimple("----------------------------Accedemos al Valor: " + id + " en el enum");
                                        codigo +=  "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), String.valueOf(index), nodoIzq.getResultado());
                                        codigo +=  "\n" + Generador.guardarAcceso(pos, "Heap", nodoIzq.getResultado());
                                        Nodo nodo = new Nodo();
                                        nodo.setCodigo3D(codigo);
                                        nodo.setResultado(pos);
                                        nodo.setTipo(Tipo.WORD);
                                        return nodo;
                                        
                                    }
                                    index++;
                                }
                                MessageError mensaje = new MessageError("Semantico",l,c,"No se encontro el valor: " + id + " en el enum");
                                ambito.addSalida(mensaje);
                                return mensaje;
                            }
                        }
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="REGISTRO">
                    else if(nodoIzq.getTipo() == Tipo.REGISTRO){
                        LinkedList<Atributo> lista =(LinkedList<Atributo>)nodoIzq.getValor();
                        Atributo atributo = null;
                        int index = 0;
                        
                        for(Atributo a : lista){
                            if(a.getId().equals(id)){
                                atributo = a;
                                break;
                            }
                            index++;
                        }
                        
                        //---------------------------------------------- No encontro el id ----------------------------------------------------------
                        if (atributo == null) {
                            MessageError mensaje = new MessageError("Semantico", l, c, "El atributo: " + id + " no existe en el registro: " + nodoIzq.getId());
                            ambito.addSalida(mensaje);
                            return mensaje;
                        }
                        
                        
                        
                        String posicion = Generador.generarTemporal();
                        String value = Generador.generarTemporal();
                        codigo += "\n" + Generador.generarComentarioSimple("----------------- ACCEDEMOS AL ATRIBUTO: " + id + " del registro: " + nodoIzq.getId());
                        codigo += "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), String.valueOf(index), posicion);
                        codigo += "\n" + Generador.guardarAcceso(value, "Heap", posicion);
                        codigo += Generador.generarComentarioSimple("-----------------FIN ACCEDEMOS AL ATRIBUTO: " + id + " del registro: " + nodoIzq.getId());
                        
                        Nodo nodo = new Nodo();
                        if(atributo.getTipo().getTipo() == Tipo.ID || atributo.getTipo().getTipo() == Tipo.REGISTRO){
                            Equivalencia equi = ambito.getEquivalencia(atributo.getTipo().getId().toLowerCase());
                            if(equi != null){
                                //atributo.setTipo(equi.getTipo());
                                Registro r = (Registro)equi.getTipo().getValor();
                                nodo.setValor(r.getAtributos());
                                nodo.setTipo(Tipo.REGISTRO);
                                nodo.setId(atributo.getTipo().getId());
                            }
                            else{
                                MessageError mensaje = new MessageError("Semantico",l,c,"No existe el registro: " + atributo.getTipo().getId());
                                ambito.addSalida(mensaje);
                                return mensaje;
                            }
                        }
                        else{
                            nodo.setTipo(atributo.getTipo().getTipo());
                        }
                        
                        nodo.setCodigo3D(codigo);
                        nodo.setResultado(value);
                        nodo.setEstructura(1); //HEAP
                        nodo.setPosHeap(posicion);
                        return nodo;
                    }
                    else {
                        MessageError mensaje = new MessageError("Semantico", l, c, "No se puede acceder a un atributo del tipo: " + nodoIzq.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
//</editor-fold>
                    break;
//</editor-fold>
            
                //<editor-fold defaultstate="collapsed" desc="EXP [EXP(,EXP)+]">
                case ACCESOARRAY:
                    Object resI = izq.ejecutar(ambito);
                    if(resI instanceof MessageError) return resI;
                    
                    Nodo sim = (Nodo)resI;
                    codigo = sim.getCodigo3D();
                    //----------------------------------------- SI NO ES UN ARREGLO --------------------------------------------------------------
                    if(sim.getTipo() != Tipo.ARRAY){
                        MessageError mensaje = new MessageError("Semantico",l,c,id + " No es de tipo Arreglo, no se reconoce el tipo: " +  sim.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    String etiquetaSalto = "";
                    Object resultado = AccesoArreglo.obtenerMapeoLexicoGrafico(sim, ambito, listaExpresiones, l, c);
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                    
                    
                    Nodo temp = (Nodo)resultado;
                    codigo += "\n" + temp.getCodigo3D();
                    String res = Generador.generarTemporal();
                    codigo += "\n" + Generador.guardarAcceso(res, "Heap", temp.getResultado());
                    Nodo nodo = new Nodo();
                    nodo.setTipo(temp.getTipo());
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(res);
                    nodo.setValor(temp.getValor());
                    nodo.setCantidadDimensiones(temp.getCantidadDimensiones());
                    return nodo;
//</editor-fold>
                    
                    
                //<editor-fold defaultstate="collapsed" desc="SIZEOF">
                case SIZEOF:
                    id = id.toLowerCase();
                    Equivalencia equi = ambito.getEquivalencia(id);
                    //---------------------------------------------------------- Si no existe -----------------------------------------------------------
                    if(equi == null){
                        MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " no existe");
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    //------------------------------------------------------- SI NO ES DE TIPO REGISTRO ------------------------------------------------
                    if(equi.getTipo().getTipo() != Tipo.REGISTRO){
                        MessageError mensaje = new MessageError("Semantico",l,c,"Sizeof solo se puede aplicar al tipo Record no se reconoce: " + equi.getTipo().getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    Registro re = (Registro)equi.getTipo().getValor();
                    String contador = Generador.generarTemporal();
                    codigo = "\n" + Generador.generarComentarioSimple("---------------------- INICIO DE APARTAR TAM PARA EL REGISTRO -----------------");
                    codigo += "\n" + Generador.generarCuadruplo("=", "1","", contador);
                    for(int i = 1; i < re.getAtributos().size();i++) codigo += "\n" + Generador.generarCuadruplo("+", contador,"1", contador);
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.INT);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(contador);
                    nodo.setValor(re.getAtributos());
                    return nodo;
//</editor-fold>
                    
                //<editor-fold defaultstate="collapsed" desc="MALLOC">
                case MALLOC:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                    
                    temp = (Nodo) resultado;
                    
                    if(temp.getTipo() != Tipo.INT){
                        MessageError mensaje = new MessageError("Semantico",l,c," Malloc solo acepta tipo INTEGER  no se reconoce: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    String falso = Generador.generarEtiqueta();
                    String salto = Generador.generarEtiqueta();
                    res = Generador.generarTemporal();
                    contador = Generador.generarTemporal();
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarCuadruplo("=", "0", "", contador);
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "", res);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salto);
                    codigo += "\n" + Generador.guardarCondicional(falso, contador, temp.getResultado(), ">=");
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "-1","Heap");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarCuadruplo("+", "1", contador, contador);
                    
                   
                    
                    codigo += "\n" + Generador.saltoIncondicional(salto);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(falso);
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.MALLOC);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(res);
                    return nodo;
//</editor-fold>
                    
                    
                    
                //<editor-fold defaultstate="collapsed" desc="LENGTH">
                case LENGTH:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                    
                    temp = (Nodo)resultado;
                    //---------------------------------------------- SI NO ES UN STRING ----------------------------------------------------------------
                    if(temp.getTipo() != Tipo.WORD && temp.getTipo() != Tipo.STRING){
                        MessageError mensaje = new MessageError("Semantico",l,c,"LENGTH NECESITA UN WORD O STRING, NO SE RECONOCE: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    contador = Generador.generarTemporal();
                    String salida = Generador.generarEtiqueta();
                    String valor = Generador.generarTemporal();
                    String loop = Generador.generarEtiqueta();
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("---------------------------- FUNCION LENGTH ------------------------------------------");
                    codigo += "\n" + Generador.generarCuadruplo("=", "0", "", contador);
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "\n" + Generador.guardarAcceso(valor, "Heap", temp.getResultado());
                    codigo += "\n" + Generador.guardarCondicional(salida, valor, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple(" Si es el final de la cadena salimos del Lopp");
                    codigo += "\n" + Generador.generarCuadruplo("+", contador, "1", contador);
                    codigo += "\n" + Generador.generarCuadruplo("+", temp.getResultado(), "1", temp.getResultado());
                    codigo += "\n" + Generador.saltoIncondicional(loop);
                    codigo += "\n" + Generador.generarComentarioSimple("---------------------------- FIN FUNCION LENGTH ------------------------------------------");
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.INT);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(contador);
                    return nodo;
                    //</editor-fold>
                                        
                //<editor-fold defaultstate="collapsed" desc="REPLACE">
                case REPLACE:
                    if((nodoIzq.getTipo() != Tipo.WORD && nodoIzq.getTipo() != Tipo.STRING) || (nodoDer.getTipo() != Tipo.WORD && nodoDer.getTipo() != Tipo.STRING)){
                        MessageError mensaje = new MessageError("Semantico",l,c,"La funcion replace necesita dos cadenas no se reconoce: " + nodoIzq.getTipo() + " y " + nodoDer.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    salida = Generador.generarEtiqueta();
                    loop = Generador.generarEtiqueta();
                    String primeraCadena = Generador.generarEtiqueta();
                    
                    String val1 = Generador.generarTemporal();
                    String val2 = Generador.generarTemporal();
                    String result = Generador.generarTemporal();
                    codigo = nodoIzq.getCodigo3D();
                    codigo += "\n" + nodoDer.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------------- FUNCION REPLACE ---------------------------------------");
                    
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "", result);
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "\n" + Generador.guardarAcceso(val1, "Heap", nodoIzq.getResultado());
                    codigo += "   " + Generador.generarComentarioSimple("  Obtenemos el caracter de la cadena1");
                    codigo += "\n" + Generador.guardarAcceso(val2, "Heap", nodoDer.getResultado());
                    codigo += "   " + Generador.generarComentarioSimple("  Obtenemos el caracter de la cadena2");
                    
                    codigo += "\n" + Generador.guardarCondicional(salida, val1, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple("Si estamos al final de la primera cadena");
                    codigo += "\n" + Generador.guardarCondicional(primeraCadena, val2, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple("Si estamos al final de la segunda cadena");
                    
                    codigo += "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), "1",nodoIzq.getResultado());
                    codigo += "\n" + Generador.generarCuadruplo("+", nodoDer.getResultado(), "1",nodoDer.getResultado());
                    
                    codigo += "\n" + Generador.guardarCondicional(loop, val1, val2, "=");
                    codigo += "   " + Generador.generarComentarioSimple("Si son iguales no almacenamos nada");
                    
                    codigo += "\n" + Generador.generarCuadruplo("-", nodoIzq.getResultado(), "1",nodoIzq.getResultado());
                    codigo += "\n" + Generador.guardarEtiqueta(primeraCadena);
                    codigo += "   " + Generador.generarComentarioSimple("Llegamos al final de la segunda cadena o no hay match con la cadena");
                    codigo += "\n" + Generador.guardarAcceso(val1, "Heap", nodoIzq.getResultado());
                    codigo += "\n" + Generador.guardarCondicional(salida, val1, "0", "=");
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", val1, "Heap");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), "1",nodoIzq.getResultado());
                    codigo += "\n" + Generador.saltoIncondicional(primeraCadena);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "0", "Heap");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------------- FIN FUNCION REPLACE ---------------------------------------");
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.WORD);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(result);
                    return nodo;
                    
//</editor-fold>
                    
                //<editor-fold defaultstate="collapsed" desc="TOLOWERCASE">
                case TOLOWERCASE:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"Semantico");
                    
                    temp = (Nodo)resultado;
                    
                    if(temp.getTipo() != Tipo.STRING && temp.getTipo() != Tipo.WORD){
                        MessageError mensaje = new MessageError("Semantico",l,c,"TOLOWERCASE NECESITA UNA CADENA NO SE RECONOCE: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    result = Generador.generarTemporal();
                    salida = Generador.generarEtiqueta();
                    String falsa = Generador.generarEtiqueta();
                    salto = Generador.generarEtiqueta();
                    loop = Generador.generarEtiqueta();
                    valor = Generador.generarTemporal();
                    
                    
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- TOLOWERCASE ------------------------------------");
                    codigo += "\n" + Generador.generarCuadruplo("=","H", "", result);
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "  " + Generador.generarComentarioSimple("  Encargada de recorrer la cadena original");
                    codigo += "\n" + Generador.guardarAcceso(valor, "Heap", temp.getResultado());
                    codigo += "\n" + Generador.guardarCondicional(salida, valor, "0", "=");
                    codigo += "\n" + Generador.guardarCondicional(falsa, valor, "64", "<");
                    codigo += "  " + Generador.generarComentarioSimple("Si el codigo ascii es menor a 64 entonces no es una letra");
                    codigo += "\n" + Generador.guardarCondicional(falsa, valor, "90", ">");
                    codigo += "  " + Generador.generarComentarioSimple("Si el codigo ascii es mayor a 90 entonces no es una letra MAYUSCULA");
                    codigo += "\n" + Generador.generarCuadruplo("+", valor, "32", valor);
                    codigo += "\n" + Generador.saltoIncondicional(salto);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(falsa);
                    codigo += "  " + Generador.generarComentarioSimple("Si se sale de los limites");
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salto);
                    codigo += "  " + Generador.generarComentarioSimple("Salida de los if");
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", valor, "Heap");
                    
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarCuadruplo("+", temp.getResultado(), "1", temp.getResultado());
                    codigo += "\n" + Generador.saltoIncondicional(loop);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "0", "Heap");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- FIN TOLOWERCASE ------------------------------------");
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.WORD);
                    nodo.setResultado(result);
                    nodo.setCodigo3D(codigo);
                    return nodo;
//</editor-fold>
                    
                //<editor-fold defaultstate="collapsed" desc="TOUPPERCASE">
                case TOUPPERCASE:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"Semantico");
                    
                    temp = (Nodo)resultado;
                    
                    if(temp.getTipo() != Tipo.STRING && temp.getTipo() != Tipo.WORD){
                        MessageError mensaje = new MessageError("Semantico",l,c,"TOUPPERCASE NECESITA UNA CADENA NO SE RECONOCE: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    result = Generador.generarTemporal();
                    salida = Generador.generarEtiqueta();
                    falsa = Generador.generarEtiqueta();
                    salto = Generador.generarEtiqueta();
                    loop = Generador.generarEtiqueta();
                    valor = Generador.generarTemporal();
                    
                    
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- TOUPPERCASE ------------------------------------");
                    codigo += "\n" + Generador.generarCuadruplo("=","H", "", result);
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "  " + Generador.generarComentarioSimple("  Encargada de recorrer la cadena original");
                    codigo += "\n" + Generador.guardarAcceso(valor, "Heap", temp.getResultado());
                    codigo += "\n" + Generador.guardarCondicional(salida, valor, "0", "=");
                    codigo += "\n" + Generador.guardarCondicional(falsa, valor, "97", "<");
                    codigo += "  " + Generador.generarComentarioSimple("Si el codigo ascii es menor a 97 entonces no es una letra MINUSCULA");
                    codigo += "\n" + Generador.guardarCondicional(falsa, valor, "122", ">");
                    codigo += "  " + Generador.generarComentarioSimple("Si el codigo ascii es mayor a 122 entonces no es una letra");
                    codigo += "\n" + Generador.generarCuadruplo("-", valor, "32", valor);
                    codigo += "\n" + Generador.saltoIncondicional(salto);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(falsa);
                    codigo += "  " + Generador.generarComentarioSimple("Si se sale de los limites");
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salto);
                    codigo += "  " + Generador.generarComentarioSimple("Salida de los if");
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", valor, "Heap");
                    
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarCuadruplo("+", temp.getResultado(), "1", temp.getResultado());
                    codigo += "\n" + Generador.saltoIncondicional(loop);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "0", "Heap");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------- FIN TOUPPERCASE ------------------------------------");
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.WORD);
                    nodo.setResultado(result);
                    nodo.setCodigo3D(codigo);
                    return nodo;
//</editor-fold>
            
                //<editor-fold defaultstate="collapsed" desc="EQUALS">
                case EQUALS:
                    if((nodoIzq.getTipo() != Tipo.WORD && nodoIzq.getTipo() != Tipo.STRING) || (nodoDer.getTipo() != Tipo.WORD && nodoDer.getTipo() != Tipo.STRING)){
                        MessageError mensaje = new MessageError("Semantico",l,c,"La funcion EQUALS necesita dos cadenas no se reconoce: " + nodoIzq.getTipo() + " y " + nodoDer.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    salida = Generador.generarEtiqueta();
                    String salida2 = Generador.generarEtiqueta();
                    falsa = Generador.generarEtiqueta();
                    loop = Generador.generarEtiqueta();
                    salto = Generador.generarEtiqueta();
                    
                    val1 = Generador.generarTemporal();
                    val2 = Generador.generarTemporal();
                    result = Generador.generarTemporal();
                    codigo = nodoIzq.getCodigo3D();
                    codigo += "\n" + nodoDer.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------------- FUNCION EQUALS ---------------------------------------");
                    
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "\n" + Generador.guardarAcceso(val1, "Heap", nodoIzq.getResultado());
                    codigo += "   " + Generador.generarComentarioSimple("  Obtenemos el caracter de la cadena1");
                    codigo += "\n" + Generador.guardarAcceso(val2, "Heap", nodoDer.getResultado());
                    codigo += "   " + Generador.generarComentarioSimple("  Obtenemos el caracter de la cadena2");
                    
                    codigo += "\n" + Generador.guardarCondicional(salida, val1, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple("Si estamos al final de la primera cadena");
                    codigo += "\n" + Generador.guardarCondicional(salida2, val2, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple("Si estamos al final de la segunda cadena");
                    
                    codigo += "\n" + Generador.generarCuadruplo("+", nodoIzq.getResultado(), "1",nodoIzq.getResultado());
                    codigo += "\n" + Generador.generarCuadruplo("+", nodoDer.getResultado(), "1",nodoDer.getResultado());
                    
                    codigo += "\n" + Generador.guardarCondicional(loop, val1, val2, "=");
                    codigo += "   " + Generador.generarComentarioSimple("Si son iguales seguimos en el loop");

                    codigo += "\n" + Generador.saltoIncondicional(falsa);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    codigo += "\n" + Generador.guardarCondicional(falsa, val2, "0", "<>");
                    codigo += "   " + Generador.generarComentarioSimple("Si la segunda cadena no llega a su final no son iguales");
                    codigo += "\n" + Generador.generarCuadruplo("=", "1", "", result);
                    codigo += "   " + Generador.generarComentarioSimple("Son iguales");
                    codigo += "\n" + Generador.saltoIncondicional(salto);
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salida2);
                    codigo += "\n" + Generador.guardarCondicional(falsa, val1, "0", "<>");
                    codigo += "   " + Generador.generarComentarioSimple("Si la primera cadena no llega a su final no son iguales");
                    codigo += "\n" + Generador.generarCuadruplo("=", "1", "", result);
                    codigo += "   " + Generador.generarComentarioSimple("Son iguales");
                    codigo += "\n" + Generador.saltoIncondicional(salto);
                    
                    
                    codigo += "\n" + Generador.guardarEtiqueta(falsa);
                    codigo += "\n" + Generador.generarCuadruplo("=", "0", "", result);
                    codigo += "   " + Generador.generarComentarioSimple("No son iguales");
                    
                    codigo += "\n" + Generador.guardarEtiqueta(salto);
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------------------- FIN FUNCION EQUALS ---------------------------------------");
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.BOOLEAN);
                    nodo.setEtiquetaV(null);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(result);
                    return nodo;
                    
//</editor-fold>
                    
                //<editor-fold defaultstate="collapsed" desc="TRUNK">
                case TRUNK:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                    
                    temp = (Nodo)resultado;
                    
                    if(temp.getTipo() != Tipo.DOUBLE){
                        MessageError mensaje = new MessageError("Semantico",l,c,"Solo se puede aplicar TRUNK a un decimal, no se reconoce: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    String pos = Generador.generarTemporal();
                    String retorno = Generador.generarTemporal();
                    
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------- INICIO DE TRUNCAR UN NUMERO ---------------------- ");
                    codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(ambito.getTam()), "P");
                    codigo += "   " + Generador.generarComentarioSimple("--------------------------- inicio simulacion de ambito ---------------------- ");
                    codigo += "\n" + Generador.generarCuadruplo("+", "P", "0", pos);
                    codigo += "\n" + Generador.generarCuadruplo("=", pos, temp.getResultado(), "Stack");
                    codigo += "\n" + Generador.llamarAFuncion("funcionTrunk");
                    
                    codigo += "\n" + Generador.generarCuadruplo("+", "P", "1", pos);
                    codigo += "\n" + Generador.guardarAcceso(retorno, "Stack", pos);
                    codigo += "   " + Generador.generarComentarioSimple("--------------------------- capturamos el valor de retorno ---------------------- ");
                    codigo += "\n" + Generador.generarCuadruplo("-", "P", String.valueOf(ambito.getTam()), "P");
                    codigo += "   " + Generador.generarComentarioSimple("--------------------------- fin simulacion de ambito ---------------------- ");
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------- FIN DE TRUNCAR UN NUMERO ---------------------- ");
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.INT);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(retorno);
                    return nodo;
//</editor-fold>
                   
                //<editor-fold defaultstate="collapsed" desc="- EXP">
                case NEGATIVO:
                    if(nodoIzq.getTipo() != Tipo.INT && nodoIzq.getTipo() != Tipo.DOUBLE){
                        MessageError mensaje = new MessageError("Semantico",l,c,"No se puede convertir a negativo el tipo: " + nodoIzq.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    res = Generador.generarTemporal();
                    codigo = nodoIzq.getCodigo3D();
                    codigo += "\n" + Generador.generarCuadruplo("*", nodoIzq.getResultado(), "-1", res);
                    codigo += "  " + Generador.generarComentarioSimple("CONVERTIR A NEGATIVO : " + nodoIzq.getResultado());
                    nodo = new Nodo();
                    nodo.setTipo(nodoIzq.getTipo());
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(res);
                    return nodo;
//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="ROUND">
                case ROUND:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                    
                    temp = (Nodo)resultado;
                    
                    if(temp.getTipo() != Tipo.DOUBLE){
                        MessageError mensaje = new MessageError("Semantico",l,c,"Solo se puede aplicar ROUND a un decimal, no se reconoce: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    pos = Generador.generarTemporal();
                    retorno = Generador.generarTemporal();
                    
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------- INICIO DE REDONDEAR UN NUMERO ---------------------- ");
                    codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(ambito.getTam()), "P");
                    codigo += "   " + Generador.generarComentarioSimple("--------------------------- inicio simulacion de ambito ---------------------- ");
                    codigo += "\n" + Generador.generarCuadruplo("+", "P", "0", pos);
                    codigo += "\n" + Generador.generarCuadruplo("=", pos, temp.getResultado(), "Stack");
                    codigo += "\n" + Generador.llamarAFuncion("funcionRound");
                    
                    codigo += "\n" + Generador.generarCuadruplo("+", "P", "1", pos);
                    codigo += "\n" + Generador.guardarAcceso(retorno, "Stack", pos);
                    codigo += "   " + Generador.generarComentarioSimple("--------------------------- capturamos el valor de retorno ---------------------- ");
                    codigo += "\n" + Generador.generarCuadruplo("-", "P", String.valueOf(ambito.getTam()), "P");
                    codigo += "   " + Generador.generarComentarioSimple("--------------------------- fin simulacion de ambito ---------------------- ");
                    codigo += "\n" + Generador.generarComentarioSimple("--------------------------- FIN DE REDONDEAR UN NUMERO ---------------------- ");
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.INT);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(retorno);
                    return nodo;
//</editor-fold>    
                    
                //<editor-fold defaultstate="collapsed" desc="TOCHARARRAY">
                case TOCHARARRAY:
                    resultado = listaExpresiones.get(0).ejecutar(ambito);
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"Semantico");
                    
                    temp = (Nodo)resultado;
                    
                    if(temp.getTipo() != Tipo.STRING && temp.getTipo() != Tipo.WORD){
                        MessageError mensaje = new MessageError("Semantico",l,c,"TOCHARARRAY NECESITA UNA CADENA NO SE RECONOCE: " + temp.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    codigo = Generador.generarComentarioSimple("---------------------------------------- TOCHARARRAY ----------------------------------");
                    contador = Generador.generarTemporal();
                    salida = Generador.generarEtiqueta();
                    valor = Generador.generarTemporal();
                    loop = Generador.generarEtiqueta();
                    String limSuperior = Generador.generarTemporal();
                    String valorTemp = Generador.generarTemporal();
                    result = Generador.generarTemporal();
                    codigo += "\n" + temp.getCodigo3D();
                    
                    codigo += "\n" + Generador.generarCuadruplo("=", temp.getResultado(), "", valorTemp);
                    codigo += "\n" + Generador.generarComentarioSimple("---------------------------- FUNCION LENGTH ------------------------------------------");
                    codigo += "\n" + Generador.generarCuadruplo("=", "0", "", contador);
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "\n" + Generador.guardarAcceso(valor, "Heap", valorTemp);
                    codigo += "\n" + Generador.guardarCondicional(salida, valor, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple(" Si es el final de la cadena salimos del Lopp");
                    codigo += "\n" + Generador.generarCuadruplo("+", contador, "1", contador);
                    codigo += "\n" + Generador.generarCuadruplo("+", valorTemp, "1", valorTemp);
                    codigo += "\n" + Generador.saltoIncondicional(loop);
                    codigo += "\n" + Generador.generarComentarioSimple("---------------------------- FIN FUNCION LENGTH ------------------------------------------");
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "", result);
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", "0", "Heap");
                    codigo += "   " + Generador.generarComentarioSimple(" Limite Inferior");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarCuadruplo("-", contador, "1", limSuperior);
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", limSuperior, "Heap");
                    codigo += "   " + Generador.generarComentarioSimple(" Limite Superior");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    codigo += "\n" + Generador.generarCuadruplo("=", "H", contador, "Heap");
                    codigo += "   " + Generador.generarComentarioSimple(" Tam del Arreglo");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                    
                    loop = Generador.generarEtiqueta();
                    salida = Generador.generarEtiqueta();
                    codigo += "\n" + Generador.generarComentarioSimple("---------------------------- GUARDAR CADENA COMO ARREGLO ------------------------------------------");
                    codigo += "\n" + Generador.guardarEtiqueta(loop);
                    codigo += "\n" + Generador.guardarAcceso(valor, "Heap", temp.getResultado());
                    codigo += "\n" + Generador.guardarCondicional(salida, valor, "0", "=");
                    codigo += "   " + Generador.generarComentarioSimple(" Si es el final de la cadena salimos del Lopp");
                    codigo += "\n" + Generador.generarCuadruplo("=","H", valor, "Heap");
                    codigo += "\n" + Generador.generarCuadruplo("+", "H", "1","H");
                    codigo += "\n" + Generador.generarCuadruplo("+", temp.getResultado(), "1", temp.getResultado());
                    codigo += "\n" + Generador.saltoIncondicional(loop);
                    codigo += "\n" + Generador.generarComentarioSimple("---------------------------- FIN GUARDAR CADENA COMO ARREGLO ------------------------------------------");
                    codigo += "\n" + Generador.guardarEtiqueta(salida);
                    
                    
                    codigo += "\n" + Generador.generarComentarioSimple("----------------------------------------FIN  TOCHARARRAY ----------------------------------");
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.ARRAY);
                    nodo.setTipoArreglo(new Type("",Tipo.CHAR));
                    nodo.setCantidadDimensiones(1);
                    nodo.setResultado(result);
                    nodo.setCodigo3D(codigo);
                    return nodo;
                    //</editor-fold>    
                case LLAMADA:
                    return llamadaMetodos(ambito);
               
            }
        } //------------------------------------------ VALORES PRIMARIOS -----------------------------------------------------------------------------
        else {
            Nodo nodo = new Nodo();
            nodo.setResultado(valor.toString());
            nodo.setTipo(tipo);
            nodo.setCodigo3D("");

            switch (tipo) {
                
                case INT:
                case DOUBLE:
                    return nodo;
                case CHAR:
                    nodo.setResultado(String.valueOf(Character.getNumericValue(valor.toString().charAt(1))));
                    return nodo;
                case NULL:
                    nodo.setResultado("-1");
                    return nodo;
                case STRING:
                    return (valor.toString().length() < 254) ? guardarCadena3D(valor.toString().replaceAll("\"", ""), Tipo.WORD, ambito) : guardarCadena3D(valor.toString().replaceAll("\"", ""), tipo.STRING, ambito);
                    
                case BOOLEAN:
                    
                    if(valor.toString().toLowerCase().equals("true")) nodo.setResultado("1");
                    else nodo.setResultado("0");
                    nodo.setEtiquetaV(null);
                    return nodo;
                case ID:
                    String identificador = valor.toString().toLowerCase();
                    Simbolo s = ambito.getSimbolo(identificador);
                    if(s != null){
                        if(s.getInicializada()){
                             nodo.setTipo(s.getTipo());
                             String tempPos = Generador.generarTemporal();
                             String tempVar = Generador.generarTemporal();
                             codigo = Generador.generarComentarioSimple("Accediendo a la variable(e): " + identificador);
                             
                             if(s.getIsAtributo()){
                                 codigo += "\n" + Generador.generarCuadruplo("+", ambito.getPosPadre(), String.valueOf(s.getPosRelativa()), tempPos);
                                 codigo += "   " + Generador.generarComentarioSimple("Obtenemos la posicion del registro en la posicion: " + s.getPosRelativa());
                                 codigo += "\n" + Generador.guardarAcceso(tempVar,"Heap", tempPos);
                             }
                            else {
                                if (!(s.getAmbito().equalsIgnoreCase("global"))) {
                                    codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(s.getPosRelativa()), tempPos);
                                } else {
                                    codigo += "\n" + Generador.generarCuadruplo("=", String.valueOf(s.getPosStack()), "", tempPos);
                                }
                                codigo += "\n" + Generador.guardarAcceso(tempVar, "Stack", tempPos);
                            }
                             
                             
                             
                             
                             nodo.setResultado(tempVar);
                             nodo.setCodigo3D(codigo);
                             nodo.setValor(s.getValor());
                             nodo.setEstructura(0); //STACK
                             nodo.setTipoArreglo(s.getTipoArreglo());
                             nodo.setId(s.getId());
                             nodo.setCantidadDimensiones(s.getCantidadDimensiones());
                             nodo.setPos(s.getPosStack());
                             return nodo;
                        } else {
                            MessageError mensajeError = new MessageError("Sintactico", l, c, "la variable: " + identificador + " no ha sido inicializada");
                            ambito.addSalida(mensajeError);
                            return mensajeError;
                        }
                       
                    } else {
                        MessageError mensajeError = new MessageError("Sintactico", l, c, " no existe la variable: " + identificador);
                        ambito.addSalida(mensajeError);
                        return mensajeError;
                    }
                   
                   
            }
        }
        return error;
    }

    /**
     * METODO QUE TRADUCE LA CREACION DE UNA CADENA EN EL HEAP
     * @param cadena
     * @param tipo
     * @param ambito
     * @return 
     */
    public static Nodo guardarCadena3D(String cadena, Tipo tipo, Ambito ambito) {
        Nodo nuevo = new Nodo();
        nuevo.setTipo(tipo);
        nuevo.setResultado(Generador.generarTemporal());
        String codigo = Generador.generarComentarioSimple("INICIO GUARDAR CADENA: " + cadena);
        codigo += "\n" + Generador.generarCuadruplo("=", "0", "", nuevo.getResultado());
        
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "0", nuevo.getResultado());
        
        int secuenciaDeEscape = 0;
        for (char c : cadena.toCharArray()) {
            
            /**
             * SECUENCIAS DE ESCAPE
             * \t tabulacion
             * \' '
             * \" "
             * \\ \
             * \? ?
             * \0 null
             * \b retorno
             * \f nueva pagina
             * \n salto de linea
             * \r retorno de carro
             */
            
            if(c == '\\' && secuenciaDeEscape == 0)secuenciaDeEscape = 1;
            else{
                if(secuenciaDeEscape == 1){
                    if(c == 't') c = '\t';
                    else if(c == '\'') c = '\'';
                    else if(c == '\"') c = '\"';
                    else if(c == '?') c = '?';
                    else if(c == '\\') c = '\\';
                    else if(c == '0') c = '\0';
                    else if(c == 'b') c = '\b';
                    else if(c == 'f') c = '\f';
                    else if(c == 'n') c = '\n';
                    else  c = '\r';

                    secuenciaDeEscape = 0;
                }
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", String.valueOf((int) c));
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
            }
            
        }
        codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", "0");
        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
        codigo += "\n" + Generador.generarComentarioSimple("FIN GUARDAR CADENA: " + cadena);
        nuevo.setCodigo3D(codigo);
        return nuevo;
    }

    /**
     * METODO QUE CONCATENA DOS EXPRESIONES Y LAS GUARDA EN EL HEAP
     * @param cadena
     * @param tipo
     * @param ambito 
     */
    private String concatenar(String cadena, Tipo tipo,Ambito ambito) {
        String codigo = "";
        switch (tipo) {
            case INT:
            case DOUBLE:
                String pos = Generador.generarTemporal();
                codigo = Generador.generarComentarioSimple("Se llama al metodo NumeroToCadena");
                codigo += "\n" + Generador.generarCuadruplo("+", "P",String.valueOf(ambito.getTam()) , "P");
                codigo += "  " + Generador.generarComentarioSimple(" Simulamos el cambio de ambito");
                codigo += "\n" + Generador.generarCuadruplo("+", "P", "0", pos);
                codigo += "\n" + Generador.generarCuadruplo("=", pos, cadena, "Stack");
                codigo += "  " + Generador.generarComentarioSimple(" Pasamos " + cadena + " como parametro");
                codigo += "\n" + Generador.llamarAFuncion("numeroToCadena");
                
                codigo += "\n" + Generador.generarCuadruplo("-", "P",String.valueOf(ambito.getTam()) , "P");
                codigo += "  " + Generador.generarComentarioSimple(" FIN Simulamos el cambio de ambito");
                codigo += "\n" + Generador.generarComentarioSimple("FIN Se llama al metodo NumeroToCadena");
                
                
                break;
                
            case CHAR:
                codigo += "\n" + Generador.generarComentarioSimple("Se adjunta el char: " + cadena);
                int codigoAscii = (int) cadena.charAt(1);
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", String.valueOf(codigoAscii));
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                break;
                
            case WORD:
            case STRING:
                codigo += "\n" + Generador.generarComentarioSimple("Se adjunta la cadena: " + cadena);
                String temporalAux = Generador.generarTemporal();
                String EtiquetaFinal = Generador.generarEtiqueta();
                String EtiquetaInicio = Generador.generarEtiqueta();
                codigo += "\n" + Generador.guardarEtiqueta(EtiquetaInicio);
                codigo += "\n" + Generador.guardarAcceso(temporalAux, "Heap", cadena);
                codigo += "\n" + Generador.generarComentarioSimple("Si no es el fin de la cadena guardar el valor");
                codigo += "\n" + Generador.guardarCondicional(EtiquetaFinal,temporalAux, "0", "=");
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", temporalAux);
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                codigo += "\n" + Generador.generarComentarioSimple("Aumentar el contador que lleva la posicion del Heap");
                codigo += "\n" + Generador.generarCuadruplo("+", cadena, "1", cadena);
                codigo += "\n" + Generador.saltoIncondicional(EtiquetaInicio);
                codigo += "\n" + Generador.guardarEtiqueta(EtiquetaFinal);
                break;
            
        }
        return codigo;
    }


    
    /**
     * METODO QUE SE ENCARGA DE GENERAR EL 3D DE >,<,>=,<=
     * @param operacion
     * @param texto
     * @param izq
     * @param der
     * @param ambito
     * @return 
     */
    private Object generarRelacional(String operacion,String texto, Nodo izq, Nodo der, Ambito ambito){
        Nodo nodo = new Nodo();
        nodo.setTipo(Tipo.BOOLEAN);
        nodo.addEtiquetaV(Generador.generarEtiqueta());
        nodo.addEtiquetaF(Generador.generarEtiqueta());
        
        if (izq.getTipo() == Tipo.INT && der.getTipo() == Tipo.DOUBLE
                || izq.getTipo() == Tipo.DOUBLE && der.getTipo() == Tipo.INT
                || izq.getTipo() == Tipo.DOUBLE && der.getTipo() == Tipo.DOUBLE
                || izq.getTipo() == Tipo.INT && der.getTipo() == Tipo.INT
                || izq.getTipo() == Tipo.DOUBLE && der.getTipo() == Tipo.CHAR
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.DOUBLE
                || izq.getTipo() == Tipo.INT && der.getTipo() == Tipo.CHAR
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.INT
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.CHAR) {
            String codigo = izq.getCodigo3D();
            codigo += "\n" + der.getCodigo3D();
            codigo += "\n" + guardarValorBoolean(nodo,izq,der,operacion);
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        else{
            MessageError mensaje = new MessageError("Semantico",l,c,"No se puede conocer el: " + operacion + " de los tipo: " + izq.getTipo() + " con: " + der.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
            
        }        
    }
    
    /**
     * METODO QUE SE ENCARGA DE GENERAR EL 3D DE = <>
     * @param operacion
     * @param texto
     * @param izq
     * @param der
     * @param ambito
     * @return 
     */
    public  Object generarRelacionalEspecial(String operacion,String texto, Nodo izq, Nodo der, Ambito ambito){
        Nodo nodo = new Nodo();
        nodo.setTipo(Tipo.BOOLEAN);
        nodo.addEtiquetaV(Generador.generarEtiqueta());
        nodo.addEtiquetaF(Generador.generarEtiqueta());
        //---------------------------------------------- VALORES NUMERICOS -----------------------------------------------------------------
        if (izq.getTipo() == Tipo.INT && der.getTipo() == Tipo.DOUBLE
                || izq.getTipo() == Tipo.DOUBLE && der.getTipo() == Tipo.INT
                || izq.getTipo() == Tipo.DOUBLE && der.getTipo() == Tipo.DOUBLE
                || izq.getTipo() == Tipo.INT && der.getTipo() == Tipo.INT
                || izq.getTipo() == Tipo.DOUBLE && der.getTipo() == Tipo.CHAR
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.DOUBLE
                || izq.getTipo() == Tipo.INT && der.getTipo() == Tipo.CHAR
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.INT
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.CHAR) {
            
            String codigo = izq.getCodigo3D();
            codigo += "\n" + der.getCodigo3D();
            codigo += "\n" + guardarValorBoolean(nodo,izq,der,operacion);
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        else if(izq.getTipo() == Tipo.BOOLEAN && der.getTipo() == Tipo.BOOLEAN){
            String codigo = izq.getCodigo3D();
            
            codigo += "\n" + der.getCodigo3D();
            codigo += "\n" + validarBoolean(izq);
            codigo += "\n" + validarBoolean(der);
            codigo += "\n" + guardarValorBoolean(nodo, izq, der, operacion);
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        //---------------------------------------------------- CADENAS -----------------------------------------------------------------------
        else if(
                izq.getTipo() == Tipo.STRING && der.getTipo() == Tipo.STRING
                || izq.getTipo() == Tipo.WORD && der.getTipo() == Tipo.STRING
                || izq.getTipo() == Tipo.STRING && der.getTipo() == Tipo.WORD
                || izq.getTipo() == Tipo.WORD && der.getTipo() == Tipo.WORD){
            String codigo = izq.getCodigo3D();
            codigo += "\n" + der.getCodigo3D();
            codigo += "\n" + compararCadenas(nodo,izq,der,operacion);
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        else if(izq.getTipo() == Tipo.NULL && der.getTipo() == Tipo.REGISTRO){
            String codigo = der.getCodigo3D();
            codigo += "\n" + Generador.guardarCondicional(nodo.getEtiquetaV().get(0), "-1", der.getResultado(), operacion);
            codigo += "\n" + Generador.saltoIncondicional(nodo.getEtiquetaF().get(0));
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        
        else if(izq.getTipo() == Tipo.REGISTRO && der.getTipo() == Tipo.NULL){
            String codigo = izq.getCodigo3D();
            codigo += "\n" + Generador.guardarCondicional(nodo.getEtiquetaV().get(0), "-1", izq.getResultado(), operacion);
            codigo += "\n" + Generador.saltoIncondicional(nodo.getEtiquetaF().get(0));
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        else{
            MessageError mensaje = new MessageError("Semantico",l,c,"No se puede conocer el: " + operacion + " de los tipo: " + izq.getTipo() + " con: " + der.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
            
        }        
    }
    
    /**
     * METODO QUE GUARDA EN LAS ETIQUETAS LOS POSIBLES VALORES DE UN VALOR NULL
     * @param nodo
     * @param izq
     * @param der
     * @param operacion
     * @return 
     */
    private String guardarValorBoolean(Nodo nodo,Nodo izq, Nodo der, String operacion){
        String codigo = "";
       
        codigo += "\n" + Generador.generarComentarioSimple("Verificar si las expresiones son : " + operacion);
        codigo += "\n" + Generador.guardarCondicional(nodo.getEtiquetaV().get(0), izq.getResultado(), der.getResultado(), operacion);
        codigo += "\n" + Generador.saltoIncondicional(nodo.getEtiquetaF().get(0));
        codigo += "\n" + Generador.generarComentarioSimple("FIN Verificar si las expresiones son : " + operacion);
        return codigo;
    }
    
    /**
     * METODO QUE SE ENCARGA DE COMPARA DOS CADENAS
     * @param nodo
     * @param izq
     * @param der
     * @param operacion
     * @return 
     */
    private String compararCadenas(Nodo nodo, Nodo izq, Nodo der, String operacion){
        String codigo = "";
        codigo += "\n" + Generador.generarComentarioSimple("Verificar si las expresiones son : " + operacion);
        String verdaderaTemporal = Generador.generarEtiqueta();
        String ciclo = Generador.generarEtiqueta();
        String temporalIzq = Generador.generarTemporal();
        String temporalDer = Generador.generarTemporal();
        String valorDer = Generador.generarTemporal();
        String valorIzq = Generador.generarTemporal();
        /*----------------------------------------------------- VERIFICAR SI UN CARACTER ES IGUAL AL OTRO -------------------------------------------------
        ---------------------------------------------------------- SI ES VERDADERO SALTAMOS  A VERIFICAR SI ES EL FIN DE LA CADENA ------------------------
        ---------------------------------------------------------- SI ES FALSO SALTAMOS A LA SALUDA DE UNA VEZ ------------------------------------------*/
        codigo += "\n" + Generador.generarComentarioSimple("Validando Caracter por Caracter : " + operacion);
        codigo += "\n" + Generador.generarCuadruplo("+", izq.getResultado(), "0", temporalIzq);
        codigo += "\n" + Generador.generarCuadruplo("+", der.getResultado(), "0", temporalDer);
        codigo += "\n" + Generador.guardarEtiqueta(ciclo);
        codigo += "\n" + Generador.guardarAcceso(valorIzq, "Heap", temporalIzq);
        codigo += "\n" + Generador.guardarAcceso(valorDer, "Heap", temporalDer);
        codigo += "\n" + Generador.generarComentarioSimple("Si son iguales validamos si estamos al final. si no son iguales saltamos a nuestra falsa : " + operacion);
        codigo += "\n" + Generador.guardarCondicional(verdaderaTemporal, valorIzq, valorDer, operacion);
        codigo += "\n" + Generador.saltoIncondicional(nodo.getEtiquetaF().get(0));
        
        //------------------------------------------------------- VERIFICAR SI ES EL FIN DE LA CADENA ----------------------------------------------------
        codigo += "\n" + Generador.generarComentarioSimple("Verificar si estamos al final de la cadena : " + operacion);
        codigo += "\n" + Generador.guardarEtiqueta(verdaderaTemporal);
        codigo += "\n" + Generador.guardarCondicional(nodo.getEtiquetaV().get(0),valorIzq , "0", operacion);
        codigo += "\n" + Generador.generarCuadruplo("+", temporalIzq, "1", temporalIzq);
        codigo += "\n" + Generador.generarCuadruplo("+", temporalDer, "1", temporalDer);
        codigo += "\n" + Generador.saltoIncondicional(ciclo);
        
        
        
        codigo += "\n" + Generador.generarComentarioSimple("FIN Verificar si las expresiones son : " + operacion);
        return codigo;
    }
    
    /**
     * METODO QUE SE ENCARGA DE CONTROLAR UN BOOLEAN
     * @param nodo
     * @return 
     */
    private String validarBoolean(Nodo nodo){
        String codigo = "";
        String salto = Generador.generarEtiqueta();
        if(nodo.getEtiquetaV() != null){
            nodo.setResultado(Generador.generarTemporal());
            codigo += Generador.getAllEtiquetas(nodo.getEtiquetaV());
            codigo += "\n" + Generador.generarCuadruplo("=", "1", "", nodo.getResultado());
            codigo += "\n" + Generador.saltoIncondicional(salto);
            codigo += "\n" + Generador.getAllEtiquetas(nodo.getEtiquetaF());
            codigo += "\n" + Generador.generarCuadruplo("=", "0", "", nodo.getResultado());
            codigo += "\n" + Generador.guardarEtiqueta(salto);
        }
        return codigo;
    }

    /**
     * LLAMADA A METODOS O FUNCIONES
     * @param ambito
     * @return 
     */
    private Object llamadaMetodos(Ambito ambito){
        Nodo nodo = new Nodo();
        id = id.toLowerCase();
        
        String codigo = "";
        LinkedList<Nodo> valores = new LinkedList<>();
        LinkedList<String> listaValores = new LinkedList<>();
        
        for(Expresion e : listaExpresiones){
            Object resultado = e.ejecutar(ambito);
            if(resultado instanceof MessageError) return new MessageError("",l,c,"");
            
            Nodo temp = (Nodo)resultado;
            valores.addLast(temp);
        }
        
        
        Funcion funcion = ambito.buscarFuncionLlamada(id.toLowerCase(), valores);
        if(funcion == null){
            
            System.out.println("ERROR:" +  id + " con: " + ambito.getId());
            MessageError mensaje = new MessageError("Semantico", l,c,"No se encuetra la funcion/procedure: " + id );
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        
        
        codigo += " \n" + Generador.generarComentarioSimple("  VALORES A PASAR");
        for(int i = 0; i < valores.size(); i++){
            codigo += "\n" + valores.get(i).getCodigo3D();
            if(valores.get(i).getTipo() == Tipo.BOOLEAN){
                if(valores.get(i).getEtiquetaV() != null){
                    String temporal = Generador.generarTemporal();
                    valores.get(i).setResultado(temporal);
                    
                    codigo += "\n" + Generador.getAllEtiquetas(valores.get(i).getEtiquetaV());
                    codigo += "\n" + Generador.generarCuadruplo("=", "1", "", temporal);
                    codigo += "\n" + Generador.getAllEtiquetas(valores.get(i).getEtiquetaF());
                    codigo += "\n" + Generador.generarCuadruplo("=", "0", "", temporal);
                }
            }
            listaValores.addLast(valores.get(i).getResultado());
        }
        codigo += " \n" + Generador.generarComentarioSimple(" FIN VALORES A PASAR");
        codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(ambito.getTam()), "P");
        codigo += "  " + Generador.generarComentarioSimple("  SIMULACION DE CAMBIO DE AMBITO");
        
        //----------------------------------------- GUARDAR EL CODIGO DE LOS VALORES A PASAR ------------------------------------------------------
        int contador = 0;
        for(int i = 0; i < funcion.getListaParametros().size(); i++){
            for(int j = 0; j < funcion.getListaParametros().get(i).getLista().size(); j++){
                String pos = Generador.generarTemporal();
                codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(contador), pos);
                codigo += "\n" + Generador.generarCuadruplo("=", pos, listaValores.get(contador), "Stack");
                codigo += " " + Generador.generarComentarioSimple(" GUARDAMOS EL VALOR EN EL PARAMETRO: " + i + j + 1);
                contador++;
            }
            
        }
        if(funcion.getTipo().getTipo() != Tipo.VOID){
            String posRetorno = Generador.generarTemporal();
            codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(funcion.getPosRelativaRetorno()), posRetorno);
            codigo += "\n" + Generador.generarCuadruplo("=", posRetorno, "-1", "Stack");
            codigo += "   " + Generador.generarComentarioSimple("       APARTAMOS EL STACK PARA EL RETORNO");
        }
        //------------------------------------------ LLAMAMOS LA FUNCION --------------------------------------------------------------------------
        codigo += "\ncall,,," + funcion.getIdentificador();
        
        contador = 0;
        
        for(int i = 0; i < funcion.getListaParametros().size(); i++){
            Parametro temp = funcion.getListaParametros().get(i);
            for(int j = 0; j < funcion.getListaParametros().get(i).getLista().size(); j++){
                if(temp.getReferencia()){
                    String nombreParametro = funcion.getListaParametros().get(i).getLista().get(j);
                    Nodo referencia = valores.get(contador);
                    String tempValor = Generador.generarTemporal();
                        String posTemporal = Generador.generarTemporal();
                        codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(contador), posTemporal);
                        codigo += "\n" + Generador.guardarAcceso(tempValor,"Stack",posTemporal);
                        codigo += "   " + Generador.generarComentarioSimple(" Obtenemos el valor del parametro: " + nombreParametro + " es a referencia");
                    //---------------------------------- HEAP --------------------------------------------
                    if(referencia.getEstructura() == 1)codigo += "\n" + Generador.generarCuadruplo("=", referencia.getPosHeap(), tempValor, "Heap");
                    //---------------------------------- STACK ------------------------------------------
                    else  codigo += "\n" + Generador.generarCuadruplo("=", String.valueOf(referencia.getPos()), tempValor, "Stack");

                    codigo += "   " + Generador.generarComentarioSimple(" Guardamos la referencia de regreso");
                    
                }
                contador++;
            }
            
        }
        
        
        
        
        
        
        if(funcion.getTipo().getTipo() != Tipo.VOID){
            String posRetorno = Generador.generarTemporal();
            String retorno = Generador.generarTemporal();
             codigo += "\n" + Generador.generarCuadruplo("+","P", String.valueOf(funcion.getPosRelativaRetorno()), posRetorno);
             codigo += "\n" + Generador.guardarAcceso(retorno, "Stack", posRetorno );
             nodo.setResultado(retorno);
        }
        
       
        codigo += "\n" + Generador.generarCuadruplo("-", "P", String.valueOf(ambito.getTam()), "P");
        codigo += "  " + Generador.generarComentarioSimple(" FIN SIMULACION DE CAMBIO DE AMBITO");
        nodo.setCodigo3D(codigo);
        
        nodo.setTipo(funcion.getTipo().tipo);
        return nodo;
    }

    
}


