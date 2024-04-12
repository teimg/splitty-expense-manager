package client.utils;

import client.dialog.Popup;
import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import commons.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DebtsBuilder {

    private ArrayList<Debt> debts;

    private Map<Participant, Double> positiveBalances;
    private ArrayList<TitledPane> panes;

    private final Event event;

    private final Translator translator;

    private final EmailCommunicator emailCommunicator;

    private final ExpenseCommunicator expenseCommunicator;

    private final MainCtrl mainCtrl;

    public DebtsBuilder(Event event, Translator translator,
                        EmailCommunicator emailCommunicator,
                        ExpenseCommunicator expenseCommunicator,
                        MainCtrl mainCtrl) {
        this.event = event;
        this.debts = new ArrayList<>();
        this.positiveBalances = new HashMap<>();
        this.panes = new ArrayList<>();
        this.translator = translator;
        this.emailCommunicator = emailCommunicator;
        this.expenseCommunicator = expenseCommunicator;
        this.mainCtrl = mainCtrl;
        findDebts();
        simplifyDebts();
        simplifyTransitiveNature();
        //uncomment this one for only showing positive balances:
//        findPositiveBalances();
        //this one for showing all balances, both positive and negative
        findBalanceChange();
        removeImprecision();
    }

    public ArrayList<Debt> getDebts() {
        return debts;
    }

    public ArrayList<TitledPane> getPanes() {
        return panes;
    }


    public Map<Participant, Double> getPositiveBalances() {
        return positiveBalances;
    }

    public ArrayList<Debt> findDebts() {
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.addAll(event.getExpenses());
        for (Expense expense : expenses) {
            int numDebtors = expense.getDebtors().size();
            for (Participant debtor : expense.getDebtors()) {
                if (!debtor.equals(expense.getPayer())) {
                    debts.add(new Debt(expense.getPayer(), debtor,
                            expense.getAmount()/numDebtors));
                }
            }
        }
        return debts;
    }

    public ArrayList<Debt> simplifyDebts() {
        ArrayList<Pair<Participant, Participant>> allPossiblePairs = new ArrayList<>();
        for (int i = 0; i < event.getParticipants().size(); i++) {
            for (int j = i + 1; j < event.getParticipants().size(); j++) {
                allPossiblePairs.add(new Pair<>(event.getParticipants().get(i),
                        event.getParticipants().get(j)));
            }
        }
        debts = simplifier(allPossiblePairs);
        return debts;
    }

    public ArrayList<Debt> simplifier(ArrayList<Pair<Participant, Participant>> allPossiblePairs) {
        ArrayList<Debt> simplifiedDebts = new ArrayList<>();
        for (Pair<Participant, Participant> pair : allPossiblePairs) {
            double amount = 0.0;
            for (Debt debt : debts) {
                amount = amount + amountFinder(debt, pair);
            }
            if (amount > 0) {
                simplifiedDebts.add(new Debt(pair.getKey(), pair.getValue(), amount));
            }
            else if (amount < 0) {
                simplifiedDebts.add(new Debt(pair.getValue(), pair.getKey(), (-1*amount)));
            }
        }
        return simplifiedDebts;
    }

    public double amountFinder(Debt debt, Pair<Participant, Participant> pair) {
        double amount = 0;
        if (containsParticipantsSame(debt, pair)) {
            amount = amount + debt.getAmount();
        }
        else if (containsParticipantsOpposite(debt, pair)) {
            amount = amount - debt.getAmount();
        }
        return amount;
    }

    public ArrayList<Debt> simplifyTransitiveNature() {
        Map<Participant, Double> balanceChange = findBalanceChange();
        ArrayList<Debt> transitiveDebts = new ArrayList<>();
        ArrayList<Participant> negative = new ArrayList<>();
        ArrayList<Participant> nonNegative = new ArrayList<>();
        for (Map.Entry<Participant, Double> entry : balanceChange.entrySet()) {
            if (entry.getValue() < 0) {
                negative.add(entry.getKey());
            }
            else {
                nonNegative.add(entry.getKey());
            }
        }
        if (negative.isEmpty()) {
            debts = new ArrayList<>();
            return debts;
        }
        Participant selected = negative.get(0);
        for (int i = 1; i < negative.size(); i++) {
            Participant debtor = negative.get(i);
            transitiveDebts.add(new Debt(selected, debtor, Math.abs(balanceChange.get(debtor))));
        }
        for (Participant creditor : nonNegative) {
            transitiveDebts.add(new Debt(creditor, selected,
                    Math.abs(balanceChange.get(creditor))));
        }
        transitiveDebts.removeIf(debt -> debt.getAmount() == 0);
        debts = transitiveDebts;
        return debts;
    }

    public Map<Participant, Double> findBalanceChange() {
        Map<Participant, Double> balanceChange = new HashMap<>();
        for (Debt debt : debts) {
            if (balanceChange.containsKey(debt.getCreditor())) {
                balanceChange.replace(debt.getCreditor(),
                        balanceChange.get(debt.getCreditor()) + debt.getAmount());
            }
            else {
                balanceChange.put(debt.getCreditor(), debt.getAmount());
            }
            if (balanceChange.containsKey(debt.getDebtor())) {
                balanceChange.replace(debt.getDebtor(),
                        balanceChange.get(debt.getDebtor()) - debt.getAmount());
            }
            else {
                balanceChange.put(debt.getDebtor(), -1*debt.getAmount());
            }
        }
        return balanceChange;
    }

    public Map<Participant, Double> findPositiveBalances() {
        Map<Participant, Double> allBalances = findBalanceChange();

        Map<Participant, Double> negative = new HashMap<>();
        Map<Participant, Double> nonNegative = new HashMap<>();

        for (Map.Entry<Participant, Double> entry : allBalances.entrySet()) {
            if (entry.getValue() < 0) {
                negative.put(entry.getKey(), entry.getValue());
            }
            else {
                nonNegative.put(entry.getKey(), entry.getValue());
            }
        }
        positiveBalances = nonNegative;
        return positiveBalances;
    }

    public boolean containsParticipantsSame(Debt debt, Pair<Participant, Participant> pair) {
        return ((debt.getCreditor().equals(pair.getKey()) &&
                debt.getDebtor().equals(pair.getValue())));
    }

    public boolean containsParticipantsOpposite(Debt debt, Pair<Participant, Participant> pair) {
        return (debt.getCreditor().equals(pair.getValue()) &&
                debt.getDebtor().equals(pair.getKey()));
    }

    public ArrayList<TitledPane> buildPanes() {
        for (Debt debt : debts) {
            HBox title = new HBox();
            VBox contents = new VBox();
            title.getChildren().add(new Label(getSummary(debt)));
            String label;
            if (debt.getCreditor().getBankAccount() != null) {
                label = translator.getTranslation("OpenDebts.BankInfo-label")
                        + "\n" + translator.getTranslation("OpenDebts.AccountHolder-label")
                        + debt.getCreditor().getName() + "\n" +
                        "IBAN: " + debt.getCreditor().getBankAccount().getIban() + " \n" +
                        "BIC: " + debt.getCreditor().getBankAccount().getBic() + "\n";
                title.getChildren().add(prepareIcons("BankIcon"));
            }
            else {
                label = translator.getTranslation(
                        "OpenDebts.NoBankInfo-label") + "\n";
                title.getChildren().add(prepareIcons("NoBankIcon"));
            }
            if (debt.getDebtor().getEmail() != null && !debt.getDebtor().getEmail().isEmpty()) {
                label = label + "\n" + translator.getTranslation("OpenDebts.Email-label")  ;
                Button button = new Button(translator
                        .getTranslation("OpenDebts.ReminderEmail-Button"));
                button.setOnAction(event -> handleEmailButtonClick(debt));
                contents.getChildren().addAll(new Label(label), button);
                title.getChildren().add(prepareIcons("MailIcon"));
            }
            else {
                label = label + "\n" + translator
                        .getTranslation("OpenDebts.NoEmail-label");
                contents.getChildren().addAll(new Label(label));
                title.getChildren().add(prepareIcons("NoMailIcon"));
            }
            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            spacer.setMaxWidth(Double.MAX_VALUE);
            title.getChildren().add(spacer);
            title.setAlignment(Pos.CENTER);
            Button settleButton = createButton(debt);
            title.getChildren().add(settleButton);
            TitledPane titledPane = new TitledPane();
            titledPane.setText(null);
            titledPane.setGraphic(title);
            titledPane.setContent(contents);
            panes.add(titledPane);
        }
        return panes;
    }

    public Button createButton(Debt debt) {
        Button settleButton = new Button(translator
                .getTranslation("OpenDebts.SettleDebt-Button"));
        settleButton.setOnAction(event -> settleDebt(debt));
        settleButton.setFont(Font.font(12));
        settleButton.setPrefWidth(120);
        settleButton.setPrefHeight(5);
        settleButton.setMaxWidth(Double.MAX_VALUE);
        settleButton.setMaxHeight(Double.MAX_VALUE);
        settleButton.setStyle("-fx-background-color: rgb(" +
                "" + 180 + ", " + 180 + ", " + 180 + ");");
        return settleButton;
    }

    public void settleDebt(Debt debt) {
        ArrayList<Participant> debtor = new ArrayList<>();
        debtor.add(debt.getCreditor());
        Expense newExpense = new Expense(event.getId(),
                "Debt Settlement", debt.getAmount(), debt.getDebtor(),
                debtor, LocalDate.now(), null);
        event.addExpense(newExpense);
        expenseCommunicator.createExpense(newExpense);
        mainCtrl.showOpenDebts();
    }

    public String getSummary(Debt debt) {
        return debt.getDebtor().getName() + " " +
                translator.getTranslation("OpenDebts.Summary-owes")
                + " " + Math.round(mainCtrl.getExchanger().getStandardConversion(
                        debt.getAmount(), LocalDate.now())*100.0)/100.0
                + mainCtrl.getExchanger().getCurrentSymbol() + " "
                + translator.getTranslation("OpenDebts.Summary-to")
                + " " + debt.getCreditor().getName();
    }

    public ImageView prepareIcons(String path) {
        String iconPath = "file:client/src/main/resources/client/icons/debt/" + path + ".png";
        Image image = new Image(iconPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        return imageView;
    }

    private void removeImprecision() {
        debts.removeIf(debt -> debt.getAmount() < 0.005);
    }

    public void handleEmailButtonClick(Debt debt) {

        (new Thread(new Runnable() {

            public double rounder(double val) {
                return Math.round(val * 100) / 100.0;
            }

            @Override
            public void run() {
                try {
                    EmailRequest emailRequest = new EmailRequest(debt.getDebtor().getEmail(),
                            "Debt Reminder", "This is a reminder for " +
                            "an open debt. " + "You owe " + debt.getCreditor().getName()
                            + " " + rounder(debt.getAmount()) + "$. (" +
                            rounder(mainCtrl.getExchanger().getStandardConversion
                                    (debt.getAmount(), LocalDate.now())) +
                            " " + mainCtrl.getExchanger().getCurrentSymbol() + ")");
                    emailCommunicator.sendEmail(emailRequest);
                }
                catch (Exception e) {
                    Platform.runLater( () -> {
                        (new Popup("Unable to send the email", Popup.TYPE.ERROR)).show();
                    });
                }

            }
        })).start();

    }

}
