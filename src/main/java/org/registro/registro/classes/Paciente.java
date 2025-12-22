package org.registro.registro.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Paciente {
    private UUID id;
    private LocalDateTime fechaRegistro;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String localidad;
    private String direccion;
    private String telefono;
    private String email;
    private String dni;
    private String obraSocial;
    private String numeroAfiliado;
    ArrayList<Turno> turnos;

    public Paciente(String nombre, String apellido, LocalDate fechaNacimiento, String localidad,
                    String direccion, String telefono, String email, String dni,
                    String obraSocial, String numeroAfiliado) {
        this.id = UUID.randomUUID();
        this.fechaRegistro = LocalDateTime.now();
        this.turnos = new ArrayList<Turno>();

        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.localidad = localidad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.dni = dni;
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public void addTurno(Turno turno) {
        this.turnos.add(turno);
    }

    public void removeTurno(Turno turno) {
        this.turnos.remove(turno);
    }

    public UUID getId() {
        return id;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getObraSocial() {
        return obraSocial;
    }
    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }
    public String getNumeroAfiliado() {
        return numeroAfiliado;
    }
    public void setNumeroAfiliado(String numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }
    public ArrayList<Turno> getTurnos() {
        return turnos;
    }
}
