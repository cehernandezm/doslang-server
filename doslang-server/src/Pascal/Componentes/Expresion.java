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
import Pascal.Analisis.TipoDato;

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
            Nodo nodoDer = (Nodo) op2;
            codigo = "";
            switch (operacion) {
                case SUMA:
                    //-------------------------------- INT + INT --------------------------------------------------------
                    if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.INT) {
                        String temp = Generador.generarTemporal();
                        codigo = Generador.generarCuadruplo("+", nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        return resultado;
                    } //------------------------------- INT DOUBLE | DOUBLE INT | DOUBLE DOUBLE -----------------------------------
                    else if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.DOUBLE || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.DOUBLE) {
                        String temp = Generador.generarTemporal();
                        codigo = Generador.generarCuadruplo("+", nodoIzq.getResultado(), nodoDer.getResultado(), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.DOUBLE);
                        resultado.setResultado(temp);
                        return resultado;
                    } //-------------------------- CHAR INT -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.INT) {
                        String temp = Generador.generarTemporal();
                        int codigoAscii = (int) nodoIzq.getResultado().charAt(1);
                        codigo = Generador.generarCuadruplo("+", String.valueOf(codigoAscii), nodoDer.getResultado(), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        return resultado;
                    } //-------------------------- INT CHAR -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        int codigoAscii = (int) nodoDer.getResultado().charAt(1);
                        codigo = Generador.generarCuadruplo("+", nodoIzq.getResultado(), String.valueOf(codigoAscii), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        return resultado;
                    } //-------------------------- CHAR CHAR -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        int codigoAscii = (int) nodoDer.getResultado().charAt(1);
                        int codigoAscii2 = (int) nodoIzq.getResultado().charAt(1);
                        codigo = Generador.generarCuadruplo("+", String.valueOf(codigoAscii2), String.valueOf(codigoAscii), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.INT);
                        resultado.setResultado(temp);
                        return resultado;
                    } //-------------------------- CHAR DOUBLE -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.CHAR && nodoDer.getTipo() == Tipo.DOUBLE) {
                        String temp = Generador.generarTemporal();
                        int codigoAscii = (int) nodoIzq.getResultado().charAt(1);
                        codigo = Generador.generarCuadruplo("+", String.valueOf(codigoAscii), nodoDer.getResultado(), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.DOUBLE);
                        resultado.setResultado(temp);
                        return resultado;
                    } //-------------------------- INT CHAR -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.CHAR) {
                        String temp = Generador.generarTemporal();
                        int codigoAscii = (int) nodoDer.getResultado().charAt(1);
                        codigo = Generador.generarCuadruplo("+", nodoIzq.getResultado(), String.valueOf(codigoAscii), temp);
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.DOUBLE);
                        resultado.setResultado(temp);
                        return resultado;
                    } //-------------------------- WORD X | X WORD | X STRING | STRING X -------------------------------------------------------------------------
                    else if (nodoIzq.getTipo() == Tipo.WORD || nodoDer.getTipo() == Tipo.WORD || nodoIzq.getTipo() == Tipo.STRING || nodoDer.getTipo() == Tipo.STRING) {
                        Tipo sendTipo = (nodoIzq.getTipo() == Tipo.STRING && nodoDer.getTipo() == Tipo.STRING) ? Tipo.STRING : Tipo.WORD;
                        Nodo resultado = new Nodo();
                        resultado.setTipo(sendTipo);
                        String temp = Generador.generarTemporal();
                        resultado.setResultado(temp);
                        codigo = Generador.generarComentarioSimple("INICIO Concatenacion: ");
                        codigo += "\n" + Generador.generarCuadruplo("+", "H", "0", temp);
                        codigo += "\n" + Generador.generarComentarioSimple("Reservando nuevo espacio: ");
                        ambito.addCodigo(codigo);
                        concatenar(nodoIzq.getResultado(),nodoIzq.getTipo(),ambito);
                        concatenar(nodoDer.getResultado(),nodoDer.getTipo(),ambito);
                        codigo = Generador.generarComentarioSimple("FIN Concatenacion: ");
                        codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", "0");
                        codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                        ambito.addCodigo(codigo);
                        return resultado;
                        
                    }

                    break;

                case RESTA:
                case MULTIPLICACION:
                case DIVISION:
                    //------------------------------------ INT DOUBLE | DOUBLE INT | DOUBLE DOUBLE ------------
                    if (nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.DOUBLE || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.INT
                            || nodoIzq.getTipo() == Tipo.DOUBLE && nodoDer.getTipo() == Tipo.DOUBLE) {
                        String temp = Generador.generarTemporal();
                        String simbolo = "";
                        if (operacion == Operacion.RESTA) {
                            simbolo = "-";
                        } else if (operacion == Operacion.MULTIPLICACION) {
                            simbolo = "*";
                        } else {
                            simbolo = "/";
                        }
                        codigo = simbolo + "," + nodoIzq.getResultado() + "," + nodoDer.getResultado() + "," + temp;
                        ambito.addCodigo(codigo);
                        Nodo resultado = new Nodo();
                        resultado.setTipo(Tipo.DOUBLE);
                        resultado.setResultado(temp);
                        return resultado;
                    }
                    break;
            }
        } //------------------------------------------ VALORES PRIMARIOS -----------------------------------------------------------------------------
        else {
            Nodo nodo = new Nodo();
            nodo.setResultado(valor.toString());
            nodo.setTipo(tipo);

            switch (tipo) {

                case INT:
                case DOUBLE:
                case CHAR:
                    return nodo;
                case STRING:
                    return (valor.toString().length() < 254) ? guardarCadena3D(valor.toString().replaceAll("\"", ""), Tipo.WORD, ambito) : guardarCadena3D(valor.toString().replaceAll("\"", ""), tipo.STRING, ambito);
            }
        }
        return error;
    }

    private Nodo guardarCadena3D(String cadena, Tipo tipo, Ambito ambito) {
        Nodo nuevo = new Nodo();
        nuevo.setTipo(tipo);
        nuevo.setResultado(Generador.generarTemporal());
        String codigo = Generador.generarComentarioSimple("INICIO GUARDAR CADENA: " + cadena);
        ambito.addCodigo(codigo);
        codigo = Generador.generarCuadruplo("+", "H", "0", nuevo.getResultado());
        ambito.addCodigo(codigo);
        for (char c : cadena.toCharArray()) {
            codigo = Generador.guardarEnPosicion("Heap", "H", String.valueOf((int) c));
            ambito.addCodigo(codigo);
            codigo = Generador.generarCuadruplo("+", "H", "1", "H");
            ambito.addCodigo(codigo);
        }
        codigo = Generador.guardarEnPosicion("Heap", "H", "0");
        ambito.addCodigo(codigo);
        codigo = Generador.generarCuadruplo("+", "H", "1", "H");
        ambito.addCodigo(codigo);
        codigo = Generador.generarComentarioSimple("FIN GUARDAR CADENA: " + cadena);
        ambito.addCodigo(codigo);
        return nuevo;
    }

    private void concatenar(String cadena, Tipo tipo, Ambito ambito) {
        String codigo = "";
        switch (tipo) {
            case INT:
            case DOUBLE:
                codigo = Generador.guardarEnPosicion("Heap", "H", "-777.777"); // numero para separar el anterior fin de la cadena es decir el 0 que guardo para finalizar se adjunta el -777.77 que seria concatenar
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                codigo += "\n" + Generador.generarComentarioSimple("Se adjunta el numero: " + cadena);
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", cadena);
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                ambito.addCodigo(codigo);
                break;
                
            case CHAR:
                codigo += "\n" + Generador.generarComentarioSimple("Se adjunta el char: " + cadena);
                int codigoAscii = (int) cadena.charAt(1);
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", String.valueOf(codigoAscii));
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                ambito.addCodigo(codigo);
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
                codigo += "\n" + Generador.guardarCondicional(EtiquetaFinal,temporalAux, "0", "==");
                codigo += "\n" + Generador.guardarEnPosicion("Heap", "H", temporalAux);
                codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
                codigo += "\n" + Generador.generarComentarioSimple("Aumentar el contador que lleva la posicion del Heap");
                codigo += "\n" + Generador.generarCuadruplo("+", cadena, "1", cadena);
                codigo += "\n" + Generador.saltoIncondicional(EtiquetaInicio);
                codigo += "\n" + Generador.guardarEtiqueta(EtiquetaFinal);
                ambito.addCodigo(codigo);
                break;
        }
    }
}
