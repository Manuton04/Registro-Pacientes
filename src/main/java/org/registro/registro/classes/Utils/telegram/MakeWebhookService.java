package org.registro.registro.classes.Utils.telegram;

import com.google.gson.JsonObject;
import org.registro.registro.classes.ConfigHandler;
import org.registro.registro.classes.Paciente;
import org.registro.registro.classes.Sistema;
import org.registro.registro.classes.Turno;
import org.registro.registro.classes.Utils.condiciones.CondicionFechaTurno;

import java.net.URI;
import java.net.http.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MakeWebhookService {

    private final String webhookUrl;
    private final HttpClient httpClient;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public MakeWebhookService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

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

    private List<TurnoConPaciente> getTurnosForDate(LocalDate date) {
        List<TurnoConPaciente> turnosForDate = new ArrayList<>();

        Sistema sistema = ConfigHandler.getSistema();

        // Use CondicionFechaTurno to filter patients with turnos on that date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        CondicionFechaTurno condicion = new CondicionFechaTurno(startOfDay, endOfDay);

        List<Paciente> pacientes = sistema.getPacientes(condicion);

        for (Paciente paciente : pacientes) {
            String nombreCompleto = paciente.getNombre() + " " + paciente.getApellido();

            for (Turno turno : paciente.getTurnos()) {
                if (turno.getFecha().toLocalDate().equals(date)) {
                    TurnoConPaciente tcp = new TurnoConPaciente();
                    tcp.pacienteNombre = nombreCompleto;
                    tcp.fecha = turno.getFecha();
                    tcp.hora = turno.getFecha().format(TIME_FORMATTER);
                    tcp.descripcion = String.join(", ", turno.getDescripcion());
                    turnosForDate.add(tcp);
                }
            }
        }

        // Sort by time
        turnosForDate.sort(Comparator.comparing(t -> t.fecha));

        return turnosForDate;
    }

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

    private boolean sendToWebhook(JsonObject payload) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("✅ Turnos enviados a Make.com");
                return true;
            } else {
                System.err.println("❌ Error. Código: " + response.statusCode());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }

    public boolean sendTestMessage() {
        JsonObject payload = new JsonObject();
        payload.addProperty("message", "🧪 <b>Test</b>\n\nConexión exitosa!");
        payload.addProperty("isTest", true);
        return sendToWebhook(payload);
    }

    private static class TurnoConPaciente {
        String pacienteNombre;
        LocalDateTime fecha;
        String hora;
        String descripcion;
    }
}