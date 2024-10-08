package client.scenes;


import client.ModelView.EventOverviewMv;
import client.dialog.ConfPopup;
import client.dialog.Popup;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.language.Translator;
import client.nodes.UIIcon;
import client.utils.communicators.interfaces.IEventUpdateProvider;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import javafx.geometry.Insets;


import commons.Participant;
import commons.Tag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventOverviewCtrl extends SceneController
    implements Initializable, LanguageSwitch, ShortCuts{

    private EventOverviewMv eventOverviewMv;
    private Expense currentlySelectedExpense;


    private class ExpenseListCell extends ListCell<Expense> {
        private HBox container;
        private Text content;

        private Text tagText;
        private Pane filler;
        private Button editButton;
        private Button deleteButton;


        public ExpenseListCell() {
            super();
            container = new HBox();
            content = new Text();
            tagText = new Text();
            filler = new Pane();
            editButton = new Button();
            deleteButton = new Button();
            deleteButton.setGraphic(UIIcon.icon(UIIcon.NAME.DELETE));
            editButton.setGraphic(UIIcon.icon(UIIcon.NAME.EDIT));
            HBox.setHgrow(filler, Priority.ALWAYS);
            HBox.setMargin(deleteButton, new Insets(0, 0, 0, 5));
            container.getChildren().addAll(content, tagText, filler, editButton, deleteButton);

            // Not totally sure how this works, but it seems to. Had to use internet resources here.
            editButton.focusedProperty().addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean>
                                        observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        Expense expenseToBeEdited = getItem();
                        if (expenseToBeEdited != null) {
                            currentlySelectedExpense = expenseToBeEdited;
                        }
                    }
                    else {
                        if (!expensesList.isFocused()) {
                            currentlySelectedExpense = null;
                        }
                    }
                }
            });
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
            Tag tag = expense.getTag();
            if (tag == null) {
                tagText.setText(" (Debt Settlement)   ");
                tagText.setFill(Color.rgb(0, 0, 0));
            }
            else {
                tagText.setText(" (" + tag.getName() + ")   ");
                tagText.setFill(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
            }
            editButton.setOnAction(actionEvent -> {
                mainCtrl.showAddEditExpense(eventOverviewMv.getEvent(), expense);
            });

            deleteButton.setOnAction(event -> {
                boolean isConfirm = ConfPopup.create(
                    mainCtrl.getTranslator().getTranslation(
                        "Conf.DeleteExpense")
                ).isConfirmed();

                try {
                    if(isConfirm){
                        eventOverviewMv.deleteEvent(expense.getId());
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    handleException(e);
                }

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
                "\n" +
                debtorsStringify(expense.getDebtors());
        }

    }

    private String debtorsStringify(List<Participant> participantList){
        StringBuilder res = new StringBuilder();
        res.append(mainCtrl.getTranslator().getTranslation(
            "EventOverview.Participants-label"
        ));
        res.append(": ");

        if(participantList.isEmpty()) return "";

        int count = participantList.size() -1;
        for (int i = 0; i < count ; i++) {
            res.append(participantList.get(i).getName())
                .append(", ");
        }

        res.append(participantList.get(count).getName());
        return res.toString();
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

    @FXML
    private Button renameEventButton;


    private ObservableList<Expense> shownExpenses;

    private final MainCtrl mainCtrl;

    @Inject
    public EventOverviewCtrl(MainCtrl mainCtrl, EventOverviewMv eventOverviewMv) {
        super(mainCtrl);
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
        renameEventButton.setText(mainCtrl.getTranslator().getTranslation(
            "EventOverview.RenameEvent-Button"));
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

        participantDropDown.setOnAction(actionEvent -> handleExpenseVisibilityChange());
    }

    public void loadEvent(Event event) {
        try {
            IEventUpdateProvider updateProvider = eventOverviewMv.getEventUpdateProvider();
            updateProvider.stop();
            updateProvider.start(event.getId());
            updateProvider.setUpdateHandler(change -> {
                mainCtrl.trackRecentEvent(change.getEvent());
                updateUI(change.getEvent());
            });
            updateProvider.setDeleteHandler(change -> {
                new Popup(mainCtrl.getTranslator().getTranslation(
                        "Popup.EventDeletedWhileActive"
                ), Popup.TYPE.ERROR).showAndWait();
                mainCtrl.showStartScreen();
            });

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
//        stopEventUpdatesLongPolling();
//        startEventUpdatesLongPolling(event.getId());
        } catch (Exception e) {
            e.printStackTrace();
            String msg = mainCtrl.getTranslator().getTranslation(
                "Popup.ServerOffline"
            );

            mainCtrl.showStartScreen();
            (new Popup(msg, Popup.TYPE.ERROR)).show();
        }
    }

    @Override
    public void listeners() {
        Scene s = editParticipantButton.getScene();
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.S, this::handleStatistics);
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.O, this::handleOpenDebt);
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.A, this::handleAddExpense);
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.P, this::handleAddParticipant);
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.I, this::handleSendInvites);
        mainCtrl.getKeyBoardListeners().addListener(
            s, KeyCode.B, () -> handleBack(new ActionEvent()));
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.E, () ->
            mainCtrl.showAddEditExpense(eventOverviewMv.getEvent(), currentlySelectedExpense));
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
                try {
                    eventOverviewMv.deleteParticipant(optionalParticipant);
                }
                catch (jakarta.ws.rs.BadRequestException e) {
                    new Popup(mainCtrl.getTranslator().getTranslation(
                        "Popup.ParticipantCannotBeDeleted"), Popup.TYPE.ERROR).showAndWait();
                }
            } else {
                new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.NoParticipantIDSelected"), Popup.TYPE.ERROR).showAndWait();
            }
            loadEvent(eventOverviewMv.getEventCommunicator()
                .getEvent(eventOverviewMv.getEvent().getId()));
        }

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
        mainCtrl.showOpenDebts();
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showStartScreen();
    }


    public void handleStatistics() {
        mainCtrl.showStatistics(eventOverviewMv.getEvent());
    }




    public void handleRenameEvent(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog(eventOverviewMv.getEvent().getName());
        dialog.setTitle(mainCtrl.getTranslator().getTranslation(
            "EventOverview.RenameEvent-Button"));
        dialog.setHeaderText(mainCtrl.getTranslator().getTranslation(
            "Popup.EnterName"));
        dialog.setContentText(mainCtrl.getTranslator().getTranslation(
            "Popup.Name"));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.isEmpty()) {
                Event updatedEvent = eventOverviewMv.eventCommRenameEvent(name);
                if (updatedEvent != null) {
                    loadEvent(updatedEvent);
                    new Popup(mainCtrl.getTranslator().getTranslation(
                        "Popup.RenameSuccessful"
                    ), Popup.TYPE.INFO).showAndWait();
                }
                else {
                    new Popup(mainCtrl.getTranslator().getTranslation(
                        "Popup.RenameFail"
                    ), Popup.TYPE.ERROR).showAndWait();
                }
            }
            else {
                new Popup(mainCtrl.getTranslator().getTranslation(
                    "Popup.RenameCannotBeEmpty"
                ), Popup.TYPE.ERROR).showAndWait();
            }
        });
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

        System.out.println("event updated");

    }
}
