package Main.Controllers;

import Main.Containers.*;
import Main.Models.Storage.ICollectionBuilder;

import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 * Will be used for connection to the model (ICollectionBuilder) and the view.
 */
public class DataDisplayController implements IDataDisplayController {
  private final BoardGameCollection collection;
  private final Plays plays;
  private final Player[] players;

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
      names[i] = collection.getGames()[i].getName();
    }
    return names;
  }

  @Override
  public double[] getComplexities() {
    double[] complexities = new double[collection.getGames().length];
    for (int i = 0; i < complexities.length; i++) {
      complexities[i] = collection.getGames()[i].getComplexity();
    }
    return complexities;
  }

  @Override
  public int[] getMaxLengths() {
    int[] maxLengths = new int[collection.getGames().length];
    for (int i = 0; i < maxLengths.length; i++) {
      maxLengths[i] = collection.getGames()[i].getMaxPlaytime();
    }
    return maxLengths;
  }

  @Override
  public int[] getMinLengths() {
    int[] minLengths = new int[collection.getGames().length];
    for (int i = 0; i < minLengths.length; i++) {
      minLengths[i] = collection.getGames()[i].getMinPlaytime();
    }
    return minLengths;
  }

  @Override
  public int[] getMinPlayers() {
    int[] minPlayers = new int[collection.getGames().length];
    for (int i = 0; i < minPlayers.length; i++) {
      minPlayers[i] = collection.getGames()[i].getMinPlayers();
    }
    return minPlayers;
  }

  @Override
  public int[] getMaxPlayers() {
    int[] maxPlayers = new int[collection.getGames().length];
    for (int i = 0; i < maxPlayers.length; i++) {
      maxPlayers[i] = collection.getGames()[i].getMaxPlayers();
    }
    return maxPlayers;
  }

  @Override
  public int[] getNumberOfPlays() {
    int[] playsArray = new int[collection.getGames().length];
    for (int i = 0; i < playsArray.length; i++) {
      playsArray[i] = collection.getGames()[i].getNumberOfPlays();
    }
    return playsArray;
  }

  @Override
  public String[] getPersonalRatings() {
    String[] personalRatings = new String[collection.getGames().length];
    for (int i = 0; i < personalRatings.length; i++) {
      personalRatings[i] = collection.getGames()[i].getPersonalRating();
    }
    return personalRatings;
  }

  @Override
  public String[] getAverageRatings() {
    String[] averageRatings = new String[collection.getGames().length];
    for (int i = 0; i < averageRatings.length; i++) {
      averageRatings[i] = collection.getGames()[i].getAverageRating();
    }
    return averageRatings;
  }

  @Override
  public String[] getPlayerNames() {
    return plays.getPlayerNames();
  }

  @Override
  public Play[] getAllPlaysSorted() {
    return plays.getAllPlaysSorted();
  }

  @Override
  public Player[] getAllPlayers() {
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
