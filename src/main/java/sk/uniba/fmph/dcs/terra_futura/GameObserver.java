package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaObserverInterface;

/**
 * Klasa GameObserver si udrziava zaznam o sledujucich spolu s ich Id-ckom.
 */
class GameObserver {
    private final java.util.Map<Integer, TerraFuturaObserverInterface> observers;

    GameObserver() {
        observers = new java.util.HashMap<>();
    }

    /**
     * Metoda uklada udaje o observerovi.
     * @param observer observer
     * @param id observerovo id
     */
    void addObserver(final TerraFuturaObserverInterface observer, final Integer id) {
        if (observers.get(id) != null) {
            throw new IllegalArgumentException("Observer with id " + id + " already exists!");
        }
        observers.put(id, observer);
    }

    /**
     * Metoda vymaze observera s Id ak takeho ma.
     * @param id id observera, ktoreho ma metoda vymazat
     */
    void removeObserver(final int id) {
        if (observers.get(id) == null) {
            throw new IllegalArgumentException("Observer with id " + id + " does not exist!");
        }
        observers.remove(id);
    }

    /**
     * Prisluchajucim observerom oznami novy stav.
     * @param state state hry o ktorom sa ma observer dozvediet
     */
    void notifyAllNewState(final java.util.Map<Integer, String> state) {

        for (Integer key : state.keySet()) {
            if (observers.get(key) != null) {
                observers.get(key).notify(state.get(key));
            }
        }
    }
}
