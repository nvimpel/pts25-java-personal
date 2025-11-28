package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.*;

import static sk.uniba.fmph.dcs.terra_futura.PileGenerator.pileGenerator;



class Pile {
    private List<Card> pile;
    private List<Card> visible = new ArrayList<>();


    public Pile(Deck deck) {
        if (deck == Deck.I){
            pile = pileGenerator(Deck.I);
        }
        else{
            pile = pileGenerator(Deck.II);
        }

        for  (int i = 1; i <= 4; i++) {
            visible.addFirst(pile.removeFirst());
        }
    }

    Optional<Card> getCard(int index) {
        return Optional.of(visible.get(index));
    }

    public void takeCard(int index) {
        visible.remove(index);
        visible.addFirst(pile.removeFirst());
    }

    public void removeLastCard() {
        visible.removeLast();
        visible.addFirst(pile.removeFirst());
    }

    String state() {
        return "";
    }
}
