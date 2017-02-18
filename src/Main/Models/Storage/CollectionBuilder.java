package Main.Models.Storage;

import Main.Models.Network.IConnectionHandler;
import Main.Models.Structure.*;
import Main.Models.Structure.Holders.ItemPlayersNodesHolder;
import Main.Models.Structure.Holders.NodeHolder;
import Main.Models.Structure.Holders.PlayerNodeInformationHolder;
import Main.Models.Structure.Holders.otherValuesHolder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by Peter on 28/09/16.
 */
public class CollectionBuilder implements ICollectionBuilder {
  private final IConnectionHandler connectionHandler;
  private String username;
  private Document collectionDocument;
  private final Plays plays;
  private Player[] allPlayers;
  private Semaphore expandedSem = new Semaphore(0);
  private Semaphore playsSem = new Semaphore(0);
  private Semaphore playersSem = new Semaphore(0);

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
  public synchronized BoardGameCollection getCollection(String username) {
    this.username = username;

    if (collectionDocument == null) {
      collectionDocument = connectionHandler.getCollection(username);

      if (collectionDocument == null) {
        // Invalid user
        return null;
      }
    }
    BoardGame[] games = buildCollection(collectionDocument);
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
    try {
      playersSem.acquire();
      playersSem.release();
      return allPlayers;
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

  private synchronized BoardGame[] buildCollection(Document document) {
    NodeList nodeList = document.getElementsByTagName("item");
    ArrayList<BoardGame> games = new ArrayList<>();
    int[] uniqueIDArray = new int[nodeList.getLength()];
    HashMap<Integer, BoardGame> idToGameMap = new HashMap<>();

    // Creating games with basic information
    for (int i = 0; i < nodeList.getLength(); i++) {

      // Finding statsNode and numPlaysNode
      NodeHolder nodeHolder = calculateStatsAndNumPlaysNodes(nodeList, i);
      Node statsNode = nodeHolder.statsNode;
      Node numPlaysNode = nodeHolder.numPlaysNode;

      // Calculating game information
      String name = calculateGameName(nodeList, i);
      int uniqueID = calculateUniqueID(nodeList, i);
      int minPlayers = calculateMinPlayers(statsNode);
      int maxPlayers = calculateMaxPlayers(statsNode, minPlayers);
      int minPlaytime = calculateMinPlaytime(statsNode);
      int maxPlaytime = calculateMaxPlaytime(statsNode, minPlaytime);
      int numPlays = calculateNumberOfPlays(numPlaysNode);
      String type = calculateGameType(statsNode);

      // Returning string, as value might be N/A
      String personalRatingString = statsNode.getChildNodes().item(1).getAttributes().getNamedItem("value").getTextContent();
      String averageRating = statsNode.getChildNodes().item(1).getChildNodes().item(3).getAttributes().getNamedItem("value").getTextContent();

      // Finally, building game
      BoardGame game = new BoardGame(name, uniqueID, minPlayers, maxPlayers, minPlaytime, maxPlaytime, personalRatingString, numPlays, averageRating, type);

      // Adding to structure
      games.add(game);
      idToGameMap.put(uniqueID, game);
      uniqueIDArray[i] = uniqueID;
    }

    // TODO: 06-Dec-16 not tested. What happens if we use expanded game info without having added it?
    // If no games were added, no need to make another network call
    if (uniqueIDArray.length == 0) {
      return asArray(games);
    }

    addExpandedGameInfo(idToGameMap, uniqueIDArray);
    addPlays(idToGameMap, uniqueIDArray);
    try {
      expandedSem.acquire();
      expandedSem.release();
      playsSem.acquire();
      playsSem.release();

      buildPlayers();
      playersSem.release();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Finally, return all games with full information
    return asArray(games);
  }

  // Expanded game info: Complexity, whether it is expansion, categories,
  private synchronized void addExpandedGameInfo(HashMap<Integer, BoardGame> idToGameMap, int[] uniqueIDArray) {

    new Thread(new Runnable() {
      @Override
      public void run() {
        Document gamesDoc = connectionHandler.getGames(uniqueIDArray);
        NodeList gamesList = gamesDoc.getElementsByTagName("item");
        for (int i = 0; i < gamesList.getLength(); i++) {
          Node item = gamesList.item(i);

          // Connecting the item to our own data through unique id
          int uniqueID = Integer.valueOf(item.getAttributes().getNamedItem("id").getNodeValue());

          boolean isExpansion = calculateIfExpansion(item);
          otherValuesHolder otherValues = calculateMissingInformation(item);

          // Add expanded info to game
          BoardGame game = idToGameMap.get(uniqueID);
          game.addExpandedGameInfo(otherValues.complexity, isExpansion, otherValues.categories, otherValues.mechanisms,
                  otherValues.bestPlayerCount, otherValues.recommendedPlayerCount);
        }
        expandedSem.release();
      }
    }).start();
  }

  // Adding specific plays
  private synchronized void addPlays(HashMap<Integer, BoardGame> idToGameMap, int[] uniqueIDArray) {
    new Thread(new Runnable() {
      @Override
      public void run() {

        ArrayList<Document> playsDocs = connectionHandler.getPlays(username);

        // If no plays are registered
        if (playsDocs == null) {
          return;
        }
        for (Document playsDoc : playsDocs) {
          NodeList playsList = playsDoc.getElementsByTagName("play");
          for (int i = 0; i < playsList.getLength(); i++) {

            Node currentItem = playsList.item(i);

            // Calculating item and players nodes
            ItemPlayersNodesHolder holder = calculateItemAndPlayersNodes(currentItem);
            Node itemNode = holder.itemNode;
            Node playersNode = holder.playersNode;


            // Get game
            int uniqueID = Integer.valueOf(itemNode.getAttributes().getNamedItem("objectid").getNodeValue());
            BoardGame game = idToGameMap.get(uniqueID);

            // Skipping plays of games not owned by the user. Can be modified to search for game info at the cost of another network call
            if (game == null) {
              continue;
            }

            NamedNodeMap playAttributes = currentItem.getAttributes();

            // Get date and quantity
            String date = playAttributes.getNamedItem("date").getNodeValue();
            int quantity = Integer.valueOf(playAttributes.getNamedItem("quantity").getNodeValue());

            PlayerNodeInformationHolder[] playerInformationArray = calculatePlayerInformation(playersNode);
            HashMap<String, Double> playerRatings = new HashMap<>();
            String[] names = new String[playerInformationArray.length];
            for (int j = 0; j < playerInformationArray.length; j++) {
              String name = playerInformationArray[j].playerName;
              double rating = playerInformationArray[j].rating;

              playerRatings.put(name, rating);
              names[j] = name;
            }

            // Adding the plays
            Play play = new Play(game, date, names, quantity, playerRatings);
            plays.addPlay(play);
          }
        }
        playsSem.release();

      }

    }).start();

  }

  private void buildPlayers() {

    Play[] allPlays = plays.getAllPlays();
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
    allPlayers = new Player[totalPersons];
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
  }

  private PlayerNodeInformationHolder[] calculatePlayerInformation(Node playersNode) {

    // Get plays
    PlayerNodeInformationHolder[] players = new PlayerNodeInformationHolder[0];
    if (playersNode != null) { // TODO: 06-Dec-16 needed?
      NodeList playersNodeList = playersNode.getChildNodes();
      int j = 1;
      players = new PlayerNodeInformationHolder[(playersNodeList.getLength() - 1) / 2];
      int arrayPos = 0;
      while (j < playersNodeList.getLength()) {
        Node playerJ = playersNodeList.item(j);
        String playerJName = playerJ.getAttributes().getNamedItem("name").getNodeValue();
        String ratingString = playerJ.getAttributes().getNamedItem("rating").getNodeValue();
        double rating;
        try {
          rating = Double.valueOf(ratingString);
          if (rating > 10 || rating < 0) {
            rating = 0;
          }
        } catch (NumberFormatException e) {
          rating = 0;
        }
        players[arrayPos] = new PlayerNodeInformationHolder(playerJName, rating);
        arrayPos++;
        j += 2;
      }
    }
    return players;

  }

  private ItemPlayersNodesHolder calculateItemAndPlayersNodes(Node currentItem) {

    int itemPos = 1;
    Node itemNode = null;
    Node playersNode = null;
    while (true) {
      try {
        String temp = currentItem.getChildNodes().item(itemPos).getNodeName();
        if (temp.equals("item")) {
          itemNode = currentItem.getChildNodes().item(itemPos);
        } else if (temp.equals("players")) {
          playersNode = currentItem.getChildNodes().item(itemPos);
        }
        itemPos += 2;
      } catch (Exception e) {
        break;
      }
    }
    return new ItemPlayersNodesHolder(itemNode, playersNode);

  }

  private otherValuesHolder calculateMissingInformation(Node item) {
    NodeList subNodes = item.getChildNodes();

    double complexity = 0;
    ArrayList<GameCategory> categoriesList = new ArrayList<>();
    ArrayList<GameMechanism> mechanismsList = new ArrayList<>();
    ArrayList<Integer> bestList = new ArrayList<>();
    ArrayList<Integer> recommendedList = new ArrayList<>();

    for (int j = 0; j < subNodes.getLength(); j++) {
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
            } catch (NullPointerException e) {
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
              } catch (NullPointerException e) {
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

    return new otherValuesHolder(complexity, cats, mechs, bestPlayerCount, recommendedPlayerCount);
  }

  private boolean calculateIfExpansion(Node item) {
    String category = item.getAttributes().getNamedItem("type").getNodeValue();
    boolean isExpansion = false;
    if (!category.equals("boardgame")) {
      isExpansion = true;
    }
    return isExpansion;
  }

  private String calculateGameName(NodeList nodeList, int i) {
    return nodeList.item(i).getChildNodes().item(1).getTextContent();
  }

  private NodeHolder calculateStatsAndNumPlaysNodes(NodeList nodeList, int i) {
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
    return new NodeHolder(statsNode, numPlaysNode);
  }

  private int calculateUniqueID(NodeList nodeList, int i) {
    String uniqueIDString = nodeList.item(i).getAttributes().item(1).getTextContent();
    int uniqueID = Integer.valueOf(uniqueIDString);
    return uniqueID;


  }

  private String calculateGameType(Node statsNode) {
    String type;
    try {
      type = statsNode.getChildNodes().item(1).getChildNodes().item(11).getChildNodes().item(3).getAttributes().getNamedItem("name").getTextContent();
    } catch (NullPointerException e) {
      type = null; // On some games, this isn't set
    }
    return type;
  }

  private int calculateNumberOfPlays(Node numPlaysNode) {
    int numPlays;

    String numPlaysString = numPlaysNode.getTextContent();
    numPlays = Integer.valueOf(numPlaysString);
    return numPlays;
  }

  private int calculateMaxPlaytime(Node statsNode, int minPlaytime) {
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
    return maxPlaytime;
  }

  private int calculateMinPlaytime(Node statsNode) {
    int minPlaytime;
    try {
      String minPlaytimeString = statsNode.getAttributes().getNamedItem("minplaytime").getTextContent();
      minPlaytime = Integer.valueOf(minPlaytimeString);
    } catch (NullPointerException e) {
      minPlaytime = 0;
    }
    return minPlaytime;
  }

  private int calculateMaxPlayers(Node statsNode, int minPlayers) {
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
    return maxPlayers;
  }

  private int calculateMinPlayers(Node statsNode) {
    int minPlayers;
    try {
      String minPlayersString = statsNode.getAttributes().getNamedItem("minplayers").getTextContent();
      minPlayers = Integer.valueOf(minPlayersString);
    } catch (NullPointerException e) {
      minPlayers = 0;
    }
    return minPlayers;
  }

  private BoardGame[] asArray(ArrayList<BoardGame> games) {
    BoardGame[] arr = new BoardGame[games.size()];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = games.get(i);
    }
    return arr;
  }
}
