package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.List;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;

import static sk.uniba.fmph.dcs.terra_futura.ProcessActionUtils.processCardAction;
import static sk.uniba.fmph.dcs.terra_futura.ProcessActionUtils.isEffectValid;

/**
 * Overí, či sa daná akcia (aktivácia karty) dá vykonať
 * a vykoná ju. Ak sa niečo nedá vykonať, vráti false.
 * Bez podpory Assistance.
 */
public final class ProcessAction {

    private ProcessAction() {}

    public static boolean activateCard (
            final GridPosition cardPosition,
            final Grid grid,
            final List<SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution
    ) {
        // Overenie, že karta existuje.
        final Optional<Card> cardOpt = grid.getCard(cardPosition);
        if (cardOpt.isEmpty()) {
            return false;
        }
        final Card card = cardOpt.get();

        if (!card.canPutResources(outputs)) {
            return false;
        }

        boolean isEffectOk = isEffectValid(card, inputs, outputs, pollution);
        if (!isEffectOk) {
            return false;
        }

        return processCardAction(card, cardPosition, grid, inputs, outputs, pollution);
    }
}
