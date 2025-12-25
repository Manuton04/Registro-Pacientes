package org.registro.registro.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.registro.registro.classes.Utils.adapters.LocalDateAdapter;
import org.registro.registro.classes.Utils.adapters.LocalDateTimeAdapter;
import org.registro.registro.classes.Utils.condiciones.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConfigHandler {
    private static  Sistema sistema;
    private static Path path;
    private static String webhookUrl;
    private static String chatId;
    public static LocalTime webhookTime;
    private static Path pacientesPath;
    private static Path configPath;
    private static JsonObject  configJson;


    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void loadConfig() throws IOException {
        path = getAppDataPath();
        inicializarCarpeta();
        pacientesPath = path.resolve("pacientes");
        inicializarCarpetaPacientes();
        sistema = new Sistema();
        loadAll(sistema);

        configPath = path.resolve("config.json");
        crearCarpetConfig();

        String json = Files.readString(configPath);
        configJson = JsonParser.parseString(json).getAsJsonObject();

        webhookUrl = getString("telegram.webhookUrl");
        chatId = getString("telegram.chatId");
        webhookTime = LocalTime.of(getInt("app.notificationHour"), getInt("app.notificationMinute"));


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

    }

    public static void crearCarpetConfig() throws IOException {
        if (!Files.exists(configPath)) {
            try (InputStream is = ConfigHandler.class.getResourceAsStream("/org/registro/registro/config.json")) {
                Files.createDirectories(configPath.getParent());
                Files.copy(is, configPath);
            }
        }
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

    public static void loadAll(Sistema sistema) throws IOException{
        if (!Files.exists(pacientesPath))
            return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pacientesPath, "*.json")) {
            for (Path archivo : stream) {
                String json = Files.readString(archivo);
                Paciente p = gson.fromJson(json, Paciente.class);
                sistema.addPaciente(p);
            }
        }

    }

    public static void saveAll(Sistema sistema) throws IOException{
        ConfigHandler.inicializarCarpetaPacientes();
        for (Paciente paciente : sistema.getHashMap().values()) {
            savePaciente(paciente);
        }
    }

    public static void savePaciente(Paciente paciente) throws IOException{
        final Path pacientesPath = ConfigHandler.getPacientesPath();
        Path filePath = pacientesPath.resolve(paciente.getId() + ".json");

        try (Writer writer = Files.newBufferedWriter(filePath)) {
            ConfigHandler.getGson().toJson(paciente, writer);
        }
    }

    public static Path getPath() {
        return path;
    }

    public static Path getPacientesPath() {
        return path.resolve("pacientes");
    }

    public static String getWebhookUrl() {
        return webhookUrl;
    }

    public static void setWebhookUrl(String webhookUrl) throws IOException {
        ConfigHandler.webhookUrl = webhookUrl;
        ConfigHandler.setString("telegram.webhookUrl", webhookUrl);
        ConfigHandler.saveConfig();
    }

    public static String getChatId() {
        return chatId;
    }

    public static void setChatId(String chatId) throws IOException {
        ConfigHandler.chatId = chatId;
        ConfigHandler.setString("telegram.chatId", chatId);
        ConfigHandler.saveConfig();
    }

    public static LocalTime getWebhookTime() {
        return webhookTime;
    }

    public static void setWebhookTime(LocalTime webhookTime) throws IOException {
        ConfigHandler.webhookTime = webhookTime;
        ConfigHandler.setString("app.notificationHour", String.valueOf(webhookTime.getHour()));
        ConfigHandler.setString("app.notificationMinute", String.valueOf(webhookTime.getMinute()));
        ConfigHandler.saveConfig();

    }

    public static void setWebhookTime(int hour, int minute) throws IOException {
        ConfigHandler.webhookTime = LocalTime.of(hour, minute);
        ConfigHandler.setString("app.notificationHour", String.valueOf(hour));
        ConfigHandler.setString("app.notificationMinute", String.valueOf(minute));
        ConfigHandler.saveConfig();
    }

    public static Gson getGson() {
        return gson;
    }

    public static Sistema getSistema() {
        return sistema;
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

    public static Path getAppDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            // Windows AppData
            String appData = System.getenv("LOCALAPPDATA");  // C:\Users\manut\AppData\Local
            if (appData != null) {
                return Path.of(appData, "RegistroMedicoApp");
            }
        }

        // Mac/Linux - use hidden folder in home
        return Path.of(userHome, "RegistroMedicoApp");
    }

    public static String getString(String path) {
        String[] keys = path.split("\\.");
        JsonObject current = configJson;

        for (int i = 0; i < keys.length - 1; i++) {
            current = current.getAsJsonObject(keys[i]);
        }

        return current.get(keys[keys.length - 1]).getAsString();
    }

    public static int getInt(String path) {
        String[] keys = path.split("\\.");
        JsonObject current = configJson;

        for (int i = 0; i < keys.length - 1; i++) {
            current = current.getAsJsonObject(keys[i]);
        }

        return current.get(keys[keys.length - 1]).getAsInt();
    }

    public static void setString(String path, String value) {
        String[] keys = path.split("\\.");
        JsonObject current = configJson;

        for (int i = 0; i < keys.length - 1; i++) {
            current = current.getAsJsonObject(keys[i]);
        }

        current.addProperty(keys[keys.length - 1], value);
    }

    public static void setInt(String path, int value) {
        String[] keys = path.split("\\.");
        JsonObject current = configJson;

        for (int i = 0; i < keys.length - 1; i++) {
            current = current.getAsJsonObject(keys[i]);
        }

        current.addProperty(keys[keys.length - 1], value);
    }

    public static void saveConfig() throws IOException {
        String jsonString = gson.toJson(configJson);
        Files.writeString(configPath, jsonString);
        System.out.println("✅ Config guardado");
    }

}
