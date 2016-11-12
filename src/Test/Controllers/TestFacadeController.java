package Test.Controllers;

import Main.Containers.Play;
import Main.Containers.Player;
import Main.Controllers.FacadeController;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.CollectionBuilderStub;
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
    assertTrue(facadeController.getAllGames().length > 0);
  }

  @Test
  public void shouldReturnSizeOfGamesList() {
    int size = facadeController.getNumberOfGames();
    assertEquals(2, size);
  }


  @Test
  public void shouldReturnAllGameNames() {
    String[] gameNames = facadeController.getAllGameNames();
    assertEquals("Agricola", gameNames[0]);
    assertEquals("Hive", gameNames[1]);
  }

  @Test
  public void shouldReturnAllMinimumLengths() {
    int[] minLengths = facadeController.getAllMinLengths();
    assertEquals(30, minLengths[0]);
    assertEquals(20, minLengths[1]);
  }

  @Test
  public void shouldReturnAllMaximumLengths() {
    int[] minLengths = facadeController.getAllMaxLengths();
    assertEquals(150, minLengths[0]);
    assertEquals(20, minLengths[1]);
  }

  @Test
  public void shouldReturnAllComplexities() {
    double[] minLengths = facadeController.getAllComplexities();
    assertEquals(2.3453, minLengths[0]);
    assertEquals(3.6298, minLengths[1]);
  }

  @Test
  public void shouldReturnAllMinPlayers() {
    int[] minPlayers = facadeController.getAllMinPlayers();
    assertEquals(1, minPlayers[0]);
    assertEquals(2, minPlayers[1]);
  }

  @Test
  public void shouldReturnAllMaxPlayers() {
    int[] maxPlayers = facadeController.getAllMaxPlayers();
    assertEquals(5, maxPlayers[0]);
    assertEquals(2, maxPlayers[1]);
  }

  @Test
  public void shouldReturnNumPlays() {
    int[] numPlays = facadeController.getAllNumberOfPlays();
    assertEquals(0, numPlays[0]);
    assertEquals(1, numPlays[1]);
  }

  @Test
  public void shouldReturnPersonalRating() {
    String[] personalRating = facadeController.getAllPersonalRatings();
    assertEquals(8, Integer.parseInt(personalRating[0]));
    assertEquals(10, Integer.parseInt(personalRating[1]));
  }

  @Test
  public void shouldReturnAverageRating() {
    String[] averageRatings = facadeController.getAllAverageRatings();
    assertEquals(8.07978, Double.parseDouble(averageRatings[0]));
    assertEquals(7.34394, Double.parseDouble(averageRatings[1]));
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
