package Main.Controllers;

import Main.Models.Storage.ICollectionBuilder;
import Main.Models.Structure.*;
import Main.Sorting.InsertionSort;

import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 * Will be used for connection to the model (ICollectionBuilder) and the view.
 */
public class DataDisplayController implements IDataController {
  private final BoardGameCollection collection;
  private final Plays plays;
  private Player[] players;

  public DataDisplayController(ICollectionBuilder collectionBuilder, String username) {
    collection = collectionBuilder.getCollection(username);
    plays = collectionBuilder.getPlays();
    players = collectionBuilder.getPlayers();
  }

  @Override
  public BoardGame[] getAllGames() {
    return collection.getGames();
  }

  public String[] getGameNames() {
    String[] names = new String[collection.getGames().length];

    for(int i = 0; i<collection.getGames().length; i++) {
      names[i] = collection.getGames()[i].name;
    }
    return names;
  }

  @Override
  public Play[] getAllPlays() {
    return plays.getAllPlays();
  }

  @Override
  public Player[] getAllPlayers() {
    players = InsertionSort.sortPlayers(players);
    return players;
  }

  public int getNumberOfGames() {
    return collection.getGames().length;
  }

  public ArrayList<Play> getPlays(int uniqueID) {
    return plays.getPlays(uniqueID);
  }

  public ArrayList<Play> getPlays(String name) {
    return plays.getPlays(name);
  }

}
