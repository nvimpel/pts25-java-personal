package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public class ProcessAction {
    static public boolean activateCard(GridPosition cardPosition, Grid grid,
            List<SimpleEntry<Resource, GridPosition>> inputs,
            List<Resource> outputs,
            List<GridPosition> pollution) {
        return false;
    }
}
