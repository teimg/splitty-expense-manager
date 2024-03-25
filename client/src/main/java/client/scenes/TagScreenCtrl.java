package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import client.utils.communicators.implementations.TagCommunicator;
import client.utils.communicators.interfaces.ITagCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class TagScreenCtrl implements SceneController, LanguageSwitch {

    @FXML
    private Label titleLabel;

    @FXML
    private Label tagNameLabel;

    @FXML
    private Button backButton;

    @FXML
    private Label tagColorLabel;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button submitButton;

    @FXML
    private TextField tagNameField;

    private final MainCtrl mainCtrl;

    private final ITagCommunicator tagCommunicator;

    private Event event;

    @Inject
    public TagScreenCtrl(MainCtrl mainCtrl, TagCommunicator tagCommunicator) {
        this.mainCtrl = mainCtrl;
        this.tagCommunicator = tagCommunicator;
    }

    public void handleSubmit(ActionEvent actionEvent) {
        // TODO
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showAddEditExpense(event);
    }

    public void loadInfo(Event event, Tag tag) {
        this.event = event;
        if (tag == null) {
            return;
        }
        else {
            tagNameField.setText(tag.getName());
            colorPicker.setValue(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
        }
    }

    @Override
    public void setLanguage() {

    }
}
