package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * EffectOr je composite effektov.
 * jeho check prejde ak prejde aspon na jednom z efektov
 * ma dva konstruktory, bud mu passneme efekty alebo vyvtorime prazdny
 **/

public final class EffectOr implements Effect {

    private final List<Effect> effects;

    public EffectOr() {
        effects = new ArrayList<>();
    }

    public EffectOr(final List<Effect> effects) {
        if (effects == null) {
            throw new NullPointerException("List effektov nemoze byt null");
        }
        this.effects = effects;
    }

    public void addEffect(final Effect effect) {
        if (effect == null) {
            throw new NullPointerException("Effect is null");
        }
        this.effects.add(effect);
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input == null || output == null) {
            throw new NullPointerException("Input or output is null");
        }
        for (Effect effect : this.effects) {
            if (effect.check(input, output, pollution)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasAssistance() {
        for (Effect effect : this.effects) {
            if (effect.hasAssistance()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String state() {
        JSONObject json = new JSONObject();
        JSONArray children = new JSONArray();

        for (Effect effect : this.effects) {
            children.put(new JSONObject(effect.state()));
        }

        json.put("type", "EffectOr");
        json.put("effects", children);

        return json.toString();
    }

}
