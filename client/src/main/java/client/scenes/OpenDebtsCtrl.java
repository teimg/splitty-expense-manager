package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class OpenDebtsCtrl implements Initializable, LanguageSwitch, SceneController {

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


    private final MainCtrl mainCtrl;

    private Event event;

    @Inject
    public OpenDebtsCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        noDebtMessage.setVisible(false);
        debtList = new ArrayList<>();

        /**
         * HERE WE NEED TO ADD PROCESS OF CREATING DEBT ENTRIES AND FILLING IN THE ACCORDION
         */

        if (debtList.isEmpty()) {
            noDebtMessage.setVisible(true);
        }
    }

    public void loadEvent(Event event){
        this.event = event;
    }

    public void abortButtonPressed() {
        Event res = this.event;
        this.event = null;
        clearScene();
        mainCtrl.showEventOverview(res);
    }

    private void clearScene() {
        //TODO clear all fields, lists, etc when quiting
    }


    @Override
    public void setLanguage() {
        titleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.Title-label"));
        noDebtMessage.setText(mainCtrl.getTranslator().getTranslation(
                "OpenDebts.NoDebtsMessage-label"));;
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
