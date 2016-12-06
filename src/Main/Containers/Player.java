package Main.Containers;

import Main.Containers.Holders.GamePlayHolder;
import Main.Containers.Holders.PlayerNodeInformationHolder;

import java.util.HashMap;

/**
 * Created by Peter on 10/11/2016.
 */
public class Player {
  public final String name;
  public final Play[] allPlays;
  public int totalPlays = 0;
  public HashMap<BoardGame, Integer> gameToPlaysMap = new HashMap<>();
  public HashMap<String, Integer> playerNameToPlaysMap = new HashMap<>();
  private double averageComplexity;
  private double maxComplexity;
  private double minComplexity;

  public Player(String name, Play[] allPlays) {
    this.name = name;
    this.allPlays = allPlays;
    interpretPlays();
    calculateComplexity();
  }

  private void calculateComplexity() {
    double counter = 0;
    double totalComplexity = 0;
    double max = 1;
    double min = 5;
    for (BoardGame key : gameToPlaysMap.keySet()) {
      double keyComplexity = key.getComplexity();
      counter++;
      totalComplexity += keyComplexity;
      // Finding max complexity
      if (keyComplexity > max) {
        max = keyComplexity;
      }
      // Finding min complexity
      if(keyComplexity < min) {
        min = keyComplexity;
      }

    }
    if (counter > 0) {
      averageComplexity = totalComplexity / counter;
      maxComplexity = max;
      minComplexity = min;
      return;
    }
  }

  private void interpretPlays() {
    for (Play play : allPlays) {
      int quantity = play.getQuantity();
      BoardGame game = play.getGame();
      PlayerNodeInformationHolder[] allPlayers = play.playerInformation;

      totalPlays += quantity;


      // Tracking total plays of all games
      if (gameToPlaysMap.containsKey(game)) {
        int value = gameToPlaysMap.get(game);
        value += quantity;
        gameToPlaysMap.put(game, value);
      } else {
        gameToPlaysMap.put(game, quantity);
      }

      // Tracking total plays of all players
      for (PlayerNodeInformationHolder holder : allPlayers) {
        if (playerNameToPlaysMap.containsKey(holder.playerName)) {
          int value = playerNameToPlaysMap.get(holder.playerName);
          value += quantity;
          playerNameToPlaysMap.put(holder.playerName, value);
        } else {
          playerNameToPlaysMap.put(holder.playerName, quantity);
        }
      }
    }
  }

  public GamePlayHolder getMostPlayedGame() {
    int maxValue = 0;
    BoardGame maxGame = null;
    for (BoardGame key : gameToPlaysMap.keySet()) {
      int current = gameToPlaysMap.get(key);
      if (current > maxValue) {
        maxValue = current;
        maxGame = key;
      }
    }
    return new GamePlayHolder(maxGame, maxValue);
  }

  public String getMostCommonFriend() {
    int maxValue = 0;
    String maxPlayer = "";
    for (String key : playerNameToPlaysMap.keySet()) {
      int current = playerNameToPlaysMap.get(key);
      if (current > maxValue) {
        maxValue = current;
        maxPlayer = key;
      }
    }
    return maxPlayer;
  }

  public String getMostRecentGame() {

    int mostRecentYear = 0;
    int mostRecentMonth = 0;
    int mostRecentDay = 0;
    String mostRecentGame = "";

    for (Play play : allPlays) {

      String gameName = play.getGame().getName();

      String date = play.getDate();
      String[] splitDate = date.split("-");
      int year = Integer.valueOf(splitDate[0]);
      int month = Integer.valueOf(splitDate[1]);
      int day = Integer.valueOf(splitDate[2]);

      if (mostRecentYear > year) {
        continue;
      }
      if (mostRecentYear < year) {
        mostRecentYear = year;
        mostRecentMonth = month;
        mostRecentDay = day;
        mostRecentGame = gameName;
        continue;
      }
      if (mostRecentMonth > month) {
        continue;
      }
      if (mostRecentMonth < month) {
        mostRecentYear = year;
        mostRecentMonth = month;
        mostRecentDay = day;
        mostRecentGame = gameName;
        continue;
      }
      if (mostRecentDay > day) {
        continue;
      }
      if (mostRecentDay < day) {
        mostRecentYear = year;
        mostRecentMonth = month;
        mostRecentDay = day;
        mostRecentGame = gameName;
      }
    }
    return mostRecentGame;
  }

  public boolean hasPlayed(BoardGame game) {
    return gameToPlaysMap.containsKey(game);
  }

  public double getAverageComplexity() {
    return averageComplexity;
  }

  public double getMaxComplexity() {
    return maxComplexity;
  }

  public double getMinComplexity() {
    return minComplexity;
    
  }

  @Override
  public String toString() {
    return name;
  }
}
