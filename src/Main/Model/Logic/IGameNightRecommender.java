package Main.Model.Logic;

import Main.Model.Structure.BoardGame;
import Main.Model.Structure.BoardGameCounter;
import Main.Model.Structure.Player;

/**
 * Created by Peter on 31/01/2017.
 */
public interface IGameNightRecommender {
  BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight);

  BoardGameCounter[] getRecommendationCounterForSingleGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight);
}
