package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.*;
import sk.uniba.fmph.dcs.terra_futura.enums.*;

class TerraFuturaComponent implements TerraFuturaInterface {

    @Override
    public boolean takeCard(int playerId, CardSource source, GridPosition destination) {
        return false;
    }

    @Override
    public boolean discardLastCardFromDeckPlayerId(int playerId, Deck deck) {
        return false;
    }

    @Override
    public boolean activateCard(int playerId, int card, GridPosition inputs, java.util.List<Pair<Resource, GridPosition>> outputs,
                                java.util.List<GridPosition> pollution) {
        return false;
    }

    @Override
    public boolean activateCard(Player player, Card card, Grid grid,
                                 java.util.List<Pair<Resource, GridPosition>> outputs,
                                 java.util.List<GridPosition> pollution,
                                 Player otherPlayer, java.util.Optional<GridPosition> otherPos) {
        return false;
    }


    @Override
    public boolean selectReward(int playerId, Resource resource) {
        return false;
    }

    @Override
    public boolean turnFinished(int playerId) {
        return false;
    }

    @Override
    public boolean selectActivationPattern(int playerId, int card, int id) {
        return false;

    }

    @Override
    public boolean selectScoring(int playerId, int card, int b) {
        return false;
    }
}

