package Model.Network;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Peter on 27/09/16.
 */
public class ConnectionHandler implements IConnectionHandler {

  private boolean firstTime = true;

  /**
   * Used for returning a users boardgame collection for manipulation in other classes.
   *
   * @param username is the username used to create the account on bgg
   * @return a boardgame collection class containing the interpreted xml or null if no user exists by this id
   */
  @Override
  public Document getCollection(String username) {
    String url = buildCollectionURL(username);
    Document xmlResponseInDocument = sendRequest(url);

    // TODO: 03/02/2017 will return null if bad connection
    if (xmlResponseInDocument.getElementsByTagName("error").getLength() > 0) {
      // Invalid user id, remember to check for null on receiving end
      return null;
    }

    // Below section fixes bug caused by bgg's API when loading an user for the first time.
    if (xmlResponseInDocument.getElementsByTagName("item").getLength() == 0) {
      if (!firstTime) {
        return null;
      }
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        firstTime = false;
      }
      return getCollection(username);
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
  public ArrayList<Document> getPlays(String username) {
    String url = buildPlaysURL(username, 1);
    Document xmlResponseInDocument = sendRequest(url);
    if (xmlResponseInDocument.getElementsByTagName("play").getLength() == 0) {
      // No plays found for username. Either because of wrong username or no plays logged.
      return null;
    }
    ArrayList<Document> allPages = new ArrayList<>();
    allPages.add(xmlResponseInDocument);
    if (xmlResponseInDocument.getElementsByTagName("play").getLength() != 100) {
      return allPages;
    }

    int counter = 2;
    while (true) {
      url = buildPlaysURL(username, counter);
      Document nextPage = sendRequest(url);
      allPages.add(nextPage);
      if (nextPage.getElementsByTagName("play").getLength() == 0 || nextPage.getElementsByTagName("play").getLength() < 100) {
        break;
      }
      counter++;
    }
    return allPages;
  }


  /**
   * Used to build the bgg url to avoid errors. Works for collection and plays.
   *
   * @param username is the username for which the collection or plays should be fetched.
   * @return built url
   */
  private String buildPlaysURL(String username, int pageNumber) {
    return String.format("https://www.boardgamegeek.com/xmlapi2/plays?username=%s&page=%d", username, pageNumber);
  }

  /**
   * Used to build the bgg url to avoid errors. Works for collection and plays.
   *
   * @param username is the username for which the collection or plays should be fetched.
   * @return built url
   */
  private String buildCollectionURL(String username) {
    return String.format("https://www.boardgamegeek.com/xmlapi2/collection?stats=1&username=%s", username);
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
      sb.append(",").append(gameIDArray[i]);
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
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      URL url = new URL(urlString);
      Document document = db.parse(url.openStream());
      return document;

    } catch (Exception e) {
      return null;
    }
  }
}
