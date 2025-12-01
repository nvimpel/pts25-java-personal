package sk.uniba.fmph.dcs.terra_futura.effects;

import org.json.JSONArray;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.Effect;

import java.util.List;

/**
 * Implementácia efektu, ktorý iba generuje suroviny.
 * Generovať môže vždy len jednu surovinu naraz, no môže mať definovaných
 * viacero typov surovín, ktoré vie produkovať.
 * {@code check} overuje, či generovaná surovina presne zodpovedá jednej
 * zo surovín, ktoré daný efekt dokáže vytvoriť.
 * Ak sa v {@code output} nachádza viac než jedna surovina, metóda vráti {@code false}.
 * {@code input} aj {@code pollution} musia byť prázdne, respektíve nulové.
 */

public final class ArbitraryBasic implements Effect {
    private final List<Resource> to;

    public ArbitraryBasic(final List<Resource> to) {
        if (to == null) {
            throw new NullPointerException("List to cant be null");
        }
        this.to = List.copyOf(to);
    }

    @Override
    public boolean check(final List<Resource> input, final List<Resource> output, final int pollution) {
        if (input == null || output == null) {
            throw new NullPointerException("Input or output cant be null");
        }
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
