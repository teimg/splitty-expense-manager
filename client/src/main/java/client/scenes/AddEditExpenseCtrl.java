package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddEditExpenseCtrl {

    @FXML
    private Button abortButton;

    @FXML
    private Button addButton;

    @FXML
    private ComboBox<?> currencyField;

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
    private ComboBox<?> tagField;

    @FXML
    private ComboBox<?> whoPaidField;

}
