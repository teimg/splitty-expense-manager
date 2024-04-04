package client.nodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UIIcon extends ImageView {
    private final int wSize = 14;
    private final int hSize = 14;
    private final String path = "file:client/src/main/resources/client/icons/";
    private final UIIcon.NAME name;

    public UIIcon(UIIcon.NAME name) {
        this.name = name;
        this.setImage(createImage());
    }

    static  public UIIcon icon(UIIcon.NAME name){
        return new UIIcon(name);
    }

    private Image createImage(){
        return new Image(path + name.toString(), wSize, hSize, true, true);
    }


    public enum NAME {
        DELETE("delete.png"),
        EDIT("edit.png"),
        CLIPBOARD("clipboard.png");

        private final String name;

        NAME(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }


}
