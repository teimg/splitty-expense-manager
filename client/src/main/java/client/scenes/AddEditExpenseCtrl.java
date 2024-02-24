package client.scenes;

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
        initCheckbox();
        for (int i = 0; i < 10; i++) {
            new Innercheckbox("test " + i, innerCheckboxes);

        }
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

    /**
     * create the final expanse
     */
    public void createExpense(){

    }
}
