package Test.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.Player;
import Main.Controllers.FacadeController;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Peter on 14/11/2016.
 */
public class TestLogicController {
  private FacadeController facadeController;
  private Player[] allPlayers;

  @Before
  public void setup() {
    IConnectionHandler stub = new ConnectionHandlerStub();
    ICollectionBuilder collectionBuilder = new CollectionBuilder(stub);
    facadeController = new FacadeController(collectionBuilder, "cwaq");
    allPlayers = facadeController.getAllPlayers();

    //System.out.println(facadeController.getAllPlayers()[6].name); Charlotte
    //System.out.println(facadeController.getAllPlayers()[0].name); Alf
    //System.out.println(facadeController.getAllPlayers()[10].name); Famse
  }

  /**
   * Player 10 has played sequence twice and resistance three times.
   * Since sequence can be played by two players in time 10-120 minutes, it should be on the suggested list
   */
  @Test
  public void suggestedGamesShouldContainSequence() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGame[] suggestedGames = facadeController.suggestGames(players, 40);
    boolean foundGame = false;
    for (BoardGame game : suggestedGames) {
      if(game.getName().equals("Sequence")) {
        foundGame = true;
        break;
      }
    }
    assertTrue(foundGame);
  }

  /**
   * Player 10 has played sequence twice and resistance three times.
   * Sequence has a complexity of 1.37 and resistance a weight of 1.64.
   * Camel up has a weight of 1.50 and a playtime of 20-30 minutes. It can also play 2 players,
   * which is the main criteria.
   */
  @Test
  public void suggestedGamesShouldContainCamelUp() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGame[] suggestedGames = facadeController.suggestGames(players, 40);

    boolean foundGame = false;
    for (BoardGame game : suggestedGames) {
      if(game.getName().equals("Camel Up")) {
        foundGame = true;
        break;
      }
    }
    assertTrue(foundGame);
  }
}
