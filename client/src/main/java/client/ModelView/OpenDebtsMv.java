package client.ModelView;

import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.DebtsBuilder;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import client.utils.communicators.interfaces.IEventUpdateProvider;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.EventChange;
import commons.Participant;
import javafx.scene.control.TitledPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OpenDebtsMv {

    private final IEventUpdateProvider eventUpdateProvider;

    private final EmailCommunicator emailCommunicator;

    private final ExpenseCommunicator expenseCommunicator;

    private ArrayList<Debt> debts;

    private ArrayList<TitledPane> panes;

    private Map<Participant, Double> positiveBalanceParticipants;

    @Inject
    public OpenDebtsMv(EmailCommunicator emailCommunicator,
                       ExpenseCommunicator expenseCommunicator,
                       IEventUpdateProvider eventUpdateProvider) {
        this.emailCommunicator = emailCommunicator;
        this.expenseCommunicator = expenseCommunicator;
        this.eventUpdateProvider = eventUpdateProvider;
    }

    public void loadInfo(Translator translator, MainCtrl mainCtrl) {

        DebtsBuilder debtsBuilder = new DebtsBuilder(eventUpdateProvider.event(), translator,
                emailCommunicator, expenseCommunicator, mainCtrl);
        debtsBuilder.buildPanes();
        this.debts = debtsBuilder.getDebts();
        this.panes = debtsBuilder.getPanes();
        //this one to show only positive balance participants:
//        this.positiveBalanceParticipants = debtsBuilder.getPositiveBalances();
        //this one to show all balances
        this.positiveBalanceParticipants = debtsBuilder.findBalanceChange();

        Map<Participant, Double> simplified = new HashMap<>();
        for (var p : positiveBalanceParticipants.entrySet()) {
            if (Math.abs(p.getValue()) >= 0.005) {
                simplified.put(p.getKey(), p.getValue());
            }
        }
        positiveBalanceParticipants = simplified;
    }

    public Event getEvent() {
        return eventUpdateProvider.event();
    }

    public void onUpdate(Consumer<EventChange> handler) {
        eventUpdateProvider.setUpdateHandler(handler);
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

    public Map<Participant, Double> getPositiveBalanceParticipants() {
        return positiveBalanceParticipants;
    }

}
