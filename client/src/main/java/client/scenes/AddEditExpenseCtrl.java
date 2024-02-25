package client.scenes;

import client.utils.ExpenseBuilder;
import com.sun.scenario.animation.shared.FiniteClipEnvelope;
import commons.Expense;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.checkerframework.checker.units.qual.C;

import java.net.URL;
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

    private class Innercheckbox{
        private  CheckBox checkBox;

        public Innercheckbox(String name, VBox parent) {
            this.checkBox = new CheckBox(name);
            this.checkBox.setPadding(new Insets(5, 0, 0, 0));
            parent.getChildren().add(this.checkBox);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expenseBuilder = new ExpenseBuilder();

        initCheckbox();
        initCurrency();

        for (int i = 0; i < 10; i++) {
            new Innercheckbox("test " + i, innerCheckboxes);

        }
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
            innerCheckboxes.setDisable(true);
            return;
        }
        someCheckbox.setSelected(true);
        innerCheckboxes.setDisable(false);
    }

    public void addButtonPressed(){
        System.out.println();
    }

    /**
     * create the final expense
     *
     * @return an expense
     */
    public Expense createExpense(){
        expenseBuilder.setPurchase(decsriptionField.getText());

        System.out.println(expenseBuilder.toString());
        return expenseBuilder.build();
    }
}
