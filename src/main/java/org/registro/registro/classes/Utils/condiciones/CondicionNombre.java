package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionNombre implements Condicion{

    private String nombre;

    public CondicionNombre(String nombre){
        this.nombre = nombre;
    }

    public boolean cumple(Paciente p) {
        return p.getNombre().contains(nombre) || p.getApellido().contains(nombre);
    }
}
