package client.dialog;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfPopup extends Alert {

    public ConfPopup(String question) {
        super(AlertType.CONFIRMATION, question , ButtonType.CANCEL, ButtonType.YES);
    }


    /**
     * Create popup without calling new (for efficient calling)
     * @param question question to ask teh user
     * @return new ConfPopup
     */
    public static ConfPopup create(String question){
        return new ConfPopup(question);
    }

    /**
     * displays the alert and waits for input
     *
     * @return the popup
     */
    public ConfPopup display(){
        this.showAndWait();
        return this;
    }

    /**
     *
     * @return true if yes was pressed
     */

    public boolean isConfirmed(){
        return this.getResult() == ButtonType.YES;
    }
}
