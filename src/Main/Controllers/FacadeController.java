package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Models.Storage.ICollectionBuilder;

import java.util.ArrayList;

/**
 * Created by Peter on 05/10/2016.
 */
public class FacadeController {
  private final String username;
  private IDataDisplayController dataDisplayController;

  public FacadeController(ICollectionBuilder collectionBuilder, String username) {
    this.username = username;
    dataDisplayController = new DataDisplayController(collectionBuilder, username);
  }

  public ArrayList<BoardGame> getAllGames() {
    return dataDisplayController.getAllGames();
  }

  public int getNumberOfGames() {
    return dataDisplayController.getNumberOfGames();
  }

  public String[] getAllGameNames() {
    return dataDisplayController.getGameNames();
  }

  public int[] getAllMinLengths() {
    return dataDisplayController.getMinLengths();
  }

  public int[] getAllMaxLengths() {
    return dataDisplayController.getMaxLengths();
  }

  public double[] getAllComplexities() {
    return dataDisplayController.getComplexities();
  }

  public int[] getAllMinPlayers() {
    return dataDisplayController.getMinPlayers();
  }

  public int[] getAllMaxPlayers() {
    return dataDisplayController.getMaxPlayers();
  }

  public int[] getAllNumberOfPlays() {
    return dataDisplayController.getNumberOfPlays();
  }

  public String[] getAllPersonalRatings() {
    return dataDisplayController.getPersonalRatings();
  }

  public String[] getAllAverageRatings() {
    return dataDisplayController.getAverageRatings();
  }
}
