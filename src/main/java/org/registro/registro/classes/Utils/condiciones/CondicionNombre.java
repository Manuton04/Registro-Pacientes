package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionNombre implements Condicion{

    private String nombre;

    public CondicionNombre(String nombre){
        this.nombre = nombre.toLowerCase();
    }

    public boolean cumple(Paciente p) {
        return p.getNombre().toLowerCase().contains(nombre) || p.getApellido().toLowerCase().contains(nombre);
    }
}
