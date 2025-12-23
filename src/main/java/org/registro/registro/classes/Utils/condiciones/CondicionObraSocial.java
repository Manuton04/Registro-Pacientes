package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionObraSocial implements Condicion{
    private String obraSocial;

    public CondicionObraSocial(String obraSocial){
        this.obraSocial = obraSocial;
    }

    public boolean cumple(Paciente paciente){
        return paciente.getObraSocial().contains(obraSocial);
    }
}
