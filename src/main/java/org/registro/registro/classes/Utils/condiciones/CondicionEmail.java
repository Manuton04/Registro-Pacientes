package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionEmail implements Condicion{
    private String email;

    public CondicionEmail(String email){
        this.email = email;
    }

    public boolean cumple(Paciente p){
        return p.getEmail().contains(email);
    }
}
