package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionLocalidad implements Condicion{
    private String localidad;

    public CondicionLocalidad(String localidad) {
        this.localidad = localidad.toLowerCase();
    }

    public boolean cumple(Paciente p) {
        return p.getLocalidad().toLowerCase().contains(localidad);
    }
}
