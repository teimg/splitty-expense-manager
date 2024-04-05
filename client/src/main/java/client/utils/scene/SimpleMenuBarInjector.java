//package client.utils.scene;
//
//import javafx.collections.ObservableList;
//import javafx.scene.Node;
//import javafx.scene.layout.Pane;
//
//public class SimpleMenuBarInjector implements MenuBarInjector {
//    @Override
//    public void accept(SceneWrapper sceneWrapper, Node menuBar) {
//        ObservableList<Node> allNodes = ((Pane) sceneWrapper.getScene().getRoot()).getChildren();
//        if (allNodes.getFirst() != menuBar) {
//            allNodes.addFirst(menuBar);
//        }
//    }
//}
