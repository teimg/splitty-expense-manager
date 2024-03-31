package client.ModelView;

import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OpenDebtsMvTest {

    private OpenDebtsMv openDebtsMv;

    @Mock
    private EmailCommunicator emailCommunicator;

    @Mock
    private ExpenseCommunicator expenseCommunicator;

    @Mock
    private Event event;

    @Mock
    private Translator translator;

    @Mock
    private MainCtrl mainCtrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openDebtsMv = new OpenDebtsMv(emailCommunicator, expenseCommunicator);
    }

    @Test
    public void testLoadInfo() {
        openDebtsMv.loadInfo(event, translator, mainCtrl);
        assertNotNull(openDebtsMv.getDebts());
        assertNotNull(openDebtsMv.getPanes());
    }

    @Test
    public void testGetPanes() {
        openDebtsMv.loadInfo(event, translator, mainCtrl);
        assertEquals(new ArrayList<>(), openDebtsMv.getPanes());
    }

    @Test
    public void testGetDebts() {
        openDebtsMv.loadInfo(event, translator, mainCtrl);
        assertEquals(new ArrayList<>(), openDebtsMv.getPanes());
    }

    @Test
    public void testCheckVisibilityTrue() {
        openDebtsMv.loadInfo(event, translator, mainCtrl);
        assertTrue(openDebtsMv.checkVisibility());
    }

}
