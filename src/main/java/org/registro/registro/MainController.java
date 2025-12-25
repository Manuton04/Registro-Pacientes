package org.registro.registro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.registro.registro.classes.ConfigHandler;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Utils.condiciones.Condicion;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    private static final Sistema sistema = ConfigHandler.getSistema();
    @FXML
    private TextField buscador;

    @FXML
    private VBox patientList;

    public String getText() {
        return buscador.getText();
    }

    @FXML
    public void initialize() {
        refreshPatientList();
    }

    @FXML
    private void onSearch(KeyEvent event) {
        System.out.println("Searching: " + buscador.getText());
        refreshPatientList();
    }

    public void refreshPatientList() {
        patientList.getChildren().clear();
        List<Paciente> lista = new ArrayList<>(sistema.getPacientes());
        if (getText() != null && !getText().isEmpty()) {
            Condicion condicion = ConfigHandler.getCondicionBuscador(getText());
            lista = sistema.getPacientes(condicion);

        }


        for (Paciente p : lista) {
            Button patientBtn = new Button(p.getNombre()+" "+p.getApellido());
            patientBtn.setMaxWidth(Double.MAX_VALUE);
            patientBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

            patientBtn.setOnAction(e -> {
                System.out.println("Selected: " + p.getNombre());
            });

            patientList.getChildren().add(patientBtn);
        }
    }
}