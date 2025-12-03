package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.ArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.effects.EffectOr;
import sk.uniba.fmph.dcs.terra_futura.effects.Exchange;
import sk.uniba.fmph.dcs.terra_futura.effects.MaterialExchange;
import sk.uniba.fmph.dcs.terra_futura.effects.MaterialsToMaterials;
import sk.uniba.fmph.dcs.terra_futura.effects.TransformationFixed;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
 * 2x: 2x yellow + red -> car || yellow + red -> car + pollution
 * 1x: money -> 2x yellow || money -> 3x yellow + pollution
 * 1x: money -> 2x red || money -> 3x red + pollution
 * 1x: money -> 2x green || money -> 3x green + pollution
 * 1x: 2x money -> whatever product + pollution
 **/


final class CardGenerator {
    private CardGenerator() {
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

    public static ScoringMethod generateScoringCard() {
        return pickRandomScoring();
    }

    public static ActivationPattern generateActivationPattern(final Grid grid) {
        Collection<GridPosition> pattern = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            pattern.add(new GridPosition(random.nextInt() % 3 - 1, random.nextInt() % 3 - 1));
        }
        return new ActivationPattern(grid, pattern);
    }

    private static List<Card> deck1() {
        List<Card> cards = new ArrayList<>();

        cards.addAll(generateArbitrary(Resource.Money, 4));
        cards.addAll(generateArbitrary(Resource.Green, 6));
        cards.addAll(generateArbitrary(Resource.Red, 6));
        cards.addAll(generateArbitrary(Resource.Yellow, 5));


        Effect effect1 = new MaterialsToMaterials(1, 1, 0);
        Effect effect2 = new MaterialsToMaterials(1, 2, 1);
        Effect effectOr = efOR(effect1, effect2);

        cards.addAll(bulkCreate(Optional.of(effectOr), Optional.empty(),  2, 2));


        return cards;
    }


    private static List<Card> deck2() {
        List<Card> cards = new ArrayList<>();

        // 2x: money -> bulb + pollution || exchange
        // 2x: money -> gear + pollution || exchange
        // 2x: money -> car + 2x pollution || exchange
        Effect effect = ex(List.of(Resource.Bulb, Resource.Car, Resource.Gear),
                List.of(Resource.Gear, Resource.Car, Resource.Gear));
        cards.addAll(bulkCreate(Optional.of(
                efOR(
                        tfx(List.of(Resource.Money), List.of(Resource.Bulb), 1),
                        effect
                )
        ), Optional.empty(), 0, 2));
        cards.addAll(bulkCreate(Optional.of(
                efOR(
                        tfx(List.of(Resource.Money), List.of(Resource.Gear), 1),
                        effect
                )
        ), Optional.empty(), 0, 2));
        cards.addAll(bulkCreate(Optional.of(
                efOR(
                        tfx(List.of(Resource.Money), List.of(Resource.Car), 2),
                        effect
                )
        ), Optional.empty(), 0, 2));

        // 1x: 2x any -> gear + pollution
        // 1x: 2x any -> bulb + pollution
        // 1x: 3x any -> car + pollution
        cards.add(new Card(Optional.of(
                mx(2, List.of(Resource.Bulb), 1)
        ), Optional.empty(), 0));
        cards.add(new Card(Optional.of(
                mx(2, List.of(Resource.Gear), 1)
        ), Optional.empty(), 0));
        cards.add(new Card(Optional.of(
                mx(3, List.of(Resource.Car), 1)
        ), Optional.empty(), 0));

        //polution collector
        cards.addAll(bulkCreate(Optional.empty(), Optional.empty(),
                3, 3));

        //4x any to material to any product
        cards.addAll(bulkCreate(Optional.of(
                mx(3, List.of(Resource.Gear, Resource.Car, Resource.Bulb), 0)
        ), Optional.empty(), 0, 3));

        //2x: 2x green -> bulb || green -> bulb + pollution
        cards.addAll(bulkCreate(Optional.of(
                efOR(
                        tfx(List.of(Resource.Green, Resource.Green),
                                List.of(Resource.Bulb), 0),
                        tfx(List.of(Resource.Green), List.of(Resource.Bulb), 1)
                )
        ), Optional.empty(), 0, 2));
        //2x: 2x red -> gear || red -> gear + pollution
        cards.addAll(bulkCreate(Optional.of(
                efOR(
                        tfx(List.of(Resource.Red, Resource.Red),
                                List.of(Resource.Gear), 0),
                        tfx(List.of(Resource.Red), List.of(Resource.Gear), 1)
                )
        ), Optional.empty(), 0, 2));


        //2x: 2x yellow + red -> car || yellow + red -> car + pollution
        cards.addAll(bulkCreate(Optional.of(
                efOR(
                        tfx(List.of(Resource.Yellow, Resource.Yellow, Resource.Red),
                                List.of(Resource.Bulb), 0),
                        tfx(List.of(Resource.Yellow, Resource.Red), List.of(Resource.Car), 1)
                )
        ), Optional.empty(), 0, 2));

        //1x money -> 2x red || money -> 3x red pollution
        cards.add(new Card(Optional.of(
                efOR(
                        tfx(List.of(Resource.Money), List.of(Resource.Red, Resource.Red), 0),
                        tfx(List.of(Resource.Money), List.of(Resource.Red,
                                Resource.Red, Resource.Red), 1)
                )), Optional.empty(), 0
        ));
        //1x money -> 2x green || money -> 3x green + pollution
        cards.add(new Card(Optional.of(
                efOR(
                        tfx(List.of(Resource.Money), List.of(Resource.Green, Resource.Green), 0),
                        tfx(List.of(Resource.Money), List.of(Resource.Green,
                                Resource.Green, Resource.Green), 1)
                )), Optional.empty(), 0
        ));
        //1x money -> 2x yellow || money -> 3x yellow + pollution
        cards.add(new Card(Optional.of(
                efOR(
                        tfx(List.of(Resource.Money), List.of(Resource.Yellow, Resource.Yellow), 0),
                        tfx(List.of(Resource.Money), List.of(Resource.Yellow,
                                Resource.Yellow, Resource.Yellow), 1)
                )), Optional.empty(), 0
        ));

        //2x money -> whatever product + pollution
        cards.add(new Card(Optional.of(
                efOR(tfx(List.of(Resource.Money, Resource.Money), List.of(Resource.Bulb), 1),
                        tfx(List.of(Resource.Money, Resource.Money), List.of(Resource.Gear), 1),
                        tfx(List.of(Resource.Money, Resource.Money), List.of(Resource.Car), 1)
                )), Optional.empty(), 0
        ));



        return cards;
    }

