package org.registro.registro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField buscador;

    @FXML
    public String getText() {
        return buscador.getText();
    }
}