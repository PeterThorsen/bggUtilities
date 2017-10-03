package Model.Logic;

import Model.Structure.BoardGame;
import Model.Structure.BoardGameCounter;
import Model.Structure.Player;
import Model.Structure.Reason;

import java.util.ArrayList;

/**
 * Created by Peter on 31/01/2017.
 */
public class GameNightUtil {

  public double calculateAverageComplexity(Player[] players) {
    double averageComplexityGivingAllPlayersEqualWeight = 0;

    for (Player player : players) {
      double currentAverage = player.getAverageComplexity();
      averageComplexityGivingAllPlayersEqualWeight += currentAverage;
    }

    // To direct algorithm towards better recommendations
    averageComplexityGivingAllPlayersEqualWeight = averageComplexityGivingAllPlayersEqualWeight / Double.valueOf(players.length);
    return averageComplexityGivingAllPlayersEqualWeight;
  }

  public BoardGame[] getAllValidBoardgames(Player[] players, int maxTime, BoardGame[] allGames) {
    int numberOfPlayers = players.length + 1; // +1 to include bgg user herself
    ArrayList<BoardGame> allGamesMatchingCriteria = new ArrayList<>();


    for (BoardGame game : allGames) {
      if (numberOfPlayers > game.maxPlayers || numberOfPlayers < game.minPlayers) {
        continue;
      }
      if (maxTime < game.minPlaytime) {
        continue;
      }
      allGamesMatchingCriteria.add(game);
    }

    BoardGame[] allValid = new BoardGame[allGamesMatchingCriteria.size()];

    for (int i = 0; i < allValid.length; i++) {
      allValid[i] = allGamesMatchingCriteria.get(i);
    }
    return allValid;
  }

  public double calculatePersonalComplexity(Player[] players) {
    double magicComplexityGivingAllPlayersEqualWeight = 0;

    for (Player player : players) {
      double currentComplexity = player.getMagicComplexity();
      magicComplexityGivingAllPlayersEqualWeight += currentComplexity;
    }
    // To direct algorithm towards better recommendations
    magicComplexityGivingAllPlayersEqualWeight = magicComplexityGivingAllPlayersEqualWeight / (double) players.length;
    return magicComplexityGivingAllPlayersEqualWeight;

  }

  private double[] getMinMaxComplexities(Player[] players) {
    double minComplexity = 6.0;
    double maxComplexity = 0.0;

    for (Player player : players) {
      if (player.getMaxComplexity() > maxComplexity) maxComplexity = player.getMaxComplexity();
      if (player.getMinComplexity() < minComplexity) minComplexity = player.getMinComplexity();
    }

    return new double[]{minComplexity, maxComplexity, 0, 0};
  }

  public double[] calculateComplexities(Player[] players) {
    double[] complexities = getMinMaxComplexities(players);
    double personalComplexity = calculatePersonalComplexity(players);
    double averageComplexity = calculateAverageComplexity(players);
    complexities[2] = personalComplexity;
    complexities[3] = averageComplexity;
    return complexities;
  }

  public BoardGameCounter[] suggestReasonsForAllGames(BoardGame[] allValid, Player[] players, int playTime) {
    BoardGameCounter[] withReasons = new BoardGameCounter[allValid.length];

    for (int i = 0; i < withReasons.length; i++) {
      withReasons[i] = suggestReason(allValid[i], players, playTime);
    }

    return withReasons;
  }

  private BoardGameCounter suggestReason(BoardGame boardGame, Player[] players, int playTime) {
    ArrayList<Reason> reasons = new ArrayList<>();

    // Favor games that the user haven't played much
    if (boardGame.numPlays == 0) {
      reasons.add(new Reason("New game: You haven't tried this game yet.", 1));
    }

    // Players ratings
    Reason playerRatingsReason = getPlayerRatingsReason(boardGame, players);
    if(playerRatingsReason != null) reasons.add(playerRatingsReason);


    // If players haven't played the game and it is within time and complexity
    // Favor games that other players
    // If players have played the game, recommend it more
    // Recommended with, best with
    // Calculating for days since last play for each player
    // points based on type match
    // points based on mechanisms match
    // points based on category match
    // Personal rating should matter
    // How fitting is the complexity
    // Favor single games taking a lot of time

    // Not recommended with
    // Complexity too high
    // Complexity too low
    // Approx time too high to play the game
    // Players dislike the game

    return new BoardGameCounter(boardGame, calculateValue(reasons), reasons);
  }

  private int calculateValue(ArrayList<Reason> reasons) {
    int value = 0;
    for (Reason reason : reasons) {
      value += reason.value;
    }
    return value;
  }

  private Reason getPlayerRatingsReason(BoardGame boardGame, Player[] players) {
    ArrayList<Integer> hasRatings = new ArrayList<>();
    int negatives = 0;
    int iteration = 0;
    for (Player player : players) {
      if(player.getPersonalRating(boardGame) > 0) {
        hasRatings.add(iteration);
        if(player.getPersonalRating(boardGame) < 6) {
          negatives++;
        }
      }
      iteration++;
    }
    if(hasRatings.isEmpty()) return null;

    StringBuilder sb = new StringBuilder();
    if(negatives == 0) {
      sb.append("All players liked the game. ");
    }
    else {
      sb.append("Some players didn't like the game. ");
    }
    while(true) {
      int position = hasRatings.remove(hasRatings.size()-1);
      if (hasRatings.size() == 0) {
        sb.append(players[position].name).append(" rated it ").append(players[position].getPersonalRating(boardGame)).append(".");
        break;
      }
      else {
        sb.append(players[position].name).append(" rated it ").append(players[position].getPersonalRating(boardGame)).append(", ");
      }
    }

    return new Reason(sb.toString(), negatives == 0 ? 1 : -1);

  }
}
