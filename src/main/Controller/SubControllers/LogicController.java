package Controller.SubControllers;

import Controller.FacadeController;
import Model.Logic.GameNightUtil;
import Model.Logic.IGameNightRecommender;
import Model.Structure.BoardGame;
import Model.Structure.BoardGameCounter;
import Model.Structure.BoardGameSuggestion;
import Model.Structure.Player;

import java.util.ArrayList;

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
  public BoardGameSuggestion suggestGamesForGameNight(Player[] players, int maxTime, BoardGame[] allGames, Player[] allPlayersEver) {

    double[] complexities = gameNightUtil.calculateComplexities(players);
    double minComplexity = complexities[0];
    double maxComplexity = complexities[1];
    double magicComplexity = complexities[2];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[3];

    // Calculate valid games and best combination for given time and players
    BoardGame[] allValid = gameNightUtil.getAllValidBoardgames(players, maxTime, allGames);
    BoardGameCounter[] bestCombination = gameNightRecommender.buildBestGameNight(allValid, players, maxTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight, false, allPlayersEver);

    // Return calculated data
    return new BoardGameSuggestion(bestCombination, allValid);
  }

  /**
   * Get best recommendation for a single game
   */
  public BoardGameCounter[] getBestCombinationForGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime, Player[] allPlayersEver) {
    double[] complexities = gameNightUtil.calculateComplexities(players);
    double magicComplexity = complexities[2];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[3];
    return gameNightRecommender.getRecommendationCounterForSingleGame(actualSuggestionAsGames, players, maxTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight, allPlayersEver);
  }


  public BoardGameCounter[] helpPickGameNight(Player[] players, int playTime, BoardGame[] allGames, int[] gamesToExclude, Player[] allPlayersEver) {

    double[] complexities = gameNightUtil.calculateComplexities(players);
    double minComplexity = complexities[0];
    double maxComplexity = complexities[1];
    double magicComplexity = complexities[2];
    double averageComplexityGivingAllPlayersEqualWeight = complexities[3];

    BoardGame[] allValid = gameNightUtil.getAllValidBoardgames(players, playTime, allGames);

    BoardGameCounter[] recommendations =  gameNightRecommender.buildBestGameNight(allValid, players, playTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight, true, allPlayersEver);
    ArrayList<BoardGameCounter> withoutExcludedGamesArray = new ArrayList<>();

    outer:
    for (BoardGameCounter recommendation : recommendations) {
      for (int idOfGameToExclude : gamesToExclude) {
        if(recommendation.game.id == idOfGameToExclude) continue outer;
      }
      withoutExcludedGamesArray.add(recommendation);
    }

    BoardGameCounter[] withoutExcludedGames = new BoardGameCounter[withoutExcludedGamesArray.size()];
    for (int i = 0; i < withoutExcludedGames.length; i++) {
      withoutExcludedGames[i] = withoutExcludedGamesArray.get(i);
    }
    return withoutExcludedGames;
  }
}
