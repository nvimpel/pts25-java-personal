package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

final class ProcessActionUtils {

    private ProcessActionUtils() {}

    /** Kontrola platnosti efektu karty. */
    public static boolean isEffectValid(
            final Card card,
            final List<AbstractMap.SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution
    ) {
        List<Resource> inputResources = inputs.stream().map(AbstractMap.SimpleEntry::getKey).toList();
        return card.checkUpper(inputResources, outputs, pollution.size())
                || card.checkLower(inputResources, outputs, pollution.size());
    }

    /** Spracovanie akcie karty. */
    public static boolean processCardAction(
            final Card card,
            final GridPosition cardPosition,
            final Grid grid,
            final List<AbstractMap.SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution
    ) {
        boolean wasRemoveValid = removeResources(grid, inputs);
        if (!wasRemoveValid) {
            return false;
        }

        try {
            card.putResources(outputs);
        } catch (RuntimeException e) {
            return false;
        }

        boolean wasPollutionValid = placePollution(grid, pollution);
        if (!wasPollutionValid) {
            return false;
        }

        grid.setActivated(cardPosition);
        return true;
    }

    /** Odobranie vstupných zdrojov. */
    private static boolean removeResources(
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
    private static boolean placePollution(
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
