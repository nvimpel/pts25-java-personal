package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static sk.uniba.fmph.dcs.terra_futura.PileGenerator.pileGenerator;



public class Pile {
    private List<Card> pile;
    private List<Card> visible = new ArrayList<>();

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
        return Optional.of(visible.get(index));
    }

    public final void takeCard(final int index) {
        visible.remove(index);
        visible.addFirst(pile.removeFirst());
    }

    public final void removeLastCard() {
        visible.removeLast();
        visible.addFirst(pile.removeFirst());
    }

    final String state() {
        return "";
    }
}
