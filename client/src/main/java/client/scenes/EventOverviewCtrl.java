package client.scenes;

import client.language.LanguageSwitch;
import client.utils.communicators.implementations.EventCommunicator;
import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import client.utils.communicators.implementations.ParticipantCommunicator;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;

import commons.Participant;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


public class EventOverviewCtrl implements Initializable, LanguageSwitch, SceneController {

    private final IEventCommunicator eventCommunicator;

    private final IParticipantCommunicator participantCommunicator;

    private Event event;

    private Participant selectedPayer;

    private Task<Void> longPollingTask = null;
    private Thread pollingThread = null;

    private class ExpenseCellFactory
            implements Callback<ListView<Expense>, ListCell<Expense>> {
        /**
         * Should return a new ListCell usable in the expense ListView.
         * @param listView the expense ListView
         * @return a usable ListCell
         */
        @Override
        public ListCell<Expense> call(ListView<Expense> listView) {
            return new ListCell<>() {
                /**
                 * Ran when a cell is shown with a new items or is shown emptied.
                 * @param expense The new item for the cell.
                 * @param empty whether this cell represents data from the list. If it
                 *        is empty, then it does not represent any domain data, but is a cell
                 *        being used to render an "empty" row.
                 */
                @Override
                public void updateItem(Expense expense, boolean empty) {
                    super.updateItem(expense, empty);
                    if (empty) {
                        setText(null);
                        return;
                    }
                    setText(expenseDescription(expense));
                }

                private String expenseDescription(Expense expense) {
                    StringBuilder desc = new StringBuilder(expense.getDate().toString() + "    "
                            + expense.getPayer().getName() + " " + mainCtrl.getTranslator()
                            .getTranslation("EventOverview.ExpenseLabel-paid")
                            + " " + expense.getAmount() + "$ " + mainCtrl.getTranslator()
                            .getTranslation("EventOverview.ExpenseLabel-for") +
                            " " + expense.getPurchase() + "\n");
                    desc.append(" ".repeat(32));
                    desc.append(mainCtrl.getTranslator()
                            .getTranslation("EventOverview.ExpenseLabel-debtors"));
                    for (int i = 0; i < expense.getDebtors().size() - 1; i++) {
                        desc.append(expense.getDebtors().get(i).getName()).append(", ");
                    }
                    desc.append(expense.getDebtors().get(expense.getDebtors().size()-1).getName());
                    return desc.toString();
                }
            };
        }
    }

    @FXML
    private Text eventTitle;

    @FXML
    private Text participantsList;

    @FXML
    private RadioButton expenseSelectorAll;

    @FXML
    private RadioButton expenseSelectorFrom;

    @FXML
    private RadioButton expenseSelectorIncluding;

    @FXML
    private ListView<Expense> expensesList;

    @FXML
    private Text inviteCode;

    @FXML
    private Button inviteCodeCopyBtn;

    @FXML
    private Button sendInviteButton;

    @FXML
    private Label participantsLabel;

    @FXML
    private Button removeParticipantButton;

    @FXML
    private Button addParticipantButton;

    @FXML
    private Label expensesLabel;

    @FXML
    private Label forLabel;

    @FXML
    private Label inviteCodeLabel;

    @FXML
    private Button addExpense;

    @FXML
    private Button editParticipantButton;

    @FXML
    private ChoiceBox<String> participantDropDown;

    @FXML
    private Button openDebtBtn;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button backButton;

    private ObservableList<Expense> shownExpenses;

    private final MainCtrl mainCtrl;

    @Inject
    public EventOverviewCtrl(EventCommunicator eventCommunicator, MainCtrl mainCtrl,
                             ParticipantCommunicator participantCommunicator) {
        this.eventCommunicator = eventCommunicator;
        this.participantCommunicator = participantCommunicator;
        this.mainCtrl = mainCtrl;
    }



    @Override
    public void setLanguage() {
        inviteCodeLabel.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.InviteCode-label"));
        participantsLabel.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Participants-label"));
        expensesLabel.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Expenses-label"));
        forLabel.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.For-label"));
        sendInviteButton.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.SendInvite-Button"));
        inviteCodeCopyBtn.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Copy-Button"));
        removeParticipantButton.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Remove-Button"));
        addParticipantButton.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.AddParticipant-Button"));
        addExpense.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.AddExpense-Button"));
        expenseSelectorAll.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.All-R-Button"));
        expenseSelectorFrom.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.From-R-Button"));
        expenseSelectorIncluding.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Including-R-Button"));
        editParticipantButton.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.EditParticipant-Button"));
        openDebtBtn.setText(mainCtrl.getTranslator().getTranslation(
            "EventOverview.OpenDebt-Button"));
        statisticsButton.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Statistics-Button"));
        backButton.setText(mainCtrl.getTranslator().getTranslation(
                "EventOverview.Back-Button"));
        loadEvent(event);
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
        // Create toggle group for radio buttons
        ToggleGroup expenseSelectorToggle = new ToggleGroup();
        expenseSelectorAll.setToggleGroup(expenseSelectorToggle);
        expenseSelectorFrom.setToggleGroup(expenseSelectorToggle);
        expenseSelectorIncluding.setToggleGroup(expenseSelectorToggle);
        expenseSelectorAll.setSelected(true);
        // Populate expense list
        expensesList.setCellFactory(new ExpenseCellFactory());
    }

