package org.registro.registro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Utils.condiciones.Condicion;

import java.util.ArrayList;
import java.util.List;

import static org.registro.registro.HelloApplication.getCondicionBuscador;

public class HelloController {
    private static Sistema sistema =  HelloApplication.sistema;
    @FXML
    private static TextField buscador;

    @FXML
    public static String getText() {
        if (buscador != null) {
            return buscador.getText();
        }
        return "";
    }

    public static void refreshPatientList() {
        List<Paciente> lista = new ArrayList<>(sistema.getPacientes());
        if (getText() != null && !getText().isEmpty()) {
                Condicion condicion = getCondicionBuscador(getText());
                lista = sistema.getPacientes(condicion);

        }


        for (Paciente p : lista) {
            Button patientBtn = new Button(p.getNombre());
            patientBtn.setMaxWidth(Double.MAX_VALUE);
            patientBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

            patientBtn.setOnAction(e -> {
                System.out.println("Selected: " + p.getNombre());
            });

            HelloApplication.patientList.getChildren().add(patientBtn);
        }
    }

    public void onSearch(InputMethodEvent eText) {
        System.out.println("Searching for: " + getText());
        refreshPatientList();
    }
}