package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SelectRewardTests {

    // Minimal Effect implementation
    private static class FakeEffect implements Effect {
        @Override public boolean check(List<Resource> input, List<Resource> output, int pollution) { return false; }
        @Override public boolean hasAssistance() { return false; }
        @Override public String state() { return "fake"; }
    }

    private SelectReward selectReward;
    private Card card;

    @Before
    public void setUp() {
        selectReward = new SelectReward();

        // pollutionSpaces = 3 → card stays "clear"
        card = new Card(
                Optional.of(new FakeEffect()),
                Optional.of(new FakeEffect()),
                3
        );
    }

    @Test
    public void testCanSelectReward() {
        selectReward.setReward(1, card,
                List.of(Resource.Green, Resource.Red, Resource.Car));

        assertTrue(selectReward.canSelectReward(Resource.Green));
        assertTrue(selectReward.canSelectReward(Resource.Car));
        assertFalse(selectReward.canSelectReward(Resource.Money));
    }

    @Test
    public void testSelectRewardAllowedAddsResourceToCard() {
        selectReward.setReward(1, card, List.of(Resource.Green));

        selectReward.selectReward(Resource.Green);

        List<Resource> onCard = card.resourcesOnCard();
        assertEquals(1, onCard.size());
        assertEquals(Resource.Green, onCard.getFirst());
    }

    @Test
    public void testSelectRewardNotAllowedDoesNothing() {
        selectReward.setReward(1, card, List.of(Resource.Green));

        selectReward.selectReward(Resource.Gear); // not allowed

        assertTrue(card.resourcesOnCard().isEmpty());
    }

    @Test
    public void testStateContainsPlayerCardAndSelection() {
        selectReward.setReward(2, card, List.of(Resource.Bulb, Resource.Gear));

        String json = selectReward.state();

        assertTrue(json.contains("assistingPlayer"));
        assertTrue(json.contains("Bulb"));
        assertTrue(json.contains("Gear"));
    }
}
