package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementácia efektu, ktory iba generuje suroviny.
 * Generovat moze iba jednu naraz, ale moze mat viacero surovin ktore vie
 * generovat
 * {@code check} skontroluje ci generovana surovina sa presne zhoduje s nejakou
 * ktoru dany efekt vie generovat.
 * Ak je v outpute viac ako jedna surovina {@return false}
 * Input a pollution musia byt prazdne respektive nulove
 *
 **/
public final class ArbitraryBasic implements Effect {
    private final List<Resource> to;

    public ArbitraryBasic(final List<Resource> to) {
        if (to == null) {
            throw new NullPointerException("List to nemoze byt null");
        }
        this.to = new ArrayList<>(to);
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (pollution != 0) {
            return false;
        }
        if (!input.isEmpty()) {
            return false;
        }
        if (output.size() != 1) {
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
        JSONArray arr = new JSONArray();

        for (Resource r : to) {
            arr.put(r.toString());
        }

        json.put("type", "ArbitraryBasic");
        json.put("generates", arr);

        return json.toString();
    }

}
