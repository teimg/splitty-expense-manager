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

import client.dialog.Popup;
import client.scenes.*;
import client.utils.communicators.implementations.CurrencyCommunicator;
import client.utils.communicators.implementations.TagCommunicator;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private static MainCtrl mainCtrl;

    private static TagCommunicator tagCommunicator;

    private static CurrencyCommunicator curCommunicator;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            mainCtrl = INJECTOR.getInstance(MainCtrl.class);
            tagCommunicator = INJECTOR.getInstance(TagCommunicator.class);
            curCommunicator = INJECTOR.getInstance(CurrencyCommunicator.class);
            clearCacheTxt();
            HashMap<String, Object> sceneMap = new HashMap<>();
            sceneMap.put("QuoteOverview",
                    FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml"));
            sceneMap.put("AddQuote",
                    FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml"));
            sceneMap.put("AddEditExpense",
                    FXML.load(AddEditExpenseCtrl.class, "client", "scenes", "AddEditExpense.fxml"));
            sceneMap.put("Invitation",
                    FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml"));
            sceneMap.put("OpenDebts",
                    FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebtsScreen.fxml"));
            sceneMap.put("StartScreen",
                    FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml"));
            sceneMap.put("Statistics",
                    FXML.load(StatisticsScreenCtrl.class, "client", "scenes",
                            "StatisticsScreen.fxml"));
            sceneMap.put("ContactInfo",
                    FXML.load(StatisticsScreenCtrl.class, "client", "scenes", "ContactInfo.fxml"));
            sceneMap.put("EventOverview",
                    FXML.load(EventOverviewCtrl.class, "client", "scenes", "EventOverview.fxml"));
            sceneMap.put("MenuBar",
                    FXML.load(MenuBarCtrl.class, "client", "scenes", "MenuBar.fxml"));
            sceneMap.put("AdminLogIn",
                    FXML.load(AdminLogInCtrl.class, "client", "scenes", "AdminLogIn.fxml"));
            sceneMap.put("AdminScreen",
                    FXML.load(AdminScreenCtrl.class, "client", "scenes", "AdminScreen.fxml"));
            sceneMap.put("TagScreen",
                    FXML.load(TagScreenCtrl.class, "client", "scenes", "TagScreen.fxml"));

            mainCtrl.initialize(primaryStage, sceneMap);
        }
        catch (RuntimeException e) {
            new Popup(mainCtrl.getTranslator().getTranslation("Server Error/Offline"
                    + " - Cannot load Add/Edit Expense scene:") + e.getMessage(),
                    Popup.TYPE.ERROR).show();
        }

    }

    /**
     * Called by the application before it is stopped.
     */
    @Override
    public void stop() {
        clearCacheTxt();
        mainCtrl.stop();
    }

    private void clearCacheTxt() {
        System.out.println(curCommunicator.clearCache());
    }

}