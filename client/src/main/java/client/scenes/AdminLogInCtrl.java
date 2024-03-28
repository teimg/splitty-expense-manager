package client.scenes;

import client.ModelView.AdminLogInMv;
import client.language.LanguageSwitch;
import client.utils.SceneController;
import client.utils.communicators.implementations.EventCommunicator;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLogInCtrl implements Initializable, LanguageSwitch, SceneController {

    @FXML
    private Label logInLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private Button enterButton;

    private final MainCtrl mainCtrl;

    private final AdminLogInMv adminLogInMv;

    @Inject
    public AdminLogInCtrl(MainCtrl mainCtrl, AdminLogInMv adminLogInMv) {
        this.mainCtrl = mainCtrl;
        this.adminLogInMv = adminLogInMv;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.textProperty().bindBidirectional(adminLogInMv.passwordFieldProperty());
        passwordField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                enterPassword();
            }
        });

    }

    public void enterPassword() {
        try {
            adminLogInMv.validatePassword();
            mainCtrl.showAdminScreen();
        }catch (Exception e){
            handleException(e, mainCtrl.getTranslator());
        }
    }

    @Override
    public void setLanguage() {
        logInLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AdminLogIn.LogIn-label"));
        passwordLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AdminLogIn.Password-label"));
        enterButton.setText(mainCtrl.getTranslator().getTranslation(
            "AdminLogIn.Enter-Button"));
    }
}
