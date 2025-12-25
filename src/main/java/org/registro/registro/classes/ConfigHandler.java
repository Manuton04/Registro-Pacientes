package org.registro.registro.classes;

import org.registro.registro.HelloApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;

public class ConfigHandler {
    private static Path path = HelloApplication.path;
    private static String webhookUrl;
    private static String chatId;
    public static LocalTime webhookTime;

    public static void loadConfig() throws IOException {
        inicializarCarpeta();
    }

    public static void inicializarCarpeta() throws IOException {
        System.out.println("Base path: " + path.toAbsolutePath());
        System.out.println("Existe: " + Files.exists(path));

        if (!Files.exists(getPath())) {
            Files.createDirectories(getPath());
            System.out.println("¡Creada la carpeta principal!");
        } else {
            System.out.println("¡La carpeta principal ya existia!");
        }

        Yaml config = new Yaml();

    }

    public static void inicializarCarpetaPacientes() throws IOException {
        System.out.println("Pacientes path: " + getPacientesPath().toAbsolutePath());

        if (!Files.exists(getPacientesPath())) {
            Files.createDirectories(getPacientesPath());
            System.out.println("¡Creada la carpeta pacientes!");
        } else {
            System.out.println("¡La carpeta de pacientes ya existia!");
        }
    }

    public static Path getPath() {
        return path;
    }

    public static Path getPacientesPath() {
        return path.resolve("pacientes");
    }

    public static void setPath(Path newPath) {
        path = newPath;
    }

    public static String getWebhookUrl() {
        return webhookUrl;
    }

    public static void setWebhookUrl(String webhookUrl) {
        ConfigHandler.webhookUrl = webhookUrl;
    }

    public static String getChatId() {
        return chatId;
    }

    public static void setChatId(String chatId) {
        ConfigHandler.chatId = chatId;
    }

    public static LocalTime getWebhookTime() {
        return webhookTime;
    }

    public static void setWebhookTime(LocalTime webhookTime) {
        ConfigHandler.webhookTime = webhookTime;
    }

    public static void setWebhookTime(int hour, int minute) {
        ConfigHandler.webhookTime = LocalTime.of(hour, minute);
    }

}