    public void loadEvent(Event event) {
        this.event = event;
        eventTitle.setText(event.getName());
        participantsList.setText(String.join(", ", event.getParticipants()
                .stream().map(Participant::getName).toList()));
        participantDropDown.setItems(FXCollections.observableList(event.getParticipants().
                stream().map(Participant::getName).toList()));
        expenseSelectorAll.setSelected(true);
//        expenseSelectorFrom.setText("From " + selectedPayer.getName());
//        expenseSelectorIncluding.setText("Including " + selectedPayer.getName());
        // Populate expense list
        shownExpenses = FXCollections.observableArrayList(event.getExpenses());
        expensesList.setItems(shownExpenses);
        inviteCode.setText(event.getInviteCode());
        stopEventUpdatesLongPolling();
        startEventUpdatesLongPolling(event.getId());
    }

    public void copyInviteCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(event.getInviteCode());
        clipboard.setContent(content);
    }

    /**
     * Changes the expenses shown in the ListView to match the selected toggle.
     * Called by an input to one of the selector RadioButtons.
     */
    public void handleExpenseVisibilityChange() {
        Optional<Participant> optionalParticipant = event.getParticipants().stream()
                .filter(participant -> participant.getName().equals(participantDropDown.getValue()))
                .findFirst();
        if (optionalParticipant.isPresent()) {
            selectedPayer = optionalParticipant.get();
        }
        else {
            System.out.println("Database error");
        }
        if (expenseSelectorAll.isSelected()) {
            shownExpenses.setAll(event.getExpenses());
        } else if (expenseSelectorFrom.isSelected()) {
            shownExpenses.setAll(event.getExpenses().stream()
                    .filter(expense -> expense.getPayer().equals(selectedPayer)).toList());
        } else if (expenseSelectorIncluding.isSelected()) {
            shownExpenses.setAll(event.getExpenses().stream()
                    .filter(expense -> expense.getPayer().equals(selectedPayer)
                                    || expense.getDebtors().contains(selectedPayer)).toList());
        }
        expensesList.setItems(shownExpenses);
    }

    // TODO: implement these methods with proper server communication
    public void handleSendInvites() {
        mainCtrl.showInvitation(event);
    }

    public void handleRemoveParticipant() {
        Optional<Participant> optionalParticipant = event.getParticipants().stream()
                .filter(participant -> participant.getName().equals(participantDropDown.getValue()))
                .findFirst();
        // TODO: give a "confirmation" pop-up
        if (optionalParticipant.isPresent()) {
            participantCommunicator.deleteParticipant(optionalParticipant.get().getId());
        }
        // TODO: if no participant is selected
        else {
            System.out.println("Error");
        }
        loadEvent(eventCommunicator.getEvent(event.getId()));
    }

    public void handleAddParticipant() {
        mainCtrl.showContactInfo(event, null);
    }

    public void handleEditParticipant(ActionEvent actionEvent) {
        Optional<Participant> optionalParticipant = event.getParticipants().stream()
                .filter(participant -> participant.getName().equals(participantDropDown.getValue()))
                .findFirst();
        optionalParticipant.ifPresent(participant -> mainCtrl.showContactInfo(event, participant));
        // TODO: if no participant is selected
        if (optionalParticipant.isEmpty()) System.out.println("Error");
    }

    public void handleAddExpense() {
        mainCtrl.showAddEditExpense(event);
    }

    public void handleOpenDebt() {
        mainCtrl.showOpenDebts(event);
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showStartScreen();
    }

    public void handleStatistics(ActionEvent actionEvent) {
        mainCtrl.showStatistics(event);
    }

    private void startEventUpdatesLongPolling(long eventId) {
        longPollingTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    while (!isCancelled()) {
                        Event updatedEvent = eventCommunicator.checkForEventUpdates(eventId);
                        if (updatedEvent != null && !updatedEvent.equals(event)) {
                            updateUI(updatedEvent);
                        }
                        Thread.sleep(5000); // 5 seconds
                    }
                } catch (InterruptedException e) {
                    // Handle if the thread is interrupted
                }
                return null;
            }
        };

        pollingThread = new Thread(longPollingTask);
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    private void stopEventUpdatesLongPolling() {
        if (longPollingTask != null) {
            longPollingTask.cancel();
            pollingThread.interrupt();
        }
    }

    /**
     * Updates the UI components with the data from the updated event.
     * This method is designed to be run on the JavaFX Application Thread.
     * @param updatedEvent The updated event object containing new data.
     */
    private void updateUI(Event updatedEvent) {
        if (updatedEvent == null) {
            System.err.println("The updated event is null, cannot update UI.");
            return;
        }

        // Ensure UI updates are run on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Update event title
            eventTitle.setText(updatedEvent.getName());

            // Update participants list
            participantsList.setText(String.join(", ", updatedEvent.getParticipants()
                    .stream().map(Participant::getName).toList()));

            // Update expenses list
            shownExpenses.clear();
            shownExpenses.addAll(FXCollections.observableArrayList(updatedEvent.getExpenses()));
            expensesList.setItems(shownExpenses);

            // Update other UI components as needed
            inviteCode.setText(updatedEvent.getInviteCode());

            // Replace the local event object with the updated one
            this.event = updatedEvent;

            System.out.println("UI has been updated with new event data.");
        });
    }

    public void stop() {
        stopEventUpdatesLongPolling();
    }

}
