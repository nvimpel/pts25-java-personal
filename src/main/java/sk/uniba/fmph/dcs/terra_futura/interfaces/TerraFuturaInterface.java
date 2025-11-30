package sk.uniba.fmph.dcs.terra_futura.interfaces;

import sk.uniba.fmph.dcs.terra_futura.datatypes.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Optional;

import sk.uniba.fmph.dcs.terra_futura.enums.Deck;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

public interface TerraFuturaInterface {
    boolean takeCard(int playerId, CardSource source, GridPosition destination);

    boolean discardLastCardFromDeckPlayerId(int playerId, Deck deck);

    boolean activateCard(int playerId, GridPosition cardPosition,
            List<SimpleEntry<Resource, GridPosition>> inputs, List<Resource> outputs,
            List<GridPosition> pollution, Optional<Integer> otherPlayerId,
            Optional<GridPosition> otherPos);

    boolean selectReward(int playerId, Resource resource);

    boolean turnFinished(int playerId);

    boolean selectActivationPattern(int playerId, int card);

    boolean selectScoring(int playerId, int card);
}
