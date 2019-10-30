/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Arreglos;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Expresion;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class AccesoArreglo implements Instruccion {
    Expresion id;
    Expresion valor;
    LinkedList<Expresion> dimensiones;
    int l;
    int c;
    Boolean accion;
    /**
     * CONSTRUCTOR DE LA CLASE
     * @param id
     * @param valor
     * @param dimensiones
     * @param l
     * @param c
     * @param accion TRUE = ACCESO | FALSE = ASIGNACION
     */
    public AccesoArreglo(Expresion id, Expresion valor, LinkedList<Expresion> dimensiones, int l, int c) {
        this.id = id;
        this.valor = valor;
        this.dimensiones = dimensiones;
        this.l = l;
        this.c = c;
        this.accion = accion;
    }

    
    

    @Override
    public Object ejecutar(Ambito ambito) {
       String codigo = "";  
       Nodo temp = new Nodo();
       Object resultI = id.ejecutar(ambito);
       Object resultE = valor.ejecutar(ambito);
       
       if(resultI instanceof MessageError) return new MessageError("",l,c,"");
       if(resultE instanceof MessageError) return resultE; 
       
       
       Nodo nodoId = (Nodo)resultI;
       Nodo nodoValor = (Nodo)resultE;
       //------------------------------------------------------------------ SI ACCEDEMOS A UN TIPO NO ARRAY ------------------------------------------------
       if(nodoId.getTipo() != Tipo.ARRAY){
           MessageError mensaje = new MessageError("Semantico", l,c,"No se puede acceder a un tipo No array: " + nodoId.getTipo());
           ambito.addSalida(mensaje);
           return mensaje;
       }
       
       codigo += "\n" + nodoId.getCodigo3D();
       codigo += "\n" + nodoValor.getCodigo3D();
       
       Object res = obtenerMapeoLexicoGrafico(nodoId, ambito, dimensiones, l, c);
       if(res instanceof MessageError) return res;
       
       Nodo acceso = (Nodo)res;
       
       codigo += "\n" + acceso.getCodigo3D();
       
       if(Declaracion.casteoImplicito(acceso.getTipo(), nodoValor.getTipo())){
        codigo += "\n" + Generador.generarComentarioSimple("------------------------Almacenando el valor en el arreglo---------------------");
        codigo += "\n" + Generador.generarCuadruplo("=", acceso.getResultado(), nodoValor.getResultado(), "Heap");
        codigo += "\n" + Generador.generarComentarioSimple("------------------------FIN Almacenando el valor en el arreglo---------------------");
       }
       else{
           MessageError mensaje = new MessageError("Semantico", l ,c, "Los valores del Arreglo son de tipo: " + acceso.getTipo() + " y se quiere almacenar: " + nodoValor.getTipo());
           ambito.addSalida(mensaje);
           return mensaje;
       }
       
       
       
       
       
       temp.setCodigo3D(codigo);
       return temp;
        
        
        
        
        
    }
    
    
    /**
     * METODO QUE SE ENCARGARA DE OBTENER EL MAPEO LEXICOGRAFICO DE UNA ARREGLO
     * @param sim
     * @param ambito
     * @param etiquetaSalto
     * @return 
     */
    public  static Object obtenerMapeoLexicoGrafico(Nodo sim, Ambito ambito,LinkedList<Expresion> dimensiones2, int l , int c){
        String codigo = "";
        LinkedList<String> posiciones = new LinkedList<>();
        //------------------------------------------------------------ ALMACENAMOS EL CODIGO 3D DE LAS DIMENSIONES ----------------------------------------
        for(Expresion e : dimensiones2){
            Object resultado = e.ejecutar(ambito);
            if(resultado instanceof MessageError) return new MessageError("",l,c,"");
            
            Nodo res = (Nodo)resultado;
            
            if(res.getTipo() != Tipo.INT){
                MessageError mensaje = new MessageError("Semantico",l,c,"La dimension tiene que ser de tipo INT, no se reconoce: " + res.getTipo());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
            codigo += "\n" + res.getCodigo3D();
            posiciones.addLast(res.getResultado());
            
            
        }
        Nodo nodo = new Nodo();
        String posDinamica = Generador.generarTemporal();
        
        //-------------------------------------------------- SI SON DEL MISMO TAMANIO --------------------------------------------------------------
        if(sim.getCantidadDimensiones() == dimensiones2.size()) nodo.setTipo(sim.getTipoArreglo());  
        
        //--------------------------------------------------- estoy accediendo a un sub arreglo ---------------------------------------------------
        else if(sim.getCantidadDimensiones() > dimensiones2.size()){
            nodo.setCantidadDimensiones(sim.getCantidadDimensiones() - dimensiones2.size());
            nodo.setTipo(Tipo.ARRAY);
            nodo.setTipoArreglo(sim.getTipoArreglo());
        }
        else{
            MessageError mensaje = new MessageError("Semantico",l,c,"El Arreglo tiene : " + sim.getCantidadDimensiones() + " y se quiere acceder con: " + dimensiones2.size());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        codigo += "\n" + Generador.generarComentarioSimple("-------------- ACCEDEMOS AL ARREGLO ------------------");
        codigo += "\n" + Generador.generarCuadruplo("=", sim.getResultado(), "", posDinamica);
        
        for (int i = 0; i < dimensiones2.size(); i++) {
            String valorDime = Generador.generarTemporal();
            String limiteIf = Generador.generarTemporal();
            codigo += "\n" + Generador.generarComentarioSimple("-------------- ACCEDEMOS A LA DIMENSION " + (i + 1) + " ------------------");
            codigo += "\n" + Generador.guardarAcceso(limiteIf, "Heap", posDinamica);
            codigo += "  " + Generador.generarComentarioSimple(" Accedemos al limite inferior");
            codigo += "\n" + Generador.generarCuadruplo("+", posDinamica, "3", posDinamica);
            codigo += "\n" + Generador.generarCuadruplo("-", posiciones.get(i), limiteIf, valorDime);
            codigo += "\n" + Generador.generarCuadruplo("+", posDinamica, valorDime, posDinamica);
            if (i + 1 < dimensiones2.size()) {
                codigo += "\n" + Generador.guardarAcceso(posDinamica, "Heap", posDinamica);
            }
            codigo += "\n" + Generador.generarComentarioSimple("-------------- FIN ACCEDEMOS A LA DIMENSION " + (i + 1) + " ------------------");
        }

        codigo += "\n" + Generador.generarComentarioSimple("-------------- FIN ACCEDEMOS AL ARREGLO ------------------");
        
        
        
        
        nodo.setCodigo3D(codigo);
        nodo.setResultado(posDinamica);
        
       return nodo;
    }
    
    
}
