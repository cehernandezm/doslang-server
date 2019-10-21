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
import Pascal.Componentes.UserTypes.Equivalencia;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Declaracion  implements Instruccion {
    String id;
    Expresion valor;
    int l;
    int c;
    Boolean constante;
    LinkedList<String> lista;
    Type tipo;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param valor
     * @param l
     * @param c
     * @param constante 
     */
    public Declaracion(String id, Expresion valor, int l, int c, Boolean constante, Type tipo) {
        this.id = id.toLowerCase();
        this.valor = valor;
        this.l = l;
        this.c = c;
        this.constante = constante;
        this.tipo = tipo;
    }

    public Declaracion(Expresion valor, int l, int c, Boolean constante, Type tipo,LinkedList<String> lista) {
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
        Boolean resultado = buscarTipo(ambito);
        if(!resultado){
            MessageError mensajeError = new MessageError("Semantico",l,c,"No existe el type: " + tipo.getId());
            ambito.addSalida(mensajeError);
            return mensajeError;
        }
        if(lista == null){
            lista = new LinkedList<String>();
            lista.addLast(id);
        }
        return guardarListaVariables(ambito,dato);
        
   }

    
    /**
     * METODO QUE SE ENCARGA DE ALMACENAR UNA O MAS VARIABLES
     * @param ambito
     * @param dato
     * @return 
     */
    private Object guardarListaVariables(Ambito ambito, Object dato) {
        
        Nodo nodo = new Nodo();
        //------------------------------------ SI LA EXPRESION TRAE UN ERROR NO SE ALMACENA LA VARIABLE
        if (!(dato instanceof MessageError)) nodo = (Nodo) dato;
        else return new MessageError("",l,c,"");
        
        //----------------------------------------------------- SI NO ES NULL Y ES UN BOOLEAN HAY QUE OBTENER SUS ETIQUETAS DE VERDADERO Y FALSO
        if (dato != null) {
            ambito.addCodigo(nodo.getCodigo3D());
            if (nodo.getTipo() == Tipo.BOOLEAN) ambito.addCodigo(Generador.generarBoolean(Generador.generarTemporal(), nodo));       
        }
        
        //------------------------------------------- SI ES UN ARREGLO NO PUEDE SER IGUAL A UNA EXPRESION --------------------------
        if (tipo.getTipo() == Tipo.ARRAY && dato != null ) {
            MessageError er = new MessageError("Semantico", l, c, "Un arreglo no puede ser igualado a una expresion");
            ambito.addSalida(er);
            return er;
        }
        
         //------------------------------------------- SI ES UN REGISTRO NO PUEDE SER IGUAL A UNA EXPRESION --------------------------
        if (tipo.getTipo() == Tipo.REGISTRO && dato != null ) {
            MessageError er = new MessageError("Semantico", l, c, "Un Registro no puede ser igualado a una expresion");
            ambito.addSalida(er);
            return er;
        }
        
       
        for (String s : lista) {
            s = s.toLowerCase();
            //----------------------------------- NO SE INICIALIZO LA VARIABLE ------------------------------------------------------
            if (dato == null) {
                if(tipo.getTipo() == Tipo.ARRAY){
                    Object res = ((Instruccion)tipo.getValor()).ejecutar(ambito);
                    if(res instanceof MessageError) return new MessageError("",l,c,"");
                    
                    Nodo temp = (Nodo)res;
                    String codigo = temp.getCodigo3D();
                    Simbolo sim = new Simbolo(s,constante,true,Tipo.ARRAY,Generador.generarStack(),ambito.getRelativa());
                    sim.setTipoArreglo(temp.getTipo());
                    sim.setCantidadDimensiones(temp.getCantidadDimensiones());
                    Boolean resultado = ambito.addSimbolo(sim);
                    //----------------------------------------------- SI YA EXISTE ----------------------------------------
                    if (!resultado) {
                        MessageError er = new MessageError("Semantico", l, c, "El identificador : " + s + " ya existe");
                        ambito.addSalida(er);
                        return er;
                    }
                    
                    ambito.addCodigo(codigo);
                    ambito.addCodigo(Generador.generarComentarioSimple("------------- Guardando la variable : " + s));

                    String temporalP = Generador.generarTemporal();
                    ambito.addCodigo(Generador.generarCuadruplo("+", "P", String.valueOf(sim.getPosRelativa()), temporalP));
                    ambito.addCodigo(Generador.generarCuadruplo("=", temporalP, temp.getResultado(), "Stack"));

                    ambito.addCodigo(Generador.generarComentarioSimple("-------------- FIN guardar variable : " + s));

                    
                    return -1;
                }
                else if(tipo.getTipo() == Tipo.REGISTRO){
                    Object res = ((Instruccion)tipo.getValor()).ejecutar(ambito);
                    if(res instanceof MessageError) return new MessageError("",l,c,"");
                    
                    Nodo temp = (Nodo)res;
                    Simbolo sim = new Simbolo(s,constante,false,Tipo.REGISTRO,Generador.generarStack(),ambito.getRelativa());
                    sim.setValor(temp.getValor());
                    Boolean resultado = ambito.addSimbolo(sim);
                     //----------------------------------------------- SI YA EXISTE ----------------------------------------
                    if (!resultado) {
                        MessageError er = new MessageError("Semantico", l, c, "El identificador : " + s + " ya existe");
                        ambito.addSalida(er);
                        return er;
                    }
                    
                    String codigo = Generador.generarComentarioSimple("------------------------- RESERVANDO ESPACIO PARA EL REGISTRO: " + s);
                    codigo += "\n" + temp.getCodigo3D();
                    codigo += "\n" + Generador.generarComentarioSimple("------------------------- FIN RESERVANDO ESPACIO PARA EL REGISTRO: " + s);
                    codigo += "\n" + Generador.generarComentarioSimple("------------------------- ALMACENANDO LA VARIABLE: " + s);
                    codigo += "\n" + Generador.generarCuadruplo("=", String.valueOf(sim.getPosRelativa()), temp.getResultado(), "Stack");
                    codigo += "\n" + Generador.generarComentarioSimple("------------------------- FIN ALMACENANDO LA VARIABLE: " + s);
                    ambito.addCodigo(codigo);
                    return -1;
                }
                else{
                    
                    Boolean resultado = ambito.addSimbolo(new Simbolo(s, constante, false, tipo.getTipo(), Generador.generarStack(), ambito.getRelativa()));
                    if (!resultado) {
                        MessageError er = new MessageError("Semantico", l, c, "El identificador : " + s + " ya existe");
                        ambito.addSalida(er);
                        return er;
                    }
                }
               
            } //-------------------------------- SI SE INICIALIZO ---------------------------------------------------------------------
            else {
                if (casteoImplicito(tipo.getTipo(),nodo.getTipo() )) {
                    Boolean resultado = ambito.addSimbolo(new Simbolo(s, constante, true, tipo.getTipo(), Generador.generarStack(), ambito.getRelativa()));

                    if (resultado) {

                        ambito.addCodigo(Generador.generarComentarioSimple("------------- Guardando la variable : " + s));
                        
                        String temporalP = Generador.generarTemporal();
                        Simbolo sim = ambito.getSimbolo(s);
                        ambito.addCodigo(Generador.generarCuadruplo("+", "P", String.valueOf(sim.getPosRelativa()), temporalP));
                        ambito.addCodigo(Generador.generarCuadruplo("=", temporalP, nodo.getResultado(), "Stack"));

                        ambito.addCodigo(Generador.generarComentarioSimple("-------------- FIN guardar variable : " + s));
                    } else {
                        MessageError er = new MessageError("Semantico", l, c, "El identificador : " + s + " ya existe");
                        ambito.addSalida(er);
                        return er;
                    }

                } else {
                    MessageError er = new MessageError("Semantico", l, c, "No coinciden los datos: " + tipo.getTipo() + " con: " + nodo.getTipo());
                    ambito.addSalida(er);
                    return er;
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
    public static Boolean casteoImplicito(Tipo tipo1, Tipo tipo2){
        if(tipo1 == Tipo.STRING && tipo2 == Tipo.WORD) return true;
        else if(tipo1 == Tipo.STRING && tipo2 == Tipo.NULL) return true;
        else if(tipo1 == Tipo.INT && tipo2 == Tipo.CHAR) return true;
        else if(tipo1 == Tipo.DOUBLE && tipo2 == Tipo.INT) return true;
        else if(tipo1 == Tipo.DOUBLE && tipo2 == Tipo.CHAR) return true;
        else if(tipo1 == Tipo.STRING && tipo2 == Tipo.NULL) return true;
        return tipo1 == tipo2;
    }
    
    
    private Boolean buscarTipo(Ambito ambito){
        if(tipo.getTipo() == Tipo.ID){
            Equivalencia equi = ambito.getEquivalencia(tipo.getId());
            if(equi != null)tipo = equi.getTipo();
            else return false;    
        }
        return true;
    }
    
}
