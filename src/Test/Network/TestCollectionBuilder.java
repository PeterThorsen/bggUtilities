package Test.Network;

import Main.Containers.BoardGameCollection;
import Main.Containers.Boardgame;
import Main.Network.CollectionBuilder;
import Main.Network.ConnectionHandler;
import Main.Network.ICollectionBuilder;
import Main.Network.IConnectionHandler;
import Test.StubsAndMocks.ConnectionHandlerMock;
import org.junit.*;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 28/09/16.
 */
public class TestCollectionBuilder {
  IConnectionHandler connectionHandler;
  ICollectionBuilder collectionBuilder;
  BoardGameCollection collection;
  ArrayList<Boardgame> games;

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandlerMock();
    collectionBuilder = new CollectionBuilder(connectionHandler);
    collection = collectionBuilder.getCollection("cwaq");
    games = collection.getGames();
  }

  @Ignore // To avoid spamming bgg. Can be removed for tests.
  @Test
  public void shouldReturnBoardGameCollectionOnValidUsername() {
    connectionHandler = new ConnectionHandler();
    collectionBuilder = new CollectionBuilder(connectionHandler);
    String user = "cwaq";
    BoardGameCollection collection = collectionBuilder.getCollection(user);
    assertNotNull(collection);
  }

  @Ignore // To avoid spamming bgg. Can be removed for tests.
  @Test
  public void shouldReturnNullOnInvalidUsername() {
    String invalidUser = "notanusername";
    connectionHandler = new ConnectionHandler();
    collectionBuilder = new CollectionBuilder(connectionHandler);
    BoardGameCollection collection = collectionBuilder.getCollection(invalidUser);
    assertNull(collection);
  }

  @Test
  public void connectionHandlerMockShouldWork() {
    assertNotNull(collection);
  }

  @Test
  public void collectionShouldContainArrayListOfBoardgames() {
    assertNotNull(games);
  }

  @Test
  public void collectionShouldContainNonEmptyListOfBoardGames() {
    assertTrue(games.size() > 0);
  }

  @Test
  public void collectionShouldContainAgricola() {
    boolean agricolaExists = false;

    for (Boardgame game : games) {
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

    for (Boardgame game : games) {
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

    for (Boardgame game : games) {
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
    Boardgame game = games.get(0);
    uniqueID = game.getID();
    assertEquals(31260, uniqueID);
  }

  @Test
  public void hiveShouldHaveUniqueID2655() {
    int uniqueID = 0;
    Boardgame game = games.get(20);
    uniqueID = game.getID();
    assertEquals(2655, uniqueID);
  }

  @Test
  public void agricolaShouldHaveMinPlayers1() {
    int minPlayers = 0;
    Boardgame game = games.get(0);
    minPlayers = game.getMinPlayers();
    assertEquals(1, minPlayers);
  }

  @Test
  public void hiveShouldHaveMinPlayers2() {
    int minPlayers = 0;
    Boardgame game = games.get(20);
    minPlayers = game.getMinPlayers();
    assertEquals(2, minPlayers);
  }

  @Test
  public void hiveShouldHaveMaxPlayers2() {
    int maxPlayers = 0;
    Boardgame game = games.get(20);
    maxPlayers = game.getMaxPlayers();
    assertEquals(2, maxPlayers);
  }

  @Test
  public void agricolaShouldHaveMaxPlayers5() {
    int maxPlayers = 0;
    Boardgame game = games.get(0);
    maxPlayers = game.getMaxPlayers();
    assertEquals(5, maxPlayers);
  }

  @Test
  public void agricolaShouldHaveMinPlaytime30Min() {
    int minPlaytime = 0;
    Boardgame game = games.get(0);
    minPlaytime = game.getMinPlaytime();
    assertEquals(30, minPlaytime);
  }

  @Test
  public void agricolaShouldHaveMaxPlaytime150Min() {
    int maxPlaytime = 0;
    Boardgame game = games.get(0);
    maxPlaytime = game.getMaxPlaytime();
    assertEquals(150, maxPlaytime);
  }

  @Test
  public void agricolaShouldHavePersonalRating8() {
    String personalRating = "8";
    Boardgame game = games.get(0);
    personalRating = game.getPersonalRating();
    int rating = Integer.valueOf(personalRating);
    assertEquals(8, rating);
  }

  @Test
  public void hiveShouldHavePersonalRating10() {
    String personalRating = "";
    Boardgame game = games.get(20);
    personalRating = game.getPersonalRating();
    int rating = Integer.valueOf(personalRating);
    assertEquals(10, rating);
  }

  @Test
  public void agricolaShouldHaveNumPlays0() {
    int numPlays = -5;
    Boardgame game = games.get(0);
    numPlays = game.getNumberOfPlays();
    assertEquals(0, numPlays);

  }

  @Test
  public void hiveShouldHaveNumPlays1() {
    int numPlays = 0;
    Boardgame game = games.get(20);
    numPlays = game.getNumberOfPlays();
    assertEquals(1, numPlays);
  }



}
