package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;

public class AdminScreenCtrl implements LanguageSwitch, SceneController {

    @FXML
    private Label eventsLabel;

    @FXML
    private Label adminTitleLabel;

    @FXML
    private Button downloadButton;

    @FXML
    private RadioButton titleRadioButton;

    @FXML
    private RadioButton creationRadioButton;

    @FXML
    private RadioButton activityRadioButton;

    @FXML
    private Label orderByLabel;

    @FXML
    private ListView<Event> eventListView;

    @FXML
    private Button importButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    private final MainCtrl mainCtrl;

    @Inject
    public AdminScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void setLanguage() {
        eventsLabel.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Events-label"));
        adminTitleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Title-label"));
        creationRadioButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Creation-RadioButton"));
        titleRadioButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Title-RadioButton"));
        activityRadioButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Activity-RadioButton"));
        orderByLabel.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.OrderBy-label"));
        importButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Import-Button"));
        deleteButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Delete-Button"));
        downloadButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Download-Button"));
        backButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Back-Button"));
    }

    public void handleDownload(ActionEvent actionEvent) {
    }

    public void handleOrderBy(ActionEvent actionEvent) {
    }

    public void handleImport(ActionEvent actionEvent) {
    }

    public void handleDelete(ActionEvent actionEvent) {
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showStartScreen();
    }
}
