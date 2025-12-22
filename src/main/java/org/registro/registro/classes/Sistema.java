package org.registro.registro.classes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sistema {
    Map<UUID, Paciente> pacientes;
    Map<UUID, Turno> turnos;

    public Sistema(){
        pacientes = new HashMap<>();
        turnos = new HashMap<>();
    }

    public void addPaciente(Paciente paciente){
        pacientes.put(paciente.getId(), paciente);
    }

    public void addTurno(Turno turno){
        turnos.put(turno.getId(), turno);
    }

    public void removePaciente(UUID id){
        pacientes.remove(id);
    }

    public void removeTurno(UUID id){
        turnos.remove(id);
    }

    public void saveToJson(){
        File folder = new File("data/pacientes");
    }


}
