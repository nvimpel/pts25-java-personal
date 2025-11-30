package sk.uniba.fmph.dcs.terra_futura.datatypes;

import java.util.Optional;

import sk.uniba.fmph.dcs.terra_futura.ActivationPattern;
import sk.uniba.fmph.dcs.terra_futura.ScoringMethod;
import sk.uniba.fmph.dcs.terra_futura.Grid;

public class Player {
    private Optional<Integer> points;
    private Grid grid;
    private ActivationPattern activationPattern1, activationPattern2;
    private ScoringMethod scoringMethod1, scoringMethod2;

    public Player(Grid grid, ActivationPattern activationPattern1, ActivationPattern activationPatter2,
            ScoringMethod scoringMethod1, ScoringMethod scoringMethod2) {
        this.grid = grid;
        this.activationPattern1 = activationPattern1;
        this.activationPattern2 = activationPattern1;
        this.scoringMethod1 = scoringMethod1;
        this.scoringMethod2 = scoringMethod2;
        points = Optional.empty();
    }

    public void setPoints(int points) {
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
