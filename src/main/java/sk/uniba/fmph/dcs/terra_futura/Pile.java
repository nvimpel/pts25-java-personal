package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
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
        Collections.shuffle(pile);

        for  (int i = 1; i <= NUMBER_OF_VISIBLE_CARDS; i++) {
            if (pile.isEmpty()) {
                break;
            }
            visible.addFirst(pile.removeFirst());
        }
    }

    //deterministicky konstruktor
    public Pile(final List<Card> pile) {
        this.pile = new ArrayList<>(pile);

        for  (int i = 1; i <= NUMBER_OF_VISIBLE_CARDS; i++) {
            if (this.pile.isEmpty()) {
                break;
            }
            visible.addFirst(this.pile.removeFirst());
        }
    }

    public Optional<Card> getCard(final int index) {
        if (index < 0 || index >= visible.size()) {
            return Optional.empty();
        }
        return Optional.of(visible.get(index));
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

        if (pile.isEmpty()) {
            Collections.shuffle(discardPile);
            pile.addAll(discardPile);
            discardPile.clear();
        }
        if (pile.isEmpty()) {
            return;
        }
        visible.addFirst(pile.removeFirst());
    }

    public final void removeLastCard() {
        if (visible.isEmpty()) {
            return;
        }
        discardPile.add(visible.removeLast());
        addCard();
    }

    public String state() {
        JSONObject json = new JSONObject();


        json.put("pile_size", pile.size());
        json.put("discard_size", discardPile.size());


        JSONArray visibleArr = new JSONArray();
        for (Card card : visible) {
            visibleArr.put(new JSONObject(card.state()));
        }

        json.put("visible_cards", visibleArr);
        return json.toString(2);
    }
    public int stateVisibleCount() {
        return visible.size();
    }
    public int statePileSize() {
        return pile.size();
    }

}
