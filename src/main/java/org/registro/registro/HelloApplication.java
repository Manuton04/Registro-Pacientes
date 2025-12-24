package org.registro.registro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Turno;
import org.registro.registro.classes.Utils.comparadores.Comparador;
import org.registro.registro.classes.Utils.comparadores.ComparadorFechaTurno;
import org.registro.registro.classes.Utils.condiciones.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {
    private Sistema sistema;
    @Override
    public void start(Stage stage) throws IOException {
        //main();

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Buscar paciente...");

        Button btnAgregar = new Button("Agregar Paciente");
        Button btnTurnos = new Button("Turnos");

        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnTurnos.setMaxWidth(Double.MAX_VALUE);

        VBox patientList = new VBox(5);
        patientList.setPadding(new Insets(5));

        ScrollPane scrollPane = new ScrollPane(patientList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #34495e;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        List<Paciente> lista = new ArrayList<>(sistema.getPacientes());
        if (getCondicionBuscador(searchBar.getText()) != null) {
            Condicion condicion = getCondicionBuscador(searchBar.getText());
            lista = sistema.getPacientes(condicion);
        }


        for (Paciente p : lista) {
            Button patientBtn = new Button(p.getNombre());
            patientBtn.setMaxWidth(Double.MAX_VALUE);
            patientBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

            patientBtn.setOnAction(e -> {
                System.out.println("Selected: " + p.getNombre());
            });

            patientList.getChildren().add(patientBtn);
        }

        sidebar.getChildren().addAll(btnAgregar, searchBar, scrollPane);

        StackPane content = new StackPane();
        content.setStyle("-fx-background-color: #ecf0f1;");
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(content);

        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Registro de Pacientes");
        stage.setScene(scene);
        stage.show();

    }

    public void init() throws IOException {
        //launch();
        //Donde esta documentos
        Path path = Paths.get(
                System.getProperty("user.home"),
                "OneDrive",
                "Documentos",
                "RegistroMedicoApp"
        );
        this.sistema = new Sistema(path);
        sistema.loadAll();
        //sistema.addPaciente(crearPacientePrueba());
        sistema.saveAll();


        System.out.println("Pacientes cargados:");
        for (Paciente p : sistema.getPacientes())
            System.out.println("- " + p.getNombre());
        System.out.println();


        /*
        Paciente p = sistema.getPacientes().getFirst();
        int i = new ComparadorFechaTurno().compare(p.getTurnos().get(0), p.getTurnos().get(1));
        System.out.println("Turno 1: "+ p.getTurnos().get(0).getFecha()+ " vs Turno 2: " + p.getTurnos().get(1).getFecha());
        System.out.println(i);
         */

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

    public Condicion getCondicionBuscador(String texto){
        if (texto == null || texto.isEmpty())
            return null;
        Condicion n = new CondicionNombre(texto);
        CondicionLocalidad l = new CondicionLocalidad(texto);
        CondicionEmail e = new CondicionEmail(texto);
        CondicionObraSocial o = new CondicionObraSocial(texto);
        CondicionDNI d = new CondicionDNI(texto);
        CondicionNumAfiliado a = new CondicionNumAfiliado(texto);
        CondicionEdad edad;
        try {
            edad = new CondicionEdad((Integer.parseInt(texto)));
        }catch (Exception ex){
            return null;
        }

        CondicionOr cond = new CondicionOr(n, l);
        cond = new CondicionOr(cond, e);
        cond = new CondicionOr(cond, o);
        cond = new CondicionOr(cond, d);
        cond = new CondicionOr(cond, a);
        if (edad != null)
            cond = new CondicionOr(cond, edad);

        return cond;
    }
}