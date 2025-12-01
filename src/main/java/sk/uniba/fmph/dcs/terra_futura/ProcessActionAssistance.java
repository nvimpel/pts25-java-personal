package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

/**
 * Overí, či sa daná akcia (aktivácia karty) dá vykonať
 * a vykoná ju. Ak sa niečo nedá vykonať, vráti false.
 * S podporou Assistance.
 **/
public final class ProcessActionAssistance {

    public static boolean activateCard(
            final Card assistingCard,
            final GridPosition cardPosition,
            final Grid grid,
            final int assistingPlayer,
            final List<SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution
    ) {
        // TO BE IMPLEMENTED
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
