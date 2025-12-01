package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;

public class GameIntegrationTest {
    private Game game;
    int[] pIDS = new int[]{1,4586476};

    @Before
    public void setUp() {
        game = new Game(pIDS,2,101);
    }
}
