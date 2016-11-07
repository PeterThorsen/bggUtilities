package Main.Controllers;

import Main.Containers.BoardGame;

import java.util.ArrayList;

/**
 * Created by Peter on 06/10/2016.
 */
public interface IDataDisplayController {

  ArrayList<BoardGame> getAllGames();

  int getNumberOfGames();

  String[] getGameNames();

  double[] getComplexities();

  int[] getMaxLengths();

  int[] getMinLengths();

  int[] getMinPlayers();

  int[] getMaxPlayers();

  int[] getNumberOfPlays();

  String[] getPersonalRatings();

  String[] getAverageRatings();
}
