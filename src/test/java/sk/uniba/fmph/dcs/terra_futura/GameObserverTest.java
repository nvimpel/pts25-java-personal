package sk.uniba.fmph.dcs.terra_futura;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaObserverInterface;

import java.util.HashMap;
import java.util.Map;

public class GameObserverTest {
    private GameObserver gameObserver;

    @Before
    public void setUp() {
        gameObserver = new GameObserver();
    }

    private static class MockObserver implements TerraFuturaObserverInterface {
        String lastNotification = null;

        @Override
        public void notify(String update) {
            this.lastNotification = update;
        }
    }

    @Test
    public void addObserverAndNotifyTest() {
        MockObserver observer1 = new MockObserver();
        MockObserver observer2 = new MockObserver();

        gameObserver.addObserver(observer1, 1);
        gameObserver.addObserver(observer2, 2);

        Map<Integer, String> state = new HashMap<>();
        state.put(1, "Update 1");
        state.put(2, "Update 2");

        gameObserver.notifyAllNewState(state);

        Assert.assertEquals("Update 1", observer1.lastNotification);
        Assert.assertEquals("Update 2", observer2.lastNotification);
    }

    @Test
    public void removeObserverTest() {
        MockObserver observer = new MockObserver();
        gameObserver.addObserver(observer, 1);
        gameObserver.removeObserver(1);

        Map<Integer, String> state = new HashMap<>();
        state.put(1, "Update Should Not Be Received");

        gameObserver.notifyAllNewState(state);

        Assert.assertNull(observer.lastNotification);
    }

    @Test
    public void notifyUnknownObserverIdTest() {
        Map<Integer, String> state = new HashMap<>();
        state.put(99, "Unknown ID");

        // This should simply run without throwing a NullPointerException
        gameObserver.notifyAllNewState(state);
    }

    @Test
    public void notifySubsetOfObserversTest() {
        MockObserver observer1 = new MockObserver();
        MockObserver observer2 = new MockObserver();

        gameObserver.addObserver(observer1, 1);
        gameObserver.addObserver(observer2, 2);

        Map<Integer, String> state = new HashMap<>();
        state.put(2, "Update 2 Only");

        gameObserver.notifyAllNewState(state);

        Assert.assertNull(observer1.lastNotification);
        Assert.assertEquals("Update 2 Only", observer2.lastNotification);
    }
}
