package sk.uniba.fmph.dcs.terra_futura.effects;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MaterialsToMaterialsTests {

    private MaterialsToMaterials resToRes;

    @Before
    public void setUp() {
        //Dve do jedneho s jednym polution
        resToRes = new MaterialsToMaterials(2, 1, 1);
    }

    // -------------------------------------------------------------
    // Validné prípady
    // -------------------------------------------------------------
    @Test
    public void testCheckValid() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Red);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Yellow);

        assertTrue(resToRes.check(input, output, 1));
    }

    // -------------------------------------------------------------
    // Invalidný počet vstupov/vystupov
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidInputCount() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green); // iba jeden ale potrebujeme dva

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Yellow);

        assertFalse(resToRes.check(input, output, 1));
    }

    @Test
    public void testCheckInvalidOutputCount() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Red);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);
        output.add(Resource.Yellow); // v outpute su dva prvky akceptujeme iba jeden

        assertFalse(resToRes.check(input, output, 1));
    }

    // -------------------------------------------------------------
    // Invalidný pollution
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidPollution() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Red);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Yellow);

        assertFalse(resToRes.check(input, output, 0)); // expected 1
    }

    // -------------------------------------------------------------
    // Validne materialy check
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidInputResource() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Money); // Money nie je material

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Yellow);

        assertFalse(resToRes.check(input, output, 1));
    }

    @Test
    public void testCheckInvalidOutputResource() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Yellow);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Car); // Car je produkt nie material

        assertFalse(resToRes.check(input, output, 1));
    }

    // -------------------------------------------------------------
    // Nulovy pripad
    // -------------------------------------------------------------
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullInput() {
        resToRes.check(null, new ArrayList<>(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOutput() {
        resToRes.check(new ArrayList<>(), null, 1);
    }

    // -------------------------------------------------------------
    // Ostatne metody
    // -------------------------------------------------------------
    @Test
    public void testHasAssistance() {
        assertFalse(resToRes.hasAssistance());
    }

    @Test
    public void testStateContainsCorrectValues() {
        String json = resToRes.state();
        assertNotNull(json);

        assertTrue(json.contains("ResToRes"));
        assertTrue(json.contains("\"fromCount\":2"));
        assertTrue(json.contains("\"toCount\":1"));
        assertTrue(json.contains("\"pollution\":1"));
    }

    // -------------------------------------------------------------
    // Random dalsie usefull testy
    // -------------------------------------------------------------
    @Test
    public void testCheckAllMaterialsAllowedInput() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Yellow);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Red);

        assertTrue(resToRes.check(input, output, 1));
    }

    @Test
    public void testCheckMultipleInvalidMixedMaterialOutput() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Red);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Green);
        output.add(Resource.Money); //iba druhy je neplatny

        assertFalse(resToRes.check(input, output, 1));
    }
}
