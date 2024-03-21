package client.utils;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
                if (!debtor.equals(expense.getPayer())) {
                    debts.add(new Debt(expense.getPayer(), debtor,
                            expense.getAmount()/numDebtors));
                }
            }
        }
    }

    private void buildPanes() {
        for (Debt debt : debts) {
            HBox title = new HBox();
            VBox contents = new VBox();
            title.getChildren().add(new Label(debt.getSummary()));
            String label;
            if (debt.getCreditor().getBankAccount() != null) {
                label = "Bank Account information available, transfer money to:\n" +
                        "Account Holder: " + debt.getCreditor().getName() + "\n" +
                        "IBAN: " + debt.getCreditor().getBankAccount().getIban() + " \n" +
                        "BIC: " + debt.getCreditor().getBankAccount().getBic() + "\n";
                title.getChildren().add(prepareIcons("BankIcon"));
            }
            else {
                label = "No Bank Account info available.\n";
                title.getChildren().add(prepareIcons("NoBankIcon"));
            }
            if (!debt.getDebtor().getEmail().isEmpty()) {
                label = label + "\nEmail Configured: ";
                Button button = new Button("Send Reminder Email");
                button.setOnAction(event -> handleEmailButtonClick(debt));
                contents.getChildren().addAll(new Label(label), button);
                title.getChildren().add(prepareIcons("MailIcon"));
            }
            else {
                label = label + "\nNo Email Configured ";
                contents.getChildren().addAll(new Label(label));
                title.getChildren().add(prepareIcons("NoMailIcon"));
            }
            TitledPane titledPane = new TitledPane();
            titledPane.setText(null);
            titledPane.setGraphic(title);
            titledPane.setContent(contents);
            panes.add(titledPane);
        }
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
        // TODO: Send the email
        System.out.println(debt.getDebtor().getEmail());
    }

}
