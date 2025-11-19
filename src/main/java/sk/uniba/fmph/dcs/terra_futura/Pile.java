package sk.uniba.fmph.dcs.terra_futura;

import java.util.Optional;

class Pile {
    java.util.List<Card> cards;


    Card getCard(int index) { return null; }

    Optional<Card> takeCard(int index) { return Optional.empty(); }
    void removeLastCard() {}
    String state() { return ""; }
}
