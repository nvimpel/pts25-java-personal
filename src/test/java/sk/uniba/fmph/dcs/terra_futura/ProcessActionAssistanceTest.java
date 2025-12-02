package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.effects.EffectOr;
import sk.uniba.fmph.dcs.terra_futura.effects.TransformationFixed;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ProcessActionAssistanceTest {
    private Grid grid;
    private Card cA;
    private Card cB1;
    private Card cB2;

    @Before
    public void setUp() {
        EffectOr effectOr = new EffectOr();
        effectOr.addEffect(new ArbitraryBasic(List.of(Resource.Yellow)));
        effectOr.addEffect(new ArbitraryBasic(List.of(Resource.Money)));

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

        this.cA = new Card(
                Optional.of(effectWithAssistance),
                Optional.of(effectOr),
                0);
        this.cA.putResources(List.of(Resource.Red, Resource.Yellow));

        this.cB1 = new Card(
                Optional.empty(),
                Optional.of(new ArbitraryBasic(List.of(Resource.Red))),
                1);
        this.cB1.putResources(List.of(Resource.Red, Resource.Red, Resource.Pollution));
        this.cB2 = new Card(
                Optional.of(new TransformationFixed(List.of(Resource.Red, Resource.Yellow), List.of(Resource.Car), 0)),
                Optional.of(new TransformationFixed(List.of(Resource.Red), List.of(Resource.Car), 1)),
                0);

        this.grid = new Grid() {
            @Override
            public boolean canBeActivated(GridPosition coordinate) {
                return true;
            }

            @Override
            public Optional<Card> getCard(GridPosition coordinate) {
                if (coordinate.equals(new GridPosition(0, 0))) {
                    return Optional.of(cA);
                } else {
                    return Optional.empty();
                }
            }
        };
    }

    @Test
    public void assistOpponentCreateRedCube() {
        boolean outcomeCardA = ProcessActionAssistance.activateCard(
                cB1,
                new GridPosition(0, 0),
                grid,
                List.of(),
                List.of(Resource.Red),
                List.of());

        assertTrue(outcomeCardA);
        assertTrue(cA.state().contains("\"Red\":2"));
    }

    @Test
    public void assistOpponentTransformGoods() {
        boolean outcomeCardA = ProcessActionAssistance.activateCard(
                cB2,
                new GridPosition(0, 0),
                grid,
                List.of(
                        new AbstractMap.SimpleEntry<>(Resource.Red, new GridPosition(0, 0)),
                        new AbstractMap.SimpleEntry<>(Resource.Yellow, new GridPosition(0, 0))),
                List.of(Resource.Car),
                List.of());

        assertTrue(outcomeCardA);
        assertTrue(cA.state().contains("\"Car\":1"));
    }
}
