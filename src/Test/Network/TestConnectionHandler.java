package Test.Network;

import Main.Containers.BoardGameCollection;
import Main.Network.ConnectionHandler;
import com.sun.source.tree.AssertTree;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 27/09/16.
 */
public class TestConnectionHandler {
  private String user = "cwaq";
  private ConnectionHandler connectionHandler;

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandler();
  }

  //@Ignore
  @Test
  public void collection_shouldReturnDocumentOnValidURL() {
    Document document = connectionHandler.getCollection(user);
    assertNotNull(document);
  }

  //@Ignore
  @Test
  public void collection_returnedDocumentShouldContainAtLeastOneItem() {
    Document document = connectionHandler.getCollection(user);
    NodeList list = document.getElementsByTagName("item");
    assertTrue(list.getLength() > 0);
  }

  //@Ignore
  @Test
  public void collection_shouldReturnNullInvalidURL() {
    String invalidUser = "notanusername";
    Document document = connectionHandler.getCollection(invalidUser);
    assertNull(document);
  }

  @Test
  public void game_shouldReturnNullOnInvalidID() {
    final int invalidID = 2157610;
    Document document = connectionHandler.getGame(invalidID);
    assertNull(document);
  }

 @Ignore
  @Test
  public void game_shouldReturnDocumentOnValidID() {
    final int validID = 2655;
    Document document = connectionHandler.getGame(validID);
    assertNotNull(document);
  }

}
