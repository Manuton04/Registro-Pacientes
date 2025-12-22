package org.registro.registro.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Turno {
    private UUID id;
    private UUID pacienteId;
    private LocalDateTime fecha;
    private ArrayList<String> descripcion;
    private String receta;

    public Turno(LocalDateTime fecha, ArrayList<String> descripcion, String receta, UUID pacienteId) {
        this.id = UUID.randomUUID();
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.receta = receta;
        this.pacienteId = pacienteId;
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
    public UUID getPacienteId() {
        return pacienteId;
    }
}
