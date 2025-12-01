package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

/**
 * Implementácia efektu, ktorý dokáže vymeniť ľubovoľných n materiálov
 * (green, red, yellow) za jeden ľubovoľný resource zo zoznamu {@code to},
 * ktorý je zadaný v konštruktore.
 * {@code check} overuje, či {@code List<Resource> input} obsahuje presne n prvkov
 * a či sú všetky tieto prvky typu Resource.
 * {@code List<Resource> output} môže obsahovať iba jeden prvok a tento prvok
 * sa musí zhodovať s niektorým zo zoznamu {@code to}.
 * {@code pollution} musí zodpovedať hodnotám definovaným pre daný efekt.
 */
public final class MaterialExchange implements Effect {
    private final int from;
    private final List<Resource> to;
    private final int pollution;

    public MaterialExchange(final int from, final List<Resource> to, final int pollution) {
        if (to == null) {
            throw new IllegalArgumentException("to must not be null");
        }
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

        if (EffectUtil.containsNonMaterial(input)) {
            return false;
        }
        return to.contains(output.getFirst());
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
