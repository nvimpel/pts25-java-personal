package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaObserverInterface;

class GameObserver {
    java.util.Map<Integer, TerraFuturaObserverInterface> observers;

    void notifyAllNewState(java.util.Map<Integer, String> state) {}
}
