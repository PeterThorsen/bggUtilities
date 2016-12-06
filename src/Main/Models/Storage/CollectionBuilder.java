package Main.Models.Storage;

import Main.Containers.*;
import Main.Models.Network.IConnectionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Peter on 28/09/16.
 */
public class CollectionBuilder implements ICollectionBuilder {
  private final IConnectionHandler connectionHandler;
  private String username;
  private Document collectionDocument;
  private final Plays plays;
  private Player[] players = null;

  public CollectionBuilder(IConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
    plays = new Plays();
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
    return new BoardGameCollection(games);
  }

  @Override
  public boolean verifyUser(String username) {
    collectionDocument = connectionHandler.getCollection(username);
    if (collectionDocument == null) {
      return false;
    }
    return true;
  }

  public Plays getPlays() {
    return plays;
  }

  @Override
  public Player[] getPlayers() {

    Play[] allPlays = plays.getAllPlaysSorted();
    HashMap<String, ArrayList<Play>> map = new HashMap<>();
    int totalPersons = 0;

    // Register plays for each specific person
    for (Play play : allPlays) {
      String[] currentPlayers = play.playerNames;
      for (String name : currentPlayers) {
        if (map.containsKey(name)) {
          ArrayList<Play> specificPersonPlays = map.get(name);
          specificPersonPlays.add(play);
          map.put(name, specificPersonPlays);
        } else {
          ArrayList<Play> specificPersonPlays = new ArrayList<>();
          specificPersonPlays.add(play);
          map.put(name, specificPersonPlays);
          totalPersons++;
        }
      }
    }

    // Convert map to Player[]
    Player[] allPlayers = new Player[totalPersons];
    int pos = 0;
    for (String key : map.keySet()) {
      ArrayList<Play> specificPersonPlays = map.get(key);
      Play[] asArray = new Play[specificPersonPlays.size()];
      for (int i = 0; i < asArray.length; i++) {
        asArray[i] = specificPersonPlays.get(i);
      }
      allPlayers[pos] = new Player(key, asArray);
      pos++;
    }

    return allPlayers;
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

      // Finding the stats and numPlays nodes
      int itemPos = 1;
      Node statsNode = null;
      Node numPlaysNode = null;
      while (true) {
        try {
          String temp = nodeList.item(i).getChildNodes().item(itemPos).getNodeName();
          if (temp.equals("stats")) {
            statsNode = nodeList.item(i).getChildNodes().item(itemPos);
          } else if (temp.equals("numplays")) {
            numPlaysNode = nodeList.item(i).getChildNodes().item(itemPos);
          }
          itemPos += 2;
        } catch (Exception e) {
          break;
        }
      }


      // Minimum plays
      int minPlayers;
      try {
        String minPlayersString = statsNode.getAttributes().getNamedItem("minplayers").getTextContent();
        minPlayers = Integer.valueOf(minPlayersString);
      } catch (NullPointerException e) {
        minPlayers = 0;
      }

      // Max plays
      int maxPlayers;
      try {
        String maxPlayersString = statsNode.getAttributes().getNamedItem("maxplayers").getTextContent();
        maxPlayers = Integer.valueOf(maxPlayersString);

        // If only maxPlayers was filled on bgg by game creator
        if (minPlayers == 0) {
          minPlayers = maxPlayers;
        }
      } catch (NullPointerException e) {
        maxPlayers = minPlayers;
      }

      // Min playtime
      int minPlaytime;
      try {
        String minPlaytimeString = statsNode.getAttributes().getNamedItem("minplaytime").getTextContent();
        minPlaytime = Integer.valueOf(minPlaytimeString);
      } catch (NullPointerException e) {
        minPlaytime = 0;
      }

      // Max playtime
      int maxPlaytime;
      try {
        String maxPlaytimeString = statsNode.getAttributes().getNamedItem("maxplaytime").getTextContent();
        maxPlaytime = Integer.valueOf(maxPlaytimeString);

        // If only maxPlaytime was filled on bgg by game creator
        if (minPlaytime == 0) {
          minPlaytime = maxPlaytime;
        }
      } catch (NullPointerException e) {
        maxPlaytime = minPlaytime;
      }

      // Personal rating
      String personalRatingString = statsNode.getChildNodes().item(1).getAttributes().getNamedItem("value").getTextContent();
      // Returning string, as value might be N/A

      // Number of plays
      int numPlays;

      String numPlaysString = numPlaysNode.getTextContent();
      numPlays = Integer.valueOf(numPlaysString);

      String averageRating = statsNode.getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("value").getTextContent();

      // Getting game type
      String type;
      try {
        type = statsNode.getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(3).getAttributes().getNamedItem("name").getTextContent();
      }
      catch (NullPointerException e) {
        type = null; // On some games, this isn't set
      }
      BoardGame game = new BoardGame(name, uniqueID, minPlayers, maxPlayers, minPlaytime, maxPlaytime, personalRatingString, numPlays, averageRating, type);
      games.add(game);

      idToGameMap.put(uniqueID, game);
    }

