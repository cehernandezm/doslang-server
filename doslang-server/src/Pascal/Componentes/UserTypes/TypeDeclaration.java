/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.UserTypes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.Simbolo;
import Pascal.Analisis.TipoDato;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Enumerador;
import Pascal.Componentes.Type;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class TypeDeclaration implements Instruccion {
    
    LinkedList<String> listaEquivalencias;
    int l;
    int c;
    Type tipo;
    /**
     * CONSTRUCTOR DE LA CLASE
     * @param listaEquivalencias
     * @param l
     * @param c
     * @param tipo 
     */
    public TypeDeclaration(LinkedList<String> listaEquivalencias, int l, int c, Type tipo) {
        this.listaEquivalencias = listaEquivalencias;
        this.l = l;
        this.c = c;
        this.tipo = tipo;
    }
    
    
    

    @Override
    public Object ejecutar(Ambito ambito) {
        return guardarEquivalencias(ambito);
    }
    
    /**
     * METODO QUE SE ENCARGARA DE RECORRER EL LISTADO DE EQUIVALENCIAS Y ALMACENARLAS
     * @param ambito
     * @return 
     */
    private Object guardarEquivalencias(Ambito ambito){
        String codigo = "";
        for(String e : listaEquivalencias){
            e = e.toLowerCase();
            if(tipo.getTipo() == Tipo.ID){
                MessageError mensajeError = new MessageError("Semantico",l,c," un Type no puede hacer referencia a otro Type");
                ambito.addSalida(mensajeError);
                return mensajeError;
            }
            if(tipo.getTipo() == Tipo.ENUM){
                Object res = ((Enumerador)tipo.getValor()).ejecutar(ambito);
                if(res instanceof MessageError) return res;
                Simbolo sim = new Simbolo(e,false,true,Tipo.ENUM,Generador.generarStack(),ambito.getRelativa(),ambito.getId());
                Nodo temp = (Nodo)res;
                sim.setValor(temp.getValor());
                Boolean resultado = ambito.addSimbolo(sim);
                if(!resultado){
                    MessageError mensaje = new MessageError("Semantico",l,c,"El identificador: " + e + " ya existe en este ambito");
                    ambito.addSalida(mensaje);
                    return mensaje;
                }
                
                
                String pos = Generador.generarTemporal();
                codigo += "\n" + temp.getCodigo3D();
                codigo += "\n" + Generador.generarComentarioSimple("-------------------- Guardando ENUM: " + e);
                codigo += "\n" + Generador.generarCuadruplo("+","P", String.valueOf(sim.getPosRelativa()), pos);
                codigo += "\n" + Generador.generarCuadruplo("=", pos, temp.getResultado(), "Stack");                
                codigo += "\n" + Generador.generarComentarioSimple("-------------------- FIN Guardando ENUM: " + e);
            }
            else {
                Boolean resultado = ambito.agregarEquivalencia(new Equivalencia(e, tipo));
                if (!resultado) {
                    MessageError mensajeError = new MessageError("Semantico", l, c, "Ya existe el identificador: " + e);
                    ambito.addSalida(mensajeError);
                    return mensajeError;
                }
            }
           
        }
        Nodo nodo = new Nodo();
        nodo.setCodigo3D(codigo);
        return nodo;
    }
    
}
