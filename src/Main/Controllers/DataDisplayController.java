package Main.Controllers;

import Main.Containers.*;
import Main.Models.Storage.ICollectionBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Peter on 03/10/16.
 * Will be used for connection to the model (ICollectionBuilder) and the view.
 */
public class DataDisplayController implements IDataDisplayController {
  BoardGameCollection collection;
  Plays plays;

  public DataDisplayController(ICollectionBuilder collectionBuilder, String username) {
    collection = collectionBuilder.getCollection(username);
    plays = collectionBuilder.getPlays();
  }

  @Override
  public ArrayList<BoardGame> getAllGames() {
    return collection.getGames();
  }

  public String[] getGameNames() {
    String[] names = new String[collection.getGames().size()];

    for(int i = 0; i<collection.getGames().size(); i++) {
      names[i] = collection.getGames().get(i).getName();
    }
    return names;
  }

  @Override
  public double[] getComplexities() {
    double[] complexities = new double[collection.getGames().size()];
    for (int i = 0; i < complexities.length; i++) {
      complexities[i] = collection.getGames().get(i).getComplexity();
    }
    return complexities;
  }

  @Override
  public int[] getMaxLengths() {
    int[] maxLengths = new int[collection.getGames().size()];
    for (int i = 0; i < maxLengths.length; i++) {
      maxLengths[i] = collection.getGames().get(i).getMaxPlaytime();
    }
    return maxLengths;
  }

  @Override
  public int[] getMinLengths() {
    int[] minLengths = new int[collection.getGames().size()];
    for (int i = 0; i < minLengths.length; i++) {
      minLengths[i] = collection.getGames().get(i).getMinPlaytime();
    }
    return minLengths;
  }

  @Override
  public int[] getMinPlayers() {
    int[] minPlayers = new int[collection.getGames().size()];
    for (int i = 0; i < minPlayers.length; i++) {
      minPlayers[i] = collection.getGames().get(i).getMinPlayers();
    }
    return minPlayers;
  }

  @Override
  public int[] getMaxPlayers() {
    int[] maxPlayers = new int[collection.getGames().size()];
    for (int i = 0; i < maxPlayers.length; i++) {
      maxPlayers[i] = collection.getGames().get(i).getMaxPlayers();
    }
    return maxPlayers;
  }

  @Override
  public int[] getNumberOfPlays() {
    int[] playsArray = new int[collection.getGames().size()];
    for (int i = 0; i < playsArray.length; i++) {
      playsArray[i] = collection.getGames().get(i).getNumberOfPlays();
    }
    return playsArray;
  }

  @Override
  public String[] getPersonalRatings() {
    String[] personalRatings = new String[collection.getGames().size()];
    for (int i = 0; i < personalRatings.length; i++) {
      personalRatings[i] = collection.getGames().get(i).getPersonalRating();
    }
    return personalRatings;
  }

  @Override
  public String[] getAverageRatings() {
    String[] averageRatings = new String[collection.getGames().size()];
    for (int i = 0; i < averageRatings.length; i++) {
      averageRatings[i] = collection.getGames().get(i).getAverageRating();
    }
    return averageRatings;
  }

  @Override
  public String[] getPlayerNames() {
    String[] names = plays.getPlayerNames();
    return names;
  }

  @Override
  public HashMap<String, Integer> getNumberOfPlaysByPlayers() {
    return plays.getPlayerToPlaysMap();
  }

  @Override
  public int getNumberOfPlayers() {
    return plays.getNumberOfPlayers();
  }

  @Override
  public HashMap<String, GameNameAndPlayHolder> getMostPlayedGamesByPlayers() {
    return plays.getMostPlayedGamesByPlayers();
  }

  @Override
  public HashMap<String, String> getDateOfLastPlayForEachPlayer() {
    HashMap<String, String> lastPlayDates = new HashMap<>(); // key = player name, value = game name
    String[] names = getPlayerNames();
    for (String name : names) {
      String gameName = plays.getLastPlayedGame(name);
      lastPlayDates.put(name, gameName);
    }
    return lastPlayDates;
  }

  public int getNumberOfGames() {
    return collection.getGames().size();
  }

  public ArrayList<Play> getPlays(int uniqueID) {
    return plays.getPlays(uniqueID);
  }

  public ArrayList<Play> getPlays(String name) {
    return plays.getPlays(name);
  }

}
