package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import client.utils.communicators.implementations.EventCommunicator;
import client.utils.communicators.interfaces.IEventCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
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

    private final MainCtrl mainCtrl;

    private ObservableList<Event> shownEvents;

    private final IEventCommunicator eventCommunicator;

    @Inject
    public AdminScreenCtrl(MainCtrl mainCtrl, EventCommunicator eventCommunicator) {
        this.mainCtrl = mainCtrl;
        this.eventCommunicator = eventCommunicator;
    }

    private void addEvent(Event event) {
        shownEvents.add(event);
        eventListView.refresh();
        System.out.println("websockets: event created");
    }

    private void updateEvent(Event event) {
        shownEvents.removeIf(e -> e.getId() == event.getId());
        shownEvents.add(event);
        eventListView.refresh();
        System.out.println("websockets: event created");
    }

    private void deleteEvent(Event event) {
        shownEvents.removeIf(e -> e.getId() == event.getId());
        eventListView.refresh();
        System.out.println("websockets: event deleted");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup orderByToggle = new ToggleGroup();
        titleRadioButton.setToggleGroup(orderByToggle);
        creationRadioButton.setToggleGroup(orderByToggle);
        activityRadioButton.setToggleGroup(orderByToggle);
        eventListView.setCellFactory(new AdminScreenCtrl.EventCellFactory());

        shownEvents = FXCollections.observableArrayList(eventCommunicator.getAll());
        eventListView.setItems(shownEvents);

        eventCommunicator.registerForWebSocketMessages(
                "/topic/events",
                EventChange.class,
                change -> {
                    switch (change.getType()) {
                        case CREATION -> addEvent(change.getEvent());
                        case MODIFICATION -> updateEvent(change.getEvent());
                        case DELETION -> deleteEvent(change.getEvent());
                    }
                });
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

    // TODO: JSON dump
    public void handleDownload(ActionEvent actionEvent) {
    }

    // TODO: Implement sorting (probably done after web sockets
    //  as data structures are not yet determined
    public void handleOrderBy(ActionEvent actionEvent) {
        if (titleRadioButton.isSelected()) {
            // TODO: implement sorting
        } else if (creationRadioButton.isSelected()) {
            // TODO: implement sorting
        } else if (activityRadioButton.isSelected()) {
            // TODO: implement sorting
        }
        eventListView.setItems(shownEvents);
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
        mainCtrl.showStartScreen();
    }

    private static class EventCellFactory
            implements Callback<ListView<Event>, ListCell<Event>> {
        /**
         * Should return a new ListCell usable in the expense ListView.
         *
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
                    setText(event.toString());
                }
            };
        }
    }

}
