/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Componentes;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Analizar;
import Pascal.Analisis.Estructuras;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato;
import Pascal.Componentes.Funciones.Funcion;
import Pascal.Componentes.UserTypes.TypeDeclaration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class USES implements Instruccion{
    LinkedList<String> lista;
    int l;
    int c;

    public USES(LinkedList<String> lista, int l, int c) {
        this.lista = lista;
        this.l = l;
        this.c = c;
    }

    @Override
    public Object ejecutar(Ambito ambito) {
        String code = "";
        for(String s : lista){
            String codigo = Estructuras.archivos.get(s.toLowerCase());
            
            if(codigo == null){
                MessageError mensaje = new MessageError("Semantico",l,c,"No existe el archivo: " + s.toLowerCase());
                ambito.addSalida(mensaje);
                return mensaje;
            }
            
            Analizar analisis = new Analizar(codigo,s.toLowerCase(),0,ambito);
            try {
                Object result = analisis.ejecutar();
                if(Estructuras.erroresAnalisis.size() > 0 ) {
                    ambito.addSalida(Estructuras.erroresAnalisis);
                    return new MessageError("",l,c,"");
                }
                else if(result instanceof LinkedList){
                    LinkedList<MessageError> errores =(LinkedList<MessageError>)result;
                    
                    for(MessageError e : errores) e.setArchivo(s.toLowerCase());
                    
                    ambito.addSalida(errores);
                    return new MessageError("",l,c,"");
                }
                else code += "\n" + result.toString();
            } catch (Exception ex) {
                System.err.println("Error en USE:" + ex.getMessage());
                return new MessageError("",l,c,"");
            }
           
            
        }
        Nodo temp = new Nodo();
        temp.setCodigo3D(code);
        return temp;
        
        
    }
    
   
    
    
   
    
    
}
