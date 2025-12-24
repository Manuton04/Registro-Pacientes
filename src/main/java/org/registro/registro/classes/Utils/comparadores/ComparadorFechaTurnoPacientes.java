package org.registro.registro.classes.Utils.comparadores;

import org.registro.registro.classes.Paciente;

public class ComparadorFechaTurnoPacientes implements Comparador{
    public int compare(Paciente paciente1, Paciente paciente2) {
        return paciente1.getLatestTurno().getFecha().compareTo(paciente2.getLatestTurno().getFecha());
    }
}
