package client.scenes;

import client.utils.DebtorSelector;
import client.utils.ExpenseBuilder;
import commons.Expense;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditExpenseCtrl  implements Initializable {

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
    private TextField decsriptionField;

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

    private class Innercheckbox{
        private  CheckBox checkBox;

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
     * I have no idea where to put this since backend communication and getting events I just put it here for now
     * @return dummy participant list
     */

    public List<Participant> getDummyParticipants(){

        List<Participant> res = new ArrayList<>();
        res.add(new Participant("Henk"));
        res.add(new Participant("Piet"));
        res.add(new Participant("Joost"));
        res.add(new Participant("Barry"));
        res.add(new Participant("Joe"));
        res.add(new Participant("Jan"));
        res.add(new Participant("Abel"));
        res.add(new Participant("Pietje"));
        res.add(new Participant("Trien"));

        return res;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expenseBuilder = new ExpenseBuilder();
        List<Participant> dummyParticipants = getDummyParticipants();
        debtorSelector = new DebtorSelector(dummyParticipants);

        initCheckbox();
        initCurrency();
        initDateField();


        for (int i = 0; i < dummyParticipants.size(); i++) {

            new Innercheckbox(dummyParticipants.get(i).getName(), innerCheckboxes, debtorSelector);

        }
    }

    public void initDateField(){
        // to disable manuel editing, this way a date can never be invalid hopefully
        dateField.getEditor().setEditable(false);
        dateField.getEditor().setDisable(true);
        dateField.getEditor().setOpacity(1);;


        dateField.setValue(LocalDate.now());
    }


    public void initCurrency(){
        currencyField.setValue("EUR");
        currencyField.getItems().add("EUR");
    }


    /**
     * Bring the checkboxes to there starting state (should be more advanced when editing becomes an option)
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
        value = value.replaceAll("[^.,0-9]", "");
        String[] values = value.split(",|\\.");

        try{
            res += Long.parseLong(values[0]) * 100;

            if(values.length >= 2){
                res += Long.parseLong(values[1]);
            }

        }catch (NumberFormatException e){
            res = 0;
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

    /**
     * create the final expense
     *
     * @return an expense
     */
    public Expense createExpense(){
        expenseBuilder.setPurchase(decsriptionField.getText());
        expenseBuilder.setDate(getDateFieldValue());
        expenseBuilder.setAmount(getPriceFieldValue());
        expenseBuilder.setDebtors(debtorSelector.getDebitors());

        System.out.println(expenseBuilder.toString());
        return expenseBuilder.build();
    }


}
