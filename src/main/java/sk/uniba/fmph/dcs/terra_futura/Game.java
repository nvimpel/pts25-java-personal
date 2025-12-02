package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.enums.GameState;
import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaInterface;
import sk.uniba.fmph.dcs.terra_futura.datatypes.*;
import sk.uniba.fmph.dcs.terra_futura.enums.*;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class Game implements TerraFuturaInterface {
    GameState state = GameState.TakeCardNoCardDiscarded;
    SelectReward selectReward = new SelectReward();
    int onTurn;

    List<Integer> playersIDs;
    Map<Integer,Player> players = new HashMap<>();

    int assistingPlayer;

    Pile pile1;
    Pile pile2;

    int startingPlayer;
    int turnNumber = 1;

    int actionCounter = 0;

    public Game(int[] playersIDs, int playersCount, int startingPlayer) {
        this.playersIDs = Arrays.stream(playersIDs).boxed().toList();
        pile1 =  new Pile(Deck.I);
        pile2 =  new Pile(Deck.II);
        this.startingPlayer = startingPlayer;
        onTurn = startingPlayer;
        for (int i = 0; i < playersCount; i++) {
            players.put(this.playersIDs.get(i), generatePlayer());
        }

    }

    //deterministicky konstruktor
    public Game(int[] playersIDs, int playersCount, int startingPlayer,
                List<Card> customDeck1, List<Card> customDeck2, List<Player> premadePlayers) {
        this.playersIDs = Arrays.stream(playersIDs).boxed().toList();
        this.startingPlayer = startingPlayer;
        pile1 = new Pile(customDeck1);
        pile2 = new Pile(customDeck2);
        onTurn = startingPlayer;
        for (int i = 0; i < playersCount; i++) {
            if(!premadePlayers.isEmpty()){
                players.put(this.playersIDs.get(i), premadePlayers.removeFirst());
            } else{
                players.put(this.playersIDs.get(i), generatePlayer());
            }
        }

    }

    private Player generatePlayer() {
        Grid grid = new Grid();


        return new Player(grid,
                CardGenerator.generateActivationPattern(grid),
                CardGenerator.generateActivationPattern(grid),
                CardGenerator.generateScoringCard(), CardGenerator.generateScoringCard());

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
        boolean result = MoveCard.moveCard(getPile(source.deck()), source.index(), destination,
                players.get(playerId).getGrid());

        if(result){
            state = GameState.ActivateCard;
        }
        return result;

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
        if(otherPlayerId.isPresent() && otherPos.isPresent()){
            Optional<Card> assistingCard = players.get(otherPlayerId.get()).getGrid().getCard(otherPos.get());
            if(assistingCard.isEmpty()){
                return  false;
            }
            assistingPlayer = otherPlayerId.get();
            result = ProcessActionAssistance.activateCard(assistingCard.get(),
                    cardPosition, players.get(playerId).getGrid(),
                    otherPlayerId.get(), inputs, outputs, pollution);
            if(result){
                state = GameState.SelectReward;
            }
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
        //Turn dostane dalsi hrac (ak sme v scoringu tak sa hrac meni az ked sa vyberu dve karty)
        if(actionCounter == 0){
            onTurn = nextPlayer(onTurn);
        }
        //Ak turn dostal starting player viem ze zaciname dalsie kolo
        if (onTurn == startingPlayer && actionCounter == 0) {
            turnNumber++;
        }
        //Ak je turn < 9 stale vieme pridavat karty
        //Na turn == 9 zacina dodatocan aktivacia
        //Na turn == 10 sa vyhodnoti skore
        if (turnNumber < 9){
            state = GameState.TakeCardNoCardDiscarded;
            return true;
        } else if (turnNumber == 9) {
            state = GameState.SelectActivationPattern;
            if(actionCounter == 1) {
                actionCounter = -1;
            }
            return true;
        } else if (turnNumber == 10) {
            state = GameState.SelectScoringMethod;
            actionCounter = 0;
        }

        return true;


    }

    @Override
    public boolean selectActivationPattern(int playerId, int card) {
        if(card != 1 && card != 2){
            return false;
        }
        if(playerId != onTurn){
            return false;
        }
        actionCounter++;
        Player player = players.get(playerId);
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
        if(card != 1 && card != 2){
            return false;
        }
        if(playerId != onTurn){
            return false;
        }
        actionCounter++;
        Player player = players.get(playerId);
        int currentPoints = player.getPoints().orElse(0);
        List<Resource> playerResources = getAllResources(player);
        if(card == 1){
            player.getScoringMethod1().selectThisMethodAndCalculate(playerResources);
            player.setPoints(player.getScoringMethod1().getFinalPoints().orElse(0) + currentPoints);
        } else {
            player.getScoringMethod2().selectThisMethodAndCalculate(playerResources);
            player.setPoints(player.getScoringMethod2().getFinalPoints().orElse(0) + currentPoints);
        }

        if(actionCounter == 2){
            onTurn = nextPlayer(onTurn);
            actionCounter = 0;
            if(onTurn == startingPlayer){
                calculateFinalPoints();
                state = GameState.Finish;
            }
        }
        return true;
    }

    private List<Resource> getAllResources(Player player){
        List<Resource> playerResources = new ArrayList<>();
        for(Card card : player.getGrid().getAllCards()){
            playerResources.addAll(card.resourcesOnCard());
        }
        return playerResources;
    }

    private Pile getPile(Deck deck) {
        if (deck == Deck.I){
            return pile1;
        }
        else return pile2;
    }

    private int nextPlayer(int currentPlayer) {
        int index = playersIDs.indexOf(currentPlayer);
        return playersIDs.get((index + 1) % playersIDs.size());
    }




    private void calculateFinalPoints(){
        for(Player player : players.values()){
            player.setPoints(player.getPoints().orElse(0) + finalPoints(player));
        }
    }

    private int finalPoints(Player player){
        Map<Resource, Integer> pointsPerResource = new HashMap<>();
        pointsPerResource.put(Resource.Green, 1);
        pointsPerResource.put(Resource.Red, 1);
        pointsPerResource.put(Resource.Yellow, 1);
        pointsPerResource.put(Resource.Bulb,5);
        pointsPerResource.put(Resource.Gear,5);
        pointsPerResource.put(Resource.Car,6);
        pointsPerResource.put(Resource.Pollution,5);

        int totalPoints = 0;
        for(Card card : player.getGrid().getAllCards()){
            for(Resource resource : card.resourcesOnCard()){
                totalPoints += pointsPerResource.getOrDefault(resource,0);
            }
        }
        return totalPoints;
    }

    //info-getters

    public GameState getState() {
        return state;
    }




}
