package sk.uniba.fmph.dcs.terra_futura.datatypes;

import java.util.Optional;

import sk.uniba.fmph.dcs.terra_futura.ActivationPattern;
import sk.uniba.fmph.dcs.terra_futura.ScoringMethod;
import sk.uniba.fmph.dcs.terra_futura.Grid;

public class Player {
    private Optional<Integer> points;
    private Grid grid;
    private ActivationPattern activationPattern1;
    private ActivationPattern activationPattern2;
    private ScoringMethod scoringMethod1;
    private ScoringMethod scoringMethod2;

    public Player(final Grid grid, final ActivationPattern activationPattern1,
                  final ActivationPattern activationPattern2, final ScoringMethod scoringMethod1,
                  final ScoringMethod scoringMethod2) {
        this.grid = grid;
        this.activationPattern1 = activationPattern1;
        this.activationPattern2 = activationPattern2;
        this.scoringMethod1 = scoringMethod1;
        this.scoringMethod2 = scoringMethod2;
        points = Optional.empty();
    }

    public void setPoints(final int points) {
        this.points = Optional.of(points);
    }

    public Optional<Integer> getPoints() {
        return points;
    }

    public Grid getGrid() {
        return grid;
    }

    public ActivationPattern getActivationPattern1() {
        return activationPattern1;
    }

    public ActivationPattern getActivationPattern2() {
        return activationPattern2;
    }

    public ScoringMethod getScoringMethod1() {
        return scoringMethod1;
    }

    public ScoringMethod getScoringMethod2() {
        return scoringMethod2;
    }
}
