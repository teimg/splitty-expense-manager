package client.scenes;

import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;

public class AdminScreenCtrl {

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

    private final MainCtrl mainCtrl;

    @Inject
    public AdminScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void handleDownload(ActionEvent actionEvent) {
    }

    public void handleOrderBy(ActionEvent actionEvent) {
    }

    public void handleImport(ActionEvent actionEvent) {
    }

    public void handleDelete(ActionEvent actionEvent) {
    }
}
