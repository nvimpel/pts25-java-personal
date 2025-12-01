package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.effects.TransformationFixed;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ProcessActionTest {
    private Grid grid;
    private Card c21;
    private Card c11;

    @Before
    public void setUp() {
        this.c21 = new Card(
                Optional.empty(),
                Optional.of(new ArbitraryBasic(List.of(Resource.Red))),
                1
        );
        this.c21.putResources(List.of(Resource.Red, Resource.Red,  Resource.Pollution));
        this.c11 = new Card(
                Optional.of(new TransformationFixed(List.of(Resource.Red), List.of(Resource.Gear), 1)),
                Optional.of(new TransformationFixed(List.of(Resource.Red, Resource.Red), List.of(Resource.Gear), 0)),
                0
        );

        this.grid = new Grid() {
            @Override
            public Optional<Card> getCard(GridPosition coordinate) {
                if (coordinate.equals(new GridPosition(2, 1))) {
                    return Optional.of(c21);
                } else if (coordinate.equals(new GridPosition(1, 1))) {
                    return Optional.of(c11);
                } else {
                    return Optional.empty();
                }
            }
        };
    }

    @Test
    public void testActivationSuccessful() {
        boolean outcomeCard21 = ProcessAction.activateCard(
                new GridPosition(2,1),
                grid,
                List.of(),
                List.of(Resource.Red),
                List.of()
        );

        boolean outcomeCard11 = ProcessAction.activateCard(
                new GridPosition(1,1),
                grid,
                List.of(new AbstractMap.SimpleEntry<>(Resource.Red, new GridPosition(2, 1))),
                List.of(Resource.Gear),
                List.of(new GridPosition(2, 1))
                );

        assertTrue(outcomeCard21);
        assertTrue(outcomeCard11);
        assertFalse(c21.canPutResources(List.of()));
        assertTrue(c21.state().contains("\"Red\":2"));
        assertTrue(c21.state().contains("\"Pollution\":2"));
        assertTrue(c11.state().contains("\"Gear\":1"));
        assertTrue(c11.state().contains("\"Pollution\":0"));
    }

    @Test
    public void testActivationFailed() {
        this.c21.putResources(List.of(Resource.Pollution));
        boolean outcomeCard21 = ProcessAction.activateCard(
                new GridPosition(2,1),
                grid,
                List.of(),
                List.of(Resource.Red),
                List.of()
        );

        assertFalse(outcomeCard21);
        assertTrue(c21.state().contains("\"Red\":2"));
        assertTrue(c21.state().contains("\"Pollution\":2"));
    }

    @Test
    public void testEffectIsNotValid() {
        this.c21.putResources(List.of(Resource.Pollution));
        boolean outcomeCard21 = ProcessAction.activateCard(
                new GridPosition(2,1),
                grid,
                List.of(),
                List.of(Resource.Yellow),
                List.of()
        );

        assertFalse(outcomeCard21);
    }
}
