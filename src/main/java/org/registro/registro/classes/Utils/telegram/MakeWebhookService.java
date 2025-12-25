package org.registro.registro.services;

import com.google.gson.*;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Turno;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service that collects tomorrow's turnos and sends them to Make.com
 * for scheduled delivery to Telegram at 9 PM.
 *
 * Setup:
 * 1. Create a Make.com account (free)
 * 2. Create a new Scenario with Webhook trigger
 * 3. Copy the webhook URL and set it in WEBHOOK_URL
 * 4. Add a "Delay" module set to wait until 21:00
 * 5. Add a Telegram "Send Message" module
 */
public class MakeWebhookService {

    private final Path dataDirectory;
    private final String webhookUrl;
    private final HttpClient httpClient;
    private final Gson gson;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Creates a new MakeWebhookService.
     *
     * @param dataDirectoryPath Path to folder containing patient JSON files
     * @param webhookUrl Your Make.com webhook URL
     */
    public MakeWebhookService(String dataDirectoryPath, String webhookUrl) {
        this.dataDirectory = Paths.get(dataDirectoryPath);
        this.webhookUrl = webhookUrl;
        this.httpClient = HttpClient.newHttpClient();

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create();
    }

    /**
     * Scans all patient files, finds tomorrow's turnos, and sends to Make.com.
     * Call this when the app closes or via a manual button.
     *
     * @return true if sent successfully
     */
    public boolean sendTomorrowsTurnosToMake() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<TurnoConPaciente> turnos = getTurnosForDate(tomorrow);

            String formattedMessage = formatTurnosMessage(turnos, tomorrow);

            JsonObject payload = new JsonObject();
            payload.addProperty("message", formattedMessage);
            payload.addProperty("date", tomorrow.format(DATE_FORMATTER));
            payload.addProperty("turnoCount", turnos.size());
            payload.addProperty("isEmpty", turnos.isEmpty());

            return sendToWebhook(payload);

        } catch (Exception e) {
            System.err.println("Error al enviar turnos a Make.com: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a test message to verify the webhook is working.
     */
    public boolean sendTestMessage() {
        JsonObject payload = new JsonObject();
        payload.addProperty("message", "🧪 <b>Mensaje de prueba</b>\n\nLa conexión con Make.com funciona correctamente!");
        payload.addProperty("isTest", true);

        return sendToWebhook(payload);
    }

    /**
     * Gets all turnos for a specific date across all patient files.
     */
    private List<TurnoConPaciente> getTurnosForDate(LocalDate date) throws IOException {
        List<TurnoConPaciente> turnosForDate = new ArrayList<>();

        if (!Files.exists(dataDirectory)) {
            System.err.println("Directorio de datos no existe: " + dataDirectory);
            return turnosForDate;
        }

        try (var files = Files.list(dataDirectory)) {
            List<Path> jsonFiles = files
                    .filter(path -> path.toString().endsWith(".json"))
                    .collect(Collectors.toList());

            for (Path file : jsonFiles) {
                try {
                    String content = Files.readString(file);
                    JsonObject patientJson = JsonParser.parseString(content).getAsJsonObject();

                    // Get patient name
                    String nombre = getStringOrEmpty(patientJson, "nombre");
                    String apellido = getStringOrEmpty(patientJson, "apellido");
                    String nombreCompleto = (nombre + " " + apellido).trim();

                    if (nombreCompleto.isEmpty()) {
                        nombreCompleto = "Paciente sin nombre";
                    }

                    // Get turnos array
                    if (patientJson.has("turnos") && patientJson.get("turnos").isJsonArray()) {
                        JsonArray turnosArray = patientJson.getAsJsonArray("turnos");

                        for (JsonElement turnoElement : turnosArray) {
                            JsonObject turnoObj = turnoElement.getAsJsonObject();

                            if (turnoObj.has("fecha")) {
                                String fechaStr = turnoObj.get("fecha").getAsString();
                                LocalDateTime fechaTurno = LocalDateTime.parse(fechaStr);

                                if (fechaTurno.toLocalDate().equals(date)) {
                                    TurnoConPaciente tcp = new TurnoConPaciente();
                                    tcp.pacienteNombre = nombreCompleto;
                                    tcp.fecha = fechaTurno;
                                    tcp.hora = fechaTurno.format(TIME_FORMATTER);
                                    tcp.descripcion = extractDescripcion(turnoObj);
                                    turnosForDate.add(tcp);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error procesando archivo: " + file.getFileName() + " - " + e.getMessage());
                }
            }
        }

        // Sort by time
        turnosForDate.sort(Comparator.comparing(t -> t.fecha));

        return turnosForDate;
    }

    private String getStringOrEmpty(JsonObject obj, String field) {
        if (obj.has(field) && !obj.get(field).isJsonNull()) {
            return obj.get(field).getAsString();
        }
        return "";
    }

    private String extractDescripcion(JsonObject turno) {
        if (!turno.has("descripcion")) return "Sin descripción";

        JsonElement desc = turno.get("descripcion");
        if (desc.isJsonArray()) {
            List<String> items = new ArrayList<>();
            for (JsonElement e : desc.getAsJsonArray()) {
                items.add(e.getAsString());
            }
            return items.isEmpty() ? "Sin descripción" : String.join(", ", items);
        }
        return desc.getAsString();
    }

    /**
     * Formats the message for Telegram (HTML format).
     */
    private String formatTurnosMessage(List<TurnoConPaciente> turnos, LocalDate date) {
        StringBuilder message = new StringBuilder();

        message.append("📋 <b>Turnos para mañana (")
                .append(date.format(DATE_FORMATTER))
                .append(")</b>\n\n");

        if (turnos.isEmpty()) {
            message.append("✨ No hay turnos programados.");
        } else {
            message.append("Total: ").append(turnos.size()).append(" turno(s)\n\n");

            for (TurnoConPaciente turno : turnos) {
                message.append("• <b>")
                        .append(turno.hora)
                        .append("</b> - ")
                        .append(turno.pacienteNombre)
                        .append("\n   ")
                        .append(turno.descripcion)
                        .append("\n\n");
            }
        }

        return message.toString();
    }

    /**
     * Sends the payload to Make.com webhook.
     */
    private boolean sendToWebhook(JsonObject payload) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("✅ Datos enviados a Make.com exitosamente");
                return true;
            } else {
                System.err.println("❌ Error al enviar a Make.com. Código: " + response.statusCode());
                System.err.println("Respuesta: " + response.body());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error de conexión con Make.com: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Helper classes
    // ─────────────────────────────────────────────────────────────

    /**
     * Internal class to hold turno info with patient name.
     */
    private static class TurnoConPaciente {
        String pacienteNombre;
        LocalDateTime fecha;
        String hora;
        String descripcion;
    }

    /**
     * Gson adapter for LocalDateTime.
     */
    private static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type type,
                                         JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString());
        }
    }

    /**
     * Gson adapter for LocalDate.
     */
    private static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement json, java.lang.reflect.Type type,
                                     JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }
}