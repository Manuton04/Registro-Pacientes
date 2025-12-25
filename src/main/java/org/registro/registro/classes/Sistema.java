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

    public Sistema(){
        pacientes = new HashMap<>();
    }

    public Map<UUID, Paciente> getHashMap(){
        return pacientes;
    }

    public void setHashMap(Map<UUID, Paciente> map){
        this.pacientes = map;
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
