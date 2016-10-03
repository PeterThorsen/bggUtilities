package Test.Controllers;

import Main.Containers.BoardGameCollection;
import Main.Controllers.DataDisplayController;
import Main.Network.ICollectionBuilder;
import Main.Network.IConnectionHandler;
import Test.StubsAndMocks.CollectionBuilderStub;
import Test.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

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
    controller = new DataDisplayController(collectionBuilder);
  }

  @Test
  public void collectionBuilderStubShouldReturnCollection() {
    BoardGameCollection collection = collectionBuilder.getCollection("cwaq");
    assertNotNull(collection);
  }

}
