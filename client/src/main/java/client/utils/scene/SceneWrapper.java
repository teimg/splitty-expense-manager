package client.utils.scene;

import javafx.scene.Parent;


public class SceneWrapper {

    private SceneController sceneController;

    private Parent parent;

    public SceneWrapper(SceneController sceneController, Parent parent) {
        this.sceneController = sceneController;
        this.parent = parent;


    }

    public SceneController getSceneController() {
        return sceneController;
    }

    public Parent getParent() {
        return parent;
    }

}
