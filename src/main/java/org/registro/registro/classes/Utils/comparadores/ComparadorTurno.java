package org.registro.registro.classes.Utils.comparadores;

import org.registro.registro.classes.Turno;

import java.util.Comparator;

public interface ComparadorTurno extends Comparator<Turno> {
    int compare(Turno turno1, Turno t2);
}
