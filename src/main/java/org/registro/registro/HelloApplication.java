package org.registro.registro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Utils.condiciones.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class HelloApplication extends Application {
    public static Sistema sistema;

    @Override
    public void init() throws Exception {
        Path path = Paths.get(
                System.getProperty("user.home"),
                "OneDrive",
                "Documentos",
                "RegistroMedicoApp"
        );
        sistema = new Sistema(path);
        sistema.loadAll();
        //sistema.addPaciente(crearPacientePrueba());
        sistema.saveAll();


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

    public static Condicion getCondicionBuscador(String texto){
        if (texto == null || texto.isEmpty())
            return null;
        Condicion n = new CondicionNombre(texto);
        CondicionLocalidad l = new CondicionLocalidad(texto);
        CondicionEmail e = new CondicionEmail(texto);
        CondicionObraSocial o = new CondicionObraSocial(texto);
        CondicionDNI d = new CondicionDNI(texto);
        CondicionNumAfiliado a = new CondicionNumAfiliado(texto);
        CondicionEdad edad = null;
        try {
            edad = new CondicionEdad((Integer.parseInt(texto)));
        }catch (Exception ex){
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