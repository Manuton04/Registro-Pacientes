package org.registro.registro;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.registro.registro.classes.ConfigHandler;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Utils.condiciones.Condicion;
import org.registro.registro.classes.Utils.telegram.MakeWebhookService;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    private static final Sistema sistema = ConfigHandler.getSistema();
    @FXML
    private TextField buscador;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox lista;
    
    @FXML
    private Button btnEnviarTurnos;

    public String getText() {
        return buscador.getText();
    }

    @FXML
    public void initialize() {
        scroll.setContent(lista);
        scroll.setFitToWidth(true);
        refreshPatientList();
    }

    @FXML
    private void onSearch(KeyEvent event) {
        System.out.println("Searching: " + buscador.getText());
        refreshPatientList();
    }

    public void refreshPatientList() {
        lista.getChildren().clear();

        List<Paciente> pacientes = new ArrayList<>(sistema.getPacientes());
        if (getText() != null && !getText().isEmpty()) {
            Condicion condicion = ConfigHandler.getCondicionBuscador(getText());
            pacientes = sistema.getPacientes(condicion);
        }

        for (Paciente p : pacientes) {
            Button patientBtn = new Button(p.getNombre() + " " + p.getApellido());
            patientBtn.setMaxWidth(Double.MAX_VALUE);
            patientBtn.setStyle(".patient-button");

            patientBtn.setOnAction(e -> {
                System.out.println("Selected: " + p.getNombre());
            });

            lista.getChildren().add(patientBtn);
        }
    }

    @FXML
    private void onEnviarTurnos(ActionEvent event) {
        btnEnviarTurnos.setDisable(true);
        btnEnviarTurnos.setText("Enviando...");

        new Thread(() -> {
            MakeWebhookService service = new MakeWebhookService(
                    ConfigHandler.getString("telegram.webhookUrl")
            );
            boolean success = service.sendTomorrowsTurnosToMake();

            Platform.runLater(() -> {
                btnEnviarTurnos.setDisable(false);
                btnEnviarTurnos.setText("📤 Enviar turnos de mañana");

                if (success) {
                    System.out.println("✅ Turnos enviados!");
                } else {
                    System.out.println("❌ Error al enviar");
                }
            });
        }).start();
    }
}
