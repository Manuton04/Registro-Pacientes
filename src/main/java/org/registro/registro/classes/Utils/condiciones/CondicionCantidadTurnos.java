package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionCantidadTurnos implements Condicion{

    private int cantidadMinima;

    public CondicionCantidadTurnos(int cantidadMinima){
        if (cantidadMinima <= 0)
            cantidadMinima = 1;
        this.cantidadMinima = cantidadMinima;
    }

    public boolean cumple(Paciente paciente) {
        return paciente.getTurnos().size() >= cantidadMinima;
    }
}
