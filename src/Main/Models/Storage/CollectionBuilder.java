package Main.Models.Storage;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Play;
import Main.Models.Network.IConnectionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Peter on 28/09/16.
 */
public class CollectionBuilder implements ICollectionBuilder {
  private IConnectionHandler connectionHandler;
  private String username;
  private Document collectionDocument;

  public CollectionBuilder(IConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  /**
   * For xml:
   * nodeList.item(i).getChildNodes().item(1).getTextContent();
   * gives us the game name,
   * nodeList.item(i).getChildNodes().item(9).getNodeName()
   * gives us the stats section,
   * nodeList.item(i).getChildNodes().item(11).getAttributes().getNamedItem("own").getNodeValue()
   * gives us the "own" attribute (1 if owned) and
   * nodeList.item(i).getChildNodes().item(13).getNodeName()
   * gives us numPlays.
   *
   * @param username for the bgg user
   * @return a boardGameCollection containing a list of games including features for the games
   */
  @Override
  public BoardGameCollection getCollection(String username) {
    this.username = username;

    if (collectionDocument == null) {
      collectionDocument = connectionHandler.getCollection(username);

      if (collectionDocument == null) {
        // Invalid user
        return null;
      }
    }
    ArrayList<BoardGame> games = buildCollection(collectionDocument);
    BoardGameCollection collection = new BoardGameCollection(games);
    return collection;
  }

  @Override
  public boolean verifyUser(String username) {
    collectionDocument = connectionHandler.getCollection(username);
    if (collectionDocument == null) {
      return false;
    }
    return true;
  }

  private ArrayList<BoardGame> buildCollection(Document document) {
    NodeList nodeList = document.getElementsByTagName("item");
    ArrayList<BoardGame> games = new ArrayList<>();
    int[] uniqueIDArray = new int[nodeList.getLength()];
    HashMap<Integer, BoardGame> idToGameMap = new HashMap<>();

    // Creating games with basic information
    for (int i = 0; i < nodeList.getLength(); i++) {

      // Name
      String name = nodeList.item(i).getChildNodes().item(1).getTextContent();

      // Unique id
      String uniqueIDString = nodeList.item(i).getAttributes().item(1).getTextContent();
      int uniqueID = Integer.valueOf(uniqueIDString);
      uniqueIDArray[i] = uniqueID;

      // Minimum players
      int minPlayers;
      try {
        String minPlayersString = nodeList.item(i).getChildNodes().item(9).getAttributes().getNamedItem("minplayers").getTextContent();
        minPlayers = Integer.valueOf(minPlayersString);
      }
      catch (NullPointerException e) {
        minPlayers = 0;
      }

      // Max players
      int maxPlayers;
      try {
        String maxPlayersString = nodeList.item(i).getChildNodes().item(9).getAttributes().getNamedItem("maxplayers").getTextContent();
        maxPlayers = Integer.valueOf(maxPlayersString);

        // If only maxPlayers was filled on bgg by game creator
        if(minPlayers == 0) {
          minPlayers = maxPlayers;
        }
      }
      catch (NullPointerException e) {
        maxPlayers = minPlayers;
      }

      // Min playtime
      int minPlaytime;
      try {
        String minPlaytimeString = nodeList.item(i).getChildNodes().item(9).getAttributes().getNamedItem("minplaytime").getTextContent();
        minPlaytime = Integer.valueOf(minPlaytimeString);
      }
      catch (NullPointerException e) {
        minPlaytime = 0;
      }

      // Max playtime
      int maxPlaytime;
      try {
        String maxPlaytimeString = nodeList.item(i).getChildNodes().item(9).getAttributes().getNamedItem("maxplaytime").getTextContent();
        maxPlaytime = Integer.valueOf(maxPlaytimeString);

        // If only maxPlaytime was filled on bgg by game creator
        if(minPlaytime == 0) {
          minPlaytime = maxPlaytime;
        }
      }
      catch (NullPointerException e) {
        maxPlaytime = minPlaytime;
      }

      // Personal rating
      String personalRatingString = nodeList.item(i).getChildNodes().item(9).getChildNodes().item(1).getAttributes().getNamedItem("value").getTextContent(); // // TODO: 29-Oct-16  
      // Returning string, as value might be N/A

      // Number of plays
      int numPlays;
      String numPlaysString = nodeList.item(i).getChildNodes().item(13).getTextContent(); // // TODO: 29-Oct-16
      numPlays = Integer.valueOf(numPlaysString);

      BoardGame game = new BoardGame(name, uniqueID, minPlayers, maxPlayers, minPlaytime, maxPlaytime, personalRatingString, numPlays);
      games.add(game);
      game.addComplexity(2.5);

      idToGameMap.put(uniqueID, game);
    }

    if(uniqueIDArray.length == 0) {
      return games;
    }

    // Complexity
    Document gamesDoc = connectionHandler.getGames(uniqueIDArray);
    NodeList gamesList = gamesDoc.getElementsByTagName("item");
    for (int i = 0; i < gamesList.getLength(); i++) {
      Node item = gamesList.item(i);
      int uniqueID = Integer.valueOf(item.getAttributes().getNamedItem("id").getNodeValue());

      // Finding the averageWeight node and its corresponding value
      NodeList subNodes = item.getChildNodes();
      int lengthOfSubNodes = subNodes.getLength();
      Node statisticsNode = subNodes.item(lengthOfSubNodes - 2);
      Node ratingsNode = statisticsNode.getChildNodes().item(1);
      Node averageWeightNode = ratingsNode.getChildNodes().item(25);
      double complexity = Double.valueOf(averageWeightNode.getAttributes().item(0).getNodeValue());

      // Add complexity to the game
      BoardGame game = idToGameMap.get(uniqueID);
      game.addComplexity(complexity);
    }


    // Adding specific plays
    Document playsDoc = connectionHandler.getPlays(username);

    // If no plays are registered
    if(playsDoc == null) {
      return games;
    }
    NodeList playsList = playsDoc.getElementsByTagName("play");
    for (int i = 0; i < playsList.getLength(); i++) {
      Node playNode = playsList.item(i);
      NodeList playChildren = playNode.getChildNodes();
      Node gameInfo = playChildren.item(1);

      // Get board game
      int uniqueID = Integer.valueOf(gameInfo.getAttributes().getNamedItem("objectid").getNodeValue());
      BoardGame game = idToGameMap.get(uniqueID);

      // Get date and quantity
      NamedNodeMap playAttributes = playNode.getAttributes();
      String date = playAttributes.getNamedItem("date").getNodeValue();
      int quantity = Integer.valueOf(playAttributes.getNamedItem("quantity").getNodeValue());

      // Get players
      Node playerInfo = playChildren.item(3);
      NodeList playersNode = playerInfo.getChildNodes();
      int j = 1;
      String[] playerNames = new String[(playersNode.getLength() - 1) / 2];
      int arrayPos = 0;
      while (j < playersNode.getLength()) {
        Node playerJ = playersNode.item(j);
        String playerJName = playerJ.getAttributes().getNamedItem("name").getNodeValue();
        playerNames[arrayPos] = playerJName;
        arrayPos++;
        j += 2;
      }

      // Adding the plays
      Play play = new Play(date, playerNames, quantity);
      game.addPlay(play);
    }

    return games;
  }
}
