package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.Play;
import Main.Containers.Player;
import Main.Models.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public class FacadeController {
  public final String username;
  private final IDataDisplayController dataDisplayController;

  public FacadeController(ICollectionBuilder collectionBuilder, String username) {
    this.username = username;
    dataDisplayController = new DataDisplayController(collectionBuilder, username);
  }

  public BoardGame[] getAllGames() {
    return dataDisplayController.getAllGames();
  }

  public Play[] getAllPlaysSorted() {
    return dataDisplayController.getAllPlaysSorted();
  }

  public Player[] getAllPlayers() {
    return dataDisplayController.getAllPlayersSorted();
  }
}
