package org.registro.registro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.registro.registro.classes.Sistema;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        main();
    }

    public static void main() {
        //Donde esta documentos
        Path path = Paths.get(
                System.getProperty("user.home"),
                "OneDrive",
                "Documentos",
                "RegistroMedicoApp"
        );
        Sistema sistema = new Sistema(path);
        try {
            sistema.inicializarCarpeta();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //launch();
    }
}