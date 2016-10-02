package Test.Network;

import Main.Containers.BoardGameCollection;
import Main.Network.ConnectionHandler;
import com.sun.source.tree.AssertTree;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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

  @Ignore
  @Test
  public void collection_shouldReturnDocumentOnValidURL() {
    Document document = connectionHandler.getCollection(user);
    assertNotNull(document);
  }

  @Ignore
  @Test
  public void collection_returnedDocumentShouldContainAtLeastOneItem() {
    Document document = connectionHandler.getCollection(user);
    NodeList list = document.getElementsByTagName("item");
    assertTrue(list.getLength() > 0);
  }

  @Ignore
  @Test
  public void collection_shouldReturnNullInvalidURL() {
    String invalidUser = "notanusername";
    Document document = connectionHandler.getCollection(invalidUser);
    assertNull(document);
  }

  @Test
  public void game_shouldReturnNullOnInvalidID() {
    final int invalidID = 2157610;
    int[] array = new int[1];
    array[0] = invalidID;
    Document document = connectionHandler.getGames(array);
    assertNull(document);
  }

  @Test
  public void game_shouldReturnDocumentOnValidID() {
    final int validID = 2655;
    int[] array = new int[1];
    array[0] = validID;
    Document document = connectionHandler.getGames(array);
    assertNotNull(document);
  }

  @Test
  public void game_shouldReturnDocumentOnTwoValidIDs() {
    final int validID1 = 2655;
    final int validID2 = 31260;

    int[] array = new int[2];
    array[0] = validID1;
    array[1] = validID2;
    Document document = connectionHandler.getGames(array);
    assertNotNull(document);
  }

  @Test
  public void game_returnedDocumentShouldHaveTwoItem() {
    final int validID1 = 2655;
    final int validID2 = 31260;

    int[] array = new int[2];
    array[0] = validID1;
    array[1] = validID2;
    Document document = connectionHandler.getGames(array);
    NodeList list = document.getElementsByTagName("item");
    assertEquals(2, list.getLength());
  }

}
