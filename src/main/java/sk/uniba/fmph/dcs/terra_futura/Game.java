package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.datatypes.CardSource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.datatypes.Player;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;
import sk.uniba.fmph.dcs.terra_futura.enums.GameState;
import sk.uniba.fmph.dcs.terra_futura.enums.Resource;
import sk.uniba.fmph.dcs.terra_futura.interfaces.TerraFuturaInterface;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Game implements TerraFuturaInterface {

    private static final int MAXIMUM_NUM_OF_PLAYERS = 5;
    private static final int MINIMUM_NUM_OF_PLAYERS = 2;
    private static final int ACTIVATION_TURN = 9;
    private static final int SCORING_TURN = 10;
    private static final int MATERIAL_POINTS = 1;
    private static final int PRODUCT_POINTS = 5;
    private static final int CAR_POINTS = 6;
    private static final int POLLUTION_POINTS = -1;

    private GameState state = GameState.TakeCardNoCardDiscarded;
    private final SelectReward selectReward = new SelectReward();
    private int onTurn;

    private final List<Integer> playersIDs;
    private final Map<Integer, Player> players = new HashMap<>();

    private int assistingPlayer;

    private final Pile pile1;
    private final Pile pile2;

    private final int startingPlayer;
    private int turnNumber = 1;

    private int actionCounter = 0;

    public Game(final int[] playersIDs, final int playersCount, final int startingPlayer) {
        if (playersCount < MINIMUM_NUM_OF_PLAYERS || playersCount > MAXIMUM_NUM_OF_PLAYERS) {
            throw new IllegalArgumentException("Invalid number of players: " + playersCount);
        }
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
    public Game(final int[] playersIDs, final int playersCount, final int startingPlayer,
                final List<Card> customDeck1, final List<Card> customDeck2,
                final List<Player> premadePlayers, final int currentRound) {
        this.playersIDs = Arrays.stream(playersIDs).boxed().toList();
        this.startingPlayer = startingPlayer;
        pile1 = new Pile(customDeck1);
        pile2 = new Pile(customDeck2);
        onTurn = startingPlayer;
        this.turnNumber = currentRound;
        for (int i = 0; i < playersCount; i++) {
            if (!premadePlayers.isEmpty()) {
                players.put(this.playersIDs.get(i), premadePlayers.removeFirst());
            } else {
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
    public boolean takeCard(final int playerId, final CardSource source,
                                  final GridPosition destination) {
        if (state != GameState.TakeCardNoCardDiscarded
            && state != GameState.TakeCardCardDiscarded) {
            return false;
        }
        if (onTurn != playerId) {
            return false;
        }
        boolean result = MoveCard.moveCard(getPile(source.deck()), source.index(), destination,
                players.get(playerId).getGrid());

        if (result) {
            state = GameState.ActivateCard;
        }
        return result;

    }

    @Override
    public boolean discardLastCardFromDeckPlayerId(final int playerId, final Deck deck) {

        if (state != GameState.TakeCardNoCardDiscarded || onTurn != playerId) {
            return false;
        }

        getPile(deck).removeLastCard();

        state = GameState.TakeCardCardDiscarded;
        return true;

    }

    @Override
    public boolean activateCard(final int playerId, final GridPosition cardPosition,
            final List<SimpleEntry<Resource, GridPosition>> inputs, final List<Resource> outputs,
            final List<GridPosition> pollution, final Optional<Integer> otherPlayerId,
            final Optional<GridPosition> otherPos) {
        if (onTurn != playerId) {
            return false;
        }
        if (state != GameState.ActivateCard) {
            return false;
        }
        boolean result;
        if (otherPlayerId.isPresent() && otherPos.isPresent()) {
            Optional<Card> assistingCard = players.get(otherPlayerId.get()).getGrid().getCard(otherPos.get());
            if (assistingCard.isEmpty()) {
                return  false;
            }
            assistingPlayer = otherPlayerId.get();
            result = ProcessActionAssistance.activateCard(assistingCard.get(),
                    cardPosition, players.get(playerId).getGrid(),
                    otherPlayerId.get(), inputs, outputs, pollution);
            if (result) {

                selectReward.setReward(assistingPlayer,assistingCard.get(),
                        inputs.stream().map(SimpleEntry::getKey).toList());
                state = GameState.SelectReward;
            }
        } else {
            result =  ProcessAction.activateCard(cardPosition,
                    players.get(playerId).getGrid(), inputs,
                    outputs, pollution);

        }
        return result;
    }

    @Override
    public boolean selectReward(final int playerId, final Resource resource) {
        if (state != GameState.SelectReward) {
            return false;
        }
        if (assistingPlayer != playerId) {
            return false;
        }

        if (!selectReward.canSelectReward(resource)) {
            return false;
        }

        selectReward.selectReward(resource);
        state = GameState.ActivateCard;

        return true;
    }

    @Override
    public boolean turnFinished(final int playerId) {
        if (state != GameState.ActivateCard) {
            return false;
        }
        if (onTurn != playerId) {
            return false;
        }
        //Turn dostane dalsi hrac (ak sme v scoringu tak sa hrac meni az ked sa vyberu dve karty)
        if (actionCounter == 0) {
            onTurn = nextPlayer(onTurn);
        }
        //Ak turn dostal starting player viem ze zaciname dalsie kolo
        if (onTurn == startingPlayer && actionCounter == 0) {
            turnNumber++;
        }
        //Ak je turn < 9 stale vieme pridavat karty
        //Na turn je 9 zacina dodatocan aktivacia
        //Na turn je 10 sa vyhodnoti skore
        if (turnNumber < ACTIVATION_TURN) {
            state = GameState.TakeCardNoCardDiscarded;
            return true;
        } else if (turnNumber == ACTIVATION_TURN) {
            state = GameState.SelectActivationPattern;
            if (actionCounter == 1) {
                actionCounter = -1;
            }
            return true;
        } else if (turnNumber == SCORING_TURN) {
            state = GameState.SelectScoringMethod;
            actionCounter = 0;
        }

        return true;


    }

    @Override
    public boolean selectActivationPattern(final int playerId, final int card) {
        if (state != GameState.SelectActivationPattern) {
            return false;
        }
        if (card != 1 && card != 2) {
            return false;
        }
        if (playerId != onTurn) {
            return false;
        }
        actionCounter++;
        Player player = players.get(playerId);
        if (card == 1) {
            player.getActivationPattern1().select();
        } else {
            player.getActivationPattern2().select();
        }
        state = GameState.ActivateCard;
        return true;

    }

    @Override
    public boolean selectScoring(final int playerId, final int card) {
        if (card != 1 && card != 2) {
            return false;
        }
        if (playerId != onTurn) {
            return false;
        }
        actionCounter++;
        Player player = players.get(playerId);
        int currentPoints = player.getPoints().orElse(0);
        List<Resource> playerResources = getAllResources(player);
        if (card == 1) {
            player.getScoringMethod1().selectThisMethodAndCalculate(playerResources);
            player.setPoints(player.getScoringMethod1().getFinalPoints().orElse(0) + currentPoints);
        } else {
            player.getScoringMethod2().selectThisMethodAndCalculate(playerResources);
            player.setPoints(player.getScoringMethod2().getFinalPoints().orElse(0) + currentPoints);
        }

        if (actionCounter == 2) {
            onTurn = nextPlayer(onTurn);
            actionCounter = 0;
            if (onTurn == startingPlayer) {
                calculateFinalPoints();
                state = GameState.Finish;
            }
        }
        return true;
    }

    private List<Resource> getAllResources(final Player player) {
        List<Resource> playerResources = new ArrayList<>();
        for (Card card : player.getGrid().getAllCards()) {
            playerResources.addAll(card.resourcesOnCard());
        }
        return playerResources;
    }

    private Pile getPile(final Deck deck) {
        if (deck == Deck.I) {
            return pile1;
        } else {
            return pile2;
        }
    }

    private int nextPlayer(final int currentPlayer) {
        int index = playersIDs.indexOf(currentPlayer);
        return playersIDs.get((index + 1) % playersIDs.size());
    }




    private void calculateFinalPoints() {
        for (Player player : players.values()) {
            player.setPoints(player.getPoints().orElse(0) + finalPoints(player));
        }
    }

    private int finalPoints(final Player player) {

        Map<Resource, Integer> pointsPerResource = new HashMap<>();
        pointsPerResource.put(Resource.Green, MATERIAL_POINTS);
        pointsPerResource.put(Resource.Red, MATERIAL_POINTS);
        pointsPerResource.put(Resource.Yellow, MATERIAL_POINTS);
        pointsPerResource.put(Resource.Bulb, PRODUCT_POINTS);
        pointsPerResource.put(Resource.Gear, PRODUCT_POINTS);
        pointsPerResource.put(Resource.Car, CAR_POINTS);
        pointsPerResource.put(Resource.Pollution, POLLUTION_POINTS);

        int totalPoints = 0;
        for (Card card : player.getGrid().getAllCards()) {
            for (Resource resource : card.resourcesOnCard()) {
                totalPoints += pointsPerResource.getOrDefault(resource, 0);
            }
        }
        return totalPoints;
    }

    //info-getters

    public GameState getState() {
        return state;
    }

    public String getPoints() {
        StringBuilder sb = new StringBuilder();
        for(Player player : players.values()) {
            sb.append(player.getPoints().orElse(0));
            sb.append("\n");
        }
        return sb.toString();
    }




}
