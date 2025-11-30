package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator of piles defined like this.
 * PILE 1:
 * 4x: cards generating money
 * 6x: cards generating green
 * 6x: cards generating red
 * 5x: cards generating yellow
 * 2x: two material transforming cards
 * PILE 2:
 * 2x: money -> bulb + pollution || exchange
 * 2x: money -> gear + pollution || exchange
 * 2x: money -> car + 2x pollution || exchange
 * 1x: 2x any -> gear + pollution
 * 1x: 2x any -> bulb + pollution
 * 1x: 3x any -> car + pollution
 * 3x: poluttion collector
 * 2x: 4x any resource -> any product
 * 2x: 2x green -> bulb || green -> bulb + pollution
 * 2x: 2x red -> gear || red -> gear + pollution
 * 2x: 2x yellow + red -> car || yellow + red -> car
 * 1x: money -> 2x yellow || money -> 3x yellow + pollution
 * 1x: money -> 2x red || money -> 3x red + pollution
 * 1x: money -> 2x green || money -> 3x green + pollution
 * 1x: 2x money -> whatever product + pollution
 **/
final class PileGenerator {
    private PileGenerator() {
        throw new AssertionError("PileGenerator instances forbidden!");
    }
    public static List<Card> pileGenerator(final Deck deck) {
        if (deck == null) {
            throw new NullPointerException("Deck can't be null!");
        }
        if (deck == Deck.I) {
            return deck1();
        }

        return deck2();
    }

    private static List<Card> deck1() {
        List<Card> cards = new ArrayList<>();
        /*
        cards.addAll(generateArbitrary(Resource.Money,4));
        cards.addAll(generateArbitrary(Resource.Green,6));
        cards.addAll(generateArbitrary(Resource.Red,6));
        cards.addAll(generateArbitrary(Resource.Yellow,5));



        Effect effect1 = new TransformationFixed(List.of(Resource.Green,Resource.Yellow,Resource.Red),
                List.of(Resource.Green,Resource.Yellow,Resource.Red),0);
        Effect effect2 = new TransformationFixed(List.of(Resource.Green,Resource.Yellow,Resource.Red))
        EffectOr effectOr = new EffectOr();
        */

        return cards;
    }


    private static List<Card> deck2() {
        return new ArrayList<>();
    }
/*
    private static List<Card> generateArbitrary(Resource resource, int count){
        int pollutionSpaces = 1;
        if (resource.equals(Resource.Money)) pollutionSpaces = 0;

        List<Card> cards = new ArrayList<>();
        ArbitraryBasic arbitraryBasic = new ArbitraryBasic(List.of(resource));
        for (int i = 0; i < count; i++) {
            cards.add(new Card(Optional.empty(), Optional.of(arbitraryBasic), pollutionSpaces));
        }
        return cards;
    }
    private static List<Card> generateTransform() {
        List<Card> cards = new ArrayList<>();

    }
*/
}
