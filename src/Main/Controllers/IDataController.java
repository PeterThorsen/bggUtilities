package Main.Controllers;

import Main.Models.Structure.BoardGame;
import Main.Models.Structure.Play;
import Main.Models.Structure.Player;

/**
 * Created by Peter on 06/10/2016.
 */
interface IDataController {

  BoardGame[] getAllGames();

  int getNumberOfGames();

  String[] getGameNames();

  Play[] getAllPlays();

  Player[] getAllPlayers();

  BoardGame getGame(String name);
}
