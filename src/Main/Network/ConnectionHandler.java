package Main.Network;

import Main.Containers.BoardGameCollection;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;

/**
 * Created by Peter on 27/09/16.
 */
public class ConnectionHandler {
  private String baseURL = "https://www.boardgamegeek.com/xmlapi/";

  /**
   * Used for returning a users boardgame collection for manipulation in other classes.
   *
   * @param username is the username used to create the account on bgg
   * @return a boardgame collection class containing the interpreted xml or null if no user exists by this id
   */
  public Document getCollection(String username) {
    String url = buildURL("collection", username);
    Document xmlResponseInDocument = sendRequest(url);

    if(xmlResponseInDocument.getElementsByTagName("error").getLength() > 0) {
      // Invalid user id, remember to check for null on receiving end
      return null;
    }
    return xmlResponseInDocument;
  }

  /**
   * Used to build the bgg url to avoid errors
   *
   * @param arg1 describes the category and can be:
   *             collection
   *             boardgame
   * @param arg2 specifies specific entries in this category and can be:
   *             username
   *             boardgame BGG id, can be found in it's url or inside it's entry in a users collection
   * @return built url
   */
  private String buildURL(String arg1, String arg2) {
    String url = String.format("https://www.boardgamegeek.com/xmlapi/%s/%s", arg1, arg2);
    return url;
  }

  /**
   * Lightweight request to bgg api. Returns a document containing xml.
   * @param urlString is the url for which the request will be made
   * @return a document containing xml
   */
  private Document sendRequest(String urlString) {
    try {
      // or if you prefer DOM:
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      URL url = new URL(urlString);
      Document document = db.parse(new URL(urlString).openStream());
      return document;

      /**
       *

       NodeList hi = document.getElementsByTagName("item");
       for(int i = 0; i< hi.getLength(); i++) {
       System.out.println(hi.item(i).getChildNodes().item(1).getTextContent());
       */

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
