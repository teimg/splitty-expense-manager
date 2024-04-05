package client.scenes;


import client.ModelView.EventOverviewMv;
import client.dialog.ConfPopup;
import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.language.Translator;
import client.nodes.UIIcon;
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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;


import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;




public class EventOverviewCtrl implements Initializable, LanguageSwitch, SceneController {


    private Task<Void> longPollingTask = null;
    private Thread pollingThread = null;
    private EventOverviewMv eventOverviewMv;


    private class ExpenseListCell extends ListCell<Expense> {
        private HBox container;
        private Text content;
        private Pane filler;
        private Button editButton;
        private Button deleteButton;


        public ExpenseListCell() {
            super();
            container = new HBox();
            content = new Text();
            filler = new Pane();
            editButton = new Button();
            deleteButton = new Button();
            deleteButton.setGraphic(UIIcon.icon(UIIcon.NAME.DELETE));
            editButton.setGraphic(UIIcon.icon(UIIcon.NAME.EDIT));
            HBox.setHgrow(filler, Priority.ALWAYS);
            HBox.setMargin(deleteButton, new Insets(0, 0, 0, 5));
            container.getChildren().addAll(content, filler, editButton, deleteButton);
        }


        private void setLanguage(Translator translator) {
            editButton.setText(translator
                    .getTranslation("EventOverview.EditExpense-Button"));
        }


