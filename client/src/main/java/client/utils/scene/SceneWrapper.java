package client.utils.scene;

import javafx.scene.Parent;
import javafx.scene.Scene;


public class SceneWrapper {

    private SceneController sceneController;

    private Parent parent;

    private Scene scene;

    public SceneWrapper(SceneController sceneController, Parent parent) {
        this.sceneController = sceneController;
        this.parent = parent;

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
