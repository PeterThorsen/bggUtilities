package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.Player;

/**
 * Created by Peter on 14/11/2016.
 */
public interface ILogicController {
  BoardGame[] suggestGames(Player[] array, int maxTime);
}
