package client.scenes;

import client.ModelView.StartScreenMv;
import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.*;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jakarta.ws.rs.NotFoundException;
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

    private RecentEventTracker tracker;

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
            deleteButton = new Button("Delete");
            HBox.setHgrow(filler, Priority.ALWAYS);
            container.getChildren().addAll(title, filler, deleteButton);
        }

        @Override
        public void updateItem(JoinableEvent joinableEvent, boolean empty) {
//            super.updateItem(joinableEvent, empty);
//            if (empty) {
//                setGraphic(null);
//                return;
//            }
//            title.setText(joinableEvent.name());
//            title.setOnAction(actionEvent -> {
//                Event event = server.getEvent(joinableEvent.id());
//                mainCtrl.showEventOverview(event);
//            });
//            deleteButton.setOnAction(actionEvent -> {
//                tracker.deleteEvent(joinableEvent);
//            });
//            setGraphic(container);
        }
    }


    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl, StartScreenMv startScreenMv,  RecentEventTracker tracker) {
        ;
        this.mainCtrl = mainCtrl;
        this.startScreenMv = startScreenMv;
        this.tracker = tracker;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        recentEventList.setCellFactory(listView -> new JoinableEventListCell());
        recentEventList.setItems(tracker.getEvents());
        recentEventList.itemsProperty().bindBidirectional(startScreenMv.recentEventsProperty());

        initBinding();


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

    void handleException(Exception e){
        Popup.TYPE type = Popup.TYPE.ERROR;

        String msg = mainCtrl.getTranslator().getTranslation(
            "Popup." + e.getMessage()
        );
        (new Popup(msg, type)).show();
    }

}
