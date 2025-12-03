package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class Grid implements InterfaceActivateGrid {
    private final ArrayList<ArrayList<Card>> grid = new ArrayList<>();
    private static final int MAXIMAL_GRID_LENGTH = 5;
    private static final int GRID_OFFSET = 2;
    private final List<GridPosition> activable = new ArrayList<>();
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
        int gx = coordinate.x() + GRID_OFFSET;
        int gy = coordinate.y() + GRID_OFFSET;
        if (gx < 0 || gx >= MAXIMAL_GRID_LENGTH || gy < 0 || gy >= MAXIMAL_GRID_LENGTH) {
            return Optional.empty();
        }
        Card c = grid.get(gx).get(gy);
        return Optional.ofNullable(c);
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


        //pridaj do aktivovatelnych
        activable.add(coordinate);
        //update velkosti gridu
        updateBounds(coordinate);
        //pridaj kartu
        grid.get(coordinate.x() + GRID_OFFSET).set(coordinate.y() + GRID_OFFSET, card);
        //nastva aktivovatelne vsetkym susedom
        setActivableNeighbours(coordinate);

    }

    private void setActivableNeighbours(final GridPosition coordinate) {
        for (int i = bounds.bottomBound; i <= bounds.topBound; i++) {
            GridPosition position = new GridPosition(i, coordinate.y());
            if (getCard(position).isPresent()) {
                if(!activable.contains(position)) {
                    activable.add(position);
                }
            }
        }
        for (int i = bounds.leftBound; i <= bounds.rightBound; i++) {
            GridPosition position = new GridPosition(coordinate.x(), i);
            if (getCard(position).isPresent()) {
                if(!activable.contains(position)) {
                    activable.add(position);
                }
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
    public void setActivationPattern(final Collection<GridPosition> pattern) {
        int normalization_X = 0;
        int normalization_Y = 0;
        if (bounds.topBound == 2) normalization_X = -1;
        if (bounds.topBound == 0) normalization_X = 1;
        if (bounds.leftBound == -2) normalization_Y = -1;
        if (bounds.leftBound == 0) normalization_Y = 1;

        for (GridPosition coordinate : pattern) {
            activable.add(new GridPosition(coordinate.x()+normalization_X,
                    coordinate.y()+normalization_Y));
        }
    }

    public void endTurn() {
        activable.clear();
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

    public List<Card> getAllCards(){
        List<Card> cards = new ArrayList<>();
        for(List<Card> row : grid){
            for(Card card : row){
                if(card !=  null){
                    cards.add(card);
                }
            }
        }
        return cards;
    }

    public String state() {
        JSONObject json = new JSONObject();

        JSONObject b = new JSONObject();
        b.put("left", bounds.leftBound);
        b.put("right", bounds.rightBound);
        b.put("top", bounds.topBound);
        b.put("bottom", bounds.bottomBound);
        json.put("bounds", b);

        JSONArray activableArr = new JSONArray();
        for (GridPosition pos : activable) {
            JSONObject p = new JSONObject();
            p.put("x", pos.x());
            p.put("y", pos.y());
            activableArr.put(p);
        }
        json.put("activable", activableArr);

        JSONArray cardsArr = new JSONArray();
        for (int x = 0; x < MAXIMAL_GRID_LENGTH; x++) {
            for (int y = 0; y < MAXIMAL_GRID_LENGTH; y++) {
                Card card = grid.get(x).get(y);
                if (card != null) {
                    JSONObject entry = new JSONObject();
                    entry.put("x", x - GRID_OFFSET);
                    entry.put("y", y - GRID_OFFSET);
                    entry.put("card", new JSONObject(card.state()));
                    cardsArr.put(entry);
                }
            }
        }
        json.put("cards", cardsArr);

        return json.toString();
    }

}
