package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import static org.junit.Assert.*;

public class CardTest {

    static private final class FakeEffect implements Effect {
        public boolean check(List<Resource> input, List<Resource> output, int pollution) {
            return false;
        }

        public boolean hasAssistance() {
            return false;
        }

        public String state() {
            return "FakeEffect";
        }
    }

    private Card card;
    private ArrayList<Resource> pollution, reds, car;

    @Before
    public void setUp() {
        card = new Card(Optional.empty(), Optional.of(new FakeEffect()), 1);

        pollution = new ArrayList<>(List.of(Resource.Pollution));
        reds = new ArrayList<>(List.of(Resource.Red, Resource.Red));
        car = new ArrayList<>(List.of(Resource.Car));
    }

    @Test
    public void testPutting() {
        assertEquals(true, card.canPutResources(reds));
        card.putResources(reds);
        assertEquals(true, card.canPutResources(car));
        card.putResources(pollution);
    }

    @Test
    public void testPuttingGetting() {
        assertEquals(false, card.canGetResources(car));
        card.putResources(car);
        assertEquals(true, card.canGetResources(car));
        assertEquals(false, card.canGetResources(reds));
        card.getResources(car);
        assertEquals(false, card.canGetResources(car));
        card.putResources(reds);
        assertEquals(true, card.canGetResources(reds));
        card.getResources(reds);
        assertEquals(false, card.canGetResources(reds));
    }

    @Test
    public void testPollutionHandling() {
        assertEquals(true, card.isClear());
        assertEquals(true, card.canPutResources(pollution));
        card.putResources(pollution);
        assertEquals(true, card.canGetResources(pollution));
        card.putResources(car);
        card.putResources(pollution);
        assertEquals(false, card.isClear());
        assertEquals(false, card.canPutResources(pollution));
        assertEquals(false, card.canGetResources(car));
        card.getResources(pollution);
        assertEquals(true, card.canPutResources(pollution));
        assertEquals(true, card.isClear());
    }

    @Test
    public void testState() {
        JSONObject json = new JSONObject(card.state());
        assertEquals("Optional.empty", json.get("lowerEffect"));
        assertEquals("Optional[FakeEffect]", json.get("upperEffect"));
        assertEquals(0, new JSONObject(json.get("resources").toString()).get("Red"));
        assertEquals(0, new JSONObject(json.get("resources").toString()).get("Car"));
        assertEquals(0, new JSONObject(json.get("resources").toString()).get("Pollution"));
        assertEquals(1, json.get("pollutionSpaces"));
    }
}
