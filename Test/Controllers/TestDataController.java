package Controllers;

import Controller.SubControllers.DataDisplayController;
import Model.Storage.ICollectionBuilder;
import Model.Structure.BoardGame;
import Model.Structure.BoardGameCollection;
import Model.Structure.Play;
import Models.StubsAndMocks.CollectionBuilderStub;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 03/10/16.
 */
public class TestDataController {

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
    BoardGame[] games = controller.getAllGames();
    assertNotNull(games);
    assertTrue(games.length > 0);
  }

  @Test
  public void controllerShouldReturnListOfGamesContainingHive() {
    BoardGame[] games = controller.getAllGames();

    boolean foundHive = false;

    for(BoardGame game : games) {
      if(game.name.equals("Hive")) {
        foundHive = true;
      }
    }
    assertTrue(foundHive);
  }

  @Test
  public void controllerShouldReturnListOfGamesContainingAgricola() {
    BoardGame[] games = controller.getAllGames();

    boolean foundAgricola = false;

    for(BoardGame game : games) {
      if(game.name.equals("Agricola")) {
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
  public void controllerShouldReturnOnePlayOfAgricolaGivenID() {
    int uniqueID = 31260;
    ArrayList<Play> plays = controller.getPlays(uniqueID);
    assertTrue(plays.size() == 1);
  }

  @Test
  public void controllerShouldReturnOnePlayOfAgricolaGivenName() {
    String name = "Agricola";
    ArrayList<Play> plays = controller.getPlays(name);
    assertTrue(plays.size() == 1);
  }

  @Test
  public void givenUnusedID_ReturnZeroPlays() {
    int uniqueID = 1;
    ArrayList<Play> plays = controller.getPlays(uniqueID);
    assertTrue(plays.size() == 0);
  }

  @Test
  public void givenUnusedName_ReturnZeroPlays() {
    String name = "NotAGameName";
    ArrayList<Play> plays = controller.getPlays(name);
    assertTrue(plays.size() == 0);
  }


}
