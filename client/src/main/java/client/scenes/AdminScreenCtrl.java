package client.scenes;

import client.dialog.Popup;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.utils.JsonUtils;
import client.utils.RecentEventTracker;
import client.utils.scene.SceneController;
import client.utils.communicators.implementations.EventCommunicator;
import client.utils.communicators.interfaces.IEventCommunicator;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.NotFoundException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminScreenCtrl implements LanguageSwitch, SceneController,
        Initializable, ShortCuts {

    @FXML
    private Label eventsLabel;

    @FXML
    private Label adminTitleLabel;

    @FXML
    private Button downloadButton;

    @FXML
    private RadioButton titleRadioButton;

    @FXML
    private RadioButton creationRadioButton;

    @FXML
    private RadioButton activityRadioButton;

    @FXML
    private Label orderByLabel;

    @FXML
    private ListView<Event> eventListView;

    @FXML
    private Button importButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;
    private ToggleGroup orderByToggle;

    private final MainCtrl mainCtrl;

    private ObservableList<Event> shownEvents;

    private final IEventCommunicator eventCommunicator;

    private final RecentEventTracker recentEventTracker;

    @Inject
    public AdminScreenCtrl(MainCtrl mainCtrl, EventCommunicator eventCommunicator,
                           RecentEventTracker recentEventTracker) {
        this.mainCtrl = mainCtrl;
        this.eventCommunicator = eventCommunicator;
        this.recentEventTracker = recentEventTracker;
    }

    @Override
    public void listeners() {

    }

    class AddEvent implements Runnable {
        private Event event;
        public AddEvent(Event event) {
            this.event = event;
        }
        @Override
        public void run() {
            shownEvents.add(event);
            sortShownEvents();
            System.out.println("websockets: event created");
        }
    }

    class UpdateEvent implements Runnable {
        private Event event;
        public UpdateEvent(Event event) {
            this.event = event;
        }
        @Override
        public void run() {
            shownEvents.replaceAll(e -> e.getId() == event.getId() ? event : e);
            sortShownEvents();
            System.out.println("websockets: event modified");
        }
    }

    class DeleteEvent implements Runnable {
        private Event event;
        public DeleteEvent(Event event) {
            this.event = event;
        }
        @Override
        public void run() {
            shownEvents.removeIf(e -> e.getId() == event.getId());
            eventListView.refresh();
            System.out.println("websockets: event deleted");
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        orderByToggle = new ToggleGroup();
        titleRadioButton.setToggleGroup(orderByToggle);
        creationRadioButton.setToggleGroup(orderByToggle);
        activityRadioButton.setToggleGroup(orderByToggle);
        eventListView.setCellFactory(new AdminScreenCtrl.EventCellFactory());
    }

    public void initializeScene() {
        shownEvents = FXCollections.observableArrayList(eventCommunicator.getAll());
        eventListView.setItems(shownEvents);
        eventCommunicator.establishWebSocketConnection();
        eventCommunicator.subscribeForWebSocketMessages(
                "/topic/events",
                EventChange.class,
                change -> {
                    switch (change.getType()) {
                        case CREATION -> Platform.runLater(new AddEvent(change.getEvent()));
                        case MODIFICATION -> Platform.runLater(new UpdateEvent(change.getEvent()));
                        case DELETION -> Platform.runLater(new DeleteEvent(change.getEvent()));
                    }
                });
        orderByToggle.selectToggle(titleRadioButton);
        sortShownEvents();
    }

    public void sortShownEvents() {
        if (titleRadioButton.isSelected()) {
            shownEvents.sort(Comparator.comparing(Event::getName));
        } else if (creationRadioButton.isSelected()) {
            shownEvents.sort(Comparator.comparing(Event::getCreationDate).reversed());
        } else if (activityRadioButton.isSelected()) {
            shownEvents.sort(Comparator.comparing(Event::getLastActivity).reversed());
        }
        eventListView.refresh();
    }

    @Override
    public void setLanguage() {
        eventsLabel.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Events-label"));
        adminTitleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Title-label"));
        creationRadioButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Creation-RadioButton"));
        titleRadioButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Title-RadioButton"));
        activityRadioButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Activity-RadioButton"));
        orderByLabel.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.OrderBy-label"));
        importButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Import-Button"));
        deleteButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Delete-Button"));
        downloadButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Download-Button"));
        backButton.setText(mainCtrl.getTranslator().getTranslation(
                "AdminScreen.Back-Button"));
    }

    private static final FileChooser.ExtensionFilter jsonExtensionFilter
            = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");

    public void handleDownload(ActionEvent actionEvent) {
        Event event = eventListView.getSelectionModel().getSelectedItem();
        if (event == null) {
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.eventToDownloadNotSelected") , Popup.TYPE.INFO).showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().add(jsonExtensionFilter);
        fileChooser.setSelectedExtensionFilter(jsonExtensionFilter);
        File file = fileChooser.showSaveDialog(mainCtrl.getPrimaryStage());
        ObjectMapper mapper = JsonUtils.getObjectMapper();
        try {
            mapper.writeValue(file, event);
        } catch (Exception e) {
            System.err.println("Error while saving an event: " + e.getMessage());
        }
    }

    public void handleOrderBy(ActionEvent actionEvent) {
        sortShownEvents();
    }

    private Optional<Event> getEventFromFile() {

        // Get file from user
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import");
        fileChooser.getExtensionFilters().add(jsonExtensionFilter);
        fileChooser.setSelectedExtensionFilter(jsonExtensionFilter);
        File file = fileChooser.showOpenDialog(mainCtrl.getPrimaryStage());

        // Make sure file is selected
        if (file == null) {
            new Popup(mainCtrl.getTranslator().getTranslation(
                    "Popup.NoFileSelected"
            ), Popup.TYPE.ERROR).showAndWait();
            return Optional.empty();
        }

        // Read event from JSON
        ObjectMapper mapper = JsonUtils.getObjectMapper();
        Event event;
        try {
            event = mapper.readValue(file, Event.class);
        } catch (Exception e) {
            if (e instanceof StreamReadException || e instanceof DatabindException) {
                new Popup(mainCtrl.getTranslator().getTranslation(
                        "Popup.BadJSON"
                ), Popup.TYPE.ERROR).showAndWait();
            } else {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        }

        return Optional.of(event);
    }

    public void handleImport(ActionEvent actionEvent) {

        // Get event from JSON
        Optional<Event> res = getEventFromFile();
        if (res.isEmpty()) return;
        Event event = res.get();

        // Error if event with inviteCode already exists
        try {
            eventCommunicator.getEventByInviteCode(event.getInviteCode());
            new Popup(mainCtrl.getTranslator().getTranslation(
                    "Popup.EventExists"
            ), Popup.TYPE.ERROR).showAndWait();
            return;
        } catch (NotFoundException ignored) {}

        // Confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Event import");
        alert.setContentText(mainCtrl.getTranslator().getTranslation(
                "Conf.ImportEvent"
        ) + event.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty() || result.get().equals(ButtonType.CANCEL)) {
            new Popup(mainCtrl.getTranslator().getTranslation(
                    "Popup.ImportCancel"
            ), Popup.TYPE.INFO).showAndWait();
            return;
        }

        // Add event to database
        Event importedEvent = eventCommunicator.restoreEvent(event);

        // Add event to recent events for quick navigation
        recentEventTracker.registerEvent(importedEvent);

        // Success dialog
        new Popup(mainCtrl.getTranslator().getTranslation(
                "Popup.ImportConfirm"
        ), Popup.TYPE.INFO).showAndWait();
    }

    // TODO: Once web sockets are configured this is fairly elementary
    //  must be sure to refresh page
    public void handleDelete(ActionEvent actionEvent) {
        Event toBeDeleted = eventListView.getSelectionModel().getSelectedItem();
        if (toBeDeleted != null) {
            // TODO: Add Pop Up as well as actually delete item (using web sockets).
            System.out.println(toBeDeleted.toString());
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        eventCommunicator.closeWebSocketConnection();
        shownEvents.clear();
        mainCtrl.showStartScreen();
    }

    private static class EventCellFactory
            implements Callback<ListView<Event>, ListCell<Event>> {

        private String participantsText(List<Participant> participants) {
            return String.join(", ", participants.stream().map(Participant::getName).toList());
        }

        private String expensesText(List<Expense> expenses) {
            return String.join(", ", expenses.stream().map(Expense::getPurchase).toList());
        }

        private String eventText(Event event) {
            return String.format("""
                    Name: %s
                    Created: %s
                    Last Modified: %s
                    Participants: %s
                    Expenses: %s""",
                    event.getName(),
                    event.getCreationDate(),
                    event.getLastActivity(),
                    participantsText(event.getParticipants()),
                    expensesText(event.getExpenses()));
        }

        /**
         * Should return a new ListCell usable in the expense ListView.
         * @param listView the expense ListView
         * @return a usable ListCell
         */
        @Override
        public ListCell<Event> call(ListView<Event> listView) {
            return new ListCell<>() {
                /**
                 * Ran when a cell is shown with a new items or is shown emptied.
                 * @param event The new item for the cell.
                 * @param empty whether this cell represents data from the list. If it
                 *        is empty, then it does not represent any domain data, but is a cell
                 *        being used to render an "empty" row.
                 */
                @Override
                public void updateItem(Event event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty) {
                        setText(null);
                        return;
                    }
                    setText(eventText(event));
                }
            };
        }
    }

}
