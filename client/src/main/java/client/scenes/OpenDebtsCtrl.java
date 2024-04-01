package client.scenes;

import client.ModelView.OpenDebtsMv;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
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
        updateAccordion(openDebtsMv.getPanes());
    }

    private void updateAccordion(ArrayList<TitledPane> panes) {
        accordionDebts.getPanes().clear();
        accordionDebts.getPanes().addAll(panes);
    }

    public void abortButtonPressed() {
        Event res = this.event;
        this.event = null;
        mainCtrl.showEventOverview(res);
    }

    private void clearScene() {
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
