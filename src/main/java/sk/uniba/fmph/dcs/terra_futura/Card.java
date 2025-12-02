package sk.uniba.fmph.dcs.terra_futura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import org.json.JSONObject;

/**
 * Reprezentacia karty. Kazda karta ma nejake efekty (spodny, horny, alebo oba)
 * a volne miesta pre pollutions.
 * Na karte (ak je na gride nejakeho hraca) skladujeme nejake resources. Tato
 * trieda poskytuje metody pre pridavanie/odoberanie/vyrabanie resourcov z karty
 * podla pravidiel hry.
 *
 **/
public class Card {
    private final Map<Resource, Integer> resources;
    private final int pollutionSpaces;
    private final Optional<Effect> upperEffect;
    private final Optional<Effect> lowerEffect;

    public Card(final Optional<Effect> lowerEffect, final Optional<Effect> upperEffect, final int pollutionSpaces) {
        if (lowerEffect == null || upperEffect == null) {
            throw new NullPointerException(
                    "Null value passed as Card effect. Use Optional.empty() if card has no lower/upper effect.");
        }
        this.resources = getMapWithoutResources();
        this.upperEffect = upperEffect;
        this.lowerEffect = lowerEffect;
        this.pollutionSpaces = pollutionSpaces;
    }

    /**
     * Zisti ci mozeme zobrat dane resources z kraty (musi ich obsahovat a zaroven
     * nemoze byt pollutnuta).
     * Ak je pollutnuta tak vieme zobrat iba pollution.
     **/
    public boolean canGetResources(final List<Resource> resources) {
        Map<Resource, Integer> neededCounts = getMapWithoutResources();
        for (Resource resource : resources) {
            neededCounts.put(resource, neededCounts.get(resource) + 1);
        }
        if (!isClear()) {
            if (resources.stream().distinct().count() == 1 && resources.contains(Resource.Pollution) &&
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

    /**
     * Odoberie dane resources z karty ak je to mozne.
     **/
    public void getResources(final List<Resource> resources) throws IllegalArgumentException {
        if (canGetResources(resources)) {
            for (Resource resource : resources) {
                this.resources.put(resource, this.resources.get(resource) - 1);
            }
        } else {
            throw new IllegalArgumentException("Can't get given resources");
        }
    }

    /**
     * Zisti ci mozeme pridat dane resources na kartu. (iba zisti ci nie je karta
     * nie je pollutnua).
     **/
    public boolean canPutResources(final List<Resource> resources) {
        return isClear();
    }

    /**
     * Prida dane resources na kartu ak nie je pollutnuta.
     **/
    public void putResources(final List<Resource> resources) throws IllegalArgumentException {
        if (canPutResources(resources)) {
            for (Resource resource : resources) {
                this.resources.put(resource, this.resources.get(resource) + 1);
            }
        } else {
            throw new IllegalArgumentException("Can't put given resources");
        }
    }

    /**
     * Vrati vsetky resources na karte vratane vsetkych pollutions na tejto karte.
     * (nic nezmeni, iba vrati list).
     **/
    public List<Resource> resourcesOnCard() {
        List<Resource> resources = new ArrayList<>();
        for (Entry<Resource, Integer> entry : this.resources.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                resources.add(entry.getKey());
            }
        }
        return resources;
    }

    /**
     * Zisti ci je mozne vyrobit pouzitim horneho efektu karty z danych input
     * resources output resources.
     **/
    public boolean checkUpper(final List<Resource> input, final List<Resource> output, final int pollution) {
        return upperEffect.isPresent() && upperEffect.get().check(input, output, pollution);
    }

    /**
     * Zisti ci je mozne vyrobit pouzitim dolneho efektu karty z danych input
     * resources output resources.
     **/
    public boolean checkLower(final List<Resource> input, final List<Resource> output, final int pollution) {
        return lowerEffect.isPresent() && lowerEffect.get().check(input, output, pollution);
    }

    /**
     * Zisti ci spodny alebo dolny efekt karty obsahuje asistenciu.
     **/
    public boolean hasAssistance() {
        if (lowerEffect.isPresent() && lowerEffect.get().hasAssistance()) {
            return true;
        }
        if (upperEffect.isPresent() && upperEffect.get().hasAssistance()) {
            return true;
        }
        return false;
    }

    public String state() {
        JSONObject json = new JSONObject();
        json.put("resources", new JSONObject(this.resources));
        json.put("lowerEffect", lowerEffect);
        json.put("upperEffect", upperEffect);
        putOptionalEffectToJSON(json, "lowerEffect", lowerEffect);
        putOptionalEffectToJSON(json, "upperEffect", upperEffect);
        json.put("pollutionSpaces", pollutionSpaces);
        return json.toString();
    }

    private static void putOptionalEffectToJSON(JSONObject json, String key, Optional<Effect> effect) {
        if (effect.isPresent()) {
            json.put(key, Optional.of(effect.get().state()));
        } else {
            json.put(key, effect);
        }
    }

    /**
     * Zisti ci karta je cist (teda ci neobsahuje viac pollution ako moze).
     **/
    public boolean isClear() {
        return this.resources.get(Resource.Pollution) <= pollutionSpaces;
    }

    private static Map<Resource, Integer> getMapWithoutResources() {
        Map<Resource, Integer> result = new HashMap<>();
        for (Resource resource : Resource.values()) {
            result.put(resource, 0);
        }
        return result;
    }
}
