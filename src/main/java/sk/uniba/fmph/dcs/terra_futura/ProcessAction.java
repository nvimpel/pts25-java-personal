package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class ProcessAction {
    public boolean activateCard(Card card, Grid grid,
            List<SimpleEntry<Resource, GridPosition>> inputs,
            List<SimpleEntry<Resource, GridPosition>> outputs,
            List<GridPosition> pollution) {
        return false;
    }
}
