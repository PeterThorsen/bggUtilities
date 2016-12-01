package Controllers;

import Main.Containers.BoardGame;
import Main.Containers.Play;
import Main.Containers.Player;
import Main.Controllers.FacadeController;
import Main.Models.Storage.ICollectionBuilder;
import Models.StubsAndMocks.CollectionBuilderStub;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


/**
 * Created by Peter on 07/10/2016.
 */
public class TestFacadeController {

  private FacadeController facadeController;

  @Before
  public void setUp() {
    ICollectionBuilder collectionBuilder = new CollectionBuilderStub();
    String username = "cwaq";
    facadeController = new FacadeController(collectionBuilder, username);
  }

  @Test
  public void shouldReturnListOfGames() {
    BoardGame[] allGames = facadeController.getAllGames();
    assertTrue(allGames.length == 2);
  }

  @Test
  public void listOfGamesShouldContain() {
    BoardGame[] allGames = facadeController.getAllGames();
    assertEquals("Agricola", allGames[0].getName());
    assertEquals("Hive", allGames[1].getName());
  }

  @Test
  public void shouldReturnAllPlayers() {
    Player[] players = facadeController.getAllPlayers();
    assertEquals("Peter", players[0].name);
  }

  @Test
  public void shouldReturnListOfAllPlaysSorted() {
    Play[] allPlays = facadeController.getAllPlaysSorted();
    assertEquals("2016-09-14", allPlays[0].getDate());
    assertEquals("2016-09-13", allPlays[1].getDate());
  }

}