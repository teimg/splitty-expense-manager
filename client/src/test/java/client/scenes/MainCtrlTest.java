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

import client.language.Translator;
import client.utils.ClientConfiguration;
import client.utils.RecentEventTracker;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainCtrlTest {

    @Mock
    private ClientConfiguration clientConfiguration;
    @Mock
    private Translator translator;
    @Mock
    private RecentEventTracker recentEventTracker;

    private MainCtrl mainCtrl;


    @Mock
    private Parent fakeParent;

    @Mock
    private StartScreenCtrl startScreen;
    @Mock
    private EventOverviewCtrl eventOverview;
    @Mock
    private MenuBarCtrl menuBar;

    @Mock
    private Stage primaryStage;

    @BeforeEach
    public void setup() {
        HashMap<String, Object> sceneMap = new HashMap<>(Map.of(
                "StartScreen", new Pair<>(startScreen, fakeParent),
                "EventOverview", new Pair<>(eventOverview, fakeParent),
                "MenuBar", new Pair<>(menuBar, fakeParent)
        ));

        when(clientConfiguration.getStartupLanguage()).thenReturn("english");

        mainCtrl = new MainCtrl(clientConfiguration, translator, recentEventTracker);
        mainCtrl.initialize(primaryStage, sceneMap);
    }

    @Test
    public void setupTest() {
        verify(translator).setCurrentLanguage("english");
        verify(menuBar).setLanguage();
    }

    @Test
    public void getPrimaryStage() {
        assertEquals(primaryStage, mainCtrl.getPrimaryStage());
    }

    @Test
    public void getTranslator() {
        assertEquals(translator, mainCtrl.getTranslator());
    }

    @Test
    public void recentEvents() {
        Event evA = new Event("A");
        Event evB = new Event("B");

        mainCtrl.showStartScreen();
        mainCtrl.showEventOverview(evA);
        mainCtrl.showStartScreen();
        mainCtrl.showEventOverview(evB);
        mainCtrl.showStartScreen();

        mainCtrl.stop();

        verify(recentEventTracker).registerEvent(evA);
        verify(recentEventTracker).registerEvent(evB);
        verify(recentEventTracker).persistEvents();
    }
}