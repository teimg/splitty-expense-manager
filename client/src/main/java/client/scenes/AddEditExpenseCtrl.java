package client.scenes;

import client.ModelView.AddEditExpenseMv;
import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.*;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
    private ComboBox<String> whoPaidField;


    private WhoPaidSelector whoPaidSelector;


    private final MainCtrl mainCtrl;


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
    }

    @Inject
    public AddEditExpenseCtrl (MainCtrl mainCtrl, AddEditExpenseMv addEditExpenseMv) {
        this.mainCtrl = mainCtrl;
        this.addEditExpenseMv = addEditExpenseMv;
    }

    public void loadInfo(Event event) {
        addEditExpenseMv.loadInfo(event);

        initWhoPaid();
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

    public void initBindings(){
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
        whoPaidField.itemsProperty().bindBidirectional(
            addEditExpenseMv.whoPaidItemsProperty());
    }

//    public void setEdit(){
//        whoPaidField.setValue(this.expense.getPayer().getName());
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

    public void initWhoPaid(){
        this.whoPaidField.setVisibleRowCount(3);

        whoPaidField.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent x) {

                if(x.getCode() == KeyCode.TAB){
                    return;
                }

                whoPaidField.getItems().removeAll(whoPaidField.getItems());
                whoPaidField.setValue(whoPaidField.getEditor().getText());

                whoPaidField.getItems()
                    .addAll(whoPaidSelector
                        .query(
                            whoPaidField.getEditor().getText()));
                whoPaidField.show();
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

    public void addButtonPressed(){
        createExpense();
    }

    public void abortButtonPressed(ActionEvent actionEvent) {
        addEditExpenseMv.clear();
        mainCtrl.showEventOverview(addEditExpenseMv.getEvent());
    }

    public void createExpense(){

        try {
            Expense added = addEditExpenseMv.createExpense();
            System.out.println(added.toString());
            mainCtrl.showEventOverview(addEditExpenseMv.getEvent());
            return;
        }catch (Exception e){
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