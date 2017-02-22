package Main.Controllers;

import Main.Models.Structure.BoardGame;
import Main.Models.Structure.BoardGameCounter;
import Main.Models.Structure.BoardGameSuggestion;
import Main.Models.Structure.Player;

/**
 * Created by Peter on 14/11/2016.
 */
public interface ILogicController {
  BoardGameSuggestion suggestGamesForGameNight(Player[] array, int maxTime);

  BoardGameCounter[] getRecommendationCounterForSingleGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime);
}
