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
}
