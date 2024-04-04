package client.dialog;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfPopup extends Alert {

    /**
     *
     * @param question question to ask the user
     */
    public ConfPopup(String question) {
        super(AlertType.CONFIRMATION, question, ButtonType.YES, ButtonType.NO);
        setTitle("Confirmation");
    }

    /**
     *
     * @return true if yes was pressed
     */
    public boolean isConfirmed() {
        this.showAndWait();
        return this.getResult() == ButtonType.YES;
    }


    /**
     * Create popup without calling new (for efficient calling)
     * @param question question to ask teh user
     * @return new ConfPopup
     */
    public static ConfPopup create(String question){
        return new ConfPopup(question);
    }


}
