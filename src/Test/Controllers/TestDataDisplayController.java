package Test.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Play;
import Main.Controllers.DataDisplayController;
import Main.Factories.IStartupFactory;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.CollectionBuilderStub;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;
import static junit.framework.TestCase.*;

/**
 * Created by Peter on 03/10/16.
 */
public class TestDataDisplayController {

  private DataDisplayController controller;
  private ICollectionBuilder collectionBuilder;

  @Before
  public void setUp() {
    collectionBuilder = new CollectionBuilderStub();
    controller = new DataDisplayController(collectionBuilder, "cwaq");
  }

  @Test
  public void collectionBuilderStub_ShouldReturnCollection() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    assertNotNull(collection);
  }

  @Test
  public void controllerShouldReturnListOfGameNames() {
    String[] gameNames = controller.getGameNames();
    assertNotNull(gameNames);
  }

  @Test
  public void controllerShouldReturnListOfGamesOfSize2() {
    int size = controller.getNumberOfGames();
    assertEquals(2, size);
  }

  @Test
  public void controllerShouldReturnListOfGames() {
    ArrayList<BoardGame> games = controller.getAllGames();
    assertNotNull(games);
  }

  @Test
  public void controllerShouldReturnListOfGamesContainingHive() {
    ArrayList<BoardGame> games = controller.getAllGames();

    boolean foundHive = false;

    for(BoardGame game : games) {
      if(game.getName().equals("Hive")) {
        foundHive = true;
      }
    }
    assertTrue(foundHive);
  }

  @Test
  public void controllerShouldReturnListOfGamesContainingAgricola() {
    ArrayList<BoardGame> games = controller.getAllGames();

    boolean foundAgricola = false;

    for(BoardGame game : games) {
      if(game.getName().equals("Agricola")) {
        foundAgricola = true;
      }
    }
    assertTrue(foundAgricola);
  }

  @Test
  public void controllerShouldReturnAllPlaysOfHiveGivenID() {
    int uniqueID = 2655;
    ArrayList<Play> plays = controller.getPlays(uniqueID);
    assertTrue(plays.size() > 0);
  }

  @Test
  public void controllerShouldReturnAllPlaysOfHiveGivenName() {
    String name = "Hive";
    ArrayList<Play> plays = controller.getPlays(name);
    assertTrue(plays.size() > 0);
  }

  @Test
  public void controllerShouldReturnZeroPlaysOfAgricolaGivenID() {
    int uniqueID = 31260;
    ArrayList<Play> plays = controller.getPlays(uniqueID);
    assertTrue(plays.size() == 0);
  }

  @Test
  public void controllerShouldReturnZeroPlaysOfAgricolaGivenName() {
    String name = "Agricola";
    ArrayList<Play> plays = controller.getPlays(name);
    assertTrue(plays.size() == 0);
  }


}
