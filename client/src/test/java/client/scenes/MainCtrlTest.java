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
import client.language.Translator;
import client.utils.ClientConfiguration;
import client.utils.RecentEventTracker;
import client.utils.scene.SceneController;
import client.utils.scene.SceneWrapper;
import client.utils.scene.SceneWrapperFactory;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MainCtrlTest {

    @Mock
    private ClientConfiguration clientConfiguration;
    @Mock
    private Translator translator;
    @Mock
    private RecentEventTracker recentEventTracker;
    @Mock
    private Exchanger exchanger;
    @Mock
    private KeyBoardListeners keyBoardListeners;

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
    private AddEditExpenseCtrl addEditExpense;
    @Mock
    private InvitationCtrl invitation;
    @Mock
    private OpenDebtsCtrl openDebts;
    @Mock
    private StatisticsScreenCtrl statisticsScreen;
    @Mock
    private ContactInfoCtrl contactInfo;
    @Mock
    private AdminLogInCtrl adminLogIn;
    @Mock
    private AdminScreenCtrl adminScreen;
    @Mock
    private TagScreenCtrl tagScreen;
    @Mock
    private BorderPane mockBaseScene;


    @Mock
    private Stage primaryStage;

    private HashMap<String, Object> fakeSceneMap() {
        List<Pair<String, Object>> pairs = List.of(
                new Pair<>("StartScreen", startScreen),
                new Pair<>("EventOverview", eventOverview),
                new Pair<>("MenuBar", menuBar),

                new Pair<>("AddEditExpense", addEditExpense),
                new Pair<>("Invitation", invitation),
                new Pair<>("OpenDebts", openDebts),
                new Pair<>("Statistics", statisticsScreen),
                new Pair<>("ContactInfo", contactInfo),
                new Pair<>("AdminLogIn", adminLogIn),
                new Pair<>("AdminScreen", adminScreen),
                new Pair<>("TagScreen", tagScreen)
        );

        HashMap<String, Object> sceneMap = new HashMap<>();

        for (Pair<String, Object> pair : pairs) {
            sceneMap.put(pair.getKey(), new Pair<>(pair.getValue(), fakeParent));
        }

        return sceneMap;
    }

    private SceneWrapper mockedSceneWrapper(SceneController sceneController,
                                            Parent parent) {
        SceneWrapper mockSceneWrapper = mock(SceneWrapper.class);
        when(mockSceneWrapper.getSceneController()).thenReturn(sceneController);
        when(mockSceneWrapper.getParent()).thenReturn(parent);

        return mockSceneWrapper;
    }

    private class DummyMainCtrl extends MainCtrl{
        public DummyMainCtrl(ClientConfiguration config, Translator translator,
                             RecentEventTracker recentEventTracker,
                             SceneWrapperFactory sceneWrapperFactory, Exchanger exchanger,
                             KeyBoardListeners keyBoardListeners) {
            super(config, translator, recentEventTracker,
                    sceneWrapperFactory, exchanger, keyBoardListeners);
        }

        @Override
        public void createBaseScene() {
            this.baseScene = mockBaseScene;
        }
    }

    @BeforeEach
    public void setup() {
        HashMap<String, Object> sceneMap = fakeSceneMap();

        when(clientConfiguration.getStartupLanguage()).thenReturn("english");
        when(clientConfiguration.getWindowHeight()).thenReturn(320.);
        when(clientConfiguration.getWindowWidth()).thenReturn(640.);

        mainCtrl = new DummyMainCtrl(clientConfiguration, translator, recentEventTracker,
                this::mockedSceneWrapper, exchanger, keyBoardListeners);

        mainCtrl.initialize(primaryStage, sceneMap);
    }

    @Test
    public void setupTest() {
        verify(translator).setCurrentLanguage("english");
        verify(menuBar).setLanguage();

        verify(primaryStage).setHeight(320.);
        verify(primaryStage).setWidth(640.);

        verify(startScreen).loadInfo();
        verify(primaryStage).show();
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

    @Test
    public void stopTest() {
        when(primaryStage.getHeight()).thenReturn(1000.);
        when(primaryStage.getWidth()).thenReturn(2000.);

        mainCtrl.stop();

        verify(clientConfiguration).setWindowHeight(1000.);
        verify(clientConfiguration).setWindowWidth(2000.);

        verify(clientConfiguration).save();

        verify(recentEventTracker).persistEvents();
    }

    @Test
    public void updateLanguage() {
        when(translator.getCurrentLanguage()).thenReturn("french");

        mainCtrl.updateLanguage("french");

        verify(translator).setCurrentLanguage("french");
        verify(clientConfiguration).setStartupLanguage("french");
    }

    @Test
    public void showAddEditExpenseA() {
        Event ev = new Event();
        mainCtrl.showAddEditExpense(ev);
        verify(addEditExpense).loadInfo(ev);
        verify(addEditExpense).setLanguage();
    }

    @Test
    public void showAddEditExpenseB() {
        Event ev = new Event();
        Expense ex = new Expense();
        mainCtrl.showAddEditExpense(ev, ex);
        verify(addEditExpense).loadInfo(ev, ex);
        verify(addEditExpense).setLanguage();
    }

    @Test
    public void showInvitation() {
        Event ev = new Event();
        mainCtrl.showInvitation(ev);
        verify(invitation).loadEvent(ev);
        verify(invitation).setLanguage();
    }

    @Test
    public void showOpenDebts() {
        Event ev = new Event();
        mainCtrl.showOpenDebts(ev);
        verify(openDebts).loadInfo(ev);
        verify(openDebts).setLanguage();
    }

    @Test
    public void showStatistics() {
        Event ev = new Event();
        mainCtrl.showStatistics(ev);
        verify(statisticsScreen).loadInfo(ev);
        verify(statisticsScreen).setLanguage();
    }

    @Test
    public void showContactInfo() {
        Event ev = new Event();
        Participant pa = new Participant();
        mainCtrl.showContactInfo(ev, pa);
        verify(contactInfo).loadInfo(ev, pa);
        verify(contactInfo).setLanguage();
    }

    @Test
    public void showAdminLogIn() {
        mainCtrl.showAdminLogIn();
        verify(adminLogIn).setLanguage();
    }

    @Test
    public void showAdminScreen() {
        mainCtrl.showAdminScreen();
        verify(adminScreen).initializeScene();
        verify(adminScreen).setLanguage();
    }

    @Test
    public void showTagScreen() {
        Event ev = new Event();
        Tag tag = new Tag();
        mainCtrl.showTagScreen(ev, tag);
        verify(tagScreen).loadInfo(ev, tag);
    }
}