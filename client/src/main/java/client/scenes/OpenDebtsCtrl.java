package client.scenes;

import client.language.LanguageSwitch;
import client.utils.DebtsBuilder;
import client.utils.SceneController;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;

public class OpenDebtsCtrl implements LanguageSwitch, SceneController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label noDebtMessage;

    @FXML
    private Accordion accordionDebts;

    @FXML
    private Button abortButton;

    private ArrayList<Debt> debts;

    private ArrayList<TitledPane> panes;

    private final MainCtrl mainCtrl;

    private Event event;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void loadInfo(Event event){
        this.event = event;
        this.debts = new DebtsBuilder(event, mainCtrl.getTranslator()).getDebts();
        noDebtMessage.setVisible(debts.isEmpty());
        this.panes = new DebtsBuilder(event, mainCtrl.getTranslator()).getPanes();
        updateAccordion();
    }

    private void updateAccordion() {
        accordionDebts.getPanes().clear();
        accordionDebts.getPanes().addAll(panes);
    }

    public void abortButtonPressed() {
        Event res = this.event;
        this.event = null;
        clearScene();
        mainCtrl.showEventOverview(res);
    }

    private void clearScene() {
        // TODO clear all fields, lists, etc when quiting
    }

    @Override
    public void setLanguage() {
        titleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.Title-label"));
        noDebtMessage.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.NoDebtsMessage-label"));;
        abortButton.setText(mainCtrl.getTranslator().getTranslation(
            "OpenDebts.Abort-button"));
        loadInfo(event);
    }

}
