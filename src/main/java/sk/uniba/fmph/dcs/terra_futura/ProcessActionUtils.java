package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

final class ProcessActionUtils {

    private ProcessActionUtils() {
    }

    /** Kontrola platnosti efektu karty.
     * @param card      karta, ktorej efekt sa kontroluje
     * @param inputs    zoznam vstupov ako párov resource + pozícia
     * @param outputs   zoznam výstupných resources
     * @param pollution zoznam pozícií znečistenia
     * @return true ak daný efekt existuje, false inak
     **/
    public static boolean isEffectValid(
            final Card card,
            final List<AbstractMap.SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution) {
        List<Resource> inputResources = inputs.stream().map(AbstractMap.SimpleEntry::getKey).toList();
        return card.checkUpper(inputResources, outputs, pollution.size())
                || card.checkLower(inputResources, outputs, pollution.size());
    }

    /** Spracovanie akcie karty.
     * @return true ak sa uspesne spracovala karta
     * @param card karta ktoru spracuvame
     * @param cardPosition pozicia danej karty
     * @param grid grid daneho hraca.
     * @param inputs vstupne {@code Resources}
     * @param outputs vystupne {@code Resources}
     * @param pollution kolko pollution sa ma generovat
     **/
    public static boolean processCardAction(
            final Card card,
            final GridPosition cardPosition,
            final Grid grid,
            final List<AbstractMap.SimpleEntry<Resource, GridPosition>> inputs,
            final List<Resource> outputs,
            final List<GridPosition> pollution) {
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

        if (grid.canBeActivated(cardPosition)) {
            grid.setActivated(cardPosition);
            return true;
        }
        return false;
    }

    /** Odobranie vstupných zdrojov.
     * @return true ak odoberie resources
     * @param inputs {@code Resources} na odobratie
     * @param grid hracov grid
     **/
    private static boolean removeResources(
            final Grid grid,
            final List<AbstractMap.SimpleEntry<Resource, GridPosition>> inputs) {
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
            } else {
                return false;
            }
        }
        return true;
    }

    /** Uloženie pollution.
     * @return true ak sa podarilo place pollution
     * @param grid hracov gird
     * @param pollutionPositions zoznam poluution a ciel destinacii
     **/
    private static boolean placePollution(
            final Grid grid,
            final List<GridPosition> pollutionPositions) {
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