    //create tranformation fixed
    private static Effect tfx(final List<Resource> from, final List<Resource> to,
                              final int pollution) {
        return new TransformationFixed(from, to, pollution);
    }

    //EffectOR
    private static Effect efOR(final Effect... effects) {
        return new EffectOr(List.of(effects));

    }

    private static Effect mx(final int from, final List<Resource> to, final int pollution) {
        return new MaterialExchange(from, to, pollution);
    }

    //exchabnge
    private static Effect ex(final List<Resource> from, final List<Resource> to) {
        return new Exchange(from, to);
    }

    private static List<Card> bulkCreate(final Optional<Effect> lowerEffect, final Optional<Effect> upperEffect,
                                         final int pollutionSpaces, final int repetitions) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            cards.add(new Card(lowerEffect, upperEffect, pollutionSpaces));
        }
        return cards;

    }



    private static List<Card> generateArbitrary(final Resource resource, final int count) {
        int pollutionSpaces = 1;
        if (resource.equals(Resource.Money)) {
            pollutionSpaces = 0;
        }

        List<Card> cards = new ArrayList<>();
        ArbitraryBasic arbitraryBasic = new ArbitraryBasic(List.of(resource));
        for (int i = 0; i < count; i++) {
            cards.add(new Card(Optional.empty(), Optional.of(arbitraryBasic), pollutionSpaces));
        }
        return cards;
    }

    private static ScoringMethod pickRandomScoring() {
        ArrayList<ScoringMethod> randomScores = new ArrayList<>();
        randomScores.add(concreteScore(2, Resource.Bulb, 1, Resource.Gear, 1));
        randomScores.add(concreteScore(3, Resource.Bulb, 1, Resource.Red, 1,
                Resource.Gear, 1));
        randomScores.add(concreteScore(3, Resource.Bulb, 1, Resource.Green, 1,
                Resource.Gear, 1));

        randomScores.add(concreteScore(4, Resource.Car, 2, Resource.Money, 1));
        randomScores.add(concreteScore(4, Resource.Bulb, 2, Resource.Gear, 1));
        randomScores.add(concreteScore(4, Resource.Bulb, 1, Resource.Gear, 2));

        randomScores.add(concreteScore(4, Resource.Car, 1, Resource.Gear, 1,
                Resource.Yellow, 1));
        randomScores.add(concreteScore(4, Resource.Car, 1, Resource.Bulb, 1,
                Resource.Yellow, 1));

        randomScores.add(concreteScore(5, Resource.Car, 1, Resource.Bulb, 1));
        randomScores.add(concreteScore(5, Resource.Gear, 2, Resource.Car, 1));

        randomScores.add(concreteScore(6, Resource.Gear, 2, Resource.Bulb, 2));

        randomScores.add(concreteScore(7, Resource.Bulb, 3, Resource.Car, 1));
        randomScores.add(concreteScore(7, Resource.Gear, 3, Resource.Red, 1));
        Random random = new Random();
        return randomScores.get(random.nextInt(randomScores.size()));
    }

    private static ScoringMethod concreteScore(final int points, final Object... args) {
        List<Resource> required = new ArrayList<>();
        for (int i = 0; i < args.length; i += 2) {
            Resource resource = (Resource) args[i];
            Integer count = (Integer) args[i + 1];
            required.addAll(Collections.nCopies(count, resource));
        }
        return new ScoringMethod(required, points);
    }



}
