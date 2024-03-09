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
import com.google.inject.Inject;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;

public class MainCtrl {

    private Stage primaryStage;
    private Translator translator;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private AddEditExpenseCtrl addEditExpenseCtrl;
    private Scene addEdit;

    private InvitationCtrl invitationCtrl;
    private Scene invitation;

    private OpenDebtsCtrl openDebtsCtrl;
    private Scene openDebts;

    private StartScreenCtrl startScreenCtrl;
    private Scene start;

    private StatisticsScreenCtrl statisticsScreenCtrl;
    private Scene statistics;

    private ContactInfoCtrl contactInfoCtrl;
    private Scene contactInfo;

    private LanguageSwitch currentCtrl;

    private EventOverviewCtrl eventOverviewCtrl;
    private Scene eventOverview;

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
        Pair<QuoteOverviewCtrl, Parent> over = (Pair<QuoteOverviewCtrl, Parent>)
                sceneMap.get("QuoteOverviewCtrl");
        Pair<AddQuoteCtrl, Parent> add = (Pair<AddQuoteCtrl, Parent>)
                sceneMap.get("AddQuoteCtrl");
        Pair<AddEditExpenseCtrl, Parent> addEdit = (Pair<AddEditExpenseCtrl, Parent>)
                sceneMap.get("AddEditExpenseCtrl");
        Pair<InvitationCtrl, Parent> invite = (Pair<InvitationCtrl, Parent>)
                sceneMap.get("InvitationCtrl");
        Pair<OpenDebtsCtrl, Parent> openDebt = (Pair<OpenDebtsCtrl, Parent>)
                sceneMap.get("OpenDebtsCtrl");
        Pair<StartScreenCtrl, Parent> start = (Pair<StartScreenCtrl, Parent>)
                sceneMap.get("StartScreenCtrl");
        Pair<StatisticsScreenCtrl, Parent> stats = (Pair<StatisticsScreenCtrl, Parent>)
                sceneMap.get("StatisticsScreenCtrl");
        Pair<ContactInfoCtrl, Parent>  contactInfo = (Pair<ContactInfoCtrl, Parent>)
            sceneMap.get("ContactInfoCtrl");
        Pair<MenuBarCtrl, Parent>  menuBar = (Pair<MenuBarCtrl, Parent>)
                sceneMap.get("MenuBarCtrl");
        Pair<EventOverviewCtrl, Parent> eventOverview =(Pair<EventOverviewCtrl, Parent>)
        sceneMap.get("EventOverviewCtrl");
        this.overviewCtrl = over.getKey();
        this.overview = new Scene(over.getValue());
        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());
        this.addEditExpenseCtrl = addEdit.getKey();
        this.addEdit = new Scene(addEdit.getValue());
        this.invitationCtrl = invite.getKey();
        this.invitation = new Scene(invite.getValue());
        this.openDebtsCtrl = openDebt.getKey();
        this.openDebts = new Scene(openDebt.getValue());
        this.startScreenCtrl = start.getKey();
        this.start = new Scene(start.getValue());
        this.statisticsScreenCtrl = stats.getKey();
        this.statistics = new Scene(stats.getValue());
        this.contactInfoCtrl = contactInfo.getKey();
        this.contactInfo = new Scene(contactInfo.getValue());
        this.eventOverviewCtrl = eventOverview.getKey();
        this.eventOverview = new Scene(eventOverview.getValue());
        this.menuBarCtrl = menuBar.getKey();
        this.menuBarCtrl.setLanguage();
        this.currentCtrl = startScreenCtrl;
        primaryStage.setWidth(config.getWindowWidth());
        primaryStage.setHeight(config.getWindowHeight());
        showStartScreen();
        primaryStage.show();
    }

    /**
     * Performs final actions before stopping the application.
     */
    public void stop() {

        config.setWindowWidth(primaryStage.getWidth());
        config.setWindowHeight(primaryStage.getHeight());

        config.save();

    }

    public void showOverview() {
        currentCtrl = overviewCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        currentCtrl = addCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle("Quotes: Add Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    /**
     * TODO: ADD CUSTOM METHODS SUCH AS SEEN ABOVE TO EACH OF THESE.
     * TODO: ALSO ADDING ANY NEW SCENES
     */

    public void showAddEditExpense() {
        currentCtrl = addEditExpenseCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.AddEditExpense"));
        primaryStage.setScene(addEdit);
    }

    public void showInvitation() {
        currentCtrl = invitationCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.Invitation"));
        primaryStage.setScene(invitation);
    }

    public void showOpenDebts() {
        currentCtrl = openDebtsCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.OpenDebts"));
        primaryStage.setScene(openDebts);
    }

    public void showStartScreen() {
        currentCtrl = startScreenCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.StartScreen"));
        primaryStage.setScene(start);
    }

    public void showStatistics() {
        currentCtrl = statisticsScreenCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.Statistics"));
        primaryStage.setScene(statistics);
    }

    public void showContactInfo(){
        currentCtrl = contactInfoCtrl;
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.ContactInfo"));
        primaryStage.setScene(contactInfo);
    }

    public void updateLanguage(String s) {
        translator.setCurrentLanguage(s);
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        config.setStartupLanguage(translator.getCurrentLanguage());
    }

    public void showEventOverview(Event event) {
        currentCtrl = eventOverviewCtrl;
        eventOverviewCtrl.loadEvent(event);
        currentCtrl.setLanguage(); menuBarCtrl.setLanguage();
        primaryStage.setTitle(translator.getTranslation(
                "Titles.EventOverview"));
        primaryStage.setScene(eventOverview);
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTitle(String title){
        primaryStage.setTitle(title);
    }

}