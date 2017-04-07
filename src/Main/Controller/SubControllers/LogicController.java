package Main.Controller.SubControllers;

import Main.Controller.FacadeController;
import Main.Model.Logic.GameNightUtil;
import Main.Model.Logic.IGameNightRecommender;
import Main.Model.Structure.BoardGame;
import Main.Model.Structure.BoardGameCounter;
import Main.Model.Structure.BoardGameSuggestion;
import Main.Model.Structure.Player;

public class LogicController {
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
   */
  public BoardGameSuggestion suggestGamesForGameNight(Player[] players, int maxTime, BoardGame[] allGames) {

    double[] complexities = gameNightUtil.calculateComplexities(players);
    double minComplexity = complexities[0];
    double maxComplexity = complexities[1];
    double magicComplexity = complexities[2];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[3];

    // Calculate valid games and best combination for given time and players
    BoardGame[] allValid = gameNightUtil.getAllValidBoardgames(players, maxTime, allGames, minComplexity, maxComplexity);
    BoardGameCounter[] bestCombination = gameNightRecommender.buildBestGameNight(allValid, players, maxTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight);

    // Return calculated data
    return new BoardGameSuggestion(bestCombination, allValid);
  }

  /**
   * Get best recommendation for a single game
   */
  public BoardGameCounter[] getBestCombinationForGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime) {
    double[] complexities = gameNightUtil.calculateComplexities(players);
    double magicComplexity = complexities[2];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[3];
    return gameNightRecommender.getRecommendationCounterForSingleGame(actualSuggestionAsGames, players, maxTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight);
  }


}