package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

/**
 * Trieda sluzi na premiestnovanie kariet z Pile na Grid.
 * Ma jedinu metodu moveCard, ktora toto premiestnovanie sprostredkuva
 *
 **/
public final class MoveCard {
    private MoveCard() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Metoda zistuje ci je premiestnenie validne.
     * 
     * @param pile       musi obsahovat na indexe cardIndex nejaku kartu
     * @param cardIndex  musi byt v rozsahu <0;3>
     * @param coordinate musi byt validny na vlozenie karty do gridu, to riesi grid
     * @param grid samotny grid
     * @return true ak sa posun podaril a false ak nie alebo vinimku ak je nejaky argument null
     */
    public static boolean moveCard(final Pile pile, final int cardIndex, final GridPosition coordinate, final Grid grid) {
        if (pile == null) {
            throw new NullPointerException("Pile is null");
        }
        if (coordinate == null) {
            throw new NullPointerException("GridPosition is null");
        }
        if (grid == null) {
            throw new NullPointerException("Grid is null");
        }
        final int numberOfCards = 4;
        if (cardIndex < 0 || cardIndex > numberOfCards) {
            return false;
        }
        if (pile.getCard(cardIndex).isEmpty()) {
            return false;
        }
        if (grid.canPutCard(coordinate)) {
            grid.putCard(coordinate, pile.takeCard(cardIndex));
            return true;
        } else {
            return false;
        }
    }
}
