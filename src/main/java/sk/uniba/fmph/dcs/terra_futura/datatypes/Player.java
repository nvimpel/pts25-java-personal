package sk.uniba.fmph.dcs.terra_futura.datatypes;

import sk.uniba.fmph.dcs.terra_futura.ActivationPattern;
import sk.uniba.fmph.dcs.terra_futura.ScoringMethod;
import sk.uniba.fmph.dcs.terra_futura.Grid;

public record Player(Grid grid, ActivationPattern activationPattern1,
        ActivationPattern activationPattern2, ScoringMethod scoringMethod1,
        ScoringMethod scoringMethod2) {
    public Grid getGrid() {
        return grid;
    }

    public ActivationPattern getActivationPattern1() {
        return activationPattern1();
    }

    public ActivationPattern getActivationPattern2() {
        return activationPattern2();
    }

    public ScoringMethod getScoringMethod1() {
        return scoringMethod1();
    }

    public ScoringMethod getScoringMethod2() {
        return scoringMethod2();
    }
}
