package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.MaterialsToMaterials;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GridTests {

    private Grid grid;

    @Before
    public void setUp() {
        grid = new Grid();
    }

    // -------------------------------------------------------------
    // Pridavanie a vyberanie kariet
    // -------------------------------------------------------------
    @Test
    public void testPutAndGetCard() {
        GridPosition pos = new GridPosition(1, 0);
        Effect effect = new MaterialsToMaterials(1,1,0);
        Card card = new Card(Optional.of(effect), Optional.empty(), 1);

        grid.putCard(pos, card);
        Optional<Card> retrieved = grid.getCard(pos);

        assertTrue(retrieved.isPresent());
        assertEquals(card, retrieved.get());
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullCardThrows() {
        GridPosition pos = new GridPosition(1, 0);
        grid.putCard(pos, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutCardCannotPutAtInvalidPosition() {
        GridPosition pos = new GridPosition(100, 100); // Mimo
        Effect effect = new MaterialsToMaterials(1,1,0);
        Card card = new Card(Optional.of(effect), Optional.empty(), 1);
        grid.putCard(pos, card);
    }

    // -------------------------------------------------------------
    // canPutCard logika
    // -------------------------------------------------------------
    @Test
    public void testCanPutCardValid() {
        GridPosition pos = new GridPosition(1, 0);
        assertTrue(grid.canPutCard(pos));
    }

    @Test
    public void testCanPutCardOccupied() {
        GridPosition pos = new GridPosition(0, 0); // startovacia karta
        assertFalse(grid.canPutCard(pos));
    }

    // -------------------------------------------------------------
    // Vieme aktivovat
    // -------------------------------------------------------------
    @Test
    public void testActivableAfterPutCard() {
        GridPosition pos = new GridPosition(1, 0);
        Effect effect = new MaterialsToMaterials(1,1,0);
        Card card = new Card(Optional.of(effect), Optional.empty(), 1);
        grid.putCard(pos, card);

        for (int i = -2; i <= 2; i++) {
            GridPosition pos2 = new GridPosition(i, 0);
            if(grid.getCard(pos2).isPresent()) {
                assertTrue(grid.canBeActivated(pos2));
            }
            pos2 = new GridPosition(0, i);
            if(grid.getCard(pos2).isPresent()) {
                assertTrue(grid.canBeActivated(pos2));
            }
        }
    }

    @Test
    public void testSetActivatedRemovesFromActivable() {
        GridPosition pos = new GridPosition(0, 0); // startovacia karta
        grid.setActivated(pos);
        assertFalse(grid.canBeActivated(pos));
    }

    @Test
    public void testSetActivationPattern() {
        List<GridPosition> pattern = List.of(
                new GridPosition(0, 1),
                new GridPosition(1, 0)
        );
        grid.setActivationPattern(pattern);

        for (GridPosition pos : pattern) {
            assertTrue(grid.canBeActivated(pos));
        }
    }

    @Test
    public void testEndTurnClearsActivable() {
        GridPosition pos = new GridPosition(0, 0); // startovacia karta
        grid.endTurn();
        assertFalse(grid.canBeActivated(pos));
    }

    // -------------------------------------------------------------
    // Skontroluj update boundov
    // -------------------------------------------------------------
    @Test(expected = IllegalArgumentException.class)
    public void testBoundsUpdateWithinAllowedRange() {
        Grid grid = new Grid();

        Effect effect = new MaterialsToMaterials(1, 1, 0);
        Card card = new Card(Optional.of(effect), Optional.empty(), 1);

        grid.putCard(new GridPosition(0, 2), card);

        grid.putCard(new GridPosition(0, -2), card); //karty su daleko od seba error
    }

    // -------------------------------------------------------------
    // Ak nie je karta vrati prazdne
    // -------------------------------------------------------------
    @Test
    public void testGetCardEmpty() {
        GridPosition pos = new GridPosition(2, 0);
        assertTrue(grid.getCard(pos).isEmpty());
    }
}
