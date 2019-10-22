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
import Pascal.Componentes.Arreglos.AccesoArreglo;
import Pascal.Componentes.Registros.Atributo;
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
     * CONSTRUCTOR PARA ACCEDER A UN ARREGLO
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
                        else codigo += "\n" + concatenar(nodoIzq.getResultado(), nodoIzq.getTipo());
                        
                        if (nodoDer.getTipo() == Tipo.BOOLEAN) codigo += "\n" + Generador.concatenarBoolean(nodoDer);
                        else codigo += "\n" + concatenar(nodoDer.getResultado(), nodoDer.getTipo());
                        
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
                        codigo +=  nodoDer.getCodigo3D();
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
                        codigo +=  nodoDer.getCodigo3D();
                        codigo += "\n" + Generador.generarCuadruplo(simbolo, nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        resultado.setCodigo3D("");
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
                    //<editor-fold defaultstate="collapsed" desc="comment">
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
                        nodo.setTipo(atributo.getTipo().getTipo());
                        nodo.setCodigo3D(codigo);
                        nodo.setResultado(value);
                        return nodo;
                    }
                    else {
                        MessageError mensaje = new MessageError("Semantico", l, c, "No se puede acceder a un atributo del tipo: " + nodoIzq.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    break;
//</editor-fold>
            
                //<editor-fold defaultstate="collapsed" desc="ID [EXP(,EXP)+]">
                case ACCESOARRAY:
                    id = id.toLowerCase();
                    Simbolo sim = ambito.getSimbolo(id);
                    //-------------------------------------------- Si no existe ------------------------------------------------------------------
                    if(sim == null){
                        MessageError mensaje = new MessageError("Semantico",l,c,"No existe la variable: " + id);
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    //----------------------------------------- SI NO ES UN ARREGLO --------------------------------------------------------------
                    if(sim.getTipo() != Tipo.ARRAY){
                        MessageError mensaje = new MessageError("Semantico",l,c,id + " No es de tipo Arreglo, no se reconoce el tipo: " +  sim.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    String etiquetaSalto = Generador.generarEtiqueta();
                    Object resultado = AccesoArreglo.obtenerMapeoLexicoGrafico(sim, ambito, etiquetaSalto, listaExpresiones, l, c);
                    if(resultado instanceof MessageError) return new MessageError("",l,c,"");
                    
                    
                    Nodo temp = (Nodo)resultado;
                    codigo = temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("-------- SI NO EXISTEN LOS INDICES");
                    codigo += "\n" + Generador.guardarEtiqueta(etiquetaSalto);
                    String res = Generador.generarTemporal();
                    codigo += "\n" + Generador.guardarAcceso(res, "Heap", temp.getResultado());
                    Nodo nodo = new Nodo();
                    nodo.setTipo(sim.getTipoArreglo());
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(res);
                    return nodo;
//</editor-fold>
                    
                    
                //<editor-fold defaultstate="collapsed" desc="SIZEOF">
                case SIZEOF:
                    id = id.toLowerCase();
                    Simbolo simbolo = ambito.getSimbolo(id);
                    //---------------------------------------------------------- Si no existe -----------------------------------------------------------
                    if(simbolo == null){
                        MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + id + " no existe");
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    //------------------------------------------------------- SI NO ES DE TIPO REGISTRO ------------------------------------------------
                    if(simbolo.getTipo() != Tipo.REGISTRO){
                        MessageError mensaje = new MessageError("Semantico",l,c,"Sizeof solo se puede aplicar al tipo Record no se reconoce: " + simbolo.getTipo());
                        ambito.addSalida(mensaje);
                        return mensaje;
                    }
                    
                    codigo = Generador.generarComentarioSimple("---------------------- Empezamos a contar la cantidad de atributos del registro: " + simbolo.getId());
                    LinkedList<Atributo> lista = (LinkedList<Atributo>) simbolo.getValor();
                    String contador = Generador.generarTemporal();
                    codigo += "\n" + Generador.generarCuadruplo("=", "1","", contador);
                    for(int i = 1; i < lista.size(); i++ ){
                        codigo += "\n" + Generador.generarCuadruplo("+", contador,"1", contador);
                    }
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.INT);
                    nodo.setCodigo3D(codigo);
                    nodo.setResultado(contador);
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
                    
                    nodo = new Nodo();
                    nodo.setTipo(Tipo.MALLOC);
                    return nodo;
//</editor-fold>
                    
                    
                    
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
                    nodo.setResultado(String.valueOf((int)valor.toString().charAt(l)));
                    return nodo;
                case NULL:
                    nodo.setResultado("0");
                    return nodo;
                case STRING:
                    return (valor.toString().length() < 254) ? guardarCadena3D(valor.toString().replaceAll("\"", ""), Tipo.WORD, ambito) : guardarCadena3D(valor.toString().replaceAll("\"", ""), tipo.STRING, ambito);
                    
                case BOOLEAN:
                    if(valor.toString().toLowerCase().equals("true")) nodo.setResultado("1");
                    else nodo.setResultado("0");
                    return nodo;
                case ID:
                    String identificador = valor.toString().toLowerCase();
                    Simbolo s = ambito.getSimbolo(identificador);
                    if(s != null){
                        if(s.getInicializada()){
                             nodo.setTipo(s.getTipo());
                             String tempPos = Generador.generarTemporal();
                             String tempVar = Generador.generarTemporal();
                             codigo = Generador.generarComentarioSimple("Accediendo a la variable: " + identificador);
                             codigo += "\n" + Generador.generarCuadruplo("+", "P", String.valueOf(s.getPosRelativa()), tempPos);
                             codigo += "\n" + Generador.guardarAcceso(tempVar, "Stack", tempPos);
                             codigo += "\n";
                             nodo.setResultado(tempVar);
                             nodo.setCodigo3D(codigo);
                             nodo.setValor(s.getValor());
                             nodo.setTipoArreglo(s.getTipoArreglo());
                             nodo.setId(s.getId());
                             nodo.setCantidadDimensiones(s.getCantidadDimensiones());
                             return nodo;
                        } else {
                            MessageError mensajeError = new MessageError("Sintactico", l, c, "la variable: " + identificador + " no ha sido inicializada");
                            ambito.addSalida(mensajeError);
                        }
                       
                    } else {
                        MessageError mensajeError = new MessageError("Sintactico", l, c, " no existe la variable: " + identificador);
                        ambito.addSalida(mensajeError);
                    }
                   
                    break;
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
        for (char c : cadena.toCharArray()) {
            codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", String.valueOf((int) c));
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
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
    private String concatenar(String cadena, Tipo tipo) {
        String codigo = "";
        switch (tipo) {
            case INT:
            case DOUBLE:
                codigo = Generador.guardarEnPosicion("Heap", "H", "-777.777"); // numero para separar el anterior fin de la cadena es decir el 0 que guardo para finalizar se adjunta el -777.77 que seria concatenar
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                codigo += "\n" + Generador.generarComentarioSimple("Se adjunta el numero: " + cadena);
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", cadena);
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                
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
    private Object generarRelacionalEspecial(String operacion,String texto, Nodo izq, Nodo der, Ambito ambito){
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
                || izq.getTipo() == Tipo.CHAR && der.getTipo() == Tipo.CHAR
                || izq.getTipo() == Tipo.BOOLEAN && der.getTipo() == Tipo.BOOLEAN) {
            
            String codigo = izq.getCodigo3D();
            codigo += "\n" + der.getCodigo3D();
            codigo += "\n" + guardarValorBoolean(nodo,izq,der,operacion);
            nodo.setCodigo3D(codigo);
            return nodo;
        }
        //---------------------------------------------------- CADENAS -----------------------------------------------------------------------
        else if(
                izq.getTipo() == Tipo.STRING && der.getTipo() == Tipo.STRING
                || izq.getTipo() == Tipo.WORD && der.getTipo() == Tipo.STRING
                || izq.getTipo() == Tipo.WORD && der.getTipo() == Tipo.WORD){
            String codigo = izq.getCodigo3D();
            codigo += "\n" + der.getCodigo3D();
            codigo += "\n" + compararCadenas(nodo,izq,der,operacion);
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
}