        @Override
        public void updateItem(Expense expense, boolean empty) {
            super.updateItem(expense, empty);
            if (empty) {
                setGraphic(null);
                return;
            }

            setLanguage(mainCtrl.getTranslator());

            content.setText(expenseDescription(expense));
            editButton.setOnAction(actionEvent -> {
                mainCtrl.showAddEditExpense(eventOverviewMv.getEvent(), expense);
            });

            deleteButton.setOnAction(event -> {
                boolean isConfirm = ConfPopup.create(
                    mainCtrl.getTranslator().getTranslation(
                        "Conf.DeleteExpense")
                ).isConfirmed();

                deleteButton.setDisable(true);
                editButton.setDisable(true);

                try {
                    if(isConfirm){
                        eventOverviewMv.deleteEvent(expense.getId());
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    handleException(e, mainCtrl.getTranslator());
                }

                deleteButton.setDisable(false);
                editButton.setDisable(false);

            });
            setGraphic(container);
        }


        private String expenseDescription(Expense expense) {
            return expense.getDate().toString() +
                    "    " +
                    expense.getPayer().getName() +
                    " " +
                    mainCtrl.getTranslator()
                            .getTranslation("EventOverview.ExpenseLabel-paid") +
                    "  " +
                    Math.round(mainCtrl.getExchanger().getStandardConversion(
                            expense.getAmount(), LocalDate.now()) * 100.0) / 100.0 +
                    " " +
                    mainCtrl.getExchanger().getCurrentSymbol() +
                    " " +
                    mainCtrl.getTranslator()
                            .getTranslation("EventOverview.ExpenseLabel-for") +
                    " " +
                    expense.getPurchase() +
                    "\n";
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
    public EventOverviewCtrl(MainCtrl mainCtrl, EventOverviewMv eventOverviewMv, Event event) {
        this.mainCtrl = mainCtrl;
        this.eventOverviewMv = eventOverviewMv;
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
        loadEvent(eventOverviewMv.getEvent());
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

        //UI init
        inviteCodeCopyBtn.setGraphic(UIIcon.icon(UIIcon.NAME.CLIPBOARD));

        // Create toggle group for radio buttons
        ToggleGroup expenseSelectorToggle = new ToggleGroup();
        expenseSelectorAll.setToggleGroup(expenseSelectorToggle);
        expenseSelectorFrom.setToggleGroup(expenseSelectorToggle);
        expenseSelectorIncluding.setToggleGroup(expenseSelectorToggle);
        expenseSelectorAll.setSelected(true);
        // Populate expense list
        expensesList.setCellFactory(expensesList -> new ExpenseListCell());
    }


    public void loadEvent(Event event) {
        eventOverviewMv.setEvent(event);
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
        eventOverviewMv.copyInviteCode();
    }


    /**
     * Changes the expenses shown in the ListView to match the selected toggle.
     * Called by an input to one of the selector RadioButtons.
     */
    public void handleExpenseVisibilityChange() {
        Optional<Participant> optionalParticipant = eventOverviewMv
                .getEvent().getParticipants().stream()
                .filter(participant -> participant.getName().equals(participantDropDown.getValue()))
                .findFirst();
        if (optionalParticipant.isPresent()) {
            eventOverviewMv.setSelectedPayer(optionalParticipant.get());
        }
        else {
            System.out.println("Database error");
        }
        if (expenseSelectorAll.isSelected()) {
            shownExpenses.setAll(eventOverviewMv.getEvent().getExpenses());
        } else if (expenseSelectorFrom.isSelected()) {
            shownExpenses.setAll(eventOverviewMv.getEvent().getExpenses().stream()
                    .filter(expense -> expense.getPayer()
                            .equals(eventOverviewMv.getSelectedPayer())).toList());
        } else if (expenseSelectorIncluding.isSelected()) {
            shownExpenses.setAll(eventOverviewMv.getEvent().getExpenses().stream()
                    .filter(expense -> expense.getPayer()
                            .equals(eventOverviewMv.getSelectedPayer())
                            || expense.getDebtors()
                            .contains(eventOverviewMv.getSelectedPayer())).toList());
        }
        expensesList.setItems(shownExpenses);
    }


    // TODO: implement these methods with proper server communication
    public void handleSendInvites() {
        mainCtrl.showInvitation(eventOverviewMv.getEvent());
    }


    public void handleRemoveParticipant() {
        Optional<Participant> optionalParticipant = eventOverviewMv
                .getEvent().getParticipants().stream()
                .filter(participant -> participant.getName().equals(participantDropDown.getValue()))
                .findFirst();
        boolean confirmed = ConfPopup.create
                        (mainCtrl.getTranslator().getTranslation
                                ("Popup.sureRemoveDatabase"))
                .isConfirmed();
        if (confirmed) {
            if (optionalParticipant.isPresent()) {
                eventOverviewMv.deleteParticipant(optionalParticipant);
            } else {
                new Popup(mainCtrl.getTranslator().getTranslation
                        ("Popup.NoParticipantIDSelected"), Popup.TYPE.ERROR).showAndWait();
            }
        } else {
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.databaseError"), Popup.TYPE.ERROR).showAndWait();
        }
        loadEvent(eventOverviewMv.getEventCommunicator()
                .getEvent(eventOverviewMv.getEvent().getId()));
//        loadEvent(eventOverviewMv.eventCommunicatorGetEvent());
    }


    public void handleAddParticipant() {
        mainCtrl.showContactInfo(eventOverviewMv.getEvent(), null);
    }


    public void handleEditParticipant(ActionEvent actionEvent) {
        Optional<Participant> optionalParticipant = eventOverviewMv.getEvent()
                .getParticipants().stream()
                .filter(participant
                        -> participant.getName().equals(participantDropDown.getValue()))
                .findFirst();
        optionalParticipant.ifPresent(participant
                -> mainCtrl.showContactInfo(eventOverviewMv.getEvent(), participant));
        if (optionalParticipant.isEmpty()) {
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.NoparticipantSelected"), Popup.TYPE.ERROR).showAndWait();
        }
        if (optionalParticipant.isEmpty()) System.out.println("Error");
    }


    public void handleAddExpense() {
        mainCtrl.showAddEditExpense(eventOverviewMv.getEvent());
    }


    public void handleOpenDebt() {
        mainCtrl.showOpenDebts(eventOverviewMv.getEvent());
    }


    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showStartScreen();
    }


    public void handleStatistics(ActionEvent actionEvent) {
        mainCtrl.showStatistics(eventOverviewMv.getEvent());
    }


    private void startEventUpdatesLongPolling(long eventId) {
        longPollingTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    while (!isCancelled()) {
                        Event updatedEvent = eventOverviewMv
                                .eventCommunicatorCheckForUpdate(eventId);
                        if (updatedEvent != null
                                && !updatedEvent.equals(eventOverviewMv.getEvent())) {
                            updateUI(updatedEvent);
                        }
                        Thread.sleep(5000); // 5 seconds
                    }
                } catch (Exception e) {
                    handleException(e, mainCtrl.getTranslator());
                }
                return null;
            }
        };


        pollingThread = new Thread(longPollingTask);
        pollingThread.setDaemon(true);
        pollingThread.start();
    }


    public void handleRenameEvent(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog(eventOverviewMv.getEvent().getName());
        dialog.setTitle("Rename Event");
        dialog.setHeaderText("Enter the new name for the event:");
        dialog.setContentText("Name:");


        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Event updatedEvent = eventOverviewMv.eventCommRenameEvent(name);
            if (updatedEvent != null) {
                loadEvent(updatedEvent);
                new Popup("Event renamed successfully", Popup.TYPE.INFO).showAndWait();
            } else {
                new Popup("Failed to rename event", Popup.TYPE.ERROR).showAndWait();
            }
        });
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
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.updateEventIsNull"),
                    Popup.TYPE.ERROR).showAndWait();
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
            eventOverviewMv.setEvent(updatedEvent);


            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.successfulEventUpdate"), Popup.TYPE.INFO).show();
        });
    }


//    public void stop() {
//        stopEventUpdatesLongPolling();
//    }
}

