package Test;

import Containers.BoardGameCollection;
import Main.ConnectionHandler;
import org.junit.*;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 27/09/16.
 */
public class TestConnectionHandler {

  // Run once, e.g. Database connection, connection pool
  @BeforeClass
  public static void runOnceBeforeClass() {
    // TODO
    // get URL once from server after having tested it below
  }

  @Before
  public void setUp() {

  }

  @Test
  public void shouldReturnCollectionOnValidURL() {
    String user = "cwaq";
    ConnectionHandler connectionHandler = new ConnectionHandler();
    BoardGameCollection collection = connectionHandler.getCollection(user);
    assertNotNull(collection);
  }

  @Test
  public void shouldNotReturnCollectionOnInvalidURL() {
    String invalidUser = "notanusername";
    ConnectionHandler connectionHandler = new ConnectionHandler();
    BoardGameCollection collection = connectionHandler.getCollection(invalidUser);
    assertNull(collection);
  }

}
