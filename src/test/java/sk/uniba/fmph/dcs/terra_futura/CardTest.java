package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.effects.TransformationFixed;
import sk.uniba.fmph.dcs.terra_futura.effects.Exchange;
import sk.uniba.fmph.dcs.terra_futura.effects.EffectOr;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import static org.junit.Assert.*;

public class CardTest {
    private Card cardWithUpperEffect, cardWithLowerEffect;
    private ArrayList<Resource> pollution, reds, car;

    @Before
    public void setUp() {
        cardWithUpperEffect = new Card(Optional.empty(),
                Optional.of(new ArbitraryBasic(new ArrayList<>(List.of(Resource.Red)))),
                1);

        ArrayList<Effect> lowerEffects = new ArrayList<>();
        lowerEffects.add(new TransformationFixed(
                new ArrayList<>(List.of(Resource.Red, Resource.Red)),
                new ArrayList<>(List.of(Resource.Car)), 0));
        lowerEffects.add(new Exchange(
                new ArrayList<>(List.of(Resource.Red)),
                new ArrayList<>(List.of(Resource.Green))));
        cardWithLowerEffect = new Card(Optional.of(new EffectOr(lowerEffects)), Optional.empty(), 1);

        pollution = new ArrayList<>(List.of(Resource.Pollution));
        reds = new ArrayList<>(List.of(Resource.Red, Resource.Red));
        car = new ArrayList<>(List.of(Resource.Car));
    }

    @Test
    public void testPollution() {
        assertEquals(true, cardWithUpperEffect.canPutResources(pollution));
        cardWithUpperEffect.putResources(pollution);
        assertEquals(true, cardWithUpperEffect.canGetResources(pollution));
        cardWithUpperEffect.putResources(car);
        cardWithUpperEffect.putResources(pollution);
        assertEquals(false, cardWithUpperEffect.canPutResources(pollution));
        assertEquals(false, cardWithUpperEffect.canGetResources(car));
        cardWithUpperEffect.getResources(pollution);
        assertEquals(true, cardWithUpperEffect.canPutResources(pollution));
    }

    @Test
    public void testPuttingGetting() {
        cardWithLowerEffect.putResources(reds);
        cardWithLowerEffect.putResources(car);
        assertEquals(true, cardWithLowerEffect.canGetResources(reds));
        reds.add(Resource.Car);
        assertEquals(true, cardWithLowerEffect.canGetResources(reds));
        reds.add(Resource.Car);
        assertEquals(false, cardWithLowerEffect.canGetResources(reds));
        cardWithLowerEffect.putResources(car);
        assertEquals(true, cardWithLowerEffect.canGetResources(reds));
        cardWithLowerEffect.getResources(car);
        assertEquals(false, cardWithLowerEffect.canGetResources(reds));
    }

    @Test
    public void testResourcesOnCard() {
        cardWithLowerEffect.putResources(reds);
        cardWithLowerEffect.putResources(car);
        cardWithLowerEffect.putResources(pollution);
        Map<Resource, Integer> expectedCounts = new HashMap<>();
        expectedCounts.put(Resource.Red, 2);
        expectedCounts.put(Resource.Car, 1);
        expectedCounts.put(Resource.Pollution, 1);

        Map<Resource, Integer> realCounts = new HashMap<>();
        for (Resource resource : cardWithLowerEffect.resourcesOnCard()) {
            realCounts.put(resource, realCounts.getOrDefault(resource, 0) + 1);
        }
        assertEquals(true, expectedCounts.equals(realCounts));
    }
}
