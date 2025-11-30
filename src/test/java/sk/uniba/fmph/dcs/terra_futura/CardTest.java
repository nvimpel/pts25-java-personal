package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.effects.TransformationFixed;
import sk.uniba.fmph.dcs.terra_futura.effects.Exchange;
import sk.uniba.fmph.dcs.terra_futura.effects.EffectOr;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import static org.junit.Assert.*;

public class CardTest {
    private Card upperCard, lowerCard;
    private ArrayList<Resource> pollution, reds, car;

    @Before
    public void setUp() {
        upperCard = new Card(Optional.empty(), Optional.of(new ArbitraryBasic(new ArrayList<>(List.of(Resource.Red)))),
                1);

        ArrayList<Effect> lowerEffects = new ArrayList<>();
        lowerEffects.add(new TransformationFixed(
                new ArrayList<>(List.of(Resource.Red, Resource.Red)),
                new ArrayList<>(List.of(Resource.Car)), 0));
        lowerEffects.add(new Exchange(
                new ArrayList<>(List.of(Resource.Red)),
                new ArrayList<>(List.of(Resource.Green))));
        lowerCard = new Card(Optional.of(new EffectOr(lowerEffects)), Optional.empty(), 1);

        pollution = new ArrayList<>(List.of(Resource.Pollution));
        reds = new ArrayList<>(List.of(Resource.Red, Resource.Red));
        car = new ArrayList<>(List.of(Resource.Car));
    }

    @Test
    public void testPollution() {
        assertEquals(true, upperCard.canPutResources(pollution));
        upperCard.putResources(pollution);
        assertEquals(true, upperCard.canGetResources(pollution));
        upperCard.putResources(car);
        upperCard.putResources(pollution);
        assertEquals(false, upperCard.canPutResources(pollution));
        assertEquals(false, upperCard.canGetResources(car));
        upperCard.getResources(pollution);
        assertEquals(true, upperCard.canPutResources(pollution));
    }

    @Test
    public void testPuttingGetting() {
        lowerCard.putResources(reds);
        lowerCard.putResources(car);
        assertEquals(true, lowerCard.canGetResources(reds));
        reds.add(Resource.Car);
        assertEquals(true, lowerCard.canGetResources(reds));
        reds.add(Resource.Car);
        assertEquals(false, lowerCard.canGetResources(reds));
        lowerCard.putResources(car);
        assertEquals(true, lowerCard.canGetResources(reds));
        lowerCard.getResources(car);
        assertEquals(false, lowerCard.canGetResources(reds));
    }
}
