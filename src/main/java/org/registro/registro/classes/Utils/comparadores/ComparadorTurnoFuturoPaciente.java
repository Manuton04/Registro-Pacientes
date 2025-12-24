package org.registro.registro.classes.Utils.comparadores;

import org.registro.registro.classes.Paciente;

public class ComparadorTurnoFuturoPaciente implements Comparador{
    public int compare(Paciente paciente1, Paciente paciente2){
        ComparadorFechaTurno comparadorFechaTurno = new ComparadorFechaTurno();
        return comparadorFechaTurno.compare(paciente1.getTurnoProximo(), paciente2.getTurnoProximo());

    }
}
