package org.registro.registro.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Turno {
    private UUID id;
    private LocalDateTime fecha;
    private ArrayList<String> descripcion;
    private String receta;
    private Paciente paciente;

    public Turno(LocalDateTime fecha, ArrayList<String> descripcion, String receta, Paciente paciente) {
        this.id = UUID.randomUUID();
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.receta = receta;
        this.paciente = paciente;
    }

    public UUID getId() {
        return id;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public ArrayList<String> getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(ArrayList<String> descripcion) {
        this.descripcion = descripcion;
    }
    public String getReceta() {
        return receta;
    }
    public void setReceta(String receta) {
        this.receta = receta;
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
