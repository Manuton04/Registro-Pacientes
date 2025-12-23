package org.registro.registro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Turno;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        main();
    }

    public void main() throws IOException {
        //Donde esta documentos
        Path path = Paths.get(
                System.getProperty("user.home"),
                "OneDrive",
                "Documentos",
                "RegistroMedicoApp"
        );
        Sistema sistema = new Sistema(path);
        sistema.loadAll();
        //sistema.addPaciente(crearPacientePrueba());
        sistema.saveAll();


        System.out.println("Pacientes cargados:");
        for (Paciente p : sistema.getPacientes())
            System.out.println("- " + p.getNombre());
        System.out.println();
        //launch();
    }

    public Paciente crearPacientePrueba(){
        LocalDate fechaNacimiento = LocalDate.of(1969, 5, 17);
        Paciente p = new Paciente("Mercedes", "de Piro", fechaNacimiento, "Comandante Nicanor Otamendi", "Direccion", "Num telefono", "Email", "Documento", "Obra social", "Num afiliado");
        Turno t = new Turno(LocalDateTime.now().plusDays(7), new ArrayList<String>(Arrays.asList("Consulta general", "Dolor de cabeza")), "Paracetamol 500mg cada 8 horas", p.getId());
        Turno t2 = new Turno(LocalDateTime.now().plusDays(30), new ArrayList<String>(Arrays.asList("Control general")), "Ninguna", p.getId());
        p.addTurno(t);
        p.addTurno(t2);
        return p;
    }
}