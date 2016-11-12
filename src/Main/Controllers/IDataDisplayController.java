package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.Play;
import Main.Containers.Player;

/**
 * Created by Peter on 06/10/2016.
 */
interface IDataDisplayController {

  BoardGame[] getAllGames();

  int getNumberOfGames();

  String[] getGameNames();

  Play[] getAllPlaysSorted();

  Player[] getAllPlayersSorted();
}
