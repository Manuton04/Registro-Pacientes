package org.registro.registro.classes.Utils.comparadores;

import org.registro.registro.classes.Turno;

public class ComparadorFechaTurno implements ComparadorTurno{
    public int compare(Turno turno1, Turno turno2) {
        return turno1.getFecha().compareTo(turno2.getFecha());
    }
}
