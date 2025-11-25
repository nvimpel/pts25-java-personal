package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaObserverInterface;

class GameObserver {
    java.util.Map<Integer, TerraFuturaObserverInterface> observers;

    GameObserver() {
        observers = new java.util.HashMap<>();
    }

    void addObserver(TerraFuturaObserverInterface observer, Integer id) {
        if(observers.get(id) != null) {
            throw new IllegalArgumentException("Observer with id " + id + " already exists!");
        }
        observers.put(id, observer);
    }

    void removeObserver(int id) {
        if(observers.get(id) == null) {
            throw new IllegalArgumentException("Observer with id " + id + " does not exist!");
        }
        observers.remove(id);
    }

    void notifyAllNewState(java.util.Map<Integer, String> state) {

        for(Integer key : state.keySet()) {
            if(observers.get(key) != null) {
                observers.get(key).notify(state.get(key));
            }
        }
    }
}