package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.List;
import java.util.Optional;

public class SelectReward {
    private Optional<Integer> player;
    private List<Resource> selection;
    private Card card;

    public void setReward(final int player, final Card card, final List<Resource> resources) {
        this.player = Optional.of(player);
        this.card = card;
        this.selection = List.copyOf(resources);
    }

    public boolean canSelectReward(Resource resource) {
        return selection.contains(resource);
    }

    public void selectReward(Resource resource) {
        if (!canSelectReward(resource)) {
            return;
        }
        card.putResources(List.of(resource));
    }

    public String state() {
        return "";
    }
}
