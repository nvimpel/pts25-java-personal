package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.Optional;
import java.util.List;

public class Grid {
    public Optional<Card> getCard(GridPosition coordinate) {
        return Optional.empty();
    }

    public boolean canPutCard(GridPosition coordinate) {
        return false;
    }

    public void putCard(GridPosition coordinate, Card card) {
    }

    public boolean canBeActivated(GridPosition coordinate) {
        return false;
    }

    public void setActivated(GridPosition coordinate) {
    }

    public void setActivationPattern(List<GridPosition> pattern) {
    }

    public void endTurn() {
    }

    public String state() {
        return "";
    }
}
