package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;


import static org.junit.Assert.*;

public class PileTests {

    private Pile pile;

    @Before
    public void setUp() {
        pile = new Pile(Deck.I);
    }

    @Test
    public void testInitialization() {
        assertEquals(4, pile.stateVisibleCount());
        assertTrue(pile.statePileSize() > 0);
    }

    @Test
    public void testGetCard() {
        assertTrue(pile.getCard(0).isPresent());
        assertTrue(pile.getCard(10).isEmpty());
    }

    @Test
    public void testTakeCard() {
        int initialPileSize = pile.statePileSize();
        Card card = pile.takeCard(0);

        assertNotNull(card);
        assertEquals(4, pile.stateVisibleCount()); // mala by byt pridana do visible
        assertEquals(initialPileSize - 1, pile.statePileSize());
    }

    @Test
    public void testRemoveLastCard() {
        int initialPileSize = pile.statePileSize();
        pile.removeLastCard();
        assertEquals(4, pile.stateVisibleCount());
        assertEquals(initialPileSize - 1, pile.statePileSize());
    }

    @Test
    public void testStateJson() {
        String stateJson = pile.state();
        JSONObject json = new JSONObject(stateJson);

        assertTrue(json.has("pile_size"));
        assertTrue(json.has("discard_size"));
        assertTrue(json.has("visible_cards"));

        JSONArray visible = json.getJSONArray("visible_cards");
        assertEquals(4, visible.length());

        for (int i = 0; i < visible.length(); i++) {
            JSONObject cardJson = visible.getJSONObject(i);
            assertTrue(cardJson.has("pollution") || cardJson.has("lowerEffect") || cardJson.has("upperEffect"));
        }
    }


}
