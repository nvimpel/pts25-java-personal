package sk.uniba.fmph.dcs.terra_futura.datatypes;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

public record CardSource(Deck deck, int index) {
    public Deck getDeck() {
        return deck;
    }

    public int getIndex() {
        return index;
    }
}
