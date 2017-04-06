package Models.Network;

import Main.Model.Structure.BoardGameCollection;
import Main.Model.Network.ConnectionHandler;
import Main.Model.Network.IConnectionHandler;
import Main.Model.Storage.CollectionBuilder;
import Main.Model.Storage.ICollectionBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * Created by Peter on 28/09/16.
 */
public class TestCollectionBuilder_Network {
  IConnectionHandler connectionHandler;
  ICollectionBuilder collectionBuilder;

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandler();
    collectionBuilder = new CollectionBuilder(connectionHandler);
  }

  @Test
  public void shouldReturnBoardGameCollectionOnValidUsername() {
    String user = "cwaq";
    BoardGameCollection collection = collectionBuilder.getCollection(user);
    assertNotNull(collection);
  }

  @Test
  public void shouldReturnNullOnInvalidUsername() {
    String invalidUser = "notanusername";
    BoardGameCollection collection = collectionBuilder.getCollection(invalidUser);
    assertNull(collection);
  }
}
