/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doslang.server;

import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Parser.Lexico;
import Pascal.Parser.Sintactico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class DoslangServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        interpretar("/Test.txt");
    }
    
    /**
     * METODO QUE LEERA EL ARCHIVO TEST PARA SUS RESPECTIVA TEST
     * @param direccion direccion del archivo de texto
     */
    
    private static void interpretar(String direccion){
        Sintactico sintactico;
        String path = new File("").getAbsolutePath() + "/src/Test";
        try{
            Reader reader = new BufferedReader(new FileReader(path + direccion));
            sintactico = new Sintactico(new Lexico(reader));
            sintactico.parse();
            LinkedList<Instruccion> lista = sintactico.getLista();
            Ambito global = new Ambito("global",null,direccion);
            //global.addCodigo(Generador.generarCuadruplo("+", "P", "0", "P"));
            //----------------------------- AGREGO LA FUNCION QUE TRUNCA UN NUMERO ---------------------------------------------------------
            //global.addCodigo(Generador.funcionTrunk());
            //------------------------------ AGREGO LA FUNCION NUMEROTOCADENA ------------------------------------------------------------
            //global.addCodigo(Generador.numeroToCadena());
            //-------------------------------Agrego la funncion ROUND-----------
            //global.addCodigo(Generador.funcionRound());
            
            for(Instruccion ins : lista){
                Object o = ins.ejecutar(global);
                if(o instanceof MessageError){}
                else {
                    Nodo nodo = (Nodo) o;
                    global.addCodigo(nodo.getCodigo3D());
                }
            }
            System.out.println(global.getCodigo());
            for(Object o : global.getSalida()){
                if(o instanceof MessageError){
                    MessageError er = (MessageError)o;
                    System.out.println(er.getError());
                }
            }
        }catch(Exception e){
            System.err.println("Error de compilacion: " + e.getMessage());
        }
    }
    
}
