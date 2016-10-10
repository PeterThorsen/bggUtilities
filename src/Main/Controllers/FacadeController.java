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

}