    if (uniqueIDArray.length == 0) {
      return games;
    }

    // Expanded game info: Complexity, whether it is expansion, categories,
    Document gamesDoc = connectionHandler.getGames(uniqueIDArray);
    NodeList gamesList = gamesDoc.getElementsByTagName("item");
    for (int i = 0; i < gamesList.getLength(); i++) {
      Node item = gamesList.item(i);

      // Connecting the item to our own data through unique id
      int uniqueID = Integer.valueOf(item.getAttributes().getNamedItem("id").getNodeValue());

      // Item type is either boardgame or boardgameexpansion
      String category = item.getAttributes().getNamedItem("type").getNodeValue();
      boolean isExpansion = false;
      if (!category.equals("boardgame")) {
        isExpansion = true;
      }

      NodeList subNodes = item.getChildNodes();
      int lengthOfSubNodes = subNodes.getLength();

      double complexity = 0;
      ArrayList<GameCategory> categoriesList = new ArrayList<>();
      ArrayList<GameMechanism> mechanismsList = new ArrayList<>();
      ArrayList<Integer> bestList = new ArrayList<>();
      ArrayList<Integer> recommendedList = new ArrayList<>();

      for (int j = 0; j < lengthOfSubNodes; j++) {
        Node currentNode = subNodes.item(j);

        if (currentNode.getNodeName().equals("poll")) {
          String name = currentNode.getAttributes().getNamedItem("name").getNodeValue();

          // Suggested number of players
          if (name.equals("suggested_numplayers")) {
            NodeList childrenOfCurrentNode = currentNode.getChildNodes();

            for (int k = 0; k < childrenOfCurrentNode.getLength(); k++) {
              Node pollResultHeader = childrenOfCurrentNode.item(k);
              String numPlayersString;
              try {
                numPlayersString = pollResultHeader.getAttributes().getNamedItem("numplayers").getNodeValue();
              }
              catch (NullPointerException e) {
                // every other node will be null
                continue;
              }
              int numPlayers;

              // If string is for example 5+ we ignore it.
              try {
                numPlayers = Integer.parseInt(numPlayersString);

              } catch (NumberFormatException e) {
                continue;
              }

              NodeList childrenOfPollResultHeader = pollResultHeader.getChildNodes();
              int maxVotes = 0;
              String highest = "";
              for (int l = 0; l < childrenOfPollResultHeader.getLength(); l++) {
                Node result = childrenOfPollResultHeader.item(l);
                int numVotes;
                try {
                  numVotes = Integer.parseInt(result.getAttributes().getNamedItem("numvotes").getNodeValue()); // No votes = 0
                }
                catch (NullPointerException e) {
                  // Every other node will be null
                  continue;
                }
                if (numVotes <= maxVotes) continue; // Not relevant as we only use highest

                maxVotes = numVotes;
                String value = result.getAttributes().getNamedItem("value").getNodeValue();
                highest = value;
              }
              // We convert to int[] later
              if (highest.equals("Best")) {
                bestList.add(numPlayers);
              } else if (highest.equals("Recommended")) {
                recommendedList.add(numPlayers);
              }
            }
          }
        }

        // Categories and mechanisms
        if (currentNode.getNodeName().equals("link")) {
          String type = currentNode.getAttributes().getNamedItem("type").getNodeValue();

          // Game categories
          if (type.equals("boardgamecategory")) {
            // can also get unique ID, choosing name
            String currentCategory = currentNode.getAttributes().getNamedItem("value").getNodeValue();
            categoriesList.add(new GameCategory(currentCategory));
          }
          if (type.equals("boardgamemechanic")) {
            String currentMechanism = currentNode.getAttributes().getNamedItem("value").getNodeValue();

            mechanismsList.add(new GameMechanism(currentMechanism));
          }
        }

        // Complexity
        if (currentNode.getNodeName().equals("statistics")) {
          Node ratingsNode = currentNode.getChildNodes().item(1);
          Node averageWeightNode = ratingsNode.getChildNodes().item(25);
          complexity = Double.valueOf(averageWeightNode.getAttributes().item(0).getNodeValue());
        }
      }

      // As array instead of list
      GameCategory[] cats = new GameCategory[categoriesList.size()];
      for (int j = 0; j < cats.length; j++) {
        cats[j] = categoriesList.get(j);
      }

      // As array instead of list
      GameMechanism[] mechs = new GameMechanism[mechanismsList.size()];
      for (int j = 0; j < mechs.length; j++) {
        mechs[j] = mechanismsList.get(j);
      }

      // As array instead of list
      int[] bestPlayerCount = new int[bestList.size()];
      for (int j = 0; j < bestPlayerCount.length; j++) {
        bestPlayerCount[j] = bestList.get(j);
      }
      int[] recommendedPlayerCount = new int[recommendedList.size()];
      for (int j = 0; j < recommendedPlayerCount.length; j++) {
        recommendedPlayerCount[j] = recommendedList.get(j);
      }


      // Add expanded info to game
      BoardGame game = idToGameMap.get(uniqueID);
      game.addExpandedGameInfo(complexity, isExpansion, cats, mechs, bestPlayerCount, recommendedPlayerCount);
    }


