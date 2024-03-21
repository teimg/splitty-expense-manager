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
import java.util.Optional;

public class OpenDebtsCtrl implements LanguageSwitch, SceneController {



    private static class DebtEntry {

        @FXML
        private TitledPane titledPane;

        @FXML
        private Button markRecievedButton;

        @FXML
        private Optional<Label> bankingInfo;

        @FXML
        private Optional<Button> sendEmailButton;

        /**
         * Constructor, current empty.
         */
        public DebtEntry() {}

    }

    @FXML
    private Label titleLabel;

    @FXML
    private Label noDebtMessage;

    @FXML
    private Accordion accordionDebts;

    @FXML
    private ArrayList<DebtEntry> debtList;

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
        this.debts = new DebtsBuilder(event).getDebts();
        noDebtMessage.setVisible(debts.isEmpty());
        this.panes = new DebtsBuilder(event).getPanes();
        updateAccordion();
    }

    private void updateAccordion() {
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
            "OpenDebts.Abort-button"
        ));
    }

    /**
     * Handling button events of the mark received functionality.
     * Will remove it from the open debts.
     * @param debtNumber - int to gauge which debt we are modifying.
     */
    public void handleReceivedButton(int debtNumber) {

        debtList.remove(debtNumber - 1);

        /**
         * HERE WE WILL ADD THE PROCESSING OF REMOVING THE ENTRY FROM THE LIST
         * ALSO WILL CALL BACKEND TO REMOVE IT AS AN OPEN DEBT (PAID)
         */

        if (debtList.isEmpty()) {
            noDebtMessage.setVisible(true);
        }
    }

}
