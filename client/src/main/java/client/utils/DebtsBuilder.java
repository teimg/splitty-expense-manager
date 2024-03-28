package client.utils;

import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import commons.*;
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

import java.time.LocalDate;
import java.util.ArrayList;

public class DebtsBuilder {

    private ArrayList<Debt> debts;

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
        this.panes = new ArrayList<>();
        this.translator = translator;
        this.emailCommunicator = emailCommunicator;
        this.expenseCommunicator = expenseCommunicator;
        this.mainCtrl = mainCtrl;
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
                if (!debtor.equals(expense.getPayer())) {
                    debts.add(new Debt(expense.getPayer(), debtor,
                            expense.getAmount()/numDebtors));
                }
            }
        }
    }

    private boolean containsSameParticipants(Debt firstDebt, Debt secondDebt) {
        return (firstDebt.getCreditor().equals(secondDebt.getCreditor()) &&
                firstDebt.getDebtor().equals(secondDebt.getDebtor())) ||
                (firstDebt.getCreditor().equals(secondDebt.getDebtor()) &&
                        firstDebt.getDebtor().equals(secondDebt.getCreditor()));
    }

    private void buildPanes() {
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
            if (!debt.getDebtor().getEmail().isEmpty()) {
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
    }

    private void settleDebt(Debt debt) {
        ArrayList<Participant> debtor = new ArrayList<>();
        debtor.add(debt.getCreditor());
        Expense newExpense = new Expense(event.getId(),
                "Debt Settlement", debt.getAmount(), debt.getDebtor(),
                debtor, LocalDate.now(), null);
        event.addExpense(newExpense);
        expenseCommunicator.createExpense(newExpense);
        mainCtrl.showOpenDebts(event);
    }

    private Button createButton(Debt debt) {
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

    private String getSummary(Debt debt) {
        return debt.getDebtor().getName() + " " +
                translator.getTranslation("OpenDebts.Summary-owes")
                + " " + (Math.round(debt.getAmount() * 100.0) / 100.0)
                + "$ " + translator.getTranslation("OpenDebts.Summary-to")
                + " " + debt.getCreditor().getName();
    }

    private ImageView prepareIcons(String path) {
        String iconPath = "file:client/src/main/resources/client/icons/debt/" + path + ".png";
        Image image = new Image(iconPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        return imageView;
    }

    private void handleEmailButtonClick(Debt debt) {
        try {
            EmailRequest emailRequest = new EmailRequest(debt.getDebtor().getEmail(),
                    "Debt Reminder", "This is a reminder for an open debt. " +
                    "You owe " + debt.getCreditor().getName() + " " + debt.getAmount() + "$.");
            emailCommunicator.sendEmail(emailRequest);
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

}
