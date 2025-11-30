package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MaterialExchangeTests {

    private MaterialExchange exchange;

    @Before
    public void setUp() {

        List<Resource> allowedOutputs = new ArrayList<>();
        allowedOutputs.add(Resource.Money);
        allowedOutputs.add(Resource.Car);

        exchange = new MaterialExchange(2, allowedOutputs, 1);
    }

    // -------------------------------------------------------------
    // Platny pripad
    // -------------------------------------------------------------
    @Test
    public void testCheckValid() {
        List<Resource> input = List.of(Resource.Green, Resource.Red);
        List<Resource> output = List.of(Resource.Money);

        assertTrue(exchange.check(input, output, 1));
    }

    @Test
    public void testCheckValidAlternateOutput() {
        List<Resource> input = List.of(Resource.Yellow, Resource.Green);
        List<Resource> output = List.of(Resource.Car);

        assertTrue(exchange.check(input, output, 1));
    }

    // -------------------------------------------------------------
    // Neplatny pocet vstupov a vystupov
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidInputCount() {
        List<Resource> input = List.of(Resource.Green); // iba jeden treba dva
        List<Resource> output = List.of(Resource.Money);

        assertFalse(exchange.check(input, output, 1));
    }

    @Test
    public void testCheckInvalidOutputCount() {
        List<Resource> input = List.of(Resource.Green, Resource.Red);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money);
        output.add(Resource.Car); // 2 vystupy, povoleny iba jeden

        assertFalse(exchange.check(input, output, 1));
    }

    // -------------------------------------------------------------
    // Zly pollution count
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidPollution() {
        List<Resource> input = List.of(Resource.Green, Resource.Red);
        List<Resource> output = List.of(Resource.Money);

        assertFalse(exchange.check(input, output, 0)); // ocakavame 1
    }

    // -------------------------------------------------------------
    // Neplatne resource typy
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidInputResource() {
        List<Resource> input = List.of(Resource.Green, Resource.Money); // Money nie je material
        List<Resource> output = List.of(Resource.Money);

        assertFalse(exchange.check(input, output, 1));
    }

    @Test
    public void testCheckInvalidOutputResource() {
        List<Resource> input = List.of(Resource.Green, Resource.Yellow);
        List<Resource> output = List.of(Resource.Gear); // Gear nie je medzi akceptovanymi outputmi

        assertFalse(exchange.check(input, output, 1));
    }

    // -------------------------------------------------------------
    // Testy handlovania null
    // -------------------------------------------------------------
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullInput() {
        exchange.check(null, List.of(Resource.Money), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOutput() {
        exchange.check(List.of(Resource.Green), null, 1);
    }

    // -------------------------------------------------------------
    // Ostatne metody
    // -------------------------------------------------------------
    @Test
    public void testHasAssistance() {
        assertFalse(exchange.hasAssistance());
    }

    @Test
    public void testStateContainsCorrectValues() {
        String json = exchange.state();
        JSONObject obj = new JSONObject(json);

        assertEquals("Exchange", obj.getString("type"));
        assertEquals(2, obj.getInt("fromCount"));

        JSONArray arr = obj.getJSONArray("returns");
        assertEquals(2, arr.length());
        assertTrue(arr.toList().contains("Money"));
        assertTrue(arr.toList().contains("Car"));
    }

    // -------------------------------------------------------------
    // Extra testy
    // -------------------------------------------------------------
    @Test
    public void testCheckAllMaterialsAllowed() {
        List<Resource> input = List.of(Resource.Green, Resource.Yellow);
        List<Resource> output = List.of(Resource.Car);

        assertTrue(exchange.check(input, output, 1));
    }

    @Test
    public void testCheckMultipleInvalidInputs() {
        List<Resource> input = List.of(Resource.Money, Resource.Red);
        List<Resource> output = List.of(Resource.Car);

        assertFalse(exchange.check(input, output, 1));
    }


}
