package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionEdad implements Condicion{

    private int edad;

    public CondicionEdad(int edad){
        this.edad = edad;
    }

    public boolean cumple(Paciente p) {
        return p.getEdad() >= edad;
    }
}
