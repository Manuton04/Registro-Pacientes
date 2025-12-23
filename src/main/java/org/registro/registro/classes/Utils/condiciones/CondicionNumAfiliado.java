package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

public class CondicionNumAfiliado implements Condicion{
    private String numAfiliado;

    public CondicionNumAfiliado(String numAfiliado) {
        this.numAfiliado = numAfiliado;
    }

    public boolean cumple(Paciente p) {
        return p.getNumeroAfiliado().contains(numAfiliado);
    }
}
