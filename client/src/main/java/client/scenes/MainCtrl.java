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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    @SuppressWarnings("unchecked")
    public void initialize(Stage primaryStage, HashMap<String, Object> sceneMap) {
        this.primaryStage = primaryStage;

        Pair<QuoteOverviewCtrl, Parent> over = (Pair<QuoteOverviewCtrl, Parent>)
                sceneMap.get("QuoteOverviewCtrl");
        Pair<AddQuoteCtrl, Parent> add = (Pair<AddQuoteCtrl, Parent>)
                sceneMap.get("AddQuoteCtrl");
        Pair<InvitationCtrl, Parent> addEdit = (Pair<InvitationCtrl, Parent>)
                sceneMap.get("AddEditExpenseCtrl");
        Pair<InvitationCtrl, Parent> invite = (Pair<InvitationCtrl, Parent>)
                sceneMap.get("InvitationCtrl");
        Pair<OpenDebtsCtrl, Parent> openDebt = (Pair<OpenDebtsCtrl, Parent>)
                sceneMap.get("OpenDebtsCtrl");
        Pair<StartScreenCtrl, Parent> start = (Pair<StartScreenCtrl, Parent>)
                sceneMap.get("StartScreenCtrl");
        Pair<StatisticsScreenCtrl, Parent> stats = (Pair<StatisticsScreenCtrl, Parent>)
                sceneMap.get("StatisticsScreenCtrl");

        this.overviewCtrl = over.getKey();
        this.overview = new Scene(over.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        showOverview();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Add Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
}