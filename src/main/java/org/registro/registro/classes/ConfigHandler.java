package org.registro.registro.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.registro.registro.classes.Utils.adapters.LocalDateAdapter;
import org.registro.registro.classes.Utils.adapters.LocalDateTimeAdapter;
import org.registro.registro.classes.Utils.condiciones.*;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConfigHandler {
    private static  Sistema sistema;
    private static Path path = Path.of(System.getProperty("user.home"),
            "OneDrive", "Documentos", "RegistroMedicoApp");
    private static String webhookUrl;
    private static String chatId;
    public static LocalTime webhookTime;
    private static Path pacientesPath;


    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void loadConfig() throws IOException {
        inicializarCarpeta();
        pacientesPath = path.resolve("pacientes");
        inicializarCarpetaPacientes();
        sistema = new Sistema();
        loadAll(sistema);

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

}
