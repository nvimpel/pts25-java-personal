package sk.uniba.fmph.dcs.terra_futura.datatypes.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

/**
 * Implementácia efektu, ktory vie vymenit 1 z roznych surovin za 1 inu.
 * {@code check} skontroluje ci {@code List<Resource> input} obsahuje
 * prave jeden prvok, a ci sa ten prvok nachadza v zozname akceptovanych surovin
 * taktiez skontroluje ci vieme vymenit {@code List<Resource> input} za {@code List<Resource> output},
 * ktory taktiez musi obsahovat iba jeden prvok
 * {@code pollution} musi byt nulova
 *
 **/
public final class Exchange implements Effect {

    private final List<Resource> from;
    private final List<Resource> to;

    public Exchange(final List<Resource> from, final List<Resource> to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input.size() != 1) {
            return false;
        }
        if (output.size() != 1) {
            return false;
        }
        if (!from.contains(input.getFirst())) {
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
        JSONArray arrFrom = new JSONArray();
        JSONArray arrTo = new JSONArray();

        for (Resource r : to) {
            arrFrom.put(r.toString());
        }

        for (Resource r : to) {
            arrTo.put(r.toString());
        }

        json.put("type", "Exchange");
        json.put("accepts", arrFrom);
        json.put("returns", arrTo);

        return json.toString();
    }

}
