package sk.uniba.fmph.dcs.terra_futura.interfaces;

import sk.uniba.fmph.dcs.terra_futura.datatypes.*;
import sk.uniba.fmph.dcs.terra_futura.Grid;

import java.util.AbstractMap.SimpleEntry;

import sk.uniba.fmph.dcs.terra_futura.Card;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

public interface TerraFuturaInterface {
    boolean takeCard(int playerId, CardSource source, GridPosition destination);

    boolean discardLastCardFromDeckPlayerId(int playerId, Deck deck);

    boolean activateCard(int playerId, int card, GridPosition inputs,
            java.util.List<SimpleEntry<Resource, GridPosition>> outputs,
            java.util.List<GridPosition> pollution);

    boolean activateCard(Player player, Card card, Grid grid,
            java.util.List<SimpleEntry<Resource, GridPosition>> outputs, java.util.List<GridPosition> pollution,
            Player otherPlayer, java.util.Optional<GridPosition> otherPos);

    boolean selectReward(int playerId, Resource resource);

    boolean turnFinished(int playerId);

    boolean selectActivationPattern(int playerId, int card, int id);

    boolean selectScoring(int playerId, int card, int b);
}
