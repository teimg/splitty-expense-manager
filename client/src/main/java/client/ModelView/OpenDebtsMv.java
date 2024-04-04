package client.ModelView;

import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.DebtsBuilder;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Participant;
import javafx.scene.control.TitledPane;
import java.util.ArrayList;

public class OpenDebtsMv {

    private final EmailCommunicator emailCommunicator;

    private final ExpenseCommunicator expenseCommunicator;

    private ArrayList<Debt> debts;

    private ArrayList<TitledPane> panes;

    private ArrayList<Participant> positiveBalanceParticipants;

    @Inject
    public OpenDebtsMv(EmailCommunicator emailCommunicator,
                       ExpenseCommunicator expenseCommunicator) {
        this.emailCommunicator = emailCommunicator;
        this.expenseCommunicator = expenseCommunicator;
    }

    public void loadInfo(Event event, Translator translator, MainCtrl mainCtrl) {
        DebtsBuilder debtsBuilder = new DebtsBuilder(event, translator,
                emailCommunicator, expenseCommunicator, mainCtrl);
        debtsBuilder.buildPanes();
        this.debts = debtsBuilder.getDebts();
        this.panes = debtsBuilder.getPanes();
    }

    public boolean checkVisibility() {
        return getDebts().isEmpty();
    }

    public ArrayList<TitledPane> getPanes() {
        return panes;
    }

    public ArrayList<Debt> getDebts() {
        return debts;
    }

    public ArrayList<Participant> getPositiveBalanceParticipants() {
        return positiveBalanceParticipants;
    }

    public void setPositiveBalanceParticipants(ArrayList<Participant> positiveBalanceParticipants) {
        this.positiveBalanceParticipants = positiveBalanceParticipants;
    }
}
