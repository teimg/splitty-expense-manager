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

import client.language.LanguageSwitch;
import client.language.Translator;
import client.utils.ClientConfiguration;
import client.utils.SceneController;
import client.utils.SceneWrapper;
import com.google.inject.Inject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class MainCtrl {

    private Map<String, SceneWrapper> scenes;
    private Stage primaryStage;
    private Translator translator;

//    private QuoteOverviewCtrl overviewCtrl;
//    private Scene overview;
//
//    private AddQuoteCtrl addCtrl;
//    private Scene add;
//
//    private AddEditExpenseCtrl addEditExpenseCtrl;
//    private Scene addEdit;
//
//    private InvitationCtrl invitationCtrl;
//    private Scene invitation;
//
//    private OpenDebtsCtrl openDebtsCtrl;
//    private Scene openDebts;
//
//    private StartScreenCtrl startScreenCtrl;
//    private Scene start;
//
//    private StatisticsScreenCtrl statisticsScreenCtrl;
//    private Scene statistics;
//
//    private ContactInfoCtrl contactInfoCtrl;
//    private Scene contactInfo;

    private LanguageSwitch currentCtrl;

    private final ClientConfiguration config;

    private MenuBarCtrl menuBarCtrl;

    @Inject
    public MainCtrl(ClientConfiguration config) {
        this.config = config;
        if (config != null){
            this.translator = new Translator(config.getStartupLanguage());

        }



    }

    @SuppressWarnings("unchecked")
    public void initialize(Stage primaryStage, HashMap<String, Object> sceneMap) {
        this.primaryStage = primaryStage;
        this.scenes = new HashMap<>();

//        Pair<QuoteOverviewCtrl, Parent> over = (Pair<QuoteOverviewCtrl, Parent>)
//                sceneMap.get("QuoteOverviewCtrl");
//        Pair<AddQuoteCtrl, Parent> add = (Pair<AddQuoteCtrl, Parent>)
//                sceneMap.get("AddQuoteCtrl");
//        Pair<AddEditExpenseCtrl, Parent> addEdit = (Pair<AddEditExpenseCtrl, Parent>)
//                sceneMap.get("AddEditExpenseCtrl");
//        Pair<InvitationCtrl, Parent> invite = (Pair<InvitationCtrl, Parent>)
//                sceneMap.get("InvitationCtrl");
//        Pair<OpenDebtsCtrl, Parent> openDebt = (Pair<OpenDebtsCtrl, Parent>)
//                sceneMap.get("OpenDebtsCtrl");
//        Pair<StartScreenCtrl, Parent> start = (Pair<StartScreenCtrl, Parent>)
//                sceneMap.get("StartScreenCtrl");
//        Pair<StatisticsScreenCtrl, Parent> stats = (Pair<StatisticsScreenCtrl, Parent>)
//                sceneMap.get("StatisticsScreenCtrl");
//        Pair<ContactInfoCtrl, Parent>  contactInfo = (Pair<ContactInfoCtrl, Parent>)
//            sceneMap.get("ContactInfoCtrl");
        Pair<MenuBarCtrl, Parent>  menuBar = (Pair<MenuBarCtrl, Parent>)
                sceneMap.get("MenuBarCtrl");

        initScenes(sceneMap);

        this.menuBarCtrl = menuBar.getKey();
        this.menuBarCtrl.setLanguage();

        primaryStage.setWidth(config.getWindowWidth());
        primaryStage.setHeight(config.getWindowHeight());

        showStartScreen();
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void initScenes(HashMap<String, Object> sceneMap) {
        for(String x : sceneMap.keySet()){
            Pair<Object, Parent> current = (Pair<Object, Parent>) sceneMap.get(x);

            Object d = current.getKey();
            if(!(d instanceof SceneController)){
                continue;
            }

            String key = x.substring(0, x.length() - 4);
            SceneController currentSceneController = (SceneController) current.getKey();
            this.scenes.put(key, new SceneWrapper(currentSceneController, current.getValue(), new Scene(current.getValue())));


        }
    }

    /**
     * Performs final actions before stopping the application.
     */
    public void stop() {

        config.setWindowWidth(primaryStage.getWidth());
        config.setWindowHeight(primaryStage.getHeight());

        config.save();

    }

    public void show(String scene, String title){
        SceneWrapper currentSceneWrapper = this.scenes.get(scene);

        if (currentSceneWrapper == null){
            throw new IllegalArgumentException("No such scene: " + scene);
        }

        this.currentCtrl = (LanguageSwitch) currentSceneWrapper.getSceneController();
        this.currentCtrl.setLanguage();

        primaryStage.setTitle(title);
        primaryStage.setScene(currentSceneWrapper.getScene());

    }

    public void showOverview() {
        show("QuoteOverview", "Overview");
    }

    public void showAdd() {
        show("AddQuote", "Quote add");
    }

    /**
     * TODO: ADD CUSTOM METHODS SUCH AS SEEN ABOVE TO EACH OF THESE.
     * TODO: ALSO ADDING ANY NEW SCENES
     */

    public void showAddEditExpense() {
        String title = translator.getTranslation(
            "Titles.AddEditExpense");
        show("AddEditExpense", title);
    }

    public void showInvitation() {
        String title = translator.getTranslation(
                "Titles.Invitation");
        show("Invitation", title);
    }

    public void showOpenDebts() {
        String title = translator.getTranslation(
            "Titles.OpenDebts");
        show("OpenDebts", title);

    }

    public void showStartScreen() {
        String title = translator.getTranslation(
            "Titles.StartScreen");
        show("StartScreen", title);
    }

    public void showStatistics() {
        String title = translator.getTranslation(
            "Titles.Statistics");
        show("StatisticsScreen", title);

    }

    public void showContactInfo(){
        String title = translator.getTranslation(
            "Titles.ContactInfo");
        show("ContactInfo", title);
    }

    public void updateLanguage(String s) {
        translator.setCurrentLanguage(s);
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        config.setStartupLanguage(translator.getCurrentLanguage());
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTitle(String title){
        primaryStage.setTitle(title);
    }

}