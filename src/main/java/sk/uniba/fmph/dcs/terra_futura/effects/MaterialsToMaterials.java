package sk.uniba.fmph.dcs.terra_futura.effects;


import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

/**
 * Implementácia efektu, ktorý dokáže vymeniť n ľubovoľných materiálov (green, red, yellow) z
 * a m ľubovoľných materiálov.
 * {@code check} overuje, či {@code List<Resource> input} obsahuje presne n prvkov
 * a či sú všetky tieto prvky typu Resource.
 * {@code List<Resource> output} musi obsahovať presne m prvkov a všetky musia byť materiály.
 * {@code pollution} musí presne zodpovedať požadovanej hodnote.
 */

public final class MaterialsToMaterials implements Effect {
    private final int from;
    private final int to;
    private final int pollution;

    public MaterialsToMaterials(final int from, final int to, final int pollution) {
        this.from = from;
        this.to = to;
        this.pollution = pollution;
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input == null || output == null) {
            throw new IllegalArgumentException("Input and output may not be null.");
        }
        if (input.size() != from || output.size() != to) {
            return false;
        }
        if (this.pollution != pollution) {
            return false;
        }
        if (EffectUtil.containsNonMaterial(input)) {
            return false;
        }
        return !EffectUtil.containsNonMaterial(output);

    }

    @Override
    public boolean hasAssistance() {
        return false;
    }

    @Override
    public String state() {
        JSONObject json = new JSONObject();

        json.put("type", "ResToRes");
        json.put("fromCount", from);
        json.put("toCount", to);
        json.put("pollution", pollution);

        return json.toString();
    }
}

