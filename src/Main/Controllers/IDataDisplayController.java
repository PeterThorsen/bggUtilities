package Main.Controllers;

import Main.Containers.BoardGame;

import java.util.ArrayList;

/**
 * Created by Peter on 06/10/2016.
 */
public interface IDataDisplayController {

  ArrayList<BoardGame> getAllGames();

  String[] getGameNames();
}
