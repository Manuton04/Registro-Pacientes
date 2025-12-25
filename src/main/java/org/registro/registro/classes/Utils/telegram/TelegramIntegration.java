package org.registro.registro;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.registro.registro.services.MakeWebhookService;

/**
 * Example code showing how to integrate MakeWebhookService
 * into your existing RegistroPacientesApp.
 *
 * Copy the relevant parts into your controllers.
 */
public class TelegramIntegration {

    // ════════════════════════════════════════════════════════════════
    // CONFIGURATION - Set these values
    // ════════════════════════════════════════════════════════════════

    // Your Make.com webhook URL (get this from Make.com scenario)
    private static final String WEBHOOK_URL = "https://hook.us1.make.com/xxxxxxxxxxxxx";

    // Path to your patient JSON files (adjust to your OneDrive path)
    private static final String DATA_DIRECTORY =
            System.getProperty("user.home") + "/OneDrive/Documentos/RegistroMedicoApp/Patients";

    // ════════════════════════════════════════════════════════════════

    private static MakeWebhookService webhookService;

    /**
     * Initialize the service (call this in your App or Controller init)
     */
    public static void initialize() {
        webhookService = new MakeWebhookService(DATA_DIRECTORY, WEBHOOK_URL);
    }

    // ─────────────────────────────────────────────────────────────────
    // OPTION 1: Send automatically when app closes
    // ─────────────────────────────────────────────────────────────────

    /**
     * Add this to your Application.stop() method or main stage close handler.
     *
     * Example in your main Application class:
     *
     * @Override
     * public void stop() {
     *     sendTurnosOnClose();
     * }
     */
    public void sendTurnosOnClose() {
        // Run synchronously since app is closing
        boolean success = webhookService.sendTomorrowsTurnosToMake();
        if (success) {
            System.out.println("Turnos de mañana enviados a Make.com");
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // OPTION 2: Manual button in your UI
    // ─────────────────────────────────────────────────────────────────

    /**
     * Example: Add a button to send turnos manually.
     * Add this button to your main view or settings.
     */
    public Button createSendTurnosButton() {
        Button btn = new Button("📤 Enviar turnos de mañana");
        btn.setOnAction(e -> sendTurnosManually(btn));
        return btn;
    }

    private void sendTurnosManually(Button button) {
        button.setDisable(true);
        button.setText("Enviando...");

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                return webhookService.sendTomorrowsTurnosToMake();
            }
        };

        task.setOnSucceeded(e -> {
            button.setDisable(false);
            button.setText("📤 Enviar turnos de mañana");

            if (task.getValue()) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito",
                        "Los turnos fueron enviados a Make.com.\nSerán enviados a Telegram a las 21:00.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "No se pudieron enviar los turnos. Verificar la conexión.");
            }
        });

        task.setOnFailed(e -> {
            button.setDisable(false);
            button.setText("📤 Enviar turnos de mañana");
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    // ─────────────────────────────────────────────────────────────────
    // OPTION 3: Send after saving any turno
    // ─────────────────────────────────────────────────────────────────

    /**
     * Call this after saving a new turno to automatically update Make.com.
     * This way Make.com always has the latest data.
     */
    public void onTurnoSaved() {
        // Run in background to not block UI
        new Thread(() -> {
            webhookService.sendTomorrowsTurnosToMake();
        }).start();
    }

    // ─────────────────────────────────────────────────────────────────
    // Test connection
    // ─────────────────────────────────────────────────────────────────

    public Button createTestButton() {
        Button btn = new Button("🧪 Probar conexión");
        btn.setOnAction(e -> {
            btn.setDisable(true);

            new Thread(() -> {
                boolean success = webhookService.sendTestMessage();

                Platform.runLater(() -> {
                    btn.setDisable(false);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Éxito",
                                "Conexión exitosa! Revisa tu canal de Telegram.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error",
                                "No se pudo conectar con Make.com. Verificar URL del webhook.");
                    }
                });
            }).start();
        });
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────────────────────────────

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}