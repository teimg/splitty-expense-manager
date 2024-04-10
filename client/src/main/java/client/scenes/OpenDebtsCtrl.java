package client.scenes;

import client.ModelView.OpenDebtsMv;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Participant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.time.LocalDate;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Map;

public class OpenDebtsCtrl implements LanguageSwitch, SceneController, ShortCuts {

    @FXML
    private Label titleLabel;

    @FXML
    private Label titleLabel1;

    @FXML
    private Label noDebtMessage;

    @FXML
    private Accordion accordionDebts;

    @FXML
    private Button abortButton;

    @FXML
    private ScrollPane scrollPlane;

    @FXML
    private ListView<String> expensesList;


    private final MainCtrl mainCtrl;

    private OpenDebtsMv openDebtsMv;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, OpenDebtsMv openDebtsMv) {
        this.mainCtrl = mainCtrl;
        this.openDebtsMv = openDebtsMv;
    }

    public void loadInfo(){
        openDebtsMv.onUpdate(change -> {
            Platform.runLater(this::loadInfo);
        });
        openDebtsMv.loadInfo(mainCtrl.getTranslator(), mainCtrl);
        noDebtMessage.setVisible(openDebtsMv.checkVisibility());
        accordionDebts.setVisible(!openDebtsMv.checkVisibility());
        scrollPlane.setVisible(!openDebtsMv.checkVisibility());
        expensesList.setVisible(!openDebtsMv.checkVisibility());
        titleLabel1.setVisible(!openDebtsMv.checkVisibility());
        updateAccordion(openDebtsMv.getPanes());
        updatePositiveBalances(openDebtsMv.getPositiveBalanceParticipants());
    }

    private void updateAccordion(ArrayList<TitledPane> panes) {
        accordionDebts.getPanes().clear();
        accordionDebts.getPanes().addAll(panes);
    }

//    private void updatePositiveBalances(ArrayList<Participant> list) {
//        expensesList.getItems().clear();
//        expensesList.getItems().addAll(list);
//    }

    private void updatePositiveBalances(Map<Participant, Double> map) {
        expensesList.getItems().clear();
        ArrayList<String> r = convertToStringList(map);
        expensesList.getItems().addAll(r);
    }

    private ArrayList<String> convertToStringList(Map<Participant, Double> map) {
        ArrayList<String> stringList = new ArrayList<>();
        for (Map.Entry<Participant, Double> entry : map.entrySet()) {
            String key = entry.getKey().getName();
            Double value = entry.getValue();
            Double converted = mainCtrl
                    .getExchanger().getStandardConversion(value, LocalDate.now());
            String keyValueString = key + ", "
                    + String.format("%.2f", converted)
                    + mainCtrl.getExchanger().getCurrentSymbol();
            stringList.add(keyValueString);
        }
        return stringList;
    }


    public void abortButtonPressed() {
        mainCtrl.showEventOverview(openDebtsMv.getEvent());
    }

    @Override
    public void setLanguage() {
        titleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.Title-label"));
        noDebtMessage.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.NoDebtsMessage-label"));;
        abortButton.setText(mainCtrl.getTranslator().getTranslation(
            "OpenDebts.Abort-button"));
        titleLabel1.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.PositiveBalanceTitle-label"));
        loadInfo();
    }

    @Override
    public void listeners() {
        Scene s = titleLabel.getScene();
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.B, this::abortButtonPressed);
    }

}
