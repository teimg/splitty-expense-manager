package client.utils;

import client.scenes.MenuBarCtrl;
import jakarta.inject.Inject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneWrapper {

    private SceneController sceneController;

    private Parent parent;

    private Scene scene;

    public SceneWrapper(SceneController sceneController, Parent parent) {
        this.sceneController = sceneController;
        this.parent = parent;

//        ((Pane) (this.parent)).getChildren().addFirst(menuBar);
        this.scene = new Scene(this.parent);


    }


    public SceneController getSceneController() {
        return sceneController;
    }

    public Parent getParent() {
        return parent;
    }

    public Scene getScene() {
        return scene;
    }
}
