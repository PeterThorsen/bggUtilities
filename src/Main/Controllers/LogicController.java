package Main.Controllers;

import Main.Models.Logic.GameNightUtil;
import Main.Models.Logic.IGameNightRecommender;
import Main.Models.Structure.BoardGame;
import Main.Models.Structure.BoardGameCounter;
import Main.Models.Structure.BoardGameSuggestion;
import Main.Models.Structure.Player;

/**
 * Created by Peter on 14/11/2016.
 */
public class LogicController implements ILogicController {
  private final IGameNightRecommender gameNightRecommender;
  private final GameNightUtil gameNightUtil;
  private FacadeController facadeController;

  public LogicController(FacadeController facadeController, IGameNightRecommender gameNightRecommender) {
    this.facadeController = facadeController;
    this.gameNightRecommender = gameNightRecommender;
    gameNightUtil = new GameNightUtil();
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
    double[] complexities = gameNightUtil.calculateAverageComplexity(players);
    double minComplexity = complexities[0];
    double maxComplexity = complexities[1];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[2];

    // Calculate valid games and best combination for given time and players
    BoardGame[] allValid = gameNightUtil.getAllValidBoardgames(players, maxTime, allGames, minComplexity, maxComplexity);
    BoardGameCounter[] bestCombination = gameNightRecommender.buildBestGameNight(allValid, players, maxTime, averageComplexityGivingAllPlayersEqualWeight);

    // Return calculated data
    BoardGameSuggestion suggestedGames = new BoardGameSuggestion(bestCombination, allValid);
    return suggestedGames;
  }


}
