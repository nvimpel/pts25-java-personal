package sk.uniba.fmph.dcs.terra_futura.effects;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ArbitraryBasicTests {

    private ArbitraryBasic effect;

    @Before
    public void setUp() {
        List<Resource> to = new ArrayList<>();
        to.add(Resource.Green);
        to.add(Resource.Red);

        effect = new ArbitraryBasic(to);
    }

    // -------------------------------------------------------------
    // Validne pripady
    // -------------------------------------------------------------
    @Test
    public void testCheckStandardValid() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);

        assertTrue(effect.check(input, output, 0));
    }

    // -------------------------------------------------------------
    // Invalidne pripady
    // -------------------------------------------------------------

    @Test
    public void testCheckWrongOutputResource() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money); // to = [Green,Red]

        assertFalse(effect.check(input, output, 0));
    }

    @Test
    public void testCheckMultipleOutputsShouldFail() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);
        output.add(Resource.Red); // viac nez jeden

        assertFalse(effect.check(input, output, 0));
    }

    @Test
    public void testCheckNonEmptyInputShouldFail() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Gear); // nemoze mat nic v input

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);

        assertFalse(effect.check(input, output, 0));
    }

    @Test
    public void testCheckNonZeroPollutionShouldFail() {
        List<Resource> input = new ArrayList<>();

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);

        assertFalse(effect.check(input, output, 2)); // polution musi = 0
    }

    @Test
    public void testCheckEmptyOutputShouldFail() {
        List<Resource> input = new ArrayList<>();
        List<Resource> output = new ArrayList<>();

        assertFalse(effect.check(input, output, 0));
    }

    // -------------------------------------------------------------
    // Ostatne metody
    // -------------------------------------------------------------

    @Test
    public void testHasAssistance() {
        assertFalse(effect.hasAssistance());
    }

    @Test
    public void testStateContainsCorrectValues() {
        String json = effect.state();
        assertNotNull(json);

        assertTrue(json.contains("ArbitraryBasic"));
        assertTrue(json.contains("Green"));
        assertTrue(json.contains("Red"));
        assertTrue(json.contains("generates"));
    }
}
