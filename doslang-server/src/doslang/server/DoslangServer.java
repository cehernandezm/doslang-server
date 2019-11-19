/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package doslang.server;

import Conexion.Server;
import Pascal.Analisis.Ambito;
import Pascal.Analisis.Generador;
import Pascal.Analisis.Instruccion;
import Pascal.Analisis.MessageError;
import Pascal.Analisis.Nodo;
import Pascal.Analisis.TipoDato.Tipo;
import Pascal.Componentes.Break;
import Pascal.Componentes.Continue;
import Pascal.Componentes.Funciones.Funcion;
import Pascal.Parser.Lexico;
import Pascal.Parser.Sintactico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

/**
 *
 * @author Carlos
 */
public class DoslangServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        Server server = new Server();
    }
    
    /**
     * METODO QUE LEERA EL ARCHIVO TEST PARA SUS RESPECTIVA TEST
     * @param direccion direccion del archivo de texto
     */
    
    
    
}
