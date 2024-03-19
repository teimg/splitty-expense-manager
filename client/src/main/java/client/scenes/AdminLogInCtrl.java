package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AdminLogInCtrl implements LanguageSwitch, SceneController {

    @FXML
    private Label logInLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private Button enterButton;

    private final MainCtrl mainCtrl;

    @Inject
    public AdminLogInCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }


    public void enterPassword(ActionEvent actionEvent) {
        // TODO: Link to admin page/check correct password
        System.out.println(passwordField.getText());
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
