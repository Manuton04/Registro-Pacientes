package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public interface Condicion {
    boolean cumple(Paciente paciente);
}
