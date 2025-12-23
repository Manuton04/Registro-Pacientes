package org.registro.registro.classes.Utils.condiciones;

import org.registro.registro.classes.Paciente;

import java.time.LocalDateTime;

public class CondicionFechaTurno implements Condicion{

    private LocalDateTime fecha1;
    private LocalDateTime fecha2;

    public CondicionFechaTurno(LocalDateTime fechaMinima, LocalDateTime fechaMaxima) {
        this.fecha1 = fechaMinima;
        this.fecha2 = fechaMaxima;
    }

    public boolean cumple(Paciente p) {
        for (var turno : p.getTurnos()) {
            if (turno.getFecha().isAfter(fecha1) && turno.getFecha().isBefore(fecha2)) {
                return true;
            }
        }
        // Si no encontro ningun turno en el for() que este entre las dos fechas, devuelve falso
        return false;
    }
}
