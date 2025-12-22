package org.registro.registro.classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sistema {
    private Map<UUID, Paciente> pacientes;
    private Map<UUID, Turno> turnos;
    private Path path;

    public Sistema(Path path){
        pacientes = new HashMap<>();
        turnos = new HashMap<>();
        this.path = path;
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

    public void savePaciente(Paciente paciente){

    }

    public void inicializarCarpeta() throws IOException {
        System.out.println("Base path: " + path.toAbsolutePath());
        System.out.println("Exists? " + Files.exists(path));

        Path pacientesPath = path.resolve("pacientes");
        System.out.println("Pacientes path: " + pacientesPath.toAbsolutePath());


        
        if (!Files.exists(pacientesPath)) {
            Files.createDirectories(pacientesPath);
            System.out.println("Created file pacientes!");
        } else {
            System.out.println("Pacientes already existed");
        }
    }


}
