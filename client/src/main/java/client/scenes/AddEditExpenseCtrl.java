package client.scenes;

import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.DebtorSelector;
import client.utils.ExpenseBuilder;
import client.utils.WhoPaidSelector;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditExpenseCtrl  implements Initializable, LanguageSwitch {

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
    private MenuBar menuBar;

    @FXML
    private Button abortButton;

    @FXML
    private Button addButton;

    @FXML
    private VBox innerCheckboxes;

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
    private ComboBox<String> tagField;

    @FXML
    private ComboBox<String> whoPaidField;

    private ExpenseBuilder expenseBuilder;

    private DebtorSelector debtorSelector;

    private WhoPaidSelector whoPaidSelector;

    private Event event;

    private final MainCtrl mainCtrl;

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
    public AddEditExpenseCtrl (MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    private static class Innercheckbox{
        private  CheckBox checkBox;

        /**
         *
         * @param name label used for checkbox should
         *             equal name of participant
         * @param parent ref to the parent vbox
         * @param debtorSelector ref to the debtselector object
         */
        public Innercheckbox(String name, VBox parent, DebtorSelector debtorSelector) {
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

    }

    /**
     * simple dummy event for now;
     */
    public void createDummyEvent(){
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("Henk"));
        participants.add(new Participant("Piet"));
        participants.add(new Participant("Joost"));
        participants.add(new Participant("Barry"));
        participants.add(new Participant("Joe"));
        participants.add(new Participant("Jan"));
        participants.add(new Participant("Abel"));
        participants.add(new Participant("Pietje"));
        participants.add(new Participant("Trien"));

        this.event = new Event(1, "dummyEvent", "CODE", participants, new Date(), new Date());
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
        // Should be removed once backend is created
        createDummyEvent();

        expenseBuilder = new ExpenseBuilder();
        List<Participant> participants = this.event.getParticipants();
        debtorSelector = new DebtorSelector(participants);

        initWhoPaid();
        initCheckbox();
        initCurrency();
        initDateField();


        for (int i = 0; i < participants.size(); i++) {

            new Innercheckbox(participants.get(i).getName(), innerCheckboxes, debtorSelector);

        }
    }

    public void initWhoPaid(){
        this.whoPaidSelector = new WhoPaidSelector(this.event.getParticipants());
        this.whoPaidField.setVisibleRowCount(3);
        whoPaidField.getItems().addAll(
            this.event.getParticipants()
                .stream()
                .map(Participant::getName)
                .toList());

        whoPaidField.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent x) {
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
        innerCheckboxes.setDisable(true);
        debtorSelector.setAllSelected(true);
    }

    /**
     * Event Triggered
     * Toggles the someCheckbox
     *
     */
    public void someCheckboxToggle(){
        if(someCheckbox.isSelected()){
            evenlyCheckbox.setSelected(false);
            innerCheckboxes.setDisable(false);
            debtorSelector.setAllSelected(false);
            return;
        }
        evenlyCheckbox.setSelected(true);
        innerCheckboxes.setDisable(true);
        debtorSelector.setAllSelected(true);

    }

    /**
     * Event triggered
     * Toggles the ven checkbox
     */
    public void evenlyCheckboxToggle(){
        if(evenlyCheckbox.isSelected()){
            someCheckbox.setSelected(false);
            innerCheckboxes.setDisable(true);
            debtorSelector.setAllSelected(true);
            return;
        }
        someCheckbox.setSelected(true);
        innerCheckboxes.setDisable(false);
        debtorSelector.setAllSelected(false);

    }

    public void addButtonPressed(){
        createExpense();
    }

    /**
     * get the value of the priceField
     *
     * @return the amount in euro cents right now
     */
    private long getPriceFieldValue() {

        // Code should be moved to a different class so it can be tested

        long res = 0;
        String value = priceField.getText();
//        value = value.replaceAll("[^.,0-9]", "");
        String[] values = value.split(",|\\.");

        res += Long.parseLong(values[0]) * 100;

        if(values.length == 2){
            res += Long.parseLong(values[1]);
        }

        if(values.length > 2){
            throw new NumberFormatException();
        }

        return res;
    }

    /**
     * Get the value in date field
     * @return the Date set in the date field
     */

    public LocalDate getDateFieldValue(){
        try{
            return  dateField.getValue();

        }catch (DateTimeParseException e){
            // Not a very nice solution, but will work for now
            return LocalDate.now();
        }
    }

    public  Participant getPayer(){
        System.out.println(whoPaidField.getValue());
        return whoPaidSelector.getCurrentPayer(whoPaidField.getValue());
    }

    /**
     * create the final expense
     *
     * @return an expense
     */
    public Expense createExpense(){

        try {
            expenseBuilder.setPayer(getPayer());
            expenseBuilder.setPurchase(descriptionField.getText());
            expenseBuilder.setDate(getDateFieldValue());
            expenseBuilder.setAmount(getPriceFieldValue());
            expenseBuilder.setDebtors(debtorSelector.getDebitors());
            expenseBuilder.setEvent(event);

            System.out.println(expenseBuilder.toString());
            return expenseBuilder.build();
        }catch (NumberFormatException e){
            Popup popup = new Popup("Price is not a valid digit!", Popup.TYPE.ERROR);
            popup.show();
            return null;
        }

    }


}