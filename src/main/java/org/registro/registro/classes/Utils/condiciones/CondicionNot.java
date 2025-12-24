package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionNot implements Condicion {
    private Condicion condicion;

    public CondicionNot(Condicion condicion) {
        this.condicion = condicion;
    }

    public boolean cumple(Paciente paciente) {
        return !condicion.cumple(paciente);
    }
}
