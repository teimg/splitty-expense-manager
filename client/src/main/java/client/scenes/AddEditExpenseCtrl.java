package client.scenes;

import client.ModelView.AddEditExpenseMv;
import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.*;
import client.utils.communicators.implementations.ExpenseCommunicator;
import client.utils.communicators.implementations.TagCommunicator;
import client.utils.communicators.interfaces.IExpenseCommunicator;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.checkerframework.checker.units.qual.C;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.time.format.DateTimeParseException;
import java.util.List;


public class AddEditExpenseCtrl  implements Initializable, LanguageSwitch, SceneController {

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
    private ListView<Participant> innerCheckboxes;

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
    private ComboBox<String> tagField;

    @FXML
    private ComboBox<Participant> whoPaidField;

    @FXML
    private Button deleteTagButton;

    @FXML
    private Button editTagButton;

    @FXML
    private Button addTagButton;

    private ExpenseBuilder expenseBuilder;

    private DebtorSelector debtorSelector;

    private WhoPaidSelector whoPaidSelector;

    private WhichTagSelector whichTagSelector;

    private Event event;


    // true if input is an edit meaning that the current expense is being edited
    private boolean isEdit;

    // Only used when there is an edit going on
    private Expense expense;

    private final MainCtrl mainCtrl;



    private final ExpenseCommunicator expenseCommunicator;


    private final AddEditExpenseMv addEditExpenseMv;


    private class InnerCheckBoxManger
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
            System.out.println("change!!");
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


    private final TagCommunicator tagCommunicator;

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
    public AddEditExpenseCtrl (MainCtrl mainCtrl, AddEditExpenseMv addEditExpenseMv, ExpenseCommunicator expenseCommunicator,
                               TagCommunicator tagCommunicator) {
        this.addEditExpenseMv = addEditExpenseMv;
        this.mainCtrl = mainCtrl;

        this.expenseCommunicator = expenseCommunicator;
        this.tagCommunicator = tagCommunicator;
    }

    public void loadInfo(Event event, Expense expense) {
        loadInfo(event);

       addEditExpenseMv.loadExpense(expense);
    }


    public void loadInfo(Event event) {
        addEditExpenseMv.loadInfo(event);

        initWhoPaid();
        initTag();
        initCheckbox();
        initCurrency();
        initDateField();

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

        initBindings();

    }

    public void initBindings() {
        priceField.textProperty().bindBidirectional(
            addEditExpenseMv.priceFieldProperty());
        currencyField.promptTextProperty().bindBidirectional(
            addEditExpenseMv.currencyFieldProperty());
        descriptionField.textProperty().bindBidirectional(
            addEditExpenseMv.descriptionFieldProperty());
        dateField.valueProperty().bindBidirectional(
            addEditExpenseMv.dateFieldProperty());
        whoPaidField.valueProperty().bindBidirectional(
            addEditExpenseMv.whoPaidFieldProperty());
        evenlyCheckbox.selectedProperty().bindBidirectional(
            addEditExpenseMv.evenlyCheckboxProperty());
//        whoPaidField.itemsProperty().bindBidirectional(addEditExpenseMv.whoPaidItemsProperty());
//        whoPaidField.itemsProperty().bindBidirectional(
//            addEditExpenseMv.whoPaidItemsProperty());
    }

    private static class InnerCheckbox{
        private final CheckBox checkBox;

        /**
         *
         * @param name label used for checkbox should
         *             equal name of participant
         * @param parent ref to the parent vbox
         * @param debtorSelector ref to the debtselector object
         */
        public InnerCheckbox(String name, VBox parent, DebtorSelector debtorSelector) {
            this.checkBox = new CheckBox(name);
            this.checkBox.setPadding(new Insets(5, 0, 0, 0));
            parent.getChildren().add(this.checkBox);

            this.checkBox.setOnAction( e->{
                if(this.checkBox.isSelected()){
                    debtorSelector.add(this.checkBox.getText());
                }else {
                    debtorSelector.remove(this.checkBox.getText());
                }
            });
        }

