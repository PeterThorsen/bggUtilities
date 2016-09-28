package Test;

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

  @Test
  public void shouldReturnDocumentOnValidURL() {
    ConnectionHandler connectionHandler = new ConnectionHandler();
    Document document = connectionHandler.getCollection(user);
    assertNotNull(document);
  }

  @Test
  public void returnedDocumentShouldContainAtLeastOneItem() {
    ConnectionHandler connectionHandler = new ConnectionHandler();
    Document document = connectionHandler.getCollection(user);
    NodeList list = document.getElementsByTagName("item");
    assertTrue(list.getLength() > 0);
  }

  @Test
  public void shouldReturnNullInvalidURL() {
    String invalidUser = "notanusername";
    ConnectionHandler connectionHandler = new ConnectionHandler();
    Document document = connectionHandler.getCollection(invalidUser);
    assertNull(document);
  }

}
