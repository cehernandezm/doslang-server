/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes.Registros;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Declaracion;
import Pascal.Componentes.Expresion;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class AccesoRegistro implements Instruccion {
    Expresion registro;
    Expresion expresion;
    String id;
    int l;
    int c;

    /**
     * CONSTRUCTOR DE LA CLASE
     * @param registro
     * @param expresion
     * @param id
     * @param l
     * @param c 
     */
    public AccesoRegistro(Expresion registro, Expresion expresion, String id, int l, int c) {
        this.registro = registro;
        this.expresion = expresion;
        this.id = id.toLowerCase();
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
        Object identificador = registro.ejecutar(ambito);
        Object valor = expresion.ejecutar(ambito);
        if(identificador instanceof MessageError) return new MessageError("",l,c,"");
        if(valor instanceof MessageError) return new MessageError("",l,c,"");
        
        Nodo variable = (Nodo) identificador;
        Nodo asignar = (Nodo) valor;
        
        //------------------------------------------------------------------------------- VERIFICAR QUE LA VARIABLE SEA DE TIPO RECORD ------------------------------------------
        
        if(variable.getTipo() != Tipo.REGISTRO){
            MessageError mensaje = new MessageError("Semantico",l,c,"La variable: " + variable.getId() + " no es de tipo Record, No se reconoce el tipo: " + variable.getTipo());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        //------------------------------------------------------------------------------- VERIFICAMOS QUE EXISTA EL ATRIBUTO -----------------------------------------------------
        
        LinkedList<Atributo> lista = (LinkedList<Atributo>) variable.getValor();
        Atributo atributo = null;
        int index = 0;
        
        for(Atributo a : lista){
            if(a.getId().equals(id)){
                atributo = a;
                break;
            }
            index++;
        }
        
        //------------------------------------------------------------------------- No existe el atributo -------------------------------------------------------------------
        if(atributo == null){
            MessageError mensaje = new MessageError("Semantico",l,c,"El atributo: " + id + " no existe en el registro: " + variable.getId());
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        
        //-------------------------------------------------------------------- Verificamos que el valor a asignar sea igual al del atributo ----------------------------------
        if(!(Declaracion.casteoImplicito(atributo.getTipo().getTipo(), asignar.getTipo()))){
            MessageError mensaje = new MessageError("Semantico",l,c,"No coinciden los tipo: " + atributo.getTipo().getTipo() + " con: " + asignar.getTipo() );
            ambito.addSalida(mensaje);
            return mensaje;
        }
        
        String codigo = "\n" + variable.getCodigo3D();
        codigo += "\n" + asignar.getCodigo3D();
       
        String posicion = Generador.generarTemporal();
        codigo += "\n" + Generador.generarComentarioSimple("---------------------- REGISTRO: " + variable.getId() + " atributo: " + atributo.getId());
        codigo += "\n" + Generador.generarCuadruplo("+", String.valueOf(index), variable.getResultado(), posicion);
        codigo += "\n" + Generador.generarCuadruplo("=", posicion, asignar.getResultado(), "Heap");
        codigo += "\n" + Generador.generarComentarioSimple("---------------------- FIN REGISTRO: " + variable.getId() + " atributo: " + atributo.getId());
        
        Nodo temp = new Nodo();
        temp.setCodigo3D(codigo);
        return temp;
        
        
     
    }
    
    
    
}
