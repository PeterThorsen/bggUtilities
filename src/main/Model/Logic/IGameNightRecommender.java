package main.Model.Logic;

import main.Model.Structure.BoardGame;
import main.Model.Structure.BoardGameCounter;
import main.Model.Structure.Player;

/**
 * Created by Peter on 31/01/2017.
 */
public interface IGameNightRecommender {
  BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight);

  BoardGameCounter[] getRecommendationCounterForSingleGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight);
}
