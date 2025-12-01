package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.GameState;
import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaInterface;
import sk.uniba.fmph.dcs.terra_futura.datatypes.*;
import sk.uniba.fmph.dcs.terra_futura.enums.*;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

class Game implements TerraFuturaInterface {
    GameState state = GameState.TakeCardNoCardDiscarded;
    SelectReward selectReward = new SelectReward();
    int playersCount;
    int onTurn;

    List<Integer> playersIDs;
    List<Player> players = new ArrayList<>();

    int assistingPlayer;

    Pile pile1 = new Pile(Deck.I);
    Pile pile2 = new Pile(Deck.II);

    int startingPlayer;
    int turnNumber = 0;

    int actionCounter = 0;

    public Game(int[] playersIDs, int playersCount, int startingPlayer) {
        this.playersIDs = Arrays.stream(playersIDs).boxed().toList();
        this.playersCount = playersCount;
        this.startingPlayer = startingPlayer;
        onTurn = startingPlayer;
        for (int i = 0; i < playersCount; i++) {
            players.add(generatePlayer());
        }

    }

    private Player generatePlayer() {
        Grid grid = new Grid();
        Collection<SimpleEntry<Integer, Integer>> pattern = new ArrayList<>();
        ActivationPattern pattern1 = new ActivationPattern(grid,pattern);
        ActivationPattern pattern2 = new ActivationPattern(grid,pattern);
        List<Resource> requiredCombination =  new ArrayList<>();
        ScoringMethod scoringMethod1 = new ScoringMethod(requiredCombination,0);
        ScoringMethod scoringMethod2 = new ScoringMethod(requiredCombination,0);
        return new Player(grid,
                pattern1, pattern2,
                scoringMethod1, scoringMethod2);

    }

    @Override
    public boolean takeCard(int playerId, CardSource source, GridPosition destination) {
        if(state != GameState.TakeCardNoCardDiscarded
            && state != GameState.TakeCardCardDiscarded){
            return false;
        }
        if(onTurn != playerId){
            return false;
        }
        return MoveCard.moveCard(getPile(source.deck()), source.index(), destination,
                players.get(playersIDs.indexOf(playerId)).getGrid());

    }

    @Override
    public boolean discardLastCardFromDeckPlayerId(int playerId, Deck deck) {

        if(state != GameState.TakeCardNoCardDiscarded || onTurn != playerId){
            return false;
        }

        getPile(deck).removeLastCard();

        state = GameState.TakeCardCardDiscarded;
        return true;

    }

    @Override
    public boolean activateCard(int playerId, GridPosition cardPosition,
            List<SimpleEntry<Resource, GridPosition>> inputs, List<Resource> outputs,
            List<GridPosition> pollution, Optional<Integer> otherPlayerId,
            Optional<GridPosition> otherPos) {
        if(onTurn != playerId) {
            return false;
        }
        if(state != GameState.ActivateCard){
            return false;
        }
        boolean result;
        if(otherPlayerId.isPresent()){
            assistingPlayer = otherPlayerId.get();
            result = ProcessActionAssistance.activateCard(cardPosition,
                    players.get(playerId).getGrid(), otherPlayerId.get(),
                    inputs, outputs, pollution);
        }
        else {
            result =  ProcessAction.activateCard(cardPosition,
                    players.get(playerId).getGrid(), inputs,
                    outputs, pollution);

        }

        return result;
    }

    @Override
    public boolean selectReward(int playerId, Resource resource) {
        if(state != GameState.SelectReward){
            return false;
        }
        if(assistingPlayer != playerId){
            return false;
        }

        if(!selectReward.canSelectReward(resource)){
            return false;
        }

        selectReward.selectReward(resource);
        state = GameState.ActivateCard;

        return true;
    }

    @Override
    public boolean turnFinished(int playerId) {
        if(state != GameState.ActivateCard){
            return false;
        }
        if(onTurn != playerId){
            return false;
        }
        state = GameState.TakeCardNoCardDiscarded;
        players.get(playersIDs.indexOf(playerId)).getGrid().endTurn();
        onTurn = playersIDs.get((playersIDs.indexOf(playerId) + 1) % playersIDs.size());
        if(onTurn == startingPlayer){
            turnNumber++;
        }
        if (turnNumber == 9){
            if(actionCounter == 0){
                state = GameState.SelectActivationPattern;
            } else if (actionCounter == 2){
                state = GameState.SelectScoringMethod;
            } else {
                actionCounter = 0;

            }
        }
        return true;


    }

    @Override
    public boolean selectActivationPattern(int playerId, int card) {
        actionCounter++;
        if(card != 1 && card != 2){
            return false;
        }
        Player player = players.get(playersIDs.indexOf(playerId));
        if(card == 1){
            player.getActivationPattern1().select();
        } else {
            player.getActivationPattern2().select();
        }
        state = GameState.ActivateCard;
        return true;

    }

    @Override
    public boolean selectScoring(int playerId, int card) {
        return false;
    }

    private Pile getPile(Deck deck){
        if (deck == Deck.I){
            return pile1;
        }
        else return pile2;
    }
}
