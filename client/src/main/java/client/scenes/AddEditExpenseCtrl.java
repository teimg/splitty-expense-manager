package client.scenes;

import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.*;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
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


    // true if input is an edit meaning that the current expense is being edited
    private boolean isEdit;

    // Only used when there is an edit going on
    private Expense expense;

    private final MainCtrl mainCtrl;

    private final IExpenseCommunicator expenseCommunicator;

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
    public AddEditExpenseCtrl (MainCtrl mainCtrl, ExpenseCommunicator expenseCommunicator) {
        this.mainCtrl = mainCtrl;
        this.expenseCommunicator = expenseCommunicator;
    }

    public void loadInfo(Event event) {
        this.event = event;
        expenseBuilder = new ExpenseBuilder();
        List<Participant> participants = this.event.getParticipants();
        debtorSelector = new DebtorSelector(participants);

        initWhoPaid();
        initCheckbox();
        initCurrency();
        initDateField();

        for (int i = 0; i < participants.size(); i++) {

            Participant currentParticipant = participants.get(i);
            InnerCheckbox innercheckbox =
                    new InnerCheckbox(currentParticipant.getName(),
                            innerCheckboxes, debtorSelector);
            if(this.isEdit &&
                    this.expense.getDebtors().contains(currentParticipant))
                innercheckbox.setSelected(true);

        }

        if(this.isEdit){
            setEdit();
        }
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
        // createDummyEvent();
    }

    public void setEdit(){
        whoPaidField.setValue(this.expense.getPayer().getName());
        priceField.setText(Double.toString(this.expense.getAmount()));
        evenlyCheckbox.setSelected(false);

        someCheckbox.setSelected(true);
        innerCheckboxes.setDisable(false);
        debtorSelector.setAllSelected(false);
        for(Participant x : this.expense.getDebtors()){
            if(this.expense.getDebtors().contains(x))
                debtorSelector.add(x.getName());
        }

        descriptionField.setText(this.expense.getPurchase());
        dateField.setValue(this.expense.getDate());
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
        Expense res = createExpense();
        if(res == null){
            return;
        }
        Expense added = expenseCommunicator.createExpense(res);
        event.addExpense(added);
        System.out.println(added.toString());
        mainCtrl.showEventOverview(event);
    }

    public void abortButtonPressed(ActionEvent actionEvent) {
        mainCtrl.showEventOverview(event);
    }

    /**
     * get the value of the priceField
     *
     * @return the amount in euro cents right now
     */
    private long getPriceFieldValue() {
        // Code should be moved to a different class, so it can be tested
        long res = 0;
        String value = priceField.getText();
        String[] values = value.split(",|\\.");

        try{
            res += Long.parseLong(values[0]) * 100;

            if(values.length == 2){
                res += Long.parseLong(values[1]);
            }

            if(values.length > 2 || res < 0){
                throw new NumberFormatException();
            }

        }catch (NumberFormatException e){
            throw new IllegalArgumentException("price");
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

        Participant res = whoPaidSelector.getCurrentPayer(whoPaidField.getValue());

        if(res == null){
            throw new IllegalArgumentException("payer");
        }

        return res;
    }

    public String getPurchase(){
        String res = descriptionField.getText();

        if(res == null || res.isEmpty()){
            throw new IllegalArgumentException("purchase");
        }
        return  res;
    }

    public List<Participant> getDebtors(){
        List<Participant> res = debtorSelector.getDebitors();

        if(res.isEmpty()){
            throw new IllegalArgumentException("participant");
        }

        return res;
    }

    /**
     * create the final expense
     *
     * @return an expense
     */
    public Expense createExpense(){

        try {
            expenseBuilder.setPayer(getPayer());
            expenseBuilder.setPurchase(getPurchase());
            expenseBuilder.setDate(getDateFieldValue());
            expenseBuilder.setAmount(getPriceFieldValue());
            expenseBuilder.setDebtors(getDebtors());
            expenseBuilder.setEvent(event);

            System.out.println(expenseBuilder.toString());
            return expenseBuilder.build();
        } catch (IllegalArgumentException e){
            String msg = switch (e.getMessage()) {
                case "payer" -> "PayerFieldInvalid";
                case "price" -> "PriceFieldInvalid";
                case "purchase" -> "PurchaseLeftEmpty";
                case "participant" -> "NoSelectedDebtors";
                default -> "Unknown input error";
            };

            Popup popup = new Popup(
                mainCtrl.getTranslator().getTranslation("Popup." + msg),
                Popup.TYPE.ERROR);
            popup.show();
        }catch (Exception e){
            Popup popup = new Popup(
                mainCtrl.getTranslator().getTranslation("Popup.UnknownError"),
                Popup.TYPE.ERROR);
            popup.show();
        }

        return null;

    }


}