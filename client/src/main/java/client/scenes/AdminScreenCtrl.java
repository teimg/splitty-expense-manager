package client.scenes;

import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.JsonUtils;
import client.utils.scene.SceneController;
import client.utils.communicators.implementations.EventCommunicator;
import client.utils.communicators.interfaces.IEventCommunicator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;
import commons.Expense;
import commons.Participant;
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
import java.util.ResourceBundle;

public class AdminScreenCtrl implements LanguageSwitch, SceneController, Initializable {

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

    @Inject
    public AdminScreenCtrl(MainCtrl mainCtrl, EventCommunicator eventCommunicator) {
        this.mainCtrl = mainCtrl;
        this.eventCommunicator = eventCommunicator;
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

    public void handleDownload(ActionEvent actionEvent) {
        Event event = eventListView.getSelectionModel().getSelectedItem();
        if (event == null) {
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.eventToDownloadNotSelected") , Popup.TYPE.INFO).showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
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

    // TODO: Import JSON
    public void handleImport(ActionEvent actionEvent) {
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
