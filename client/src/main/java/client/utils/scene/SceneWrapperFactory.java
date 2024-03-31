package client.utils.scene;

import javafx.scene.Parent;

import java.util.function.BiFunction;

public interface SceneWrapperFactory extends BiFunction<SceneController, Parent, SceneWrapper> {}