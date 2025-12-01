package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static sk.uniba.fmph.dcs.terra_futura.CardGenerator.pileGenerator;



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

    public Optional<Card> getCard(final int index) {
        Card card = visible.get(index);
        if (card == null) {
            return Optional.empty();
        }
        return Optional.of(card);
    }

    public Card takeCard(final int index) {
        if (getCard(index).isEmpty()) {
            throw new NoSuchElementException();
        }
        Card card = visible.remove(index);
        addCard();
        return card;

    }

    private void addCard() {
        visible.addFirst(pile.removeFirst());
        if (pile.isEmpty()) {
            Collections.shuffle(discardPile);
            pile.addAll(discardPile);
            discardPile.clear();
        }
    }

    public final void removeLastCard() {
        discardPile.add(visible.removeLast());
        addCard();
    }

    final String state() {
        return "";
    }
}
