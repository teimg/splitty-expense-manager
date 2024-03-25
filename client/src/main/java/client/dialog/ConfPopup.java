package client.dialog;


import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ConfPopup extends Alert {

    public ConfPopup(String question) {
        super(AlertType.CONFIRMATION, question , ButtonType.CANCEL, ButtonType.YES);
    }

    public static ConfPopup CREATE(String question){
        return new ConfPopup(question);
    }

    public ConfPopup display(){
        this.showAndWait();
        return this;
    }

    public boolean isConfirmed(){
        return this.getResult() == ButtonType.YES;
    }
}
