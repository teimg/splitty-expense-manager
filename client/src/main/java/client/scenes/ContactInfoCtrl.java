package client.scenes;

import client.language.LanguageSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;

public class ContactInfoCtrl implements LanguageSwitch {

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextField bicField;

    @FXML
    private TextField ibanField;

    @FXML
    private Button abortButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    void abortButtonPressed(ActionEvent event) {

    }

    @FXML
    void addButtonPressed(ActionEvent event) {

    }

    @Override
    public void setLanguage() {

    }
}
