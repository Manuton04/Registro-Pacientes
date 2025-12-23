package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionDNI implements Condicion{
    private String dni;

    public CondicionDNI(String dni) {
        this.dni = dni;
    }

    public boolean cumple(Paciente p){
        return p.getDni().contains(dni);
    }
}
