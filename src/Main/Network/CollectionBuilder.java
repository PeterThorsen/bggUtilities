package Main.Network;

import Main.Containers.BoardGameCollection;
import Main.Containers.Boardgame;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;

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
    int[] uniqueIDArray = new int[nodeList.getLength()];
    HashMap<Integer, Boardgame> idToGameMap = new HashMap<>();

    for(int i = 0; i<nodeList.getLength(); i++) {

      // Name
      String name = nodeList.item(i).getChildNodes().item(1).getTextContent();

      // Unique id
      String uniqueIDString = nodeList.item(i).getAttributes().item(1).getTextContent();
      int uniqueID = Integer.valueOf(uniqueIDString);
      uniqueIDArray[i] = uniqueID;

      // Minimum players
      int minPlayers;
      String minPlayersString = nodeList.item(i).getChildNodes().item(9).getAttributes().item(2).getTextContent();
      minPlayers = Integer.valueOf(minPlayersString);

      // Max players
      int maxPlayers;
      String maxPlayersString = nodeList.item(i).getChildNodes().item(9).getAttributes().item(0).getTextContent();
      maxPlayers = Integer.valueOf(maxPlayersString);

      // Min playtime
      int minPlaytime;
      String minPlaytimeString = nodeList.item(i).getChildNodes().item(9).getAttributes().item(3).getTextContent();
      minPlaytime = Integer.valueOf(minPlaytimeString);

      // Max playtime
      int maxPlaytime;
      String maxPlaytimeString = nodeList.item(i).getChildNodes().item(9).getAttributes().item(1).getTextContent();
      maxPlaytime = Integer.valueOf(maxPlaytimeString);

      // Personal rating
      String personalRatingString = nodeList.item(i).getChildNodes().item(9).getChildNodes().item(1).getAttributes().getNamedItem("value").getTextContent();
      // Returning string, as value might be N/A

      // Number of plays
      int numPlays;
      String numPlaysString = nodeList.item(i).getChildNodes().item(13).getTextContent();
      numPlays = Integer.valueOf(numPlaysString);

      Boardgame game = new Boardgame(name, uniqueID, minPlayers, maxPlayers, minPlaytime, maxPlaytime, personalRatingString, numPlays);
      games.add(game);
      game.addComplexity(2.5);

      idToGameMap.put(uniqueID, game);
    }

    // Complexity
    Document gamesDoc = connectionHandler.getGames(uniqueIDArray);
    NodeList gamesList = gamesDoc.getElementsByTagName("item");
    for(int i = 0; i<gamesList.getLength(); i++) {
      Node item = gamesList.item(i);
      int uniqueID = Integer.valueOf(item.getAttributes().getNamedItem("id").getNodeValue());

      // Finding the averageWeight node and its corresponding value
      NodeList subNodes = item.getChildNodes();
      int lengthOfSubNodes = subNodes.getLength();
      Node statisticsNode = subNodes.item(lengthOfSubNodes-2);
      Node ratingsNode = statisticsNode.getChildNodes().item(1);
      Node averageWeightNode = ratingsNode.getChildNodes().item(25);
      double complexity = Double.valueOf(averageWeightNode.getAttributes().item(0).getNodeValue());

      // Add complexity to the game
      Boardgame game = idToGameMap.get(uniqueID);
      game.addComplexity(complexity);
    }
    return games;
  }
}
