package client.scenes;

import client.ModelView.StartScreenMv;
import client.dialog.Popup;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.nodes.UIIcon;
import client.utils.*;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.NotFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenCtrl extends SceneController
    implements Initializable, LanguageSwitch, ShortCuts {

    private final MainCtrl mainCtrl;

    @FXML
    private Label createNewEventLabel;

    @FXML
    private Label joinEventLabel;

    @FXML
    private Label recentlyViewedEventsLabel;

    @FXML
    private TextField newEventField;

    @FXML
    private TextField joinEventField;

    @FXML
    private Button createEventButton;

    @FXML
    private Button joinEventButton;

    @FXML
    private ListView<JoinableEvent> recentEventList;

    private final StartScreenMv startScreenMv;

    private JoinableEvent currentlySelectedJoinableEvent;

    private class JoinableEventListCell extends ListCell<JoinableEvent> {
        private HBox container;
        private Hyperlink title;
        private Pane filler;
        private Button deleteButton;

        public JoinableEventListCell() {
            super();
            container = new HBox();
            title = new Hyperlink();
            filler = new Pane();
            deleteButton = new Button();
            deleteButton.setGraphic(UIIcon.icon(UIIcon.NAME.DELETE));
            HBox.setHgrow(filler, Priority.ALWAYS);
            container.getChildren().addAll(title, filler, deleteButton);

            // Not totally sure how this works, but it seems to. Had to use internet resources here.
            title.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean>
                                            observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        JoinableEvent joinableEvent = getItem();
                        if (joinableEvent != null) {
                            currentlySelectedJoinableEvent = joinableEvent;
                        }
                    }
                    else {
                        if (!recentEventList.isFocused()) {
                            currentlySelectedJoinableEvent = null;
                        }
                    }
                }
            });

        }

        @Override
        public void updateItem(JoinableEvent joinableEvent, boolean empty) {
            super.updateItem(joinableEvent, empty);
            if (empty) {
                setGraphic(null);
                return;
            }
            title.setText(joinableEvent.name());
            title.setOnAction(actionEvent -> {
                try {
                    Event event = startScreenMv.getRecentEvent(joinableEvent.inviteCode());
                    mainCtrl.showEventOverview(event);
                } catch (NotFoundException e) {
                    new Popup("Event was not found!", Popup.TYPE.ERROR).showAndWait();
                    deleteButton.fire();
                } catch (Exception e){
                    handleException(e);
                }
            });
            deleteButton.setOnAction(actionEvent -> {
                startScreenMv.deleteEvent(joinableEvent);
            });
            setGraphic(container);
        }
    }

    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl, StartScreenMv startScreenMv) {
        super(mainCtrl);
        this.mainCtrl = mainCtrl;
        this.startScreenMv = startScreenMv;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentEventList.setCellFactory(listView -> new JoinableEventListCell());
        recentEventList.itemsProperty().bindBidirectional(startScreenMv.recentEventsProperty());

        initBinding();
        startScreenMv.init();
    }

    public void initBinding(){
        newEventField.textProperty().bindBidirectional(startScreenMv.newEventProperty());
        joinEventField.textProperty().bindBidirectional(startScreenMv.joinEventProperty());


    }

    @Override
    public void setLanguage() {
        createNewEventLabel.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Create-New-Event-label"));
        joinEventLabel.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Join-Event-label"
        ));
        recentlyViewedEventsLabel.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Recently-Viewed-label"
        ));
        createEventButton.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Create-Event-Button"
        ));
        joinEventButton.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Join-Event-Button"
        ));;
    }

    public void createEvent() {
        try{
            mainCtrl.showEventOverview(
                startScreenMv.createEvent()
            );
        }catch (Exception e){
            handleException(e);
        }
    }

    public void joinEvent() {
        try {
            mainCtrl.showEventOverview(
                startScreenMv.joinEvent()
            );
        } catch (Exception e){
            handleException(e);
        }
    }

    public void loadInfo() {
        newEventField.setText("");
        joinEventField.setText("");

        try {
            startScreenMv.updateRecents();
        } catch (Exception e) {
            new Popup(mainCtrl.getTranslator().getTranslation(
                    "Popup.RecentEventNoUpdate"
            ), Popup.TYPE.ERROR).showAndWait();
        }
    }

    @Override
    public void listeners() {
        Scene s = newEventField.getScene();
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.C, this::createEvent);
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.J, this::joinEvent);
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.E, () -> joinRecent(s));
    }

    private void joinRecent(Scene s) {
        if (currentlySelectedJoinableEvent != null) {
            Event event = startScreenMv.getRecentEvent(
                    currentlySelectedJoinableEvent.inviteCode());
            mainCtrl.showEventOverview(event);
        }
    }

}
