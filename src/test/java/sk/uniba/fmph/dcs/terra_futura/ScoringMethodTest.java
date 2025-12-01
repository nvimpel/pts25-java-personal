package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Assert;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import org.junit.Before;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScoringMethodTest {
    private final List<Resource> requiredCombination = List.of(Resource.Car, Resource.Gear, Resource.Gear);
    private final int pointsPerCombination = 5;
    private ScoringMethod scoringMethod;

    @Before
    public void setUp() {
        this.scoringMethod = new ScoringMethod(requiredCombination, pointsPerCombination);
    }

    @Test
    public void testConstructorInitializesCorrectly() {
        Assert.assertNotNull(scoringMethod);
        Assert.assertEquals("not calculated", scoringMethod.state());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsOnNullCombination() {
        new ScoringMethod(null, 10);
    }

    @Test
    public void testConstructorAllowsZeroPoints() {
        ScoringMethod method = new ScoringMethod(requiredCombination, 0);
        Assert.assertNotNull(method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsOnNegativePoints() {
        new ScoringMethod(requiredCombination, -5);
    }

    @Test
    public void testExactMatch() {
        List<Resource> playerResources = Arrays.asList(Resource.Car, Resource.Gear, Resource.Gear);

        scoringMethod.selectThisMethodAndCalculate(playerResources);

        Assert.assertEquals(pointsPerCombination, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testMultipleMatches() {
        List<Resource> playerResources = Arrays.asList(Resource.Car, Resource.Car, Resource.Gear, Resource.Gear, Resource.Gear, Resource.Gear);

        scoringMethod.selectThisMethodAndCalculate(playerResources);

        Assert.assertEquals(2 * pointsPerCombination, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testLimitingResource_CarIsLimiting() {
        List<Resource> playerResources = Arrays.asList(
                Resource.Car,
                Resource.Gear, Resource.Gear, Resource.Gear, Resource.Gear, Resource.Gear,
                Resource.Bulb
        );

        scoringMethod.selectThisMethodAndCalculate(playerResources);
        Assert.assertEquals(pointsPerCombination, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testMissingRequiredResource() {
        List<Resource> playerResources = Arrays.asList(Resource.Yellow, Resource.Yellow, Resource.Yellow);

        scoringMethod.selectThisMethodAndCalculate(playerResources);

        Assert.assertEquals(0, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testResourcesBeyondRequirement() {
        List<Resource> playerResources = Arrays.asList(Resource.Car, Resource.Gear, Resource.Gear, Resource.Money, Resource.Pollution, Resource.Yellow);

        scoringMethod.selectThisMethodAndCalculate(playerResources);

        Assert.assertEquals(pointsPerCombination, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testZeroPointsPerCombination() {
        ScoringMethod method = new ScoringMethod(requiredCombination, 0);

        List<Resource> playerResources = Arrays.asList(Resource.Car, Resource.Gear, Resource.Gear);

        method.selectThisMethodAndCalculate(playerResources);

        Assert.assertEquals(0, Integer.parseInt(method.state()));
    }

    @Test
    public void testEmptyPlayerResources() {
        List<Resource> playerResources = Collections.emptyList();

        scoringMethod.selectThisMethodAndCalculate(playerResources);

        Assert.assertEquals(0, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testNullPlayerResources() {
        scoringMethod.selectThisMethodAndCalculate(null);
        Assert.assertEquals(0, Integer.parseInt(scoringMethod.state()));
    }

    @Test
    public void testStateBeforeCalculation() {
        Assert.assertEquals("not calculated", scoringMethod.state());
    }

    @Test
    public void testStateAfterCalculation() {
        // Expected score is 5
        List<Resource> playerResources = Arrays.asList(Resource.Car, Resource.Gear, Resource.Gear);
        scoringMethod.selectThisMethodAndCalculate(playerResources);
        Assert.assertEquals("5", scoringMethod.state());
    }

}
