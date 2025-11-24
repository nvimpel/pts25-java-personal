package sk.uniba.fmph.dcs.terra_futura.datatypes.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.Collections;
import java.util.List;

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
        this.from = from;
        this.to = to;
        this.pollution = pollution;
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (!compareMultisets(from, input)) {
            return false;
        }
        if (!compareMultisets(to, output)) {
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

    private boolean compareMultisets(final List<Resource> multiset1, final List<Resource> multiset2) {
        if (multiset1.size() != multiset2.size()) {
            return false;
        }

        for (Resource r1 : multiset1) {
            if (Collections.frequency(multiset1, r1) != Collections.frequency(multiset2, r1)) {
                return false;
            }
        }
        return true;
    }

}
