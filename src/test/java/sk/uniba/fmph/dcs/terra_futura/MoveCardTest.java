package sk.uniba.fmph.dcs.terra_futura;
import org.junit.Assert;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.EffectOr;
import java.util.ArrayList;
import java.util.Optional;


public class MoveCardTest {
    private MoveCard moveCard;
    private FakePile pile;
    private FakeGrid grid;
    private GridPosition gridPosition;

    private static class FakePile extends Pile {
        boolean cardPresent = true;
        Card testCard = new Card(Optional.of(new EffectOr(new ArrayList<>())), Optional.of(new EffectOr(new ArrayList<>())), 0);
        Card takenCard = null;

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
    public void moveCard_edgeIndicesValidTest() {
        moveCard = new MoveCard();
        pile = new FakePile();
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        Assert.assertTrue(moveCard.moveCard(pile,0, gridPosition, grid));
        Assert.assertTrue(moveCard.moveCard(pile,3, gridPosition, grid));

    }

    @Test
    public void moveCard_InvalidIndexLowerBoundTest() {
        moveCard = new MoveCard();
        pile = new FakePile();
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        Assert.assertFalse(moveCard.moveCard(pile,-1, gridPosition, grid));
    }

    @Test
    public void moveCard_InvalidIndexUpperBoundTest() {
        moveCard = new MoveCard();
        pile = new FakePile();
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        Assert.assertFalse(moveCard.moveCard(pile,4, gridPosition, grid));
    }

    @Test
    public void moveCard_CardNotPresentInPileTest() {
        moveCard = new MoveCard();
        pile = new FakePile();
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        pile.cardPresent = false;
        int index = 1;
        Assert.assertFalse(moveCard.moveCard(pile, index, gridPosition, grid));
    }

    @Test
    public void moveCard_CannotPutCardOnGridTest() {
        moveCard = new MoveCard();
        pile = new FakePile();
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        int index = 1;
        grid.accept = false;
        Assert.assertFalse(moveCard.moveCard(pile, index, gridPosition, grid));
    }

    @Test
    public void moveCard_SuccessTest() {
        moveCard = new MoveCard();
        pile = new FakePile();
        grid = new FakeGrid();
        gridPosition = new GridPosition(0,0);
        int index = 2;
        Assert.assertTrue(moveCard.moveCard(pile, index, gridPosition, grid));
    }
}
