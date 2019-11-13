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
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Expresion;
import Pascal.Componentes.UserTypes.Equivalencia;

/**
 *
 * @author Carlos
 */
public class Dimension implements Instruccion {
    Expresion a;
    Expresion b;
    String id;
    int l;
    int c;

    /**
     * CONSTRUCTOR PARA UN INTERVALOR Exp .. Exp
     * @param a
     * @param b
     * @param l
     * @param c 
     */
    public Dimension(Expresion a, Expresion b, int l, int c) {
        this.a = a;
        this.b = b;
        this.l = l;
        this.c = c;
        this.id = null;
    }

    /**
     * CONSTRUCTOR PARA UN ID
     * @param id
     * @param l
     * @param c 
     */
    public Dimension(String id, int l, int c) {
        this.id = id;
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
        //---------------------------------------------------- Es un intervalo ---------------------------------------------------------------------
        if (id == null){
            Object izq = (a == null) ? null : a.ejecutar(ambito);
            Object der = (b == null) ? null : b.ejecutar(ambito);
            if(izq == null) return -1;
            if(der == null) return -1;
            
            if(izq instanceof MessageError) return new MessageError("",l,c,"");
            if(der instanceof MessageError) return new MessageError("",l,c,"");
            
            Nodo nodoIzq = (Nodo)izq;
            Nodo nodoDer = (Nodo)der;
            
            if(!(nodoIzq.getTipo() == Tipo.INT && nodoDer.getTipo() == Tipo.INT)){
                MessageError mensaje = new MessageError("Semantico",l,c,"Los intervalos tiene que ser enteros no se reconoce: "  + nodoIzq.getTipo() + " con: " + nodoDer.getTipo());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
            String codigo = nodoIzq.getCodigo3D();
            codigo += "\n" + nodoDer.getCodigo3D();
            
            String tamanio = Generador.generarTemporal();
            codigo += "\n" + Generador.generarComentarioSimple("----------------------------------- OBTENIENDO TAMANIO DE DIMENSION ----------------------------");
            codigo += "\n" + Generador.generarCuadruplo("-", nodoDer.getResultado(), nodoIzq.getResultado(), tamanio);
            codigo += "\n" + Generador.generarCuadruplo("+", tamanio, "1", tamanio);
            
            codigo += "\n" + Generador.generarComentarioSimple("----------------------------------- ALMACENANDO LIMITE INFERIOR ----------------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", nodoIzq.getResultado(), "Heap");
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
            
            codigo += "\n" + Generador.generarComentarioSimple("----------------------------------- ALMACENANDO LIMITE SUPERIOR ----------------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", nodoDer.getResultado(), "Heap");
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
            
            codigo += "\n" + Generador.generarComentarioSimple("----------------------------------- ALMACENANDO TAM ----------------------------");
            codigo += "\n" + Generador.generarCuadruplo("=", "H", tamanio, "Heap");
            codigo += "\n" + Generador.generarCuadruplo("+", "H", "1", "H");
            
            Nodo nodo = new Nodo();
            nodo.setCodigo3D(codigo);
            nodo.setResultado(tamanio);
            nodo.setTipo(Tipo.INT);
            return nodo;
            
        }
        else{
            Equivalencia equivalencia = ambito.getEquivalencia(id.toLowerCase());
            
            if(equivalencia == null){
                MessageError mensaje = new MessageError("Semantico",l,c,"No existe el type: " + id);
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
            if(equivalencia.getTipo().getTipo() != Tipo.DIMENSION){
                MessageError mensaje = new MessageError("Semantico",l,c,"Se esperaba un tipo Dimension, no se reconoce: " + equivalencia.getTipo().getTipo());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
            Dimension di = (Dimension)equivalencia.getTipo().getValor();
            Object res = di.ejecutar(ambito);
            if(res instanceof MessageError) return res;
            return res;
        }
    }
    
}
