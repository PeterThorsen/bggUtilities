package Main.Network;

import Main.Containers.BoardGameCollection;
import Main.Containers.Boardgame;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Peter on 28/09/16.
 */
public class CollectionBuilder implements ICollectionBuilder {
  private IConnectionHandler connectionHandler;
  public CollectionBuilder(IConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  /**
   * For xml:
   *    nodeList.item(i).getChildNodes().item(1).getTextContent();
   * gives us the game name,
   *    nodeList.item(i).getChildNodes().item(9).getNodeName()
   * gives us the stats section,
   *    nodeList.item(i).getChildNodes().item(11).getAttributes().getNamedItem("own").getNodeValue()
   * gives us the "own" attribute (1 if owned) and
   *    nodeList.item(i).getChildNodes().item(13).getNodeName()
   * gives us numPlays.
   *
   *
   * @param username for the bgg user
   * @return a boardGameCollection containing a list of games including features for the games
   */
  @Override
  public BoardGameCollection getCollection(String username) {

    Document document = connectionHandler.getCollection(username);

    if(document == null) {
      // Invalid user
      return null;
    }
    
    ArrayList<Boardgame> games = buildCollection(document);
    BoardGameCollection collection = new BoardGameCollection(games);
    return collection;
  }

  private ArrayList<Boardgame> buildCollection(Document document) {
    NodeList nodeList = document.getElementsByTagName("item");
    ArrayList<Boardgame> games = new ArrayList<>();
    for(int i = 0; i<nodeList.getLength(); i++) {
      String name = nodeList.item(i).getChildNodes().item(1).getTextContent();
      String uniqueIDString = nodeList.item(i).getAttributes().item(1).getTextContent();
      int uniqueID = Integer.valueOf(uniqueIDString);
      int minPlayers;

      Boardgame game = new Boardgame(name, uniqueID, 1);
      games.add(game);
    }
    return games;
  }
}
