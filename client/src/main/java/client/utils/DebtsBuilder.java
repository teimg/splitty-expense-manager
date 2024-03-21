package client.utils;

import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;

import java.util.ArrayList;

public class DebtsBuilder {

    private ArrayList<Debt> debts;

    private final Event event;

    public DebtsBuilder(Event event) {
        this.event = event;
        this.debts = new ArrayList<>();
        findDebts();
    }

    public ArrayList<Debt> getDebts() {
        return debts;
    }

    public void removeDebt(Debt debt) {
        debts.remove(debt);
    }

    private void findDebts() {
        ArrayList<Expense> expenses = (ArrayList<Expense>) event.getExpenses();
        for (Expense expense : expenses) {
            int numDebtors = expense.getDebtors().size();
            for (Participant debtor : expense.getDebtors()) {
                debts.add(new Debt(expense.getPayer(), debtor,
                        expense.getAmount()/numDebtors));
            }
        }
    }

}
