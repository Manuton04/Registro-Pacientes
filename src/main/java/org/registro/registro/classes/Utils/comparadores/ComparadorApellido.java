package org.registro.registro.classes.Utils.comparadores;

import org.registro.registro.classes.Paciente;

public class ComparadorApellido implements Comparador{
    public int compare(Paciente paciente1, Paciente paciente2){
        return paciente1.getApellido().compareToIgnoreCase(paciente2.getApellido());
    }
}
