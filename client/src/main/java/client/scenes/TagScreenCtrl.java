package client.scenes;

import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
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

    private Tag tag;

    @Inject
    public TagScreenCtrl(MainCtrl mainCtrl, TagCommunicator tagCommunicator) {
        this.mainCtrl = mainCtrl;
        this.tagCommunicator = tagCommunicator;
    }

    public void handleSubmit(ActionEvent actionEvent) {
        if (tag == null) {
            if (tagNameField.getText().isEmpty()) {
                new Popup(mainCtrl.getTranslator().getTranslation
                        ("Popup.emptyTagNameField") , Popup.TYPE.ERROR).showAndWait();
            }

            else {
                tagCommunicator.createTag(new Tag(tagNameField.getText(),
                        (int) Math.round(colorPicker.getValue().getRed()*255),
                        (int) Math.round(colorPicker.getValue().getGreen()*255),
                        (int) Math.round(colorPicker.getValue().getBlue()*255)));
                mainCtrl.showAddEditExpense(event);
            }
        }
        else {
            if (tagNameField.getText().isEmpty()) {
                new Popup(mainCtrl.getTranslator().getTranslation
                        ("Popup.emptyTagNameField") , Popup.TYPE.ERROR).showAndWait();
            }
            else {
                Tag updated = new Tag(tagNameField.getText(),
                        (int) Math.round(colorPicker.getValue().getRed()*255),
                        (int) Math.round(colorPicker.getValue().getGreen()*255),
                        (int) Math.round(colorPicker.getValue().getBlue()*255));
                updated.setId(tag.getId());
                tagCommunicator.updateTag(updated);
                mainCtrl.showAddEditExpense(event);
            }
        }
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showAddEditExpense(event);
    }

    public void loadInfo(Event event, Tag tag) {
        clearScene();
        this.event = event;
        this.tag = tag;
        if (tag == null) {
            return;
        }
        else {
            tagNameField.setText(tag.getName());
            colorPicker.setValue(Color.rgb(tag.getRed(),
                    tag.getGreen(), tag.getBlue()));
        }
    }

    private void clearScene() {
        tagNameField.setText("");
        colorPicker.setValue(Color.rgb(0, 0, 0));
    }

    @Override
    public void setLanguage() {
        titleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "TagScreen.Title-label"));
        tagNameLabel.setText(mainCtrl.getTranslator().getTranslation(
                "TagScreen.TagName-label"));
        tagColorLabel.setText(mainCtrl.getTranslator().getTranslation(
                "TagScreen.TagColor-label"));
        backButton.setText(mainCtrl.getTranslator().getTranslation(
                "TagScreen.Back-Button"));
        submitButton.setText(mainCtrl.getTranslator().getTranslation(
                "TagScreen.Submit-Button"));
    }
}
