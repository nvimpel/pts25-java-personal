package sk.uniba.fmph.dcs.terra_futura.effects;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransformationFixedTests {

    private TransformationFixed transformation;

    @Before
    public void setUp() {
        List<Resource> from = new ArrayList<>();
        List<Resource> to = new ArrayList<>();

        // Transformacia Green -> Bulb
        from.add(Resource.Green);
        to.add(Resource.Bulb);

        transformation = new TransformationFixed(from, to, 0);
    }

    // -------------------------------------------------------------
    // Validny pripad
    // -------------------------------------------------------------
    @Test
    public void testCheckStandard() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Bulb);

        assertTrue(transformation.check(input, output, 0));
    }

    // -------------------------------------------------------------
    // Invalidne testy
    // -------------------------------------------------------------

    @Test
    public void testCheckWrongInput() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Red); // zly vstup

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Bulb);

        assertFalse(transformation.check(input, output, 0));
    }

    @Test
    public void testCheckWrongOutput() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Car); // zly produkt

        assertFalse(transformation.check(input, output, 0));
    }

    @Test
    public void testCheckWrongPollution() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Bulb);

        assertFalse(transformation.check(input, output, 5)); // akceptuje iba polution = 0
    }

    @Test
    public void testCheckDifferentSizes() {
        List<Resource> wrongInput = new ArrayList<>();
        wrongInput.add(Resource.Green);
        wrongInput.add(Resource.Red); // polozka navyse

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Bulb);

        assertFalse(transformation.check(wrongInput, output, 0));
    }

    @Test
    public void testCheckMultisetMismatch() {
        // from = [Green]
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Green); // rozne pocty rovnakych prvkov

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Bulb);

        assertFalse(transformation.check(input, output, 0));
    }

    // -------------------------------------------------------------
    // Ostatne metody
    // -------------------------------------------------------------

    @Test
    public void testHasAssistance() {
        assertFalse(transformation.hasAssistance());
    }

    @Test
    public void testStateNotEmpty() {
        String json = transformation.state();
        assertNotNull(json);
        assertTrue(json.contains("TransformationFixed"));
        assertTrue(json.contains("Green"));
        assertTrue(json.contains("Bulb"));
    }
}
