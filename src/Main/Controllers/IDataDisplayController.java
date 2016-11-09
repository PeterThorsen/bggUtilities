package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.GameNameAndPlayHolder;

import java.util.ArrayList;
import java.util.HashMap;

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

  String[] getPlayerNames();

  HashMap<String,Integer> getNumberOfPlaysByPlayers();

  int getNumberOfPlayers();

  HashMap<String, GameNameAndPlayHolder> getMostPlayedGamesByPlayers();

  HashMap<String,String> getDateOfLastPlayForEachPlayer();
}
