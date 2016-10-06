package Test.Models.Network;

import Main.Models.Network.ConnectionHandler;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 27/09/16.
 */
//@Ignore // Remove for verifying, but avoid spamming bgg
public class TestConnectionHandler {
  private String user = "cwaq";
  private ConnectionHandler connectionHandler;

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandler();
  }

  @Test
  public void collection_shouldReturnDocumentOnValidURL() {
    Document document = connectionHandler.getCollection(user);
    assertNotNull(document);
  }

  @Test
  public void collection_returnedDocumentShouldContainAtLeastOneItem() {
    Document document = connectionHandler.getCollection(user);
    NodeList list = document.getElementsByTagName("item");
    assertTrue(list.getLength() > 0);
  }

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

  @Test
  public void plays_invalidUsernameShouldReturnNull() {
    String username = "notanusername";
    Document document = connectionHandler.getPlays(username);
    assertNull(document);
  }

  @Test
  public void plays_validUsernameShouldReturnDocument() {
    String username = "cwaq";
    Document document = connectionHandler.getPlays(username);
    assertNotNull(document);
  }

  @Test
  public void plays_validUsernameShouldReturnDocumentWithLengthMoreThan0() {
    String username = "cwaq";
    Document document = connectionHandler.getPlays(username);
    assertTrue(document.getElementsByTagName("play").getLength() > 0);
  }

}
