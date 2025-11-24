package sk.uniba.fmph.dcs.terra_futura.datatypes.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementácia efektu, ktory transformuje suroviny.
 * Transformovat moze z lubovolnych surovin na lubovolne
 * {@code pollution} hovori aku pollution dana transformacia generuje
 * {@code check} skontroluje ci {@code List<Resource> input} je identicky
 * ako ten s ktorym vie dana transformacia pracovat.
 * Taktiez skontroluje ci pollution na vstupe funkcie sa rovna poluttionu,
 * ktory tato transformacia generuje
 *
 **/
public final class TransformationFixed implements Effect {

    private final List<Resource> from;
    private final List<Resource> to;
    private final int pollution;

    public TransformationFixed(final List<Resource> from, final List<Resource> to, final int pollution) {
        if (from == null || to == null) {
            throw new NullPointerException("From or to is null");
        }

        this.from = from;
        this.to = to;
        this.pollution = pollution;
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input == null || output == null) {
            throw new NullPointerException("Input or output is null");
        }
        if (multisetsAreNotEqual(from, input)) {
            return false;
        }
        if (multisetsAreNotEqual(to, output)) {
            return false;
        }
        return this.pollution == pollution;

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

        json.put("type", "TransformationFixed");
        json.put("transformsFrom", arrFrom);
        json.put("transformsTo", arrTo);
        json.put("pollution", pollution);

        return json.toString();
    }

    private boolean multisetsAreNotEqual(final List<Resource> multiset1, final List<Resource> multiset2) {
        if (multiset1.size() != multiset2.size()) {
            return true;
        }

        Map<Resource, Integer> counts1 = new HashMap<>();
        Map<Resource, Integer> counts2 = new HashMap<>();

        for (Resource r : multiset1) {
            counts1.put(r, counts1.getOrDefault(r, 0) + 1);
        }
        for (Resource r : multiset2) {
            counts2.put(r, counts2.getOrDefault(r, 0) + 1);
        }

        return !counts1.equals(counts2);
    }

}
