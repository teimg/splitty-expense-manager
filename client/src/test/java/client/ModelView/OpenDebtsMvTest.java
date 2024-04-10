package client.ModelView;

import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import client.utils.communicators.interfaces.IEventUpdateProvider;
import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Mock
    private IEventUpdateProvider eventUpdateProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openDebtsMv = new OpenDebtsMv(emailCommunicator, expenseCommunicator,
                eventUpdateProvider);
        openDebtsMv.loadInfo(translator, mainCtrl);

        when(eventUpdateProvider.event()).thenReturn(event);
    }

    @Test
    public void testLoadInfo() {

        assertNotNull(openDebtsMv.getDebts());
        assertNotNull(openDebtsMv.getPanes());
    }

    @Test
    public void testGetPanes() {
        assertEquals(new ArrayList<>(), openDebtsMv.getPanes());
    }

    @Test
    public void testGetDebts() {
        assertEquals(new ArrayList<>(), openDebtsMv.getPanes());
    }

    @Test
    public void testCheckVisibilityTrue() {
        assertTrue(openDebtsMv.checkVisibility());
    }

}
