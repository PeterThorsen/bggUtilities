package Main.Controllers;

import Main.Models.Structure.BoardGameSuggestion;
import Main.Models.Structure.Player;

/**
 * Created by Peter on 14/11/2016.
 */
public interface ILogicController {
  BoardGameSuggestion suggestGamesForGameNight(Player[] array, int maxTime);
}
