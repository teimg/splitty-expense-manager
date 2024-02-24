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

    public void initCheckbox(){
        evenlyCheckbox.setSelected(true);
        someCheckbox.setSelected(false);
        evenlyCheckbox.setAllowIndeterminate(false);
        someCheckbox.setAllowIndeterminate(false);
        innerCheckboxes.setDisable(true);


    }

    public void checkboxToggle(){
        if(evenlyCheckbox.isSelected()){
            evenlyCheckbox.setSelected(false);
            someCheckbox.setSelected(true);
            return;
        }

        evenlyCheckbox.setSelected(true);
        someCheckbox.setSelected(false);

    }
}
