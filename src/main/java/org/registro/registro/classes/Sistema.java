package org.registro.registro.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.registro.registro.classes.Utils.adapters.LocalDateAdapter;
import org.registro.registro.classes.Utils.adapters.LocalDateTimeAdapter;
import org.registro.registro.classes.Utils.comparadores.Comparador;
import org.registro.registro.classes.Utils.comparadores.ComparadorTurnoFuturoPaciente;
import org.registro.registro.classes.Utils.condiciones.Condicion;
import org.registro.registro.classes.Utils.condiciones.CondicionTurnoFuturo;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Sistema {
    private Map<UUID, Paciente> pacientes;
    private Path path;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private Path pacientesPath;

    public Sistema(Path path){
        pacientes = new HashMap<>();
        this.path = path;
        this.pacientesPath = path.resolve("pacientes");
    }

    public void addPaciente(Paciente paciente){
        pacientes.put(paciente.getId(), paciente);
    }

    public void addTurno(Paciente paciente, Turno turno){
        paciente.addTurno(turno);
    }

    public void removePaciente(UUID id){
        pacientes.remove(id);
    }

    public void removeTurno(Paciente paciente, Turno turno){
        paciente.removeTurno(turno);
    }

    public void setPath(Path path) {
        this.path = path;
        this.pacientesPath = path.resolve("pacientes");
    }

    public void saveAll() throws IOException{
        inicializarCarpeta();
        for (Paciente paciente : pacientes.values()) {
            savePaciente(paciente);
        }
    }

    public void savePaciente(Paciente paciente) throws IOException{
        final Path pacientesPath = path.resolve("pacientes");
        Path filePath = pacientesPath.resolve(paciente.getId() + ".json");

        try (Writer writer = Files.newBufferedWriter(filePath)) {
            gson.toJson(paciente, writer);
        }
    }

    public void loadAll() throws IOException{
        if (!Files.exists(pacientesPath))
            return;

        this.pacientes = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pacientesPath, "*.json")) {
            for (Path archivo : stream) {
                String json = Files.readString(archivo);
                Paciente p = gson.fromJson(json, Paciente.class);
                this.addPaciente(p);
            }
        }

    }

    public void inicializarCarpeta() throws IOException {
        System.out.println("Base path: " + path.toAbsolutePath());
        System.out.println("Exists: " + Files.exists(path));

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

    public List<Paciente> getPacientes() {
        return pacientes.values().stream().toList();
    }

    public List<Paciente> getPacientes(Condicion condicion){
        List<Paciente> t = new ArrayList<>();
        for(Paciente p : getPacientes()){
            if(condicion.cumple(p))
                t.add(p);
        }
        return t;
    }

    public List<Paciente> getPacientes(Comparador comparador){
        List<Paciente> t = getPacientes();
        t.sort(comparador);
        return t;
    }
    
    public List<Paciente> getPacientes(Condicion condicion, Comparador comparador){
        List<Paciente> t = getPacientes(condicion);
        t.sort(comparador);
        return t;
    }

    // Tambien ordena por fecha mas cercana
    public List<Paciente> getPacientesWithTurnosAfterToday(){
        return getPacientes(new CondicionTurnoFuturo(), new ComparadorTurnoFuturoPaciente());
    }


}
