package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.Play;
import Main.Containers.Player;

import java.util.ArrayList;

/**
 * Created by Peter on 14/11/2016.
 */
public class LogicController implements ILogicController {
  private FacadeController facadeController;

  public LogicController(FacadeController facadeController) {
    this.facadeController = facadeController;
  }

  @Override
  public BoardGame[] suggestGames(Player[] array, int maxTime) {

    int numberOfPlayers = array.length+1; // +1 to include bgg user herself
    double minComplexity = 6.0;
    double maxComplexity = 0.0;

    double averageComplexityGivingAllPlayersEqualWeight = 0;
    double weighedAverageComplexityOfAllPlayers = 0;

    for (Player player : array) {
      double averageComplexity = 0.0;
      double weighedAverageComplexity;
      double weighedPlays = 0;
      for (Play play : player.allPlays) {
        double gameComplexity = play.getGame().getComplexity();
        averageComplexity += gameComplexity * play.getQuantity();
        weighedPlays += play.getQuantity();

        if(gameComplexity < minComplexity) {
          minComplexity = gameComplexity;
        }
        if(gameComplexity > maxComplexity) {
          maxComplexity = gameComplexity;
        }
      }

      weighedAverageComplexity = averageComplexity / weighedPlays;
      averageComplexity = averageComplexity / (double) player.allPlays.length;

      averageComplexityGivingAllPlayersEqualWeight += averageComplexity;
      weighedAverageComplexityOfAllPlayers += weighedAverageComplexity;
    }

    // To direct algorithm towards better recommendations
    averageComplexityGivingAllPlayersEqualWeight = averageComplexityGivingAllPlayersEqualWeight / array.length;
    weighedAverageComplexityOfAllPlayers = weighedAverageComplexityOfAllPlayers / array.length;


    ArrayList<BoardGame> allGamesMatchingCriteria = new ArrayList<>();

    BoardGame[] allGames = facadeController.getAllGames();

    for (BoardGame game : allGames) {
      if(numberOfPlayers > game.getMaxPlayers() || numberOfPlayers < game.getMinPlayers()) {
        continue;
      }
      if(maxTime < game.getMinPlaytime()) {
        continue;
      }
      if(minComplexity - 0.50 > game.getComplexity() || maxComplexity + 0.50 < game.getComplexity()) {
        continue;
      }
      allGamesMatchingCriteria.add(game);
    }

    BoardGame[] suggestedGames = new BoardGame[allGamesMatchingCriteria.size()];

    for (int i = 0; i < suggestedGames.length; i++) {
      suggestedGames[i] = allGamesMatchingCriteria.get(i);
    }
    return suggestedGames;
  }
}
