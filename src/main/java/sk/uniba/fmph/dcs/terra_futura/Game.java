package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.GameState;
import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaInterface;
import sk.uniba.fmph.dcs.terra_futura.datatypes.*;
import sk.uniba.fmph.dcs.terra_futura.enums.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Optional;

class Game implements TerraFuturaInterface {
    GameState state;
    int players;
    int onTurn;

    int startingPlayer;
    int turnNumber;

    @Override
    public boolean takeCard(int playerId, CardSource source, GridPosition destination) {
        return false;
    }

    @Override
    public boolean discardLastCardFromDeckPlayerId(int playerId, Deck deck) {
        return false;
    }

    @Override
    public boolean activateCard(int playerId, GridPosition cardPosition,
            List<SimpleEntry<Resource, GridPosition>> inputs, List<Resource> outputs,
            List<GridPosition> pollution, Optional<Integer> otherPlayerId,
            Optional<GridPosition> otherPos) {
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
    public boolean selectActivationPattern(int playerId, int card) {
        return false;

    }

    @Override
    public boolean selectScoring(int playerId, int card) {
        return false;
    }
}