        public void setSelected(boolean value){
            checkBox.setSelected(value);
        }

    }

//    public void setEdit(){
//        whoPaidField.setValue(this.expense.getPayer().getName());
//        if (expense.getTag() != null) {
//            tagField.setValue(this.expense.getTag().getName());
//        }
//        priceField.setText(Double.toString(this.expense.getAmount()));
//        evenlyCheckbox.setSelected(false);
//
//        someCheckbox.setSelected(true);
//        innerCheckboxes.setDisable(false);
//        debtorSelector.setAllSelected(false);
//        for(Participant x : this.expense.getDebtors()){
//            if(this.expense.getDebtors().contains(x))
//                debtorSelector.add(x.getName());
//        }
//
//        descriptionField.setText(this.expense.getPurchase());
//        dateField.setValue(this.expense.getDate());
//    }
//

    public void initWhoPaid(){
        // temp disable whoPaidSelector Since to unstable
//        whoPaidField.setEditable(false);
//        this.whoPaidField.setVisibleRowCount(3);

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

        whoPaidField.getEditor().setOnKeyReleased(x -> {

            if(!x.getCode().isLetterKey()){
                return;
            }

            whoPaidField.getItems().removeAll(whoPaidField.getItems());
            whoPaidField.setValue(whoPaidSelector.getCurrentPayer(whoPaidField.getEditor().getText()));

            whoPaidField.getItems().removeAll(whoPaidField.getItems());
            whoPaidField.getItems()
                .addAll(whoPaidSelector
                    .query(
                        whoPaidField.getEditor().getText()));
            whoPaidField.show();
        });
    }

    public void initTag(){
        this.whichTagSelector = new WhichTagSelector(this.tagCommunicator.getAllTags());
        this.tagField.setValue(null);
        tagField.getItems().clear();
        this.tagField.setVisibleRowCount(3);
        tagField.getItems().addAll(
                this.tagCommunicator.getAllTags()
                        .stream()
                        .map(Tag::getName)
                        .toList());

        tagField.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent x) {

                if(x.getCode() == KeyCode.TAB){
                    return;
                }

                tagField.getItems().removeAll(tagField.getItems());
                tagField.setValue(tagField.getEditor().getText());

                tagField.getItems().addAll(whichTagSelector.query(tagField.getEditor().getText()));

                tagField.show();
            }
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
        currencyField.setValue("EUR");
        currencyField.getItems().add("EUR");
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
        innerCheckboxes.setDisable(true);

    }

    /**
     * Event triggered
     * Toggles the ven checkbox
     */
    public void evenlyCheckboxToggle(){
        if(evenlyCheckbox.isSelected()){
            someCheckbox.setSelected(false);
            scrollPane.setDisable(true);
//            debtorSelector.setAllSelected(true);
            return;
        }
        someCheckbox.setSelected(true);
        scrollPane.setDisable(false);
//        debtorSelector.setAllSelected(false);

    }

    public void clear(){
        whoPaidField.getItems().removeAll(whoPaidField.getItems());

    }

    public Tag getTag(){
        Tag res = whichTagSelector.getCurrentTag(tagField.getValue());

        if(res == null){
            throw new IllegalArgumentException("tag");
        }

        return res;
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
        System.out.println(this.whoPaidField.getSelectionModel().getSelectedItem());
        try {
            Expense added = addEditExpenseMv.createExpense();
            Event res = addEditExpenseMv.getEvent();
            System.out.println(added.toString());
            addEditExpenseMv.clear();
            clear();
            mainCtrl.showEventOverview(res);
            return;

        }catch (Exception e){
            e.printStackTrace();
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

    public void handleDeleteTag() {
        this.tagCommunicator.deleteTag(getTag().getId());
        initTag();
    }

    public void handleEditTag() {
        mainCtrl.showTagScreen(event, getTag());
        initTag();
    }

    public void handleAddTag() {
        mainCtrl.showTagScreen(event, null);
        initTag();
    }


}