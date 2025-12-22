package org.registro.registro.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.registro.registro.classes.Utils.adapters.LocalDateAdapter;
import org.registro.registro.classes.Utils.adapters.LocalDateTimeAdapter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sistema {
    private Map<UUID, Paciente> pacientes;
    private Map<UUID, Turno> turnos;
    private Path path;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

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

    public void saveAll() throws IOException{
        for (Paciente paciente : pacientes.values()) {
            savePaciente(paciente);
        }
    }

    public void savePaciente(Paciente paciente) throws IOException{
        inicializarCarpeta();

        final Path pacientesPath = path.resolve("pacientes");
        Path filePath = pacientesPath.resolve(paciente.getId() + ".json");

        try (Writer writer = Files.newBufferedWriter(filePath)) {
            gson.toJson(paciente, writer);
        }
    }

    public void loadAll(){

    }

    public void inicializarCarpeta() throws IOException {
        System.out.println("Base path: " + path.toAbsolutePath());
        System.out.println("Exists: " + Files.exists(path));

        Path pacientesPath = path.resolve("pacientes");
        System.out.println("Pacientes path: " + pacientesPath.toAbsolutePath());


        
        if (!Files.exists(pacientesPath)) {
            Files.createDirectories(pacientesPath);
            System.out.println("Created file pacientes!");
        } else {
            System.out.println("File pacientes already existed");
        }
    }

    public Paciente getById(UUID id) {
        return pacientes.get(id);
    }

    public Paciente getPacienteFromTurno(Turno turno) {
        return pacientes.get(turno.getPacienteId());
    }


}
