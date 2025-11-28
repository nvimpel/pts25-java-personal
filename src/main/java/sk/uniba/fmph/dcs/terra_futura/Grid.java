package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

public class Grid {
    private final ArrayList<ArrayList<Card>> grid = new ArrayList<>();
    private final static int MAXIMAL_GRID_LENGTH = 5;
    private int left_bound;
    private int top_bound;
    private int right_bound;
    private int bottom_bound;

    public Grid() {
        for (int i = 0; i < MAXIMAL_GRID_LENGTH; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < MAXIMAL_GRID_LENGTH; j++) {
                grid.get(i).add(null);
            }
        }
    }


    public Optional<Card> getCard(GridPosition coordinate) {
        if (grid.get(coordinate.x() + 2).get(coordinate.y() + 2) == null) {
            return Optional.empty();
        }
        else return Optional.of(grid.get(coordinate.x() + 2).get(coordinate.y() + 2));
    }

    public boolean canPutCard(GridPosition coordinate) {
        return false;
    }

    public void output(GridPosition coordinate, Card card) {
    }

    public boolean canReActivated(GridPosition coordinate) {
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
