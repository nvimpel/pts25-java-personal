package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.effects.*;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

//----------------------------
//Nie je cele implementovane, generujeme aspon par kariet aby nam zbehly testy
//----------------------------
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

    private static List<Card> deck1() {
        List<Card> cards = new ArrayList<>();

        cards.addAll(generateArbitrary(Resource.Money,4));
        cards.addAll(generateArbitrary(Resource.Green,6));
        cards.addAll(generateArbitrary(Resource.Red,6));
        cards.addAll(generateArbitrary(Resource.Yellow,5));


        Effect effect1 = new MaterialsToMaterials(1,1,0);
        Effect effect2 = new MaterialsToMaterials(1,2,1);
        Effect effectOr = new EffectOr(List.of(effect1, effect2));

        Card card1 = new Card(Optional.of(effectOr),Optional.empty(), 2);
        Card card2 = new Card(Optional.of(effectOr),Optional.empty(), 2);
        cards.addAll(List.of(card1,card2));


        return cards;
    }


    private static List<Card> deck2() {
        List<Card> cards = new ArrayList<>();
        Effect effect1 = new TransformationFixed(List.of(Resource.Money),List.of(Resource.Bulb),1);
        Effect effect2 = new Exchange(List.of(Resource.Bulb, Resource.Car, Resource.Gear),
                List.of(Resource.Gear, Resource.Car, Resource.Gear));
        Effect effectOr = new EffectOr(List.of(effect1, effect2));
        cards.addAll(bulkCreate(Optional.of(effectOr),Optional.empty(),0,2));
        effect1 = new TransformationFixed(List.of(Resource.Money),List.of(Resource.Gear),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.addAll(bulkCreate(Optional.of(effectOr),Optional.empty(),0,2));
        effect1 = new TransformationFixed(List.of(Resource.Money),List.of(Resource.Car),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.addAll(bulkCreate(Optional.of(effectOr),Optional.empty(),0,2));

        effect1 = new MaterialExchange(2, List.of(Resource.Bulb),1);
        cards.add(new Card(Optional.of(effect1),Optional.empty(), 0));
        effect1 = new MaterialExchange(2, List.of(Resource.Gear),1);
        cards.add(new Card(Optional.of(effect1),Optional.empty(), 0));
        effect1 = new MaterialExchange(3, List.of(Resource.Car),1);
        cards.add(new Card(Optional.of(effect1),Optional.empty(), 0));

        cards.addAll(bulkCreate(Optional.empty(),Optional.empty(),3,3));

        effect1 = new MaterialExchange(3, List.of(Resource.Gear,Resource.Car,Resource.Bulb),0);
        cards.addAll(bulkCreate(Optional.of(effect1),Optional.empty(),0,3));

        effect1 = new TransformationFixed(List.of(Resource.Green, Resource.Green),List.of(Resource.Bulb),0);
        effect2 = new TransformationFixed(List.of(Resource.Green),List.of(Resource.Bulb),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.addAll(bulkCreate(Optional.of(effectOr),Optional.empty(),0,2));

        effect1 = new TransformationFixed(List.of(Resource.Red, Resource.Red),List.of(Resource.Gear),0);
        effect2 = new TransformationFixed(List.of(Resource.Red),List.of(Resource.Gear),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.addAll(bulkCreate(Optional.of(effectOr),Optional.empty(),0,2));

        effect1 = new TransformationFixed(List.of(Resource.Yellow, Resource.Yellow, Resource.Red),
                List.of(Resource.Bulb),0);
        effect2 = new TransformationFixed(List.of(Resource.Yellow, Resource.Red),List.of(Resource.Bulb),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.addAll(bulkCreate(Optional.of(effectOr),Optional.empty(),0,2));

        effect1 = new TransformationFixed(List.of(Resource.Money), List.of(Resource.Green, Resource.Green),0);
        effect2 = new TransformationFixed(List.of(Resource.Money),
                List.of(Resource.Green, Resource.Green,Resource.Green),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.add(new Card(Optional.of(effectOr),Optional.empty(), 0));

        effect1 = new TransformationFixed(List.of(Resource.Money), List.of(Resource.Red, Resource.Red),0);
        effect2 = new TransformationFixed(List.of(Resource.Money),
                List.of(Resource.Red, Resource.Red,Resource.Red),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.add(new Card(Optional.of(effectOr),Optional.empty(), 0));

        effect1 = new TransformationFixed(List.of(Resource.Money), List.of(Resource.Yellow, Resource.Yellow),0);
        effect2 = new TransformationFixed(List.of(Resource.Money),
                List.of(Resource.Yellow, Resource.Yellow,Resource.Yellow),1);
        effectOr = new EffectOr(List.of(effect1, effect2));
        cards.add(new Card(Optional.of(effectOr),Optional.empty(), 0));

        effect1 = new TransformationFixed(List.of(Resource.Money,Resource.Money), List.of(Resource.Bulb),1);
        effect2 = new TransformationFixed(List.of(Resource.Money,Resource.Money),List.of(Resource.Gear),1);
        Effect effect3 = new TransformationFixed(List.of(Resource.Money,Resource.Money),
                List.of(Resource.Car),1);
        effectOr = new EffectOr(List.of(effect1, effect2,effect3));
        cards.add(new Card(Optional.of(effectOr),Optional.empty(), 0));

    }

    private static List<Card> bulkCreate(Optional<Effect> lowerEffect, Optional<Effect> upperEffect,
                                         int pollutionSpaces, int repetitions) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            cards.add(new Card(lowerEffect, upperEffect, pollutionSpaces));
        }
        return cards;

    }

    public static ScoringMethod generateScoringCard(){
        List<Resource> requiredCombination =  new ArrayList<>();
        return new ScoringMethod(requiredCombination,0);
    }

    public static ActivationPattern generateActivationPattern(Grid grid) {
        Collection<GridPosition> pattern = new ArrayList<>();
        return new ActivationPattern(grid,pattern);
    }

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
    /*
    private static List<Card> generateTransform() {
        List<Card> cards = new ArrayList<>();

    }

     */

}
