package client.scenes;

import client.ModelView.OpenDebtsMv;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class OpenDebtsCtrl implements LanguageSwitch, SceneController {

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

    private Event event;

    private OpenDebtsMv openDebtsMv;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, OpenDebtsMv openDebtsMv) {
        this.mainCtrl = mainCtrl;
        this.openDebtsMv = openDebtsMv;
    }

    public void loadInfo(Event event){
        this.event = event;
        openDebtsMv.loadInfo(event, mainCtrl.getTranslator(), mainCtrl);
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
            Double converted = mainCtrl.getExchanger().getStandardConversion(value, LocalDate.now());
//            String currentCurrValue = " " + Math.round(mainCtrl.getExchanger().getStandardConversion(
//                    entry.getValue(), LocalDate.now())*100.0)/100.0;
            String keyValueString = key + ", " + String.format("%.2f", converted) + mainCtrl.getExchanger().getCurrentSymbol();
            stringList.add(keyValueString);
        }
        return stringList;
    }


    public void abortButtonPressed() {
        Event res = this.event;
        this.event = null;
        mainCtrl.showEventOverview(res);
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
        loadInfo(event);
    }

}
