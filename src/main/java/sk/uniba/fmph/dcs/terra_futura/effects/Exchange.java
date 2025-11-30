package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;


import java.util.List;

/**
 * Implementácia efektu, ktorý umožňuje vymeniť jednu zo zvolených surovín za inú.
 * {@code check} overuje, či {@code List<Resource> input} obsahuje presne jeden prvok
 * a či sa tento prvok nachádza v zozname akceptovaných surovín.
 * Zároveň kontroluje, či je možné vymeniť {@code List<Resource> input}
 * za {@code List<Resource> output}, ktorý taktiež musí obsahovať iba jeden prvok.
 * {@code pollution} musí byť nulové.
 **/
public final class Exchange implements Effect {

    private final List<Resource> from;
    private final List<Resource> to;

    public Exchange(final List<Resource> from, final List<Resource> to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and to may not be null");
        }
        this.from = List.copyOf(from);
        this.to = List.copyOf(to);
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input == null || output == null) {
            throw new IllegalArgumentException("Input and output may not be null");
        }
        if (input.size() != 1) {
            return false;
        }
        if (output.size() != 1) {
            return false;
        }
        if (!from.contains(input.getFirst())) {
            return false;
        }
        if (pollution != 0) {
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

        for (Resource r : from) {
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
