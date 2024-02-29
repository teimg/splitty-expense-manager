package client.scenes;

import client.language.LanguageSwitch;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MenuBarCtrl implements LanguageSwitch {

    @FXML
    private Menu languageMenu;

    @FXML
    private Menu setSceneMenu;

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

    public void showEventOverview(ActionEvent actionEvent) {}

    public void setEnglish(ActionEvent actionEvent) {
        mainCtrl.updateLanguage("english");
        setLanguage();
    }

    public void setDutch(ActionEvent actionEvent) {
        mainCtrl.updateLanguage("dutch");
        setLanguage();
    }

    public void setFrench(ActionEvent actionEvent) {
        mainCtrl.updateLanguage("french");
        setLanguage();
    }

    @Override
    public void setLanguage() {
        languageMenu.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.Language-Menu"));
        setSceneMenu.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.SetScene-Menu"));
        englishButton.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.English-Button"));
        dutchButton.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.Dutch-Button"));
        frenchButton.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.French-Button"));
    }
}
