package Test.Models.Storage;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Player;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 28/09/16.
 */
public class TestCollectionBuilder_WithStub {
  IConnectionHandler connectionHandler;
  ICollectionBuilder collectionBuilder;
  BoardGameCollection collection;
  BoardGame[] games;

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandlerStub();
    collectionBuilder = new CollectionBuilder(connectionHandler);
    collection = collectionBuilder.getCollection("cwaq");
    games = collection.getGames();
  }

  @Test
  public void connectionHandlerStubShouldWork() {
    assertNotNull(collection);
  }

  @Test
  public void collectionShouldContainArrayListOfBoardgames() {
    assertNotNull(games);
  }

  @Test
  public void collectionShouldContainNonEmptyListOfBoardGames() {
    assertTrue(games.length > 0);
  }

  @Test
  public void collectionShouldContainAgricola() {
    boolean agricolaExists = false;

    for (BoardGame game : games) {
      if (game.getName().equals("Agricola")) {
        agricolaExists = true;
        break;
      }
    }
    assertTrue(agricolaExists);
  }

  @Test
  public void collectionShouldContainHive() {
    boolean hiveExists = false;

    for (BoardGame game : games) {
      if (game.getName().equals("Hive")) {
        hiveExists = true;
        break;
      }
    }
    assertTrue(hiveExists);
  }

  @Test
  public void collectionShouldNotContainMonopoly() {
    boolean monopolyExists = false;

    for (BoardGame game : games) {
      if (game.getName().equals("Monopoly")) {
        monopolyExists = true;
        break;
      }
    }
    assertFalse(monopolyExists);
  }

  @Test
  public void agricolaShouldHaveUniqueID31260() {
    int uniqueID = 0;
    BoardGame game = games[0];
    uniqueID = game.getID();
    assertEquals(31260, uniqueID);
  }

  @Test
  public void hiveShouldHaveUniqueID2655() {
    int uniqueID = 0;
    BoardGame game = games[20];
    uniqueID = game.getID();
    assertEquals(2655, uniqueID);
  }

  @Test
  public void agricolaShouldHaveMinPlayers1() {
    int minPlayers = 0;
    BoardGame game = games[0];
    minPlayers = game.getMinPlayers();
    assertEquals(1, minPlayers);
  }

  @Test
  public void hiveShouldHaveMinPlayers2() {
    int minPlayers = 0;
    BoardGame game = games[20];
    minPlayers = game.getMinPlayers();
    assertEquals(2, minPlayers);
  }

  @Test
  public void hiveShouldHaveMaxPlayers2() {
    int maxPlayers = 0;
    BoardGame game = games[20];
    maxPlayers = game.getMaxPlayers();
    assertEquals(2, maxPlayers);
  }

  @Test
  public void agricolaShouldHaveMaxPlayers5() {
    int maxPlayers = 0;
    BoardGame game = games[0];
    maxPlayers = game.getMaxPlayers();
    assertEquals(5, maxPlayers);
  }

  @Test
  public void agricolaShouldHaveMinPlaytime30Min() {
    int minPlaytime = 0;
    BoardGame game = games[0];
    minPlaytime = game.getMinPlaytime();
    assertEquals(30, minPlaytime);
  }

  @Test
  public void agricolaShouldHaveMaxPlaytime150Min() {
    int maxPlaytime;
    BoardGame game = games[0];
    maxPlaytime = game.getMaxPlaytime();
    assertEquals(150, maxPlaytime);
  }

  @Test
  public void agricolaShouldHavePersonalRating8() {
    String personalRating;
    BoardGame game = games[0];
    personalRating = game.getPersonalRating();
    int rating = Integer.valueOf(personalRating);
    assertEquals(8, rating);
  }

  @Test
  public void hiveShouldHavePersonalRating10() {
    String personalRating;
    BoardGame game = games[20];
    personalRating = game.getPersonalRating();
    int rating = Integer.valueOf(personalRating);
    assertEquals(10, rating);
  }

  @Test
  public void agricolaShouldHaveNumPlays0() {
    int numPlays;
    BoardGame game = games[0];
    numPlays = game.getNumberOfPlays();
    assertEquals(0, numPlays);
  }

  @Test
  public void hiveShouldHaveNumPlays1() {
    int numPlays;
    BoardGame game = games[20];
    numPlays = game.getNumberOfPlays();
    assertEquals(1, numPlays);
  }

  @Test
  public void hiveShouldHaveComplexityBetween2And3() {
    double complexity;
    BoardGame game = games[20];
    complexity = game.getComplexity();
    assertTrue(complexity > 2.0 && complexity < 3.0);
  }

  @Test
  public void agricolaShouldHaveComplexityBetween3And4() {
    double complexity;
    BoardGame game = games[0];
    complexity = game.getComplexity();
    assertTrue(complexity > 3.0 && complexity < 4.0);
  }

  @Test
  public void dixitShouldHaveAverageRating7Dot54402() {
    BoardGame game = games[13];
    assertEquals(7.54402, Double.valueOf(game.getAverageRating()));
  }

  @Test
  public void playersShouldContainMultiplePeople() {
    Player[] players = collectionBuilder.getPlayers();
    assertTrue(players.length > 1);
  }
}
