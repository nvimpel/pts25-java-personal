package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

class MoveCard {

    boolean moveCard(final Pile pile, final int cardIndex, final GridPosition coordinate, final Grid grid) {
        final int numberOfCards = 4;
        if (cardIndex < 0 || cardIndex >= numberOfCards) {
            throw new IllegalArgumentException("Card index out of range <0;3>");
        }
        if (pile.getCard(cardIndex).isEmpty()) {
            throw new IllegalStateException("There is no card at index:" + cardIndex + ".");
        }
        if (grid.canPutCard(coordinate)) {
            grid.putCard(coordinate, pile.getCard(cardIndex).get());
            return true;
        } else {
            throw new IllegalStateException("Cannot put card at " + coordinate);
        }
    }
}

