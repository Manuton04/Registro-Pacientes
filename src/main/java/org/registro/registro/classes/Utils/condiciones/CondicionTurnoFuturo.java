package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionTurnoFuturo implements Condicion{
    public boolean cumple(Paciente paciente){
        return paciente.getTurnoProximo() != null;
    }
}
