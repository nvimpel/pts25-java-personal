package sk.uniba.fmph.dcs.terra_futura.datatypes.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

public final class EffectOr implements Effect {

    private final List<Effect> effects;
    public EffectOr(final List<Effect> effects) {
        this.effects = effects;
    }

    public void addEffect(final Effect effect) {
        this.effects.add(effect);
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
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
