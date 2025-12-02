package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

class ActivateGridFake implements InterfaceActivateGrid {
    ArrayList<GridPosition> activations;

    public ActivateGridFake() {
        this.activations = new ArrayList<>();
    }

    @Override
    public void setActivationPattern(Collection<GridPosition> pattern) {
        activations = new ArrayList<>(pattern);
    }
}

public class ActivationPatternTest {
    private ActivateGridFake grid;
    private ActivationPattern activationPattern;

    @Before
    public void setUp() {
        grid = new ActivateGridFake();
        ArrayList<GridPosition> patternEntries = new ArrayList<>();
        patternEntries.add(new GridPosition(0, 0));
        patternEntries.add(new GridPosition(0, 0));
        patternEntries.add(new GridPosition(-1, 1));
        activationPattern = new ActivationPattern(grid, patternEntries);
    }

    private void checkStateString(final String expectedList, final boolean expectedActivated) {
        System.out.println("Nas JSON:\n");
        System.out.println(activationPattern.state());
        System.out.println("Nas JSON:\n");
        JSONObject obj = new JSONObject(activationPattern.state());
        JSONArray arr = obj.getJSONArray("activations");
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject pair = arr.getJSONObject(i);
            s.append(String.format("(%s,%s)", pair.getInt("x"), pair.getInt("y")));
        }

        assertEquals(expectedList, s.toString());
    }

    @Test
    public void testDataForwarding() {
        checkStateString("(0,0)(0,0)(-1,1)", false);
        activationPattern.select();
        checkStateString("(0,0)(0,0)(-1,1)", true);
        assertEquals(3, grid.activations.size());
        assertEquals(0, grid.activations.get(0).x());
        assertEquals(0, grid.activations.get(0).y());
        assertEquals(0, grid.activations.get(1).x());
        assertEquals(0, grid.activations.get(1).y());
        assertEquals(-1, grid.activations.get(2).x());
        assertEquals(1, grid.activations.get(2).y());
    }
}
