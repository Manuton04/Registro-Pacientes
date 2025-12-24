package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionAnd implements Condicion{
    private Condicion condicion1;
    private Condicion condicion2;

    public CondicionAnd(Condicion condicion1, Condicion condicion2) {
        this.condicion1 = condicion1;
        this.condicion2 = condicion2;
    }

    public boolean cumple(Paciente paciente) {
        return condicion1.cumple(paciente) && condicion2.cumple(paciente);
    }
}
