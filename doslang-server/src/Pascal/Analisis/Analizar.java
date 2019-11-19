/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import Pascal.Componentes.Break;
import Pascal.Componentes.Continue;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Enumerador;
import Pascal.Componentes.Funciones.Funcion;
import Pascal.Componentes.USES;
import Pascal.Componentes.UserTypes.Equivalencia;
import Pascal.Componentes.UserTypes.TypeDeclaration;
import Pascal.Parser.Lexico;
import Pascal.Parser.Sintactico;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class Analizar {
    String codigo;
    String archivo;
    int flagTipoAnalisis;
    Ambito old;

    public Analizar(String codigo, String archivo, int flagTipoAnalisis) {
        this.codigo = codigo;
        this.archivo = archivo;
        this.flagTipoAnalisis = flagTipoAnalisis;
    }

    public Analizar(String codigo, String archivo, int flagTipoAnalisis, Ambito old) {
        this.codigo = codigo;
        this.archivo = archivo;
        this.flagTipoAnalisis = flagTipoAnalisis;
        this.old = old;
    }
    
    

    
    
    
    public Object ejecutar() throws Exception{
        Sintactico sintactico;
        Reader reader = new StringReader(codigo);
        Estructuras.erroresAnalisis = new LinkedList<>();
        sintactico = new Sintactico(new Lexico(reader));
        sintactico.parse();
        //------------------------------------- SI HAY ERRORES YA SEAN LEXICOS O SINTACTICO --------------------------------------------------------------------
        if(Estructuras.erroresAnalisis.size() > 0) {
            for(MessageError m : Estructuras.erroresAnalisis) m.setArchivo(archivo);
            return Estructuras.erroresAnalisis;
        }
        //-------------------------------------- ANALISAMOS EL ARCHIVO PRINCIPAL --------------------------------------------------------------------------------
        if(flagTipoAnalisis == 1) return analizarCodigo(sintactico.getLista()); //----------------------------- Analizamos el codigo enviado semanticamente -----------------------------------
        return analizarCodigoImport(sintactico.getLista());
        
    }
    
    
    private Object analizarCodigo(LinkedList<Instruccion> lista) {
        Generador.etiqueta = 0;
        Generador.stack = 0;
        Generador.temporal = 0;
        Ambito global = new Ambito("global", null, archivo);
       
        global.addCodigo(Generador.generarCuadruplo("+", "P", "0", "P"));
        //----------------------------- AGREGO LA FUNCION QUE TRUNCA UN NUMERO ---------------------------------------------------------
        global.addCodigo(Generador.funcionTrunk());
        //------------------------------ AGREGO LA FUNCION NUMEROTOCADENA ------------------------------------------------------------
        global.addCodigo(Generador.numeroToCadena());
        //-------------------------------Agrego la funncion ROUND-----------
        global.addCodigo(Generador.funcionRound());

        analizarInstrucciones(lista, global);

        //---------------------------------------------------------- ERRORES SEMANTICOS ---------------------------------------------------------
        if (global.getSalida().size() > 0) {
            return global.getSalida();
        }

        String json = global.getCodigo();

        return json;

    }
    
    public Object analizarCodigoImport(LinkedList<Instruccion> lista){
        Ambito global = new Ambito("global", null, archivo);
        analizarInstruccionImport(old,global,lista);
        //---------------------------------------------------------- ERRORES SEMANTICOS ---------------------------------------------------------
        if (global.getSalida().size() > 0) {
            return global.getSalida();
        }
        old.setTam(global.getTam());
        old.addSalida(global.getSalida());
        old.getListaFunciones().addAll(global.getListaFunciones());
        
        String json = global.getCodigo();
        return json;
    }
    
    
    private  void analizarInstrucciones(LinkedList<Instruccion> lista, Ambito global) {
  
        String codigo = "";
        for(Instruccion ins : lista){
                
            if(ins instanceof USES){
               Object result = ins.ejecutar(global);
               if(result instanceof MessageError){}
               else {
                   Nodo temp = (Nodo)result;
                   codigo += "\n" + temp.getCodigo3D();
               }
            }
            else if(ins instanceof TypeDeclaration){
                ins.ejecutar(global);
            }
        }
        
        /**
         * PRIMER RECORRIDO BUSCAMOS FUNCIONES Y GUARDAMOS SU RETORNO
         */

        
        
        for (Instruccion ins : lista) {
            if (ins instanceof Funcion) {
                ((Funcion) ins).setIdentificador(global.getId() + "_" + ((Funcion) ins).getId() + ((Funcion) ins).getIdentificadorParametros());
                int estado = ((Funcion) ins).primeraPasada();
                if (estado != -1 && ((Funcion) ins).getTipo().getTipo() == TipoDato.Tipo.VOID) {
                    MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), " Los Procedures no retornan ni un valor");
                    global.addSalida(mensaje);
                } else if (estado == -1 && ((Funcion) ins).getTipo().getTipo() != TipoDato.Tipo.VOID) {
                    MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), " Las funciones tienen que retornar un valor");
                    global.addSalida(mensaje);
                } else {
                    if (((Funcion) ins).getTipo().getTipo() != TipoDato.Tipo.VOID) {
                        ((Funcion) ins).setPosRelativaRetorno(estado);
                    }
                    if (((Funcion) ins).getTipo().getTipo() == TipoDato.Tipo.ID) {
                        String id = ((Funcion) ins).getTipo().getId().toLowerCase();
                        Equivalencia equi = global.getEquivalencia(id);
                        
                        if(equi != null){
                           ((Funcion) ins).getTipo().setTipo(equi.getTipo().getTipo()); 
                        }
                        
                    }
                    Boolean resul = global.addFuncion((Funcion) ins,global);
                    //System.out.println(((Funcion) ins).getId() + "_" + resul);
                    if (!resul) {
                        MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), "La funcion: " + ((Funcion) ins).getId() + " ya existe");
                        global.addSalida(mensaje);
                    }
                }

            }
        }

        codigo += "\n//-------------------------------------------- ARCHIVO: " + global.getArchivo() + "---------------------------------------------------------";
        codigo += "\n//---------------------------------------------------------------------------------------------------------------------------------------------------";
        for (Instruccion ins : lista) {
            if (!(ins instanceof Funcion) && !(ins instanceof USES) && !(ins instanceof TypeDeclaration)){
                
                //-------------------------------------------- SI ES UN BREAK ------------------------------------------------------------------------------------
                if (ins instanceof Break) {
                    MessageError mensaje = new MessageError("Semantico", ((Break) ins).getL(), ((Break) ins).getC(), "La sentencia BREAK solo puede venir en ciclos");
                    global.addSalida(mensaje);
                    break;
                }

                //-------------------------------------------- SI ES UN CCONTINUE ------------------------------------------------------------------------------------
                if (ins instanceof Continue) {
                    MessageError mensaje = new MessageError("Semantico", ((Continue) ins).getL(), ((Continue) ins).getC(), "La sentencia CONTINUE solo puede venir en ciclos");
                    global.addSalida(mensaje);
                    break;
                }

                Object o = ins.ejecutar(global);
                if (o instanceof MessageError) {
                } else {
                    Nodo nodo = (Nodo) o;
                    codigo += "\n" + nodo.getCodigo3D();
                }
            }

        }
        //---------------------------------------------------- AGREGAMOS EL CODIGO DE LAS FUNCIONES ----------------------------------------------------------

        for (FuncionAmbito ins : global.getListaFunciones()) {
            Funcion f = ins.getFuncion();

            Object res = f.ejecutar(ins.getAmbito());
            if (res instanceof MessageError) {
            } else {
                Nodo temp = (Nodo) res;
                global.addCodigoFuncion(((Funcion) f).getIdentificador(), temp.getCodigo3D());
            }

        }
        codigo += "\n//-------------------------------------------------------FIN ARCHIVO : " + global.getArchivo() + "------------------------------------------------------------------------------";
        codigo += "\n//--------------------------------------------------------------------------------------------------------------------------------------------------------------";
        //----------------------------------------------- AGREGAMOS EL CODIGO DE TODAS LAS FUNCIONES --------------------------------------------------------------
        global.addCodigo(global.getCodigoAllFunciones());
        global.addCodigo(codigo);
        global.addCodigo(Generador.generarComentarioSimple("---------------------------- LISTADO DE EXIT ------------------------"));
        global.addCodigo(Generador.getAllEtiquetas(global.getListadoExit()));
    }

    
    
    
    private void  analizarInstruccionImport(Ambito global,Ambito nuevo, LinkedList<Instruccion> lista){
        
        String codigo = "";
        for(Instruccion ins : lista){
                
            if(ins instanceof USES){
               Object result = ins.ejecutar(nuevo);
               if(result instanceof MessageError){}
               else {
                   Nodo temp = (Nodo)result;
                   codigo += "\n" + temp.getCodigo3D();
               }
            }
            else if(ins instanceof TypeDeclaration){
                ins.ejecutar(nuevo);
            }
        } 
        
        for (Instruccion ins : lista) {
            if (ins instanceof Funcion) {
                ((Funcion) ins).setIdentificador(nuevo.getId() + "_" + ((Funcion) ins).getId() + ((Funcion) ins).getIdentificadorParametros());
                int estado = ((Funcion) ins).primeraPasada();
                if (estado != -1 && ((Funcion) ins).getTipo().getTipo() == TipoDato.Tipo.VOID) {
                    MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), " Los Procedures no retornan ni un valor");
                    nuevo.addSalida(mensaje);
                } else if (estado == -1 && ((Funcion) ins).getTipo().getTipo() != TipoDato.Tipo.VOID) {
                    MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), " Las funciones tienen que retornar un valor");
                    nuevo.addSalida(mensaje);
                } else {
                    if (((Funcion) ins).getTipo().getTipo() != TipoDato.Tipo.VOID) {
                        ((Funcion) ins).setPosRelativaRetorno(estado);
                    }
                    if (((Funcion) ins).getTipo().getTipo() == TipoDato.Tipo.ID) {
                        String id = ((Funcion) ins).getTipo().getId().toLowerCase();
                        Equivalencia equi = nuevo.getEquivalencia(id);
                        
                        if(equi != null){
                           ((Funcion) ins).getTipo().setTipo(equi.getTipo().getTipo()); 
                        }
                        
                    }
                    Boolean resul = global.addFuncion((Funcion) ins,nuevo);
                    if (!resul) {
                        MessageError mensaje = new MessageError("Semantico", ((Funcion) ins).getL(), ((Funcion) ins).getC(), "La funcion: " + ((Funcion) ins).getId() + " ya existe");
                        nuevo.addSalida(mensaje);
                    }
                }

            }
        }

        codigo += "\n//-------------------------------------------- ARCHIVO: " + nuevo.getArchivo() + "---------------------------------------------------------";
        codigo += "\n//---------------------------------------------------------------------------------------------------------------------------------------------------";
        for (Instruccion ins : lista) {
            if (ins instanceof Declaracion   || ins instanceof Enumerador) {
                Object o = ins.ejecutar(nuevo);
                if (o instanceof MessageError) {
                    global.addSalida(nuevo.getSalida());
                    break;
                } else {
                    Nodo nodo = (Nodo) o;
                    codigo += "\n" + nodo.getCodigo3D();
                }
            }

        }
        
        
        
        
        codigo += "\n//-------------------------------------------------------FIN ARCHIVO : " + global.getArchivo() + "------------------------------------------------------------------------------";
        codigo += "\n//--------------------------------------------------------------------------------------------------------------------------------------------------------------";
        
        nuevo.addCodigo(codigo);
        
    }
}
