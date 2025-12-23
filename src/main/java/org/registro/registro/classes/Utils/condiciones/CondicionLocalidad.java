package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionLocalidad implements Condicion{
    private String localidad;

    public CondicionLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public boolean cumple(Paciente p) {
        return p.getLocalidad().contains(localidad);
    }
}
