package client.utils;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class DebtsBuilder {

    private ArrayList<Debt> debts;

    private ArrayList<TitledPane> panes;

    private final Event event;

    public DebtsBuilder(Event event) {
        this.event = event;
        this.debts = new ArrayList<>();
        this.panes = new ArrayList<>();
        findDebts();
        buildPanes();
    }

    public ArrayList<Debt> getDebts() {
        return debts;
    }

    public ArrayList<TitledPane> getPanes() {
        return panes;
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

    private void buildPanes() {
        for (Debt debt : debts) {
            VBox contents = new VBox();
            String label;
            if (debt.getDebtor().getBankAccount() != null) {
                label = "Bank Account information available, transfer money to:\n" +
                        "Account Holder: " + debt.getDebtor().getName() + "\n" +
                        "IBAN: " + debt.getDebtor().getBankAccount().getIban() + " \n" +
                        "BIC: " + debt.getDebtor().getBankAccount().getBic() + "\n";
            }
            else {
                label = "No Bank Account info available.\n";
            }
            if (!debt.getDebtor().getEmail().isEmpty()) {
                label = label + "\nEmail Configured: ";
                Button button = new Button("Send Reminder Email");
                button.setOnAction(event -> handleEmailButtonClick(debt));
                contents.getChildren().addAll(new Label(label), button);
            }
            else {
                label = label + "\nNo Email Configured ";
                contents.getChildren().addAll(new Label(label));
            }
            panes.add(new TitledPane(debt.getSummary(), contents));
        }
    }

    private void handleEmailButtonClick(Debt debt) {
        // TODO: Send the email
        System.out.println(debt.getDebtor().getEmail());
    }

}
