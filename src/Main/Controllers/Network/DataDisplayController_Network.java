package Main.Controllers.Network;

import Main.Controllers.DataDisplayController;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.CollectionBuilderStub;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Peter on 05/10/2016.
 */
public class DataDisplayController_Network {


  private DataDisplayController controller;
  private ICollectionBuilder collectionBuilder;

  @Before
  public void setUp() {
    ConnectionHandler connectionHandler = new ConnectionHandler();
    collectionBuilder = new CollectionBuilder(connectionHandler);
  }

  @Test
  public void controllerShouldVerifyCorrectUsername() {
    String username="cwaq";
    controller = new DataDisplayController(collectionBuilder, username);
    assertTrue(controller.verifyUser());
  }
}
