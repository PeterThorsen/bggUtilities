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

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandlerMock();
    collectionBuilder = new CollectionBuilder(connectionHandler);
  }

  @Test
  public void shouldReturnBoardGameCollectionOnValidUsername() {
    connectionHandler = new ConnectionHandler();
    collectionBuilder = new CollectionBuilder(connectionHandler);
    String user = "cwaq";
    BoardGameCollection collection = collectionBuilder.getCollection(user);
    assertNotNull(collection);
  }

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
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    assertNotNull(collection);
  }

  @Test
  public void collectionShouldContainArrayListOfBoardgames() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    assertNotNull(games);
  }

  @Test
  public void collectionShouldContainNonEmptyListOfBoardGames() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    assertTrue(games.size() > 0);
  }

  @Test
  public void collectionShouldContainAgricola() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    boolean agricolaExists = false;

    for(Boardgame game : games) {
      if(game.getName().equals("Agricola")) {
        agricolaExists = true;
        break;
      }
    }
    assertTrue(agricolaExists);
  }

  @Test
  public void collectionShouldContainHive() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    boolean hiveExists = false;

    for(Boardgame game : games) {
      if(game.getName().equals("Hive")) {
        hiveExists = true;
        break;
      }
    }
    assertTrue(hiveExists);
  }

  @Test
  public void collectionShouldNotContainMonopoly() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    boolean monopolyExists = false;

    for(Boardgame game : games) {
      if(game.getName().equals("Monopoly")) {
        monopolyExists = true;
        break;
      }
    }
    assertFalse(monopolyExists);
  }

  @Test
  public void agricolaShouldHaveUniqueID31260() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    int uniqueID = 0;

    for(Boardgame game : games) {
      if(game.getName().equals("Agricola")) {
        uniqueID = game.getID();
      }
    }
    assertEquals(31260, uniqueID);
  }

  @Test
  public void HiveShouldHaveUniqueID2655() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    ArrayList<Boardgame> games = collection.getGames();
    int uniqueID = 0;

    for(Boardgame game : games) {
      if(game.getName().equals("Hive")) {
        uniqueID = game.getID();
      }
    }
    assertEquals(2655, uniqueID);
  }

}
