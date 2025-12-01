package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

final class ProcessActionUtils {

    /** Odobranie vstupných zdrojov. */
    public static boolean removeResources(
            final Grid grid,
            final List<AbstractMap.SimpleEntry<Resource, GridPosition>> inputs
        ) {
        for (final AbstractMap.SimpleEntry<Resource, GridPosition> entry : inputs) {
            Resource resource = entry.getKey();
            GridPosition gridPosition = entry.getValue();

            Optional<Card> resourceCardOpt = grid.getCard(gridPosition);
            if (resourceCardOpt.isEmpty()) {
                return false;
            }
            Card resourceCard = resourceCardOpt.get();

            if (resourceCard.canGetResources(List.of(resource))) {
                resourceCard.getResources(List.of(resource));
            }  else {
                return false;
            }
        }
        return true;
    }

    /** Uloženie pollution. */
    public static boolean placePollution(
            final Grid grid,
            final List<GridPosition> pollutionPositions
    ) {
        for (final GridPosition position : pollutionPositions) {
            Optional<Card> pollutionCardOpt = grid.getCard(position);
            if (pollutionCardOpt.isEmpty()) {
                return false;
            }
            Card pollutionCard = pollutionCardOpt.get();
            try {
                pollutionCard.putResources(List.of(Resource.Pollution));
            } catch (RuntimeException e) {
                return false;
            }
        }
        return true;
    }
}
