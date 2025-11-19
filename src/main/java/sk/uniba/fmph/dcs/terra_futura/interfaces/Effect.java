package sk.uniba.fmph.dcs.terra_futura.interfaces;

import java.util.List;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

public interface Effect {
    boolean check(List<Resource> input, List<Resource> output, int pollution);

    boolean hasAssistance();

    String state();
}
