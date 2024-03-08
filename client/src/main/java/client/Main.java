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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private static MainCtrl mainCtrl;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");

        var addExpense = FXML.load(AddEditExpenseCtrl.class,
                "client", "scenes", "AddEditExpense.fxml");
        var invitation = FXML.load(InvitationCtrl.class,
                "client", "scenes", "Invitation.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class,
                "client", "scenes", "OpenDebtsScreen.fxml");
        var startScreen = FXML.load(StartScreenCtrl.class,
                "client", "scenes", "StartScreen.fxml");
        var statistics = FXML.load(StatisticsScreenCtrl.class,
                "client", "scenes", "StatisticsScreen.fxml");
        var contactInfo = FXML.load(StatisticsScreenCtrl.class,
            "client", "scenes", "ContactInfo.fxml");
        var menuBar = FXML.load(MenuBarCtrl.class,
                "client", "scenes", "MenuBar.fxml");
        var eventOverview = FXML.load(EventOverviewCtrl.class,
        "client", "scenes", "EventOverview.fxml");

        HashMap<String, Object> sceneMap = new HashMap<>();

        sceneMap.put("QuoteOverviewCtrl", overview);
        sceneMap.put("AddQuoteCtrl", add);

        sceneMap.put("AddEditExpenseCtrl", addExpense);
        sceneMap.put("InvitationCtrl", invitation);
        sceneMap.put("OpenDebtsCtrl", openDebts);
        sceneMap.put("StartScreenCtrl", startScreen);
        sceneMap.put("StatisticsScreenCtrl", statistics);
        sceneMap.put("ContactInfoCtrl", contactInfo);
        sceneMap.put("MenuBarCtrl", menuBar);
        sceneMap.put("EventOverviewCtrl", eventOverview);

        mainCtrl.initialize(primaryStage, sceneMap);
    }

    /**
     * Called by the application before it is stopped.
     */
    @Override
    public void stop() {

        mainCtrl.stop();

    }
}