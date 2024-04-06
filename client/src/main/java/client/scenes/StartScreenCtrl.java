package client.scenes;

import client.ModelView.StartScreenMv;
import client.language.LanguageSwitch;
import client.nodes.UIIcon;
import client.utils.*;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenCtrl implements Initializable, LanguageSwitch, SceneController {

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
                Event event = startScreenMv.getRecentEvent(joinableEvent.id());
                mainCtrl.showEventOverview(event);
            });
            deleteButton.setOnAction(actionEvent -> {
                startScreenMv.deleteEvent(joinableEvent);
            });
            setGraphic(container);
        }
    }

    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl, StartScreenMv startScreenMv) {
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
            handleException(e, mainCtrl.getTranslator());
        }
    }

    public void joinEvent() {
        try {
            mainCtrl.showEventOverview(
                startScreenMv.joinEvent()
            );
        } catch (Exception e){
            handleException(e, mainCtrl.getTranslator());
        }
    }

    public void loadInfo() {
        newEventField.setText("");
        joinEventField.setText("");
        addKeyBoardListeners();
    }

    private void addKeyBoardListeners() {
        final boolean[] ctrlCPressed = {false};
        final boolean[] ctrlJPressed = {false};
        newEventField.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode keyCode = event.getCode();
            if (event.isControlDown()) {
                if (keyCode == KeyCode.C && !ctrlCPressed[0]) {
                    ctrlCPressed[0] = true;
                    createEvent();
                }
                if (keyCode == KeyCode.J && !ctrlJPressed[0]) {
                    ctrlJPressed[0] = true;
                    joinEvent();
                }
            }
        });
        newEventField.getScene().addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.C) {
                ctrlCPressed[0] = false;
            }
            if (keyCode == KeyCode.J) {
                ctrlJPressed[0] = false;
            }
        });
    }

}
