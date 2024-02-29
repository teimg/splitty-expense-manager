package client.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;


public class Popup extends Dialog<String> {

    public enum TYPE{
        ERROR (){
            public String  toString(){
                return "Error";
            }

            public Image getImage(){
                return  new Image("file:client/src/main/resources/client/icons/error.webp", true);
            }
        },
        INFO(){
            public String  toString(){
                return "Info";
            }

            public Image getImage(){
                return  new Image("file:client/src/main/resources/client/icons/info.png", true);
            }
        };

        public Image getImage(){
            return  null;
        }
        public String  toString(){
            return "No type";
        }

    }

    private TYPE type;
    private DialogPane dialogPane;
    private String msg;
    private Label label;

    private final int  FONT_SIZE = 16;
    private final int IMAGE_SIZE = 50;
    private int width;
    private int height;

    /**
     * Init label of dialog
     */

    private void initLabel(){

        Image image = new Image("file:client/src/main/resources/client/icons/error.png", true);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);

        this.label = new Label();
        this.label.setText(msg);
        this.label.setFont(new Font(this.FONT_SIZE));

        this.label.setGraphic(imageView);
        this.label.setGraphicTextGap(20);
    }

    /**
     * Init dialog pane
     */
    private void initDialogPane(){
        this.dialogPane = new DialogPane();
        this.dialogPane.setContent(label);

        this.dialogPane.getButtonTypes().add(
            new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

        this.dialogPane.setPrefWidth(width);
        this.dialogPane.setPrefHeight(height);

        this.setDialogPane(dialogPane);
    }

    /**
     * Init dialog popu
     */
    private void initPopup(){
        this.setResizable(false);
        this.setResult("");
        this.setTitle(this.type.toString());

        this.setOnCloseRequest(
            e -> this.close()
        );
    }


    /**
     * Create a popup object extneding from Dialog
     * call popup.show() to show the Dialog/popup
     * @param msg the string to be displayed in the dialog
     * @param type The type of dialog is either Popup.TYPE.ERROR or Popup.TYPE.INFO
     */
    public Popup(String msg, Popup.TYPE type) {
        this.type = type;
        this.msg = msg;
        this.width = 300;
        this.height = 100;
        initPopup();
        initLabel();
        initDialogPane();
    }

}
