package sk.uniba.fmph.dcs.terra_futura.effects;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExchangeTests {

    private Exchange exchange;

    @Before
    public void setUp() {
        List<Resource> from = new ArrayList<>();
        from.add(Resource.Green);
        from.add(Resource.Red);

        List<Resource> to = new ArrayList<>();
        to.add(Resource.Money);
        to.add(Resource.Car);

        exchange = new Exchange(from, to);
    }

    // -------------------------------------------------------------
    // Validne pripady
    // -------------------------------------------------------------
    @Test
    public void testCheckValid() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money);

        assertTrue(exchange.check(input, output, 0));
    }

    // -------------------------------------------------------------
    // Invalidne pripady
    // -------------------------------------------------------------
    @Test
    public void testCheckInvalidInputResource() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Yellow); // nie je v from

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money);

        assertFalse(exchange.check(input, output, 0));
    }

    @Test
    public void testCheckInvalidOutputResource() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Gear); // nie je v to

        assertFalse(exchange.check(input, output, 0));
    }

    @Test
    public void testCheckMultipleInputs() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);
        input.add(Resource.Red);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money);

        assertFalse(exchange.check(input, output, 0));
    }

    @Test
    public void testCheckMultipleOutputs() {
        List<Resource> input = new ArrayList<>();
        input.add(Resource.Green);

        List<Resource> output = new ArrayList<>();
        output.add(Resource.Money);
        output.add(Resource.Car);

        assertFalse(exchange.check(input, output, 0));
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
        assertNotNull(json);

        assertTrue(json.contains("Exchange"));
        assertTrue(json.contains("accepts"));
        assertTrue(json.contains("returns"));
        assertTrue(json.contains("Green"));
        assertTrue(json.contains("Red"));
        assertTrue(json.contains("Money"));
        assertTrue(json.contains("Car"));
    }
}
