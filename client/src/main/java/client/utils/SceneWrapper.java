package client.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneWrapper {

    private SceneController sceneController;

    private Parent parent;

    private Scene scene;

    public SceneWrapper(SceneController sceneController, Parent parent, Scene scene) {
        this.sceneController = sceneController;
        this.parent = parent;
        this.scene = scene;
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
