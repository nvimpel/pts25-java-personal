package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static sk.uniba.fmph.dcs.terra_futura.PileGenerator.pileGenerator;



public class Pile {
    private final List<Card> pile;
    private final List<Card> visible = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();


    private static final int NUMBER_OF_VISIBLE_CARDS = 4;

    public Pile(final Deck deck) {
        if (deck == Deck.I) {
            pile = pileGenerator(Deck.I);
        } else {
            pile = pileGenerator(Deck.II);
        }

        for  (int i = 1; i <= NUMBER_OF_VISIBLE_CARDS; i++) {
            visible.addFirst(pile.removeFirst());
        }
    }

    public final Optional<Card> getCard(final int index) {
        Optional<Card> card = Optional.empty();
        if (visible.get(index) != null) {
            card = Optional.of(visible.remove(index));
            takeCard();
        }
        return card;
    }

    private void takeCard() {
        visible.addFirst(pile.removeFirst());
        if (pile.isEmpty()) {
            Collections.shuffle(discardPile);
            pile.addAll(discardPile);
            discardPile.clear();
        }
    }

    public final void removeLastCard() {
        discardPile.add(visible.removeLast());
        takeCard();
    }

    final String state() {
        return "";
    }
}
