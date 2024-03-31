package client.scenes;

import client.language.LanguageSwitch;
import client.utils.DebtsBuilder;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import client.utils.scene.SceneController;
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

    @FXML
    private ScrollPane scrollPlane;

    private ArrayList<Debt> debts;

    private ArrayList<TitledPane> panes;

    private final MainCtrl mainCtrl;

    private final EmailCommunicator emailCommunicator;

    private Event event;

    private DebtsBuilder debtsBuilder;

    private final ExpenseCommunicator expenseCommunicator;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl, EmailCommunicator emailCommunicator,
                         ExpenseCommunicator expenseCommunicator) {
        this.mainCtrl = mainCtrl;
        this.emailCommunicator = emailCommunicator;
        this.expenseCommunicator = expenseCommunicator;
    }

    public void loadInfo(Event event){
        this.event = event;
        this.debtsBuilder = new DebtsBuilder(event,
                mainCtrl.getTranslator(), emailCommunicator,
                expenseCommunicator, mainCtrl);
        this.debts = debtsBuilder.getDebts();
        noDebtMessage.setVisible(debts.isEmpty());
        accordionDebts.setVisible(!debts.isEmpty());
        scrollPlane.setVisible(!debts.isEmpty());
        this.panes = debtsBuilder.getPanes();
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
        //Rolling back
//        // Clear the debts list
//        if (debts != null) {
//            debts.clear();
//        }
//
//        // Clear the panes in the accordion
//        if (panes != null) {
//            panes.clear();
//        }
//        accordionDebts.getPanes().clear();
//
//        // Reset the visibility and text of the noDebtMessage
//        noDebtMessage.setVisible(true);
//        noDebtMessage.setText(mainCtrl.getTranslator().
//                getTranslation("OpenDebts.NoDebtsMessage-label"));
//
//        // Reset the visibility of the accordion and scroll pane
//        accordionDebts.setVisible(false);
//        scrollPlane.setVisible(false);
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
