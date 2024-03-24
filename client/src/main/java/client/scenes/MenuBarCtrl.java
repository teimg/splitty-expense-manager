package client.scenes;

import client.dialog.Popup;
import client.language.LanguageSwitch;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class MenuBarCtrl implements LanguageSwitch, Initializable {

    @FXML
    private MenuItem adminLogIn;

    @FXML
    private Menu adminMenu;

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
    private MenuItem templateBtn;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLanguageMenu();
    }


    public ImageView createImageView(String url){
        String prefix = "file:client/src/main/resources/client/icons/flags/";
        int wsize =  32;
        int hsize = 16;
        url = prefix + url;
        ImageView imageView = new ImageView();
        Image image = new Image(url, true);
        imageView.setFitWidth(wsize);
        imageView.setFitHeight(hsize);
        imageView.setImage(image);

        return imageView;
    }

    private void setFlagLanguageMenu(String url){
        languageMenu.setGraphic(createImageView(url));
    }

    private void initFlagLanguageMenu(String name){
        switch (name) {
            case "french":
                setFlagLanguageMenu("fr.png");
                break;
            case "dutch":
                setFlagLanguageMenu("nl.png");
                break;
            default:
                setFlagLanguageMenu("uk.png");
                break;
        }
    }

    public void initLanguageMenu(){
        englishButton.setGraphic(createImageView("uk.png"));
        frenchButton.setGraphic(createImageView("fr.png"));
        dutchButton.setGraphic(createImageView("nl.png"));

        templateBtn.setGraphic(createImageView("template.png"));

        String lan = mainCtrl.getTranslator().getCurrentLanguage();
        initFlagLanguageMenu(lan);

    }


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
        // you should navigate to the invitations screen through the event overview screen
    }

    public void showOpenDebts(ActionEvent actionEvent) {
        // you should navigate to the openDebt screen through the event overview screen
    }

    public void showStatistics(ActionEvent actionEvent) {
        // can now be navigated through event overview page
    }

    public void showEventOverview(ActionEvent actionEvent) {}

    public void setEnglish(ActionEvent actionEvent) {
        mainCtrl.updateLanguage("english");
        setFlagLanguageMenu("uk.png");
        setLanguage();
    }

    public void setDutch(ActionEvent actionEvent) {
        mainCtrl.updateLanguage("dutch");
        setFlagLanguageMenu("nl.png");
        setLanguage();
    }

    public void setFrench(ActionEvent actionEvent) {
        mainCtrl.updateLanguage("french");
        setFlagLanguageMenu("fr.png");
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
        adminMenu.setText(mainCtrl.getTranslator().getTranslation(
            "MenuBar.Admin-Menu"));
        adminLogIn.setText(mainCtrl.getTranslator().getTranslation(
            "MenuBar.Management-Button"));
    }

    public void goToAdminLogIn(ActionEvent actionEvent) {
        mainCtrl.showAdminLogIn();
    }

    public void downloadTemplateBtn(ActionEvent actionEvent) {
        downloadTemplate();
    }

    private void persistTemplate(File saveLocation){
        // Could be moved to a ModelView class later
        Path templateLocation = Paths.get(
            "client", "src", "main", "resources", "languages", "template.properties");
        try {
            Files.copy(templateLocation, saveLocation.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            (new Popup("Could not save file", Popup.TYPE.ERROR)).show();
        }
    }

    private void downloadTemplate() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save template");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.setInitialFileName("template.properties");
        File saveLocation = fileChooser.showSaveDialog(mainCtrl.getPrimaryStage());
        persistTemplate(saveLocation);

    }
}
