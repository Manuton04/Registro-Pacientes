module org.registro.registro {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires com.google.gson;
    requires java.net.http;
    opens org.registro.registro.classes to com.google.gson;

    opens org.registro.registro to javafx.fxml;
    exports org.registro.registro;

    opens org.registro.registro.classes.Utils.adapters to com.google.gson;
}