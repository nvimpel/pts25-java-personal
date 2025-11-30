package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

/**
 * Implementácia efektu, ktory vie vymenit n lubovolnych materialov (green,red,yellow)
 * pretransformovat na jeden lubovolny resource z listu {@code to} ktory je dany konstruktoru
 * {@code check} skontroluje ci {@code List<Resource> input} obsahuje
 * presne n prvkov, a ci su tieto prvky resources.
 * {@code List<Resource> output} moze obsahovat iba jeden resourece a musi sa zhodovat s {@code to}
 * {@code pollution} musi sediet podla efektu
 *
 **/
public final class MaterialExchange implements Effect {
    private final int from;
    private final List<Resource> to;
    private final int pollution;

    public MaterialExchange(final int from, final List<Resource> to, final int pollution) {
        this.from = from;
        this.to = List.copyOf(to);
        this.pollution = pollution;
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input == null || output == null) {
            throw new IllegalArgumentException("Input or output is null");
        }
        if (input.size() != from) {
            return false;
        }
        if (output.size() != 1) {
            return false;
        }
        if (pollution != this.pollution) {
            return false;
        }

        if (containsNonMaterial(input)) {
            return false;
        }
        return to.contains(output.getFirst());
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
        JSONArray arrTo = new JSONArray();

        for (Resource r : to) {
            arrTo.put(r.toString());
        }

        json.put("type", "Exchange");
        json.put("fromCount", from);
        json.put("returns", arrTo);

        return json.toString();
    }
}
