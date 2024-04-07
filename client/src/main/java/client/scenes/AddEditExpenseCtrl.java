package client.scenes;

import client.ModelView.AddEditExpenseMv;
import client.dialog.Popup;
import client.keyBoardCtrl.KeyBoardListeners;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.utils.*;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AddEditExpenseCtrl  implements Initializable, LanguageSwitch,
        SceneController, ShortCuts {

    @FXML
    private Label titleLabel;

    @FXML
    private Label whoPaidLabel;

    @FXML
    private Label whatForLabel;

    @FXML
    private Label howMuchLabel;

    @FXML
    private Label whenLabel;

    @FXML
    private Label splitLabel;

    @FXML
    private Label expenseTypeLabel;

    @FXML
    private Button abortButton;

    @FXML
    private Button addButton;

    @FXML
    private ComboBox<String> currencyField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField descriptionField;

    @FXML
    private CheckBox evenlyCheckbox;

    @FXML
    private TextField priceField;

    @FXML
    private CheckBox someCheckbox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ComboBox<Tag> tagField;

    @FXML
    private ComboBox<Participant> whoPaidField;

    @FXML
    private Button deleteTagButton;

    @FXML
    private Button editTagButton;

    @FXML
    private Button addTagButton;

    @FXML
    private Rectangle tagColor;


    private WhoPaidSelector whoPaidSelector;

    private WhichTagSelector whichTagSelector;

    private final MainCtrl mainCtrl;

    private final AddEditExpenseMv addEditExpenseMv;

    private static class InnerCheckBoxManger
        extends VBox implements ListChangeListener<Pair<Participant, BooleanProperty>>{
        private ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> state;

        public InnerCheckBoxManger(
            ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> toBind ) {
            this.setPadding(new Insets(0, 0, 0, 10));
            this.state = new SimpleObjectProperty<>(FXCollections.observableArrayList());
            this.state.bindBidirectional(toBind);

            state.getValue().addListener(this);
        }

        @Override
        public void onChanged(Change<? extends Pair<Participant, BooleanProperty>> c) {

            while (c.next()){
                if(c.wasRemoved()){
                    this.getChildren().removeAll(this.getChildren());
                }

                for(var x : c.getAddedSubList()){

                    CheckBox current = new CheckBox(x.getKey().getName());
                    current.setPadding(new Insets(5, 0, 0, 0));
                    x.getValue().bindBidirectional(current.selectedProperty());
                    this.getChildren().add(current);
                }
            }

        }
    }

    @Override
    public void setLanguage() {
        titleLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Title-label"));
        whoPaidLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.WhoPaid-label"));
        whatForLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.WhatFor-label"));
        howMuchLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.HowMuch-label"));
        whenLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.When-label"));
        splitLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Split-label"));
        expenseTypeLabel.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Expense-Type-label"));
        evenlyCheckbox.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Evenly-CheckBox"));
        someCheckbox.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Some-CheckBox"));
        abortButton.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Abort-Button"));
        addButton.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Add-Button"));
        addTagButton.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.AddTag-Button"));
        editTagButton.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.EditTag-Button"));
        deleteTagButton.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.DeleteTag-Button"));
    }

    @Inject
    public AddEditExpenseCtrl (MainCtrl mainCtrl, AddEditExpenseMv addEditExpenseMv) {
        this.addEditExpenseMv = addEditExpenseMv;
        this.mainCtrl = mainCtrl;

    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InnerCheckBoxManger innerCheckBoxManger =
                new InnerCheckBoxManger(addEditExpenseMv.debtorsProperty());
        scrollPane.setContent(innerCheckBoxManger);
        tagField.setOnAction(this::handleTagColorUpdate);
        initBindings();

    }

    public void loadInfo(Event event, Expense expense) {
        loadInfo(event);

        addEditExpenseMv.loadExpense(expense, mainCtrl.getExchanger());
    }

    public void loadInfo(Event event) {
        addEditExpenseMv.loadInfo(event, mainCtrl.getExchanger());

        initWhoPaid();
        initTag();
        initCheckbox();
        initCurrency();
        initDateField();
        tagColor.setFill(Color.rgb(150, 150, 150));
    }

    public void updateTags(Event event) {
        addEditExpenseMv.updateEvent(event);
        initTag();
    }

    public void initBindings() {
        priceField.textProperty().bindBidirectional(
            addEditExpenseMv.priceFieldProperty());
        currencyField.valueProperty().bindBidirectional(
            addEditExpenseMv.currencyFieldProperty());
        descriptionField.textProperty().bindBidirectional(
            addEditExpenseMv.descriptionFieldProperty());
        dateField.valueProperty().bindBidirectional(
            addEditExpenseMv.dateFieldProperty());
        whoPaidField.valueProperty().bindBidirectional(
            addEditExpenseMv.whoPaidFieldProperty());
        evenlyCheckbox.selectedProperty().bindBidirectional(
            addEditExpenseMv.evenlyCheckboxProperty());
        tagField.valueProperty().bindBidirectional(addEditExpenseMv.tagFieldProperty());
    }

    public void initWhoPaid(){
        this.whoPaidField.setVisibleRowCount(3);

        this.whoPaidSelector = new WhoPaidSelector(this.addEditExpenseMv.getParticipants());
        whoPaidField.setConverter(whoPaidSelector);

        whoPaidField.getItems().addAll(addEditExpenseMv.getParticipants());

        this.whoPaidField.setCellFactory(call -> new ListCell<Participant>(){
            @Override
            protected void updateItem(Participant participant, boolean empty) {
                super.updateItem(participant, empty);

                if(empty){
                    return;
                }
                setText(participant.getName());
            }
        });

        whoPaidField.getEditor().setOnKeyPressed(x -> {

            if(!x.getCode().isLetterKey()){
                return;
            }

            whoPaidField.getItems().removeAll(whoPaidField.getItems());
            whoPaidField.setValue(
                whoPaidSelector.getCurrentPayer(whoPaidField.getEditor().getText()));

            whoPaidField.getItems()
                .removeAll(whoPaidField.getItems());
            whoPaidField.getItems()
                .addAll(whoPaidSelector
                    .query(
                        whoPaidField.getEditor().getText()));
            whoPaidField.show();
        });
    }

    public void initTag(){
        this.whichTagSelector = new WhichTagSelector(this.addEditExpenseMv.getTags());
        this.tagField.setConverter(whichTagSelector);
        this.tagField.setValue(null);
        tagField.getItems().clear();
        this.tagField.setVisibleRowCount(3);
        tagField.getItems().addAll(this.addEditExpenseMv.getTags());

        tagField.getEditor().setOnKeyPressed(x -> {

            if(!x.getCode().isLetterKey()){
                return;
            }

            tagField.getItems().removeAll(tagField.getItems());
            tagField.setValue(whichTagSelector.getCurrentTag(tagField.getEditor().getText()));

            tagField.getItems().removeAll(tagField.getItems());
            tagField.getItems()
                .addAll(whichTagSelector
                    .query(
                        tagField.getEditor().getText()));
            tagField.show();
        });
    }


    /**
     * init date field
     */

    public void initDateField(){
        // to disable manuel editing, this way a date can never be invalid hopefully
        dateField.getEditor().setEditable(false);
        dateField.getEditor().setDisable(true);
        dateField.getEditor().setOpacity(1);;

        dateField.setValue(LocalDate.now());
    }


    /**
     * init the currency field
     * should become more advanced if we want to add the exentison
     */

    public void initCurrency(){
        currencyField.getItems().clear();
        currencyField.getItems().add("USD");
        currencyField.getItems().add("EUR");
        currencyField.getItems().add("CHF");
        currencyField.getItems().add("JPY");
        currencyField.setValue("");

    }


    /**
     * Bring the checkboxes to there starting state
     * (should be more advanced when editing becomes an option)
     */
    public void initCheckbox(){
        evenlyCheckbox.setSelected(true);
        someCheckbox.setSelected(false);
        evenlyCheckbox.setAllowIndeterminate(false);
        someCheckbox.setAllowIndeterminate(false);
        scrollPane.setDisable(true);
    }

    /**
     * Event Triggered
     * Toggles the someCheckbox
     *
     */
    public void someCheckboxToggle(){
        if(someCheckbox.isSelected()){
            evenlyCheckbox.setSelected(false);
            scrollPane.setDisable(false);
            return;
        }
        evenlyCheckbox.setSelected(true);
        scrollPane.setDisable(true);

    }

    /**
     * Event triggered
     * Toggles the ven checkbox
     */
    public void evenlyCheckboxToggle(){
        if(evenlyCheckbox.isSelected()){
            someCheckbox.setSelected(false);
            scrollPane.setDisable(true);
            return;
        }
        someCheckbox.setSelected(true);
        scrollPane.setDisable(false);
    }

    public void clear(){
        whoPaidField.getItems().removeAll(whoPaidField.getItems());

    }

    public Tag getTag(){
        Tag res = tagField.getValue();

        if(res == null){
            throw new IllegalArgumentException("tag");
        }

        return res;
    }

    private void handleTagColorUpdate(ActionEvent actionEvent) {
        Tag curTag = tagField.getValue();
        if (curTag != null) {
            tagColor.setFill(Color.rgb(curTag.getRed(), curTag.getGreen(), curTag.getBlue()));
        }
        else {
            tagColor.setFill(Color.rgb(150, 150, 150));
        }
    }

    public void addButtonPressed(){
        createExpense();
    }

    public void abortButtonPressed(ActionEvent actionEvent) {
        Event res = addEditExpenseMv.getEvent();
        clear();
        addEditExpenseMv.clear();
        mainCtrl.showEventOverview(res);
    }

    public void createExpense(){
        try {
            Expense added = addEditExpenseMv.createExpense();
            Event res = addEditExpenseMv.getEvent();
            addEditExpenseMv.clear();
            clear();
            mainCtrl.showEventOverview(res);
            new Popup(mainCtrl.getTranslator().getTranslation
                    ("Popup.ExpenseAddSuccessful"), Popup.TYPE.INFO).showAndWait();
            return;

        }catch (Exception e){
            e.printStackTrace();
            handleException(e, mainCtrl.getTranslator());
        }
    }

    public void refreshTags(){
        this.tagField.getItems().removeAll(tagField.getItems());
        this.tagField.getItems().addAll(addEditExpenseMv.getTags());
    }

    public void handleDeleteTag() {
        try{
            addEditExpenseMv.deleteTag();
            initTag();
        }catch (Exception e){
            handleException(e, mainCtrl.getTranslator());
        }
    }

    public void handleEditTag() {
        try{
            mainCtrl.showTagScreen(addEditExpenseMv.getEvent(), addEditExpenseMv.getTag());
        }catch (Exception e){
            handleException(e, mainCtrl.getTranslator());
        }
    }

    public void handleAddTag() {
        mainCtrl.showTagScreen(addEditExpenseMv.getEvent(), null);
    }

    @Override
    public void listeners() {
        Scene s = currencyField.getScene();
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.B, () -> abortButtonPressed(new ActionEvent()));
        mainCtrl.getKeyBoardListeners().addListener(s, KeyCode.ENTER, this::createExpense);
    }


}
