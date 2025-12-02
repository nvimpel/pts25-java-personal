package sk.uniba.fmph.dcs.terra_futura;
import org.junit.Assert;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.EffectOr;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.Optional;


public class MoveCardTest {
    private FakePile pile;
    private FakeGrid grid;
    private GridPosition gridPosition;

    private static class FakePile extends Pile {
        boolean cardPresent = true;
        Card testCard = new Card(Optional.of(new EffectOr(new ArrayList<>())), Optional.of(new EffectOr(new ArrayList<>())), 0);
        Card takenCard = null;

        public FakePile(Deck deck) {
            super(deck);
        }

        @Override
        public Optional<Card> getCard(int index) {
            return cardPresent ? Optional.of(testCard) : Optional.empty();
        }

        @Override
        public Card takeCard(int index) {
            takenCard = testCard;
            return testCard;
        }
    }

    private static class FakeGrid extends Grid {
        boolean accept = true;
        boolean putCalled = false;
        Card receivedCard = null;

        @Override
        public boolean canPutCard(GridPosition pos) {
            return accept;
        }

        @Override
        public void putCard(GridPosition pos, Card card) {
            putCalled = true;
            receivedCard = card;
        }
    }

    @Test
    public void moveCard_PileIsNullTest() {
        pile = null;
        grid = new FakeGrid();
        gridPosition = new GridPosition(0, 0);
        try {
            MoveCard.moveCard(pile, 1, gridPosition, grid);
        }catch (Exception e) {
            Assert.assertEquals("Pile is null", e.getMessage());
        }
    }

    @Test
    public void moveCard_GridIsNullTest() {
        pile = new FakePile(Deck.I);
        grid = null;
        gridPosition = new GridPosition(0, 0);
        try {
            MoveCard.moveCard(pile, 1, gridPosition, grid);
        }catch (Exception e) {
            Assert.assertEquals("Grid is null", e.getMessage());
        }
    }

    @Test
    public void moveCard_GridPositionIsNullTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = null;
        try {
            MoveCard.moveCard(pile, 1, gridPosition, grid);
        }catch (Exception e) {
            Assert.assertEquals("GridPosition is null", e.getMessage());
        }
    }

    @Test
    public void moveCard_edgeIndicesValidTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        Assert.assertTrue(MoveCard.moveCard(pile,0, gridPosition, grid));
        Assert.assertTrue(MoveCard.moveCard(pile,4, gridPosition, grid));

    }

    @Test
    public void moveCard_InvalidIndexLowerBoundTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        Assert.assertFalse(MoveCard.moveCard(pile,-1, gridPosition, grid));
    }

    @Test
    public void moveCard_InvalidIndexUpperBoundTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        Assert.assertFalse(MoveCard.moveCard(pile,5, gridPosition, grid));
    }

    @Test
    public void moveCard_CardNotPresentInPileTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        pile.cardPresent = false;
        int index = 1;
        Assert.assertFalse(MoveCard.moveCard(pile, index, gridPosition, grid));
    }

    @Test
    public void moveCard_CannotPutCardOnGridTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        int index = 1;
        grid.accept = false;
        Assert.assertFalse(MoveCard.moveCard(pile, index, gridPosition, grid));
    }

    @Test
    public void moveCard_SuccessTest() {
        pile = new FakePile(Deck.I);
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        int index = 2;
        Assert.assertTrue(MoveCard.moveCard(pile, index, gridPosition, grid));
    }
}
