package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Optional;

import static sk.uniba.fmph.dcs.terra_futura.ProcessActionUtils.processCardAction;
import static sk.uniba.fmph.dcs.terra_futura.ProcessActionUtils.isEffectValid;

/**
 * Overí, či sa daná akcia (aktivácia karty) dá vykonať
 * a vykoná ju. Ak sa niečo nedá vykonať, vráti false.
 * S podporou Assistance.
 **/
public final class ProcessActionAssistance {

    private ProcessActionAssistance() {}

    public static boolean activateCard(
            final Card assistingCard,
            final GridPosition cardPosition,
            final Grid grid,
            final List<SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution
    ) {
        // Overenie, že karta existuje.
        final Optional<Card> mainCardOpt = grid.getCard(cardPosition);
        if (mainCardOpt.isEmpty()) {
            return false;
        }
        final Card mainCard = mainCardOpt.get();

        if (!mainCard.canPutResources(outputs)) {
            return false;
        }
        if (!assistingCard.canPutResources(outputs)) {
            return false;
        }

        boolean isAssistingEffectValid = isEffectValid(assistingCard, inputs, outputs, pollution);
        if (!isAssistingEffectValid) {
            return false;
        }

        return processCardAction(mainCard, cardPosition, grid, inputs, outputs, pollution);
    }
}
