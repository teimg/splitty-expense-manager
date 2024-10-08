package client.scenes;

import client.ModelView.AdminLogInMv;
import client.dialog.Popup;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLogInCtrl extends  SceneController
    implements Initializable, LanguageSwitch, ShortCuts {

    @FXML
    private Label logInLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private Button goBackBtn;


    @FXML
    private Button enterButton;

    private final MainCtrl mainCtrl;

    private final AdminLogInMv adminLogInMv;

    @Inject
    public AdminLogInCtrl(MainCtrl mainCtrl, AdminLogInMv adminLogInMv) {
        super(mainCtrl);
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
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.adminWelcome"), Popup.TYPE.INFO).showAndWait();
        }catch (Exception e){
            handleException(e);
        }
    }

    public void handleGoBackBtn() {
        passwordField.textProperty().setValue("");
        mainCtrl.showStartScreen();
    }

    @Override
    public void setLanguage() {
        logInLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AdminLogIn.LogIn-label"));
        passwordLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AdminLogIn.Password-label"));
        enterButton.setText(mainCtrl.getTranslator().getTranslation(
            "AdminLogIn.Enter-Button"));
        goBackBtn.setText(mainCtrl.getTranslator().getTranslation(
            "AdminScreen.Back-Button"
        ));
    }

    @Override
    public void listeners() {

    }
}
