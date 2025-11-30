package sk.uniba.fmph.dcs.terra_futura;

import java.util.Optional;

class Pile {
    java.util.List<Card> cards;

    Optional<Card> getCard(int index) {
        return Optional.empty();
    }

    Card takeCard(int index) {
        return null;
    }

    void removeLastCard() {
    }

    String state() {
        return "";
    }
}
