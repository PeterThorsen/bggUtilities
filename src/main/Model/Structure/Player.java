package Model.Structure;

import Model.Structure.Holders.GamePlayHolder;

import java.util.HashMap;

/**
 * Holds information on each co-players of the user.
 */
public class Player {
  public final String name;
  public final Play[] allPlays;
  public int totalPlays = 0;
  public HashMap<BoardGame, Integer> gameToPlaysMap = new HashMap<>();
  public HashMap<String, Integer> playerNameToPlaysMap = new HashMap<>();
  private HashMap<BoardGame, Double> gameRatingsMap = new HashMap<>();
  private double averageComplexity;
  private double maxComplexity;
  private double minComplexity;
  private double magicComplexity;

  public Player(String name, Play[] allPlays) {
    allPlays = reverseArray(allPlays);
    this.name = name;
    this.allPlays = allPlays;
    interpretPlays();
    calculateComplexity();
  }

  private Play[] reverseArray(Play[] allPlays) {
    for (int i = 0; i < allPlays.length / 2; i++) {
      Play temp = allPlays[i];
      allPlays[i] = allPlays[allPlays.length - i - 1];
      allPlays[allPlays.length - i - 1] = temp;
    }
    return allPlays;
  }

  @Override
  public String toString() {
    return name;
  }

  private void calculateComplexity() {
    calculateMagicComplexity();

    double counter = 0;
    double totalComplexity = 0;
    double max = 1;
    double min = 5;
    for (BoardGame key : gameToPlaysMap.keySet()) {
      double keyComplexity = key.complexity;
      counter++;
      totalComplexity += keyComplexity;
      // Finding max complexity
      if (keyComplexity > max) {
        max = keyComplexity;
      }
      // Finding min complexity
      if (keyComplexity < min) {
        min = keyComplexity;
      }

    }
    if (counter > 0) {
      averageComplexity = totalComplexity / counter;
      maxComplexity = max;
      minComplexity = min;
    }
  }

  private void calculateMagicComplexity() {
    double totalScore = 0;
    double accumulator = 0;

    for (BoardGame key : gameToPlaysMap.keySet()) {
      double keyComplexity = key.complexity;
      double rating = getPersonalRating(key);
      if(rating < 5) continue;

      double power = 1.1;
      power += 1.2 * (rating - 5);

      double poweredComplexity = Math.pow(keyComplexity, power);
      double changedComplexity = keyComplexity * poweredComplexity;

      totalScore += changedComplexity;
      accumulator += poweredComplexity;

    }
    if(totalScore != 0 && accumulator != 0) {
      magicComplexity = totalScore / accumulator;
    }
    else {
      magicComplexity = 0;
    }
  }

  private void interpretPlays() {
    for (Play play : allPlays) {

      int quantity = play.noOfPlays;
      BoardGame game = play.game;
      String[] allPlayers = play.playerNames;

      totalPlays += quantity;


      // Tracking total plays of all games
      if (gameToPlaysMap.containsKey(game)) {
        int value = gameToPlaysMap.get(game);
        value += quantity;
        gameToPlaysMap.put(game, value);
      } else {
        gameToPlaysMap.put(game, quantity);
      }

      // Tracking total plays of all other players
      for (String name : allPlayers) {
        if (playerNameToPlaysMap.containsKey(name)) {
          int value = playerNameToPlaysMap.get(name);
          value += quantity;
          playerNameToPlaysMap.put(name, value);
        } else {
          playerNameToPlaysMap.put(name, quantity);
        }
      }

      // Finding specific rating for player on game, if any
      double rating = play.getRating(name);
      if (rating != 0) {
        gameRatingsMap.put(game, rating);
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

      String gameName = play.game.name;

      String date = play.date;
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

  public double getPersonalRating(BoardGame game) {
    if (!gameRatingsMap.containsKey(game)) {
      return 0;
    }
    double rating = gameRatingsMap.get(game);
    return rating;
  }

  public double getAverageRatingOfGamesAboveComplexity(double givenComplexity) {

    double counter = 0;
    double totalRating = 0;
    for (BoardGame game : gameRatingsMap.keySet()) {
      if (game.complexity < givenComplexity) continue;

      counter++;
      double currentRating = gameRatingsMap.get(game);
      totalRating += currentRating;
    }

    return totalRating / counter;
  }

  public double getAverageRatingOfGamesBelowComplexity(double givenComplexity) {

    double counter = 0;
    double totalRating = 0;
    for (BoardGame game : gameRatingsMap.keySet()) {
      if (game.complexity > givenComplexity) continue;

      counter++;
      double currentRating = gameRatingsMap.get(game);
      totalRating += currentRating;
    }

    return totalRating / counter;
  }

  public double getAverageRatingOfGamesBetweenComplexities(double lower, double higher) {

    double counter = 0;
    double totalRating = 0;
    for (BoardGame game : gameRatingsMap.keySet()) {
      if (game.complexity > higher || game.complexity < lower) continue;

      counter++;
      double currentRating = gameRatingsMap.get(game);
      totalRating += currentRating;
    }

    if(totalRating == 0 || counter == 0) return 0;

    return totalRating / counter;
  }

  /**
   * @return a double between 1 and 5 inclusive indicating the users actual complexity level.
   */
  public double getMagicComplexity() {
    return magicComplexity;
  }
}
