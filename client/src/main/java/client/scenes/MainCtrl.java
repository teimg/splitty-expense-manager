/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.currency.Exchanger;
import client.keyBoardCtrl.KeyBoardListeners;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.language.Translator;
import client.utils.ClientConfiguration;
import client.utils.RecentEventTracker;
import client.utils.communicators.interfaces.IEventUpdateProvider;
import client.utils.scene.SceneController;
import client.utils.scene.SceneWrapper;
import client.utils.scene.SceneWrapperFactory;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class MainCtrl {

    private Stage primaryStage;
    private Map<String, SceneWrapper> scenes;

    private Translator translator;

    private Pair<String, LanguageSwitch> currentCtrl;

    private final ClientConfiguration config;

    private MenuBarCtrl menuBarCtrl;

    private Parent menuBar;

    protected BorderPane baseScene;

    private final RecentEventTracker recentEventTracker;

    private final SceneWrapperFactory sceneWrapperFactory;

    private final KeyBoardListeners keyBoardListeners;


    private final Exchanger exchanger;

    private final IEventUpdateProvider eventUpdateProvider;

    @Inject
    public MainCtrl(ClientConfiguration config, Translator translator,
                    RecentEventTracker recentEventTracker,
                    SceneWrapperFactory sceneWrapperFactory,
                    Exchanger exchanger,
                    KeyBoardListeners keyBoardListeners,
                    IEventUpdateProvider eventUpdateProvider) {
        this.config = config;
        this.translator = translator;
        this.exchanger = exchanger;
        this.keyBoardListeners = keyBoardListeners;
        if (config != null){
            this.translator.setCurrentLanguage(config.getStartupLanguage());
            this.exchanger.setCurrentCurrency(config.getCurrency());
        }
        this.recentEventTracker = recentEventTracker;
        this.sceneWrapperFactory = sceneWrapperFactory;
        this.eventUpdateProvider = eventUpdateProvider;
    }

    @SuppressWarnings("unchecked")
    public void initialize(Stage primaryStage, HashMap<String, Object> sceneMap) {
        this.primaryStage = primaryStage;
        this.scenes = new HashMap<>();

        Pair<MenuBarCtrl, Parent>  menuBar = (Pair<MenuBarCtrl, Parent>)
                sceneMap.get("MenuBar");

        this.menuBarCtrl = menuBar.getKey();
        this.menuBar = menuBar.getValue();
        this.menuBarCtrl.setLanguage();

        initScenes(sceneMap);
        createBaseScene();

        primaryStage.setWidth(config.getWindowWidth());
        primaryStage.setHeight(config.getWindowHeight());
        showStartScreen();
        primaryStage.show();

    }

    /**
     * Creates a map for all scenes in a SceneWrapper
     * @param sceneMap map used for creating the map of scenes
     */

    @SuppressWarnings("unchecked")
    private void initScenes(HashMap<String, Object> sceneMap) {
        for(String x : sceneMap.keySet()){
            Pair<Object, Parent> current = (Pair<Object, Parent>) sceneMap.get(x);

            Object o = current.getKey();
            if(!(o instanceof SceneController)){
                continue;
            }

            SceneController currentSceneController = (SceneController) current.getKey();
            this.scenes.put(x, new SceneWrapper(currentSceneController, current.getValue()));


        }
    }

    public void createBaseScene(){
        this.baseScene = new BorderPane();
        this.primaryStage.setScene(new Scene(this.baseScene));
        this.baseScene.setTop(menuBar);
    }

    /**
     * Performs final actions before stopping the application.
     */
    public void stop() {

        config.setWindowWidth(primaryStage.getWidth());
        config.setWindowHeight(primaryStage.getHeight());
        config.setCurrency(exchanger.getCurrentCurrency());

        config.save();

        recentEventTracker.persistEvents();

    }

    /**
     * gets the title of the current scne
     * @param sceneName name of the scene you want to get the title for
     * @return the window title in right language
     */

    private String getTitle(String sceneName){
        String prefix = "Titles.";
        return translator.getTranslation(prefix + sceneName);

    }

    /**
     * General method for showing the right scene
     *
     * @param scene name of the scene to display
     * @param title window title of the new scene
     */

    private void show(String scene, String title){

        SceneWrapper currentSceneWrapper = this.scenes.get(scene);

        if (currentSceneWrapper == null){
            throw new IllegalArgumentException("No such scene: " + scene);
        }

//        menuBarInjector.accept(currentSceneWrapper, menuBar);

        this.currentCtrl =
            new Pair<> (scene, (LanguageSwitch) currentSceneWrapper.getSceneController());

        primaryStage.setTitle(title);
//        primaryStage.setScene(currentSceneWrapper.getScene());
        this.baseScene.setCenter(currentSceneWrapper.getParent());
        keyBoardListeners.resetListeners();
        ((ShortCuts) currentSceneWrapper.getSceneController()).listeners();
    }

    private void show(String scene){
        show(scene, getTitle(scene));
    }

    public void showAddEditExpense(Event event) {
        if ((this.currentCtrl.getValue()) instanceof TagScreenCtrl) {
            show("AddEditExpense");
            ((AddEditExpenseCtrl)(this.currentCtrl.getValue())).updateTags(event);
        } else {
            show("AddEditExpense");
            ((AddEditExpenseCtrl) (this.currentCtrl.getValue())).loadInfo(event);
        }
        this.currentCtrl.getValue().setLanguage();
    }

    public void showAddEditExpense(Event event, Expense expense) {
        show("AddEditExpense");
        ((AddEditExpenseCtrl)(this.currentCtrl.getValue())).loadInfo(event, expense);
        this.currentCtrl.getValue().setLanguage();
    }

    public void showInvitation(Event event) {
        show("Invitation");
        ((InvitationCtrl)(this.currentCtrl.getValue())).loadEvent(event);
        this.currentCtrl.getValue().setLanguage();
    }

    public void showOpenDebts(Event event) {
        show("OpenDebts");
        ((OpenDebtsCtrl)(this.currentCtrl.getValue())).loadInfo(event);
        this.currentCtrl.getValue().setLanguage();
    }

    public void showStartScreen() {
        eventUpdateProvider.stop();
        show("StartScreen");
        ((StartScreenCtrl)(this.currentCtrl.getValue())).loadInfo();
        this.currentCtrl.getValue().setLanguage();
    }

    public void showStatistics(Event event) {
        show("Statistics");
        ((StatisticsScreenCtrl)(this.currentCtrl.getValue())).loadInfo(event);
        this.currentCtrl.getValue().setLanguage();
    }

    public void showContactInfo(Event event, Participant participant){
        show("ContactInfo");
        ((ContactInfoCtrl)(this.currentCtrl.getValue())).loadInfo(event, participant);
        this.currentCtrl.getValue().setLanguage();
    }

    public void updateLanguage(String s) {
        translator.setCurrentLanguage(s);
        currentCtrl.getValue().setLanguage();
        primaryStage.setTitle(getTitle(currentCtrl.getKey()));
        menuBarCtrl.setLanguage();
        config.setStartupLanguage(translator.getCurrentLanguage());
    }

    public void showEventOverview(Event event) {
        // Track for recent events
        recentEventTracker.registerEvent(event);
        show("EventOverview");
        ((EventOverviewCtrl)(this.currentCtrl.getValue())).loadEvent(event);
        this.currentCtrl.getValue().setLanguage();
    }

    public void showAdminLogIn() {
        eventUpdateProvider.stop();
        show("AdminLogIn");
        this.currentCtrl.getValue().setLanguage();
    }

    public void showAdminScreen() {
        eventUpdateProvider.stop();
        show("AdminScreen");
        ((AdminScreenCtrl)this.currentCtrl.getValue()).initializeScene();
        this.currentCtrl.getValue().setLanguage();
    }

    public void showTagScreen(Event event, Tag tag) {
        show("TagScreen");
        ((TagScreenCtrl)(this.currentCtrl.getValue())).loadInfo(event, tag);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public Translator getTranslator() {
        return translator;
    }

    public BorderPane getBaseScene() {
        return baseScene;
    }

    public Exchanger getExchanger() {
        return exchanger;
    }

    public void updateExchanger(String currency) {
        exchanger.setCurrentCurrency(currency);
        config.setCurrency(currency);
        currentCtrl.getValue().setLanguage();
    }

    public KeyBoardListeners getKeyBoardListeners() {
        return keyBoardListeners;
    }

}
