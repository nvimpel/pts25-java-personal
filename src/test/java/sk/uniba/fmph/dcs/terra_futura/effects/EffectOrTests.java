package sk.uniba.fmph.dcs.terra_futura.effects;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EffectOrTests {

    private EffectOr effectOr;

    @Before
    public void setUp() {
        List<Resource> to1 = new ArrayList<>();
        to1.add(Resource.Green);

        List<Resource> to2 = new ArrayList<>();
        to2.add(Resource.Red);

        ArbitraryBasic basicEffect1 = new ArbitraryBasic(to1);
        ArbitraryBasic basicEffect2 = new ArbitraryBasic(to2);

        List<Effect> effects = new ArrayList<>();
        effects.add(basicEffect1);
        effects.add(basicEffect2);

        effectOr = new EffectOr(effects);
    }

    // -------------------------------------------------------------
    // check metoda
    // -------------------------------------------------------------
    @Test
    public void testCheckMatchesFirstEffect() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);

        assertTrue(effectOr.check(input, output, 0));
    }

    @Test
    public void testCheckMatchesSecondEffect() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Red);

        assertTrue(effectOr.check(input, output, 0));
    }

    @Test
    public void testCheckMatchesNone() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money);

        assertFalse(effectOr.check(input, output, 0));
    }

    // -------------------------------------------------------------
    // hasAssistance testy
    // -------------------------------------------------------------
    @Test
    public void testHasAssistanceFalse() {
        assertFalse(effectOr.hasAssistance());
    }

    @Test
    public void testHasAssistanceTrue() {
        Effect effectWithAssistance = new Effect() {
            @Override
            public boolean check(List<Resource> input, List<Resource> output, int pollution) {
                return false;
            }

            @Override
            public boolean hasAssistance() {
                return true;
            }

            @Override
            public String state() {
                return "{}";
            }
        };

        effectOr.addEffect(effectWithAssistance);

        assertTrue(effectOr.hasAssistance());
    }

    // -------------------------------------------------------------
    // state metoda
    // -------------------------------------------------------------
    @Test
    public void testStateContainsChildren() {
        String json = effectOr.state();
        assertNotNull(json);
        assertTrue(json.contains("EffectOr"));
        assertTrue(json.contains("effects"));
        assertTrue(json.contains("Green"));
        assertTrue(json.contains("Red"));
    }

    // -------------------------------------------------------------
    // addEffect testy
    // -------------------------------------------------------------
    @Test
    public void testAddEffect() {
        ArbitraryBasic newEffect = new ArbitraryBasic(List.of(Resource.Yellow));
        effectOr.addEffect(newEffect);

        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Yellow);

        assertTrue(effectOr.check(input, output, 0));
    }
}
