package sk.uniba.fmph.dcs.terra_futura;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;
import org.json.JSONArray;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;

public final class ActivationPattern {
    private final ArrayList<GridPosition> pattern;
    private boolean selected;
    private final InterfaceActivateGrid grid;

    public ActivationPattern(final InterfaceActivateGrid grid,
            final Collection<GridPosition> pattern) {
        this.grid = grid;
        this.pattern = new ArrayList<>(pattern); // copy the pattern
        this.selected = false;
    }

    public void select() {
        if (this.selected) {
            throw new IllegalStateException("Pattern already selected");
        }
        this.grid.setActivationPattern(this.pattern);
        this.selected = false; //pattern sa da aktivovat aj dva krat preto nechceme zakazat
    }

    public boolean isSelected() {
        return this.selected;
    }

    public String state() {
        JSONArray patternList = new JSONArray();
        for (GridPosition entry : pattern) {
            JSONObject pair = new JSONObject();
            pair.put("x", entry.x());
            pair.put("y", entry.y());
            patternList.put(pair);
        }
        JSONObject result = new JSONObject();
        result.put("selected", this.selected);
        result.put("activations", patternList);
        return result.toString();
    }
}
