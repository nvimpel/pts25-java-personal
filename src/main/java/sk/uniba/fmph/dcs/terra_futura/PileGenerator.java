package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator of piles defined like this.
 * PILE 1:
 * 4x cards generating money
 * 6x cards generating green
 * 6x cards generating red
 * 5x cards generating
 * 2x two material transforming cards
 * PILE 2:
 *
 **/
final class PileGenerator {
    private PileGenerator() {
        throw new AssertionError("PileGenerator instances forbidden!");
    }
    public static List<Card> pileGenerator(final Deck deck) {
        return deck1();
    }

    private static List<Card> deck1() {
        return new ArrayList<>();
    }
}
