package Main.Model.Logic;

import Main.Model.Structure.BoardGame;
import Main.Model.Structure.Player;

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

  public BoardGame[] getAllValidBoardgames(Player[] players, int maxTime, BoardGame[] allGames, double minComplexity, double maxComplexity) {
    int numberOfPlayers = players.length + 1; // +1 to include bgg user herself
    ArrayList<BoardGame> allGamesMatchingCriteria = new ArrayList<>();


    for (BoardGame game : allGames) {

      // Don't include expansions
      if (game.isExpansion) continue;

      if (numberOfPlayers > game.maxPlayers || numberOfPlayers < game.minPlayers) {
        continue;
      }
      if (maxTime < game.minPlaytime) {
        continue;
      }
      if (minComplexity - 0.50 > game.complexity || maxComplexity + 0.50 < game.complexity) {
        continue;
      }
      // only add games within recommended games
      boolean found = false;
      for (int i = 0; i < game.bestWith.length; i++) {
        if (game.bestWith[i] == numberOfPlayers) {
          found = true;
          break;
        }
      }
      if (!found) {
        for (int i = 0; i < game.recommendedWith.length; i++) {
          if (game.recommendedWith[i] == numberOfPlayers) {
            found = true;
            break;
          }
        }
      }
      if (!found) continue;

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

    return new double[] {minComplexity, maxComplexity, 0, 0};
  }

  public double[] calculateComplexities(Player[] players) {
    double[] complexities = getMinMaxComplexities(players);
    double personalComplexity = calculatePersonalComplexity(players);
    double averageComplexity =  calculateAverageComplexity(players);
    complexities[2] = personalComplexity;
    complexities[3] = averageComplexity;
    return complexities;
  }
}
