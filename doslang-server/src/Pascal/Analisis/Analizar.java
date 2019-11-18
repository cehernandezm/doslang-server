/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

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

    public Analizar(String codigo) {
        this.codigo = codigo;
    }
    
    
    public Object ejecutar() throws Exception{
        Sintactico sintactico;
        Reader reader = new StringReader(codigo);
        Estructuras.erroresAnalisis = new LinkedList<>();
        sintactico = new Sintactico(new Lexico(reader));
        sintactico.parse();
        if(Estructuras.erroresAnalisis.size() > 0){
            //--------------------------------------------------------- CONSTRUIMOS EL JSON DE ERRORES --------------------------------------------------------
            String json = "{\n\"error\":1,\n\"data\":[";
            int index = 1;
            for(MessageError m : Estructuras.erroresAnalisis){
                json += "{\"Tipo\":\"" + m.getTipo() + "\",\n\"Detalle\":\"" + m.getDetalle() + "\",\n\"Linea\":" + m.getL() + ",\n\"Columna\":" + m.getC() + "\n}";
                if(index == Estructuras.erroresAnalisis.size()) json += "\n";
                else json += ",\n";
                index ++;
            }
            json += "]\n}";
            return json;
        }
        return null;
    }
}
