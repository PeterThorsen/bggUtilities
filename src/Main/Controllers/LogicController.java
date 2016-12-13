package Main.Controllers;

import Main.Containers.*;
import Main.Containers.Holders.Reason;
import Main.Models.Logic.GameNightRecommender;
import Main.Sorting.InsertionSortGamesWithCounter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Peter on 14/11/2016.
 */
public class LogicController implements ILogicController {
  private final GameNightRecommender gameNightRecommender;
  private FacadeController facadeController;

  public LogicController(FacadeController facadeController) {
    this.facadeController = facadeController;
    gameNightRecommender = new GameNightRecommender();
  }


  /**
   * Using selected players and a given time limit, suggest a number of fitting games for the group
   * @param players All players selected
   * @param maxTime The time limit which the algorithm will select a number of games to fulfill.
   * @return All games possible given player count and an array of specific recommendations
   */
  @Override
  public BoardGameSuggestion suggestGamesForGameNight(Player[] players, int maxTime) {

    // Calculate or retrieve data
    BoardGame[] allGames = facadeController.getAllGames();
    double[] complexities = gameNightRecommender.calculateAverageComplexity(players);
    double minComplexity = complexities[0];
    double maxComplexity = complexities[1];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[2];

    // Calculate valid games and best combination for given time and players
    BoardGame[] allValid = gameNightRecommender.getAllValidBoardgames(players, maxTime, allGames, minComplexity, maxComplexity);
    BoardGameCounter[] bestCombination = buildBestGameNight(allValid, players, maxTime, averageComplexityGivingAllPlayersEqualWeight);

    // Return calculated data
    BoardGameSuggestion suggestedGames = new BoardGameSuggestion(bestCombination, allValid);
    return suggestedGames;
  }

  /**
   * Used inside suggestGamesForGameNight to control the calculations for best game recommendations.
   */
  private BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double averageComplexityGivingAllPlayersEqualWeight) {
    BoardGameCounter[] gamesWithCounter = gameNightRecommender.convertToCounter(allValid);

    for (int i = 0; i < gamesWithCounter.length; i++) {
      BoardGameCounter current = gamesWithCounter[i];

      gameNightRecommender.calculatePlayerScore(players, current);
      gameNightRecommender.calculateAbsoluteFactorsScore(current, maxTime, players, averageComplexityGivingAllPlayersEqualWeight);
    }
    return gameNightRecommender.calculateSuggestedGames(gamesWithCounter, maxTime);
  }


}
