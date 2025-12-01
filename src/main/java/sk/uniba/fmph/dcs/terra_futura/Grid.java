package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;


public class Grid implements InterfaceActivateGrid {
    private final ArrayList<ArrayList<Card>> grid = new ArrayList<>();
    private static final int MAXIMAL_GRID_LENGTH = 5;
    private static final int GRID_OFFSET = 2;
    private final Set<GridPosition> activable = new HashSet<>();
    private final Bounds bounds = new Bounds();

    public Grid() {


        for (int i = 0; i < MAXIMAL_GRID_LENGTH; i++) {
            grid.add(new ArrayList<>());
            for (int j = 0; j < MAXIMAL_GRID_LENGTH; j++) {
                grid.get(i).add(null);
            }
        }
        createStaringCard();
    }


    public Optional<Card> getCard(final GridPosition coordinate) {
        if (grid.get(coordinate.x() + GRID_OFFSET).get(coordinate.y() + GRID_OFFSET) == null) {
            return Optional.empty();
        } else {
            return Optional.of(grid.get(coordinate.x() + 2).get(coordinate.y() + 2));
        }
    }

    public boolean canPutCard(final GridPosition coordinate) {

        if (coordinate.x() > bounds.bottomBound + 2 || coordinate.x() < bounds.topBound - 2) {
            return false;
        }
        if (coordinate.y() > bounds.leftBound + 2 || coordinate.y() < bounds.rightBound - 2) {
            return false;
        }
        //Kontrola obsadenia pozicie
        return getCard(coordinate).isEmpty();
    }

    public void putCard(final GridPosition coordinate, final Card card) {
        if (!canPutCard(coordinate)) {
            throw new IllegalArgumentException("Cannot put a card there");
        }
        if (card == null) {
            throw new NullPointerException("Cannot put a null card");
        }


        //add to activable
        activable.add(coordinate);
        //update to new grid size
        updateBounds(coordinate);
        //add card to grid
        grid.get(coordinate.x() + GRID_OFFSET).set(coordinate.y() + GRID_OFFSET, card);
        //set activable to all card in same row and column
        setActivableNeighbours(coordinate);

    }

    private void setActivableNeighbours(final GridPosition coordinate) {
        for (int i = bounds.bottomBound; i <= bounds.topBound; i++) {
            if (getCard(coordinate).isPresent()) {
                activable.add(new GridPosition(i, coordinate.y()));
            }
        }
        for (int i = bounds.leftBound; i <= bounds.rightBound; i++) {
            if (getCard(coordinate).isPresent()) {
                activable.add(new GridPosition(coordinate.x(), i));
            }
        }
    }

    public boolean canBeActivated(final GridPosition coordinate) {
        return activable.contains(coordinate);
    }

    public void setActivated(final GridPosition coordinate) {
        activable.remove(coordinate);
    }

    @Override
    public void setActivationPattern(final List<GridPosition> pattern) {
        for (GridPosition coordinate : pattern) {
            activable.add(new GridPosition(coordinate.x(), coordinate.y()));
        }
    }

    public void endTurn() {
        activable.clear();
    }

    public String state() {
        return "";
    }

    private void updateBounds(final GridPosition coordinate) {
        if (coordinate.x() < bounds.bottomBound) {
            bounds.bottomBound = coordinate.x();
        }
        if (coordinate.x() > bounds.topBound) {
            bounds.topBound = coordinate.x();
        }
        if (coordinate.y() < bounds.leftBound) {
            bounds.leftBound = coordinate.y();
        }
        if (coordinate.y() > bounds.rightBound) {
            bounds.rightBound = coordinate.y();
        }
    }

    private void createStaringCard() {
        List<Resource> to = List.of(Resource.Money, Resource.Yellow, Resource.Red, Resource.Green);
        Effect effect = new ArbitraryBasic(to);
        Card startingCard = new Card(Optional.empty(), Optional.of(effect), 0);
        grid.get(GRID_OFFSET).set(GRID_OFFSET, startingCard);
    }

    private static final class Bounds {
        private int leftBound = 0;
        private int topBound = 0;
        private int rightBound = 0;
        private int bottomBound = 0;
    }
}
