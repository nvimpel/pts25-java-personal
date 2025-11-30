package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.List;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;

/**
 * Overí, či sa daná akcia (aktivácia karty) dá vykonať.
 * Ak sa niečo nedá vykonať, vráti false.
 * Bez podpory Assistance.
 **/
public final class ProcessAction {

    private ProcessAction() {}

    public static boolean activateCard(
            final GridPosition cardPosition,
            final Grid grid,
            final List<SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution
    ) {

        // Overenie, že karta existuje.
        final Optional<Card> cardOpt = grid.getCard(cardPosition);
        if (cardOpt.isEmpty()) { return false; }
        final Card card = cardOpt.get();

        if (!checkInputs(grid, inputs)) { return false; }
        if (!checkCardEffects(card, inputs, outputs, pollution.size())) { return false; }
        if (!card.canPutResources(outputs)) { return false; }
        if (!checkPollution(grid, pollution)) { return false; }

        return true;
    }

    /** Overenie vstupov. **/
    private static boolean checkInputs(
            final Grid grid,
            final List<SimpleEntry<Resource, GridPosition>> inputs
    ) {
        for (final SimpleEntry<Resource, GridPosition> input : inputs) {
            final Optional<Card> inputCardOpt = grid.getCard(input.getValue());
            if (inputCardOpt.isEmpty()) { return false; }
            final Card inputCard = inputCardOpt.get();

            if (!inputCard.canGetResources(List.of(input.getKey()))) return false;
        }
        return true;
    }

    /** Overenie horného a dolného efektu karty. **/
    private static boolean checkCardEffects(
            final Card card,
            final List<SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            int pollutionCount
    ) {
        final List<Resource> inputResources = inputs.stream()
                                                    .map(SimpleEntry::getKey)
                                                    .toList();
        return card.checkUpper(inputResources, outputs, pollutionCount)
                || card.checkLower(inputResources, outputs, pollutionCount);
    }

    /** Overenie pollution. **/
    private static boolean checkPollution(
            final Grid grid,
            final List<GridPosition> pollutionPositions
    ) {
        for (final GridPosition position : pollutionPositions) {
            final Optional<Card> pollutionCardOpt = grid.getCard(position);
            if (pollutionCardOpt.isEmpty()) { return false; }
            final Card pollutionCard = pollutionCardOpt.get();

            if (!pollutionCard.canPutResources(List.of(Resource.Pollution))) return false;
        }
        return true;
    }
}
