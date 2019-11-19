/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pascal.Analisis;

import Pascal.Componentes.Funciones.Funcion;

/**
 *
 * @author Carlos
 */
public class FuncionAmbito {
    Funcion funcion;
    Ambito ambito;

    public FuncionAmbito(Funcion funcion, Ambito ambito) {
        this.funcion = funcion;
        this.ambito = ambito;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Ambito getAmbito() {
        return ambito;
    }

    public void setAmbito(Ambito ambito) {
        this.ambito = ambito;
    }
    
    
}
