package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.List;
import java.util.Optional;

public class ScoringMethod {
    private List<Resource> resources;
    private Integer pointsPerCombination;
    private Optional<Integer> calculatedTotal;

    public void selectThisMethodAndCalculate() {
    }

    public String state() {
        return "";
    }
}
