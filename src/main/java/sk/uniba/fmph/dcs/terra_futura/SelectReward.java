package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.List;
import java.util.Optional;

public class SelectReward {
    private Optional<Integer> player;
    private List<Resource> selection;

    public void setReward(int player, Card card, List<Resource> resources) {
    }

    public boolean canSelectReward(Resource resource) {
        return false;
    }

    public void selectReward(Resource resource) {
    }

    public String state() {
        return "";
    }
}
