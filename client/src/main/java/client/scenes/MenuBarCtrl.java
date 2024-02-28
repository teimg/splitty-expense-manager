package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class MenuBarCtrl {

    @FXML
    private MenuItem startScreen;

    @FXML
    private MenuItem englishButton;

    @FXML
    private MenuItem dutchButton;

    @FXML
    private MenuItem frenchButton;

    @FXML
    private MenuItem quoteOverview;

    @FXML
    private MenuItem addQuote;

    @FXML
    private MenuItem invitation;

    @FXML
    private MenuItem openDebts;

    @FXML
    private MenuItem statistics;

    @FXML
    private MenuItem eventOverview;

    @FXML
    private MenuItem addExpense;

    @FXML
    private TextField newEventField;

    @FXML
    private TextField joinEventField;

    @FXML
    private Button createEventButton;

    @FXML
    private Button joinEventButton;

    private final MainCtrl mainCtrl;

    @Inject
    public MenuBarCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void showQuoteOverview(ActionEvent actionEvent) {
        mainCtrl.showOverview();
    }

    public void showAddQuote(ActionEvent actionEvent) {
        mainCtrl.showAdd();
    }

    public void showStartScreen(ActionEvent actionEvent) {
        mainCtrl.showStartScreen();
    }

    public void showAddEditExpense(ActionEvent actionEvent) {
        mainCtrl.showAddEditExpense();
    }

    public void showInvitations(ActionEvent actionEvent) {
        mainCtrl.showInvitation();
    }

    public void showOpenDebts(ActionEvent actionEvent) {
        mainCtrl.showOpenDebts();
    }

    public void showStatistics(ActionEvent actionEvent) {
        mainCtrl.showStatistics();
    }

    public void showContactInfo(ActionEvent actionEvent) {
        mainCtrl.showContactInfo();
    }

    public void showEventOverview(ActionEvent actionEvent) {
        // TODO: Fix event overview then this can be implemented
    }

    // TODO: Decide on implementation of languages (where to store it - probably in main controller)
    public void setEnglish(ActionEvent actionEvent) {
    }

    public void setDutch(ActionEvent actionEvent) {
    }

    public void setFrench(ActionEvent actionEvent) {
    }
}
