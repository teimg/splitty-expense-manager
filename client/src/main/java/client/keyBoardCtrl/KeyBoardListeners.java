package client.keyBoardCtrl;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

import java.util.*;

public class KeyBoardListeners {

    private static final ArrayList<Pair<Scene,
            ArrayList<EventHandler<KeyEvent>>>> listeners = new ArrayList<>();

    public void addListener(Scene scene, KeyCode code, KeyPressedAction action) {
        final boolean[] keyPressed = {false};

        EventHandler<KeyEvent> keyPressedHandler = event -> {
            if (event.getCode() == code && !keyPressed[0] && event.isControlDown()) {
                keyPressed[0] = true;
                action.execute();
                event.consume();
            }
        };

        EventHandler<KeyEvent> keyReleasedHandler = event -> {
            if (event.getCode() == code) {
                keyPressed[0] = false;
            }
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedHandler);

        ArrayList<EventHandler<KeyEvent>> handlers = new ArrayList<>();
        handlers.add(keyPressedHandler);
        handlers.add(keyReleasedHandler);

        listeners.add(new Pair<>(scene, handlers));
    }

    public void resetListeners() {
        for (Pair<Scene, ArrayList<EventHandler<KeyEvent>>> pair : listeners) {
            Scene s = pair.getKey();
            for (EventHandler<KeyEvent> event : pair.getValue()) {
                s.removeEventFilter(KeyEvent.KEY_PRESSED, event);
            }
        }
        listeners.clear();
    }

}
