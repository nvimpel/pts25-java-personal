package sk.uniba.fmph.dcs.terra_futura.effects;


import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

/**
 * Implementácia efektu, ktory vie vymenit n lubovolnych materialov (green,red,yellow)
 * {@code check} skontroluje ci {@code List<Resource> input} obsahuje
 * presne n prvkov, a ci su tieto prvky resources.
 * {@code List<Resource> output} moze obsahovat iba m prvkov a musia to byt materialy
 * * {@code pollution} musi sediet presne
 *
 **/
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
        if (containsNonMaterial(input)) {
            return false;
        }
        return !containsNonMaterial(output);

    }

    private boolean containsNonMaterial(final List<Resource> list) {
        List<Resource> materials = List.of(Resource.Green, Resource.Red, Resource.Yellow);
        for (Resource resource : list) {
            if (!materials.contains(resource)) {
                return true;
            }
        }
        return false;
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

