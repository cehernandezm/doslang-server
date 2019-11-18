/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import Pascal.Analisis.Analizar;
import Pascal.Analisis.Estructuras;
import io.socket.client.IO;

import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Carlos
 */
public class Server   {
    Socket socket = IO.socket("http://127.0.0.1:3000");

    /**
     *
     * @throws java.net.URISyntaxException
     */
    public Server() throws URISyntaxException   {
        
        socket.connect(); //-------------------------- Establecemos la conexion ------------------------
        //----------------------------------------- LISTENER PARA OBTENER MENSAJES DE REGRESO ----------------
        socket.on("sendCode", (Object... os) -> {
            String mensaje = (String)os[0];
            try {
                JSONArray array = new JSONArray(mensaje);
                Estructuras.archivos = new HashMap<>();
                //-------------------------- LLENAMOS EL HASHMAP CON TODOS LOS ARCHIVOS DEL PROGRAMA
                for(int i = 0; i < array.length(); i++){
                    JSONObject archivo = array.getJSONObject(i);
                    Estructuras.archivos.put(archivo.getString("name").toLowerCase(), archivo.getString("body"));
                }
                String cuerpoPrincipal = Estructuras.archivos.get("principal"); //---------------Archivo Principal
                Analizar analizar = new Analizar(cuerpoPrincipal);
                String resultado = (String)analizar.ejecutar();
                socket.emit("result",resultado); //---------------- ENVIAMOS EL RESULTADO DEL ANALISIS YA SEA UN CODIGO TRADUCIO O  UN CONJUNTO DE ERRORES
            } catch (JSONException ex) {
                System.err.println("Error al convertir la entrada a JSONArray: " +  ex.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        socket.open(); //-------------------------------------------------Abrimos la conexion ---------------------------
        
    }
    
    public void sendMessage(){
        //socket.send("new","hl");
        socket.emit("new","hahshdas");
        System.out.println("hhhsahs");
    }
    
    
    
    
    
}
