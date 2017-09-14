package Model.Logic;

import Model.Structure.BoardGame;
import Model.Structure.BoardGameCounter;
import Model.Structure.Player;

/**
 * Created by Peter on 31/01/2017.
 */
public interface IGameNightRecommender {
  BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight);

  BoardGameCounter[] getRecommendationCounterForSingleGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight);
}
