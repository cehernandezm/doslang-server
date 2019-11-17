/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.stream.Collectors;



/**
 *
 * @author Carlos
 */
public class Server {
    Socket socket;
    int puerto = 2500;

    public Server() {
        try{
            ServerSocket serverSocket = new ServerSocket(puerto);
            
            System.out.println("------------------- SERVIDOR INICIADO, LISTENING PORT: " + puerto + "----------------------------");
            while(true){
                socket = serverSocket.accept();
                System.out.println("Cliente conectado");
                
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                String line;

                // read request headers...
                do {
                    if(!reader.ready())break;
                    
                    line = reader.readLine();
                    System.out.println(line);
                    
                    // process line as needed...
                } while (true);
                
                OutputStreamWriter osw = new OutputStreamWriter(output);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("hilis");
                bw.flush();
                
                System.out.println("Mensaje Enviado");
                System.out.println("Cliente Desconectado");
                socket.close();
                        
            }
        }catch(IOException e){
            System.err.println("Error de conexion en el Servidor: " + e.getMessage());
        }
        finally{
            try{
                socket.close();
            }catch(Exception e){}
            
        }
        
    }
    
    
    
    
}
