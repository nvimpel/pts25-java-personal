package sk.uniba.fmph.dcs.terra_futura;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import org.json.JSONObject;

public class Card {
    private final Map<Resource, Integer> resources;
    private int pollutionSpaces;
    private Optional<Effect> upperEffect;
    private Optional<Effect> lowerEffect;

    public Card(Optional<Effect> lowerEffect, Optional<Effect> upperEffect, int pollutionSpaces) {
        if (lowerEffect == null || upperEffect == null || (Integer) pollutionSpaces == null) {
            throw new NullPointerException("Null value can't be passed to Card constructor.");
        }
        this.resources = getMapWithoutResources();
        this.upperEffect = upperEffect;
        this.lowerEffect = lowerEffect;
        this.pollutionSpaces = pollutionSpaces;
    }

    private boolean isActive() {
        return this.resources.get(Resource.Pollution) <= pollutionSpaces;
    }

    public boolean canGetResources(List<Resource> resources) {
        Map<Resource, Integer> neededCounts = getMapWithoutResources();
        for (Resource resource : resources) {
            neededCounts.put(resource, neededCounts.get(resource) + 1);
        }
        if (!isActive()) {
            if (resources.stream().distinct().count() == 1 &&
                    neededCounts.get(Resource.Pollution) <= this.resources.get(Resource.Pollution)) {
                return true;
            }
            return false;
        }
        for (Map.Entry<Resource, Integer> entry : neededCounts.entrySet()) {
            if (this.resources.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void getResources(List<Resource> resources) {
        if (canGetResources(resources)) {
            for (Resource resource : resources) {
                this.resources.put(resource, this.resources.get(resource) - 1);
            }
        }
    }

    public boolean canPutResources(List<Resource> resources) {
        return isActive();
    }

    public void putResources(List<Resource> resources) {
        if (canPutResources(resources)) {
            for (Resource resource : resources) {
                this.resources.put(resource, this.resources.get(resource) + 1);
            }
        }
    }

    public boolean check(List<Resource> input, List<Resource> output, int pollution) {
        return false;
    }

    public boolean checkLower(List<Resource> input, List<Resource> output, int pollution) {
        return false;
    }

    public boolean hasAssistance() {
        return false;
    }

    public String state() {
        JSONObject json = new JSONObject();
        json.put("resources", new JSONObject(this.resources));
        putOptionalEffectToJSON(json, "lowerEffect", lowerEffect);
        putOptionalEffectToJSON(json, "upperEffect", upperEffect);
        json.put("pollutionSpaces", pollutionSpaces);
        return json.toString();
    }

    private static void putOptionalEffectToJSON(JSONObject json, String key, Optional<Effect> effect) {
        if (effect.isPresent()) {
            json.put(key, effect.get().state());
        } else {
            json.put(key, JSONObject.NULL);
        }
    }

    private static Map<Resource, Integer> getMapWithoutResources() {
        Map<Resource, Integer> result = new HashMap<>();
        for (Resource resource : Resource.values()) {
            result.put(resource, 0);
        }
        return result;
    }

}
