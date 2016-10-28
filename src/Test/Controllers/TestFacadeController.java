package Test.Controllers;

import Main.Controllers.FacadeController;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.CollectionBuilderStub;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;


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
    assertTrue(facadeController.getAllGames().size() > 0);
  }

  @Test
  public void shouldReturnSizeOfGamesList() {
    int size = facadeController.getNumberOfGames();
    assertEquals(2, size);
  }


  @Test
  public void shouldReturnAllGameNames() {
    String[] gameNames = facadeController.getGameNames();
    assertEquals("Agricola", gameNames[0]);
    assertEquals("Hive", gameNames[1]);
  }

  @Test
  public void shouldReturnAllMinimumLengths() {
    int[] minLengths = facadeController.getMinLengths();
    assertEquals(30, minLengths[0]);
    assertEquals(20, minLengths[1]);
  }

  @Test
  public void shouldReturnAllMaximumLengths() {
    int[] minLengths = facadeController.getMaxLengths();
    assertEquals(150, minLengths[0]);
    assertEquals(20, minLengths[1]);
  }

  @Test
  public void shouldReturnAllComplexities() {
    double[] minLengths = facadeController.getComplexities();
    assertEquals(2.3453, minLengths[0]);
    assertEquals(3.6298, minLengths[1]);
  }

}
