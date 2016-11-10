package Main.Containers;

import java.util.HashMap;

/**
 * Created by Peter on 10/11/2016.
 */
public class Player {
  public final String name;
  private final Play[] allPlays;
  public int totalPlays = 0;
  public HashMap<String, Integer> gameToPlaysMap = new HashMap<>();
  public HashMap<String, Integer> nameToPlaysMap = new HashMap<>();

  public Player(String name, Play[] allPlays) {
    this.name = name;
    this.allPlays = allPlays;
    interpretPlays();
  }

  private void interpretPlays() {
    for (Play play : allPlays) {
      int quantity = play.getQuantity();
      BoardGame game = play.getGame();
      String gameName = game.getName();
      String[] allPlayers = play.getPlayers();

      totalPlays += quantity;


      // Tracking total plays of all games
      if (gameToPlaysMap.containsKey(gameName)) {
        int value = gameToPlaysMap.get(gameName);
        value += quantity;
        gameToPlaysMap.put(gameName, value);
      } else {
        gameToPlaysMap.put(gameName, quantity);
      }

      // Tracking total plays of all players
      for (String name : allPlayers) {
        if (nameToPlaysMap.containsKey(name)) {
          int value = nameToPlaysMap.get(name);
          value += quantity;
          nameToPlaysMap.put(name, value);
        } else {
          nameToPlaysMap.put(name, quantity);
        }
      }

    }
  }

  public String getMostPlayedGame() {
    int maxValue = 0;
    String maxGame = "";
    for (String key : gameToPlaysMap.keySet()) {
      int current = gameToPlaysMap.get(key);
      if (current > maxValue) {
        maxValue = current;
        maxGame = key;
      }
    }
    return maxGame;
  }

  public String getMostCommonFriend() {
    int maxValue = 0;
    String maxPlayer = "";
    for (String key : nameToPlaysMap.keySet()) {
      int current = nameToPlaysMap.get(key);
      if (current > maxValue) {
        maxValue = current;
        maxPlayer = key;
      }
    }
    return maxPlayer;
  }
}
