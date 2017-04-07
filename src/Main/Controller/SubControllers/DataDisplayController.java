package Main.Controller.SubControllers;

import Main.Model.Storage.ICollectionBuilder;
import Main.Model.Structure.*;
import Main.Util.Sorting.InsertionSort;

import java.util.ArrayList;

/**
 * Handles data transfer from model to view.
 */
public class DataDisplayController {
  private final BoardGameCollection collection;
  private final Plays plays;
  private Player[] players;

  public DataDisplayController(ICollectionBuilder collectionBuilder, String username) {
    collection = collectionBuilder.getCollection(username);
    plays = collectionBuilder.getPlays();
    players = collectionBuilder.getPlayers();
  }

  /**
   * @return an array of all games in the users collection.
   */
  public BoardGame[] getAllGames() {
    return collection.getGames();
  }

  /**
   * @return an array with the names of all games in the users collection.
   */
  public String[] getGameNames() {
    String[] names = new String[collection.getGames().length];

    for(int i = 0; i<collection.getGames().length; i++) {
      names[i] = collection.getGames()[i].name;
    }
    return names;
  }

  /**
   * @return all plays registered by the user.
   */
  public Play[] getAllPlays() {
    return plays.getAllPlays();
  }

  /**
   * @return an array of all players who the user has registered plays with.
   */
  public Player[] getAllPlayers() {
    players = InsertionSort.sortPlayers(players);
    return players;
  }

  /**
   * @return a BoardGame representation of the game described by the input parameter, or null if not in collection.
   */
  public BoardGame getGame(String name) {
    for (BoardGame game : getAllGames()) {
      if(game.name.equals(name)) return game;
    }
    return null;
  }

  /**
   * @return an integer describing the amount of games in the users collection.
   */
  public int getNumberOfGames() {
    return collection.getGames().length;
  }

  /**
   * @param uniqueID is the unique id that the game has.
   * @return an ArrayList of all plays of the chosen game.
   */
  public ArrayList<Play> getPlays(int uniqueID) {
    return plays.getPlays(uniqueID);
  }

  /**
   * @param name is the name of the game.
   * @return an ArrayList of all plays of the chosen game.
   */
  public ArrayList<Play> getPlays(String name) {
    return plays.getPlays(name);
  }

}
