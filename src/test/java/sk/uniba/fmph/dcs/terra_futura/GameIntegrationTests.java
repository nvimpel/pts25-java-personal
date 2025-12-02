package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.datatypes.CardSource;
import sk.uniba.fmph.dcs.terra_futura.datatypes.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.datatypes.Player;
import sk.uniba.fmph.dcs.terra_futura.enums.Deck;
import sk.uniba.fmph.dcs.terra_futura.enums.GameState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameIntegrationTests {

    private int[] players;
    private int startingPlayer;
    private List<Player> premadePlayers;
    private List<Card> deckI;
    private List<Card> deckII;

    @Before
    public void setUp() {
        players = new int[]{1, 2};
        startingPlayer = 1;

        // Deterministicke decky, su rovnake ako su definovane v generatore (non-shuffle)

        deckI = CardGenerator.pileGenerator(Deck.I);
        deckII = CardGenerator.pileGenerator(Deck.II);

        premadePlayers = new ArrayList<>();
        premadePlayers.add(makePlayer());
        premadePlayers.add(makePlayer());
    }

    private Player makePlayer() {
        Grid grid = new Grid();
        return new Player(
                grid,
                CardGenerator.generateActivationPattern(grid),
                CardGenerator.generateActivationPattern(grid),
                CardGenerator.generateScoringCard(),
                CardGenerator.generateScoringCard()
        );
    }

    private Game newGame() {

        return new Game(players, 2, startingPlayer, deckI, deckII, premadePlayers, 1);
    }

    @Test
    public void initial_take_passes__and_blocks_further_take() {
        Game game = newGame();

        assertEquals(GameState.TakeCardNoCardDiscarded, game.getState());

        boolean wrongPlayerTake = game.takeCard(2, new CardSource(Deck.I, 0), new GridPosition(1, 0));
        assertFalse("Hrac nie je na tahu", wrongPlayerTake);

        boolean took = game.takeCard(1, new CardSource(Deck.I, 0), new GridPosition(1, 0));
        assertTrue("Pre aktualneho hraca test prejde", took);
        assertEquals("Musi nastat zmena stavu", GameState.ActivateCard, game.getState());

        boolean illegalTake = game.takeCard(1, new CardSource(Deck.I, 0), new GridPosition(0, 1));
        assertFalse("Nemoze si hrac zobrat dalsiu kartu", illegalTake);

        boolean wrongPlayerTakeWrongState = game.takeCard(2, new CardSource(Deck.I, 0), new GridPosition(1, 0));
        assertFalse("Stav aj hrac je zly", wrongPlayerTakeWrongState);
    }

    @Test
    public void discard_once_then_take_allows_only_one_discard() {
        Game game = newGame();

        assertEquals(GameState.TakeCardNoCardDiscarded, game.getState());

        boolean wrongPlayerTriesDiscard = game.discardLastCardFromDeckPlayerId(2,Deck.I);
        assertFalse("Tento hrac nie je na tahu",wrongPlayerTriesDiscard);

        boolean discarded = game.discardLastCardFromDeckPlayerId(1, Deck.I);
        assertTrue("Hrac na tahu moze jednu discardnut", discarded);
        assertEquals(GameState.TakeCardCardDiscarded, game.getState());

        boolean secondDiscard = game.discardLastCardFromDeckPlayerId(1, Deck.I);
        assertFalse("Druhy discard nemoze prejst", secondDiscard);

        boolean took = game.takeCard(1, new CardSource(Deck.I, 0), new GridPosition(1, 0));
        assertTrue("Stale si moze zobrat kartu aj ked iny stav", took);
        assertEquals(GameState.ActivateCard, game.getState());

        boolean discardDuringActivate = game.discardLastCardFromDeckPlayerId(1, Deck.I);
        assertFalse("V stave aktivacie nesmie povolit zobrat kartu", discardDuringActivate);
    }

    @Test
    public void discard_on_deckI_then_take_from_deckII_is_allowed() {
        Game game = newGame();
        assertEquals(GameState.TakeCardNoCardDiscarded, game.getState());

        boolean discardedI = game.discardLastCardFromDeckPlayerId(1, Deck.I);
        assertTrue(discardedI);
        assertEquals(GameState.TakeCardCardDiscarded, game.getState());

        // moze discardnut z deck I a zobrat z deck II
        boolean tookFromII = game.takeCard(1, new CardSource(Deck.II, 0), new GridPosition(-1, 0));
        assertTrue("Zobrat z druheho decku by malo byt povolene", tookFromII);
        assertEquals(GameState.ActivateCard, game.getState());
    }

    @Test
    public void wrong_player_cannot_discard_or_take() {
        Game game = newGame();
        assertEquals(GameState.TakeCardNoCardDiscarded, game.getState());

        boolean wrongDiscard = game.discardLastCardFromDeckPlayerId(2, Deck.I);
        assertFalse("Tento hrac nie je na tahu", wrongDiscard);

        boolean wrongTake = game.takeCard(2, new CardSource(Deck.I, 0), new GridPosition(0, 0));
        assertFalse("Tento hrac nie je na tahu", wrongTake);

        // Aktivny hrac nema problem
        boolean took = game.takeCard(1, new CardSource(Deck.I, 0), new GridPosition(1, 0));
        assertTrue(took);
        assertEquals(GameState.ActivateCard, game.getState());

        // Druhy zly pokus zleho hraca
        boolean wrongTake2 = game.takeCard(2, new CardSource(Deck.I, 0), new GridPosition(0, 1));
        assertFalse(wrongTake2);
    }

    @Test
    public void invalid_visible_index_is_rejected() {
        Game game = newGame();

        // Index karty nie je platny
        boolean tookInvalid = game.takeCard(1, new CardSource(Deck.I, 99), new GridPosition(2, 2));
        assertFalse("Vyberanie neplatnej karty by nemalo prejst", tookInvalid);

        // stale mozem pokracovat normalne
        boolean discarded = game.discardLastCardFromDeckPlayerId(1, Deck.I);
        assertTrue(discarded);

        // stale nemozem zobrat zlu kartu
        boolean tookInvalid2 = game.takeCard(1, new CardSource(Deck.I, 42), new GridPosition(1, 1));
        assertFalse(tookInvalid2);
    }

    // TODO FLOW OF GAME
}