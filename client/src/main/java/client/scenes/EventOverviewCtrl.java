package client.scenes;

import client.language.LanguageSwitch;
import client.utils.IServerUserCommunicator;
import client.utils.SceneController;
import commons.Event;
import commons.Expense;

import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// TODO: parametrize the 'you' Participant?
public class EventOverviewCtrl implements Initializable, LanguageSwitch, SceneController {
    private final IServerUserCommunicator serverUserCommunicator;
    private final Event event;
    private final Participant you;

    @Override
    public void setLanguage() {

    }

    private static class ExpenseCellFactory
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
                 * @param empty whether or not this cell represents data from the list. If it
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
                    setText(expense.toString());
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

    private List<Expense> expenses;
    private ObservableList<Expense> shownExpenses;

    /**
     * Constructor, should be converted later to use dependency injection.
     * @param serverUserCommunicator IServerUserCommunicator to request resources from the server
     * @param event Event for which this EventOverview scene is created
     * @param you Participant representing this client in the application
     */
    public EventOverviewCtrl(IServerUserCommunicator serverUserCommunicator,
                             Event event, Participant you) {
        this.serverUserCommunicator = serverUserCommunicator;
        this.event = event;
        this.you = you;
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
        // Set title and participants
        eventTitle.setText(event.getName());
        participantsList.setText(String.join(", ", event.getParticipants()
                .stream().map(Participant::getName).toList()));
        // Create toggle group for radio buttons
        ToggleGroup expenseSelectorToggle = new ToggleGroup();
        expenseSelectorAll.setToggleGroup(expenseSelectorToggle);
        expenseSelectorFrom.setToggleGroup(expenseSelectorToggle);
        expenseSelectorIncluding.setToggleGroup(expenseSelectorToggle);
        expenseSelectorAll.setSelected(true);
        // Put user's name on radio buttons
        String userName = you.getName();
        expenseSelectorFrom.setText("From " + userName);
        expenseSelectorIncluding.setText("Including " + userName);
        // Populate expense list
        expenses = new ArrayList<>(serverUserCommunicator.getAllExpensesForEvent(event.getId()));
        expensesList.setCellFactory(new ExpenseCellFactory());
        shownExpenses = FXCollections.observableArrayList(expenses);
        expensesList.setItems(shownExpenses);
    }

    /**
     * Changes the expenses shown in the ListView to match the selected toggle.
     * Called by an input to one of the selector RadioButtons.
     */
    public void handleExpenseVisibilityChange() {
        // Could be cleaner with ToggleGroup::getSelectedToggle
        if (expenseSelectorAll.isSelected()) {
            shownExpenses.setAll(expenses);
        } else if (expenseSelectorFrom.isSelected()) {
            shownExpenses.setAll(expenses.stream()
                    .filter(expense -> expense.getPayer().equals(you)).toList());
        } else if (expenseSelectorIncluding.isSelected()) {
            shownExpenses.setAll(expenses.stream()
                    .filter(expense -> expense.getPayer().equals(you)
                                    || expense.getDebtors().contains(you)).toList());
        }
    }

    // All of these methods need to be implemented later
    // TODO: implement these methods with proper server communication
    public void handleSendInvites() {}
    public void handleRemoveParticipant() {}
    public void handleAddParticipant() {}
    public void handleAddExpense() {}

}
