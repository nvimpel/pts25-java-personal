package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import sk.uniba.fmph.dcs.terra_futura.Card;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;
import sk.uniba.fmph.dcs.terra_futura.datatypes.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.datatypes.effects.TransformationFixed;
import sk.uniba.fmph.dcs.terra_futura.datatypes.effects.*;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import static org.junit.Assert.*;

public class CardTest {
    private Card upperCard;
    private Card lowerCard;

    @Before
    public void setUp() {
        upperCard = new Card(null, new ArbitraryBasic(new ArrayList<>(List.of(Resource.Red))), 1);

        ArrayList<Effect> lowerEffects = new ArrayList<>();
        lowerEffects.add(new TransformationFixed(
                new ArrayList<>(List.of(Resource.Red, Resource.Red)),
                new ArrayList<>(List.of(Resource.Car)), 0));
        lowerEffects.add(new Exchange(
                new ArrayList<>(List.of(Resource.Red)),
                new ArrayList<>(List.of(Resource.Green))));
        lowerCard = new Card(new EffectOr(lowerEffects), null, 1);
    }

    @Test
    public void testPollution() {
        ArrayList<Resource> pollution = new ArrayList<>(List.of(Resource.Pollution));
        assertEquals(true, upperCard.canPutResources(pollution));
        upperCard.putResources(pollution);
        assertEquals(true, upperCard.canGetResources(pollution));
        upperCard.putResources(pollution);
        assertEquals(false, upperCard.canPutResources(pollution));
        upperCard.getResources(pollution);
        assertEquals(true, upperCard.canPutResources(pollution));
    }
}
