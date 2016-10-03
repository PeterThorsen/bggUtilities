package Main.Network;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

/**
 * Created by Peter on 27/09/16.
 */
public class ConnectionHandler implements IConnectionHandler {
  private String baseURL = "https://www.boardgamegeek.com/xmlapi/";

  /**
   * Used for returning a users boardgame collection for manipulation in other classes.
   *
   * @param username is the username used to create the account on bgg
   * @return a boardgame collection class containing the interpreted xml or null if no user exists by this id
   */
  @Override
  public Document getCollection(String username) {
    String url = buildURL("collection", username);
    Document xmlResponseInDocument = sendRequest(url);

    if (xmlResponseInDocument.getElementsByTagName("error").getLength() > 0) {
      // Invalid user id, remember to check for null on receiving end
      return null;
    }
    return xmlResponseInDocument;
  }

  @Override
  public Document getGames(int[] gameIDArray) {
    String url = buildURLForGames(gameIDArray);
    Document xmlResponseInDocument = sendRequest(url);
    if (xmlResponseInDocument.getElementsByTagName("item").getLength() == 0) {
      return null;
    }
    return xmlResponseInDocument;
  }

  @Override
  public Document getPlays(String username) {
    String url = buildURL("plays", username);
    Document xmlResponseInDocument = sendRequest(url);
    if(xmlResponseInDocument.getElementsByTagName("play").getLength() == 0) {
      // No plays found for username. Either because of wrong username or no plays logged.
      return null;
    }
    return xmlResponseInDocument;
  }

  /**
   * Used to build the bgg url to avoid errors. Works for collection and plays.
   *
   * @param category describes the category and can be:
   *                 collection
   *                 plays
   * @param username is the username for which the collection or plays should be fetched.
   * @return built url
   */
  private String buildURL(String category, String username) {
    String url = String.format("https://www.boardgamegeek.com/xmlapi2/%s?username=%s", category, username);
    return url;
  }

  /**
   * Used to build the bgg url to avoid errors. Works for "things" which is the bgg name for games.
   * Always returns a game including the stats section.
   *
   * @param gameIDArray is the array containing a list of unique IDs for games on bgg.
   * @return built url
   */
  private String buildURLForGames(int[] gameIDArray) {
    StringBuilder sb = new StringBuilder();
    sb.append("https://www.boardgamegeek.com/xmlapi2/thing?stats=1&id=");
    sb.append(gameIDArray[0]);

    for (int i = 1; i < gameIDArray.length; i++) {
      sb.append("," + gameIDArray[i]);
    }

    return sb.toString();
  }

  /**
   * Lightweight request to bgg api. Returns a document containing xml.
   *
   * @param urlString is the url for which the request will be made
   * @return a document containing xml
   */
  private Document sendRequest(String urlString) {
    Document document = null;
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      URL url = new URL(urlString);
      document = db.parse(url.openStream());
      return document;

    } catch (Exception e) {
    }
    return document;
  }
}