    // Adding specific plays
    Document playsDoc = connectionHandler.getPlays(username);

    // If no plays are registered
    if (playsDoc == null) {
      return games;
    }
    NodeList playsList = playsDoc.getElementsByTagName("play");
    for (int i = 0; i < playsList.getLength(); i++) {

      // Finding the stats and numPlays nodes
      int itemPos = 1;
      Node itemNode = null;
      Node playersNode = null;
      while (true) {
        try {
          String temp = playsList.item(i).getChildNodes().item(itemPos).getNodeName();
          if (temp.equals("item")) {
            itemNode = playsList.item(i).getChildNodes().item(itemPos);
          } else if (temp.equals("players")) {
            playersNode = playsList.item(i).getChildNodes().item(itemPos);
          }
          itemPos += 2;
        } catch (Exception e) {
          break;
        }
      }

      // Get board game
      int uniqueID = Integer.valueOf(itemNode.getAttributes().getNamedItem("objectid").getNodeValue());
      BoardGame game = idToGameMap.get(uniqueID);

      // Skipping plays of games not owned by the user. Can be modified to search for game info at the cost of another network call
      if (game == null) {
        continue;
      }

      // Get date and quantity
      NamedNodeMap playAttributes = playsList.item(i).getAttributes();
      String date = playAttributes.getNamedItem("date").getNodeValue();
      int quantity = Integer.valueOf(playAttributes.getNamedItem("quantity").getNodeValue());

      // Get plays
      String[] playerNames = new String[0];
      if (playersNode != null) {
        NodeList playersNodeList = playersNode.getChildNodes();
        int j = 1;
        playerNames = new String[(playersNodeList.getLength() - 1) / 2];
        int arrayPos = 0;
        while (j < playersNodeList.getLength()) {
          Node playerJ = playersNodeList.item(j);
          String playerJName = playerJ.getAttributes().getNamedItem("name").getNodeValue();
          // TODO: 05/12/2016 get player rating here!
          playerNames[arrayPos] = playerJName;
          arrayPos++;
          j += 2;
        }
      }

      // Adding the plays
      Play play = new Play(game, date, playerNames, quantity);
      plays.addPlay(play);
    }

    return games;
  }
}
