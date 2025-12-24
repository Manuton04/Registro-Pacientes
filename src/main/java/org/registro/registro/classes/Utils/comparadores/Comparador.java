package org.registro.registro.classes.Utils.comparadores;

import org.registro.registro.classes.Paciente;

import java.util.Comparator;

public interface Comparador extends Comparator<Paciente> {
    int compare(Paciente paciente1, Paciente paciente2);
}
