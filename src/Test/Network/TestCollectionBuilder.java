package Test.Network;

import Main.Containers.BoardGameCollection;
import Main.Network.CollectionBuilder;
import Main.Network.ConnectionHandler;
import Main.Network.ICollectionBuilder;
import Main.Network.IConnectionHandler;
import Test.StubsAndMocks.ConnectionHandlerMock;
import org.junit.*;
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
}
