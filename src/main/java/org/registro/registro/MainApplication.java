package org.registro.registro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.registro.registro.classes.ConfigHandler;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;

import java.io.IOException;
import java.time.LocalDate;

public class MainApplication extends Application {
    private Sistema sistema = new Sistema();

    @Override
    public void init() throws Exception {
        ConfigHandler.loadConfig();
        sistema = ConfigHandler.getSistema();
        org.registro.registro.TelegramIntegration.initialize();

        //sistema.addPaciente(crearPacientePrueba());

        System.out.println("Pacientes cargados:");
        for (
                Paciente p : sistema.getPacientes())
            System.out.println("- " + p.getNombre());
        System.out.println();

        /*
        Paciente p = sistema.getPacientes().getFirst();
        int i = new ComparadorFechaTurno().compare(p.getTurnos().get(0), p.getTurnos().get(1));
        System.out.println("Turno 1: "+ p.getTurnos().get(0).getFecha()+ " vs Turno 2: " + p.getTurnos().get(1).getFecha());
        System.out.println(i);
         */

    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Registro de Pacientes");
        stage.setScene(scene);
        stage.show();
    }

    public void stop() throws IOException {
        ConfigHandler.saveAll(sistema);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Paciente crearPacientePrueba(){
        String nombre = "Julio";
        String apellido = "Rodriguez";
        LocalDate fechaNacimiento = LocalDate.of(1960, 7, 13);
        String localidad = "Miramar";
        String direccion = "Calle Falsa 123";
        String numTelefono = "123456789";
        String email = "juliorod60@gmail.com";
        String dni = "20123456";
        String obraSocial = "OSDE";
        String numAfiliado = "87654321";

        Paciente p = new Paciente(nombre, apellido, fechaNacimiento, localidad, direccion, numTelefono, email, dni, obraSocial, numAfiliado);
        //Turno t = new Turno(LocalDateTime.now().plusDays(7), new ArrayList<String>(Arrays.asList("Consulta general", "Dolor de cabeza")), "Paracetamol 500mg cada 8 horas", p.getId());
        //Turno t2 = new Turno(LocalDateTime.now().plusDays(30), new ArrayList<String>(Arrays.asList("Control general")), "Ninguna", p.getId());
        //p.addTurno(t);
        //p.addTurno(t2);
        return p;
    }
}