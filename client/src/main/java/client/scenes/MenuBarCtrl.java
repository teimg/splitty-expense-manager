package client.scenes;

import client.ModelView.AdminLogInMv;
import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.interfaces.IEmailCommunicator;
import com.google.inject.Inject;
import commons.EmailRequest;
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
    private MenuItem englishButton;

    @FXML
    private MenuItem dutchButton;

    @FXML
    private MenuItem frenchButton;

    @FXML
    private MenuItem templateBtn;

    @FXML
    private MenuItem defaultEmail;

    @FXML
    private Menu currency;

    @FXML
    private MenuItem usdButton;

    @FXML
    private MenuItem eurButton;

    @FXML
    private MenuItem chfButton;

    @FXML
    private MenuItem jpyButton;

    @FXML
    private MenuItem shortCuts;

    private final MainCtrl mainCtrl;

    private final IEmailCommunicator emailCommunicator;

    private final AdminLogInMv adminLogInMv;

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
    public MenuBarCtrl(MainCtrl mainCtrl,
                       EmailCommunicator emailCommunicator, AdminLogInMv adminLogInMv) {
        this.mainCtrl = mainCtrl;
        this.emailCommunicator = emailCommunicator;
        this.adminLogInMv = adminLogInMv;

    }

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
        defaultEmail.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.DefaultEmail-Button"));
        currency.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.Currency-Menu") + " ("
                + mainCtrl.getExchanger().getCurrentCurrency() + ")");
        shortCuts.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.ShortCut-Button"));;
    }

    public void goToAdminLogIn(ActionEvent actionEvent) {
        if(adminLogInMv.isLoggedIn()){
            mainCtrl.showAdminScreen();
            return;
        }
        mainCtrl.showAdminLogIn();
    }

    public void downloadTemplateBtn(ActionEvent actionEvent) {
        downloadTemplate();
    }

    public void downloadGuideBtn(ActionEvent actionEvent) {
        downloadGuide();
    }

    private void downloadGuide() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Download ShortCut Manual");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Downloads"));
        fileChooser.setInitialFileName("keyBoardShortCuts.md");
        File saveLocation = fileChooser.showSaveDialog(mainCtrl.getPrimaryStage());
        Path templateLocation = Paths.get(
                "client", "src", "main", "resources", "guides", "keyBoardShortCuts.md");
        persistTemplate(saveLocation, templateLocation);
    }

    private void persistTemplate(File saveLocation, Path templateLocation){
        // Could be moved to a ModelView class later
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
        Path templateLocation = Paths.get(
                "client", "src", "main", "resources", "languages", "template.properties");
        persistTemplate(saveLocation, templateLocation);
    }

    public void checkDefaultEmail(ActionEvent actionEvent) {
        EmailRequest defaultEmail = new EmailRequest();
        defaultEmail.setTo(defaultEmail.getUsername());
        defaultEmail.setSubject("Default Email");
        defaultEmail.setBody("Default Body - Checking Credentials/Delivery");
        emailCommunicator.sendEmail(defaultEmail);
    }

    public void setUSD(ActionEvent actionEvent) {
        mainCtrl.updateExchanger("USD");
        setCurrencyMenu();
    }

    public void setEUR(ActionEvent actionEvent) {
        mainCtrl.updateExchanger("EUR");
        setCurrencyMenu();
    }

    public void setCHF(ActionEvent actionEvent) {
        mainCtrl.updateExchanger("CHF");
        setCurrencyMenu();
    }

    public void setJPY(ActionEvent actionEvent) {
        mainCtrl.updateExchanger("JPY");
        setCurrencyMenu();
    }

    public void setCurrencyMenu() {
        currency.setText(mainCtrl.getTranslator().getTranslation(
                "MenuBar.Currency-Menu") + " ("
                + mainCtrl.getExchanger().getCurrentCurrency() + ")");
    }

}
