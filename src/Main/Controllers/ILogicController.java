package Main.Controllers;

import Main.Containers.BoardGameSuggestion;
import Main.Containers.Player;

/**
 * Created by Peter on 14/11/2016.
 */
public interface ILogicController {
  BoardGameSuggestion suggestGamesForGameNight(Player[] array, int maxTime);
}
