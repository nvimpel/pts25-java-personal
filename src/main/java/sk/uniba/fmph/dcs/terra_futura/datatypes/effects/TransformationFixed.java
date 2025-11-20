package sk.uniba.fmph.dcs.terra_futura.datatypes.effects;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.Collections;
import java.util.List;

public final class TransformationFixed implements Effect {

    private final List<Resource> from;
    private final List<Resource> to;
    private final int pollution;

    public TransformationFixed(final List<Resource> from, final List<Resource> to, final int pollution) {
        this.from = from;
        this.to = to;
        this.pollution = pollution;
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        for (Resource r : input) {
            if (Collections.frequency(from, r) > Collections.frequency(input, r)) {
                return false;
            }
        }
        for (Resource r : output) {
            if (Collections.frequency(to, r) != Collections.frequency(output, r)) {
                return false;
            }
        }
        return this.pollution == pollution;


    }

    @Override
    public boolean hasAssistance() {
        return false;
    }

    @Override
    public String state() {
        return null;
    }

}
