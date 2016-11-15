package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameSuggestion;
import Main.Containers.Play;
import Main.Containers.Player;
import Main.Models.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public class FacadeController {
  public final String username;
  private final IDataController dataController;
  private final ILogicController logicController;

  public FacadeController(ICollectionBuilder collectionBuilder, String username) {
    this.username = username;
    dataController = new DataDisplayController(collectionBuilder, username);
    logicController = new LogicController(this);
  }

  public BoardGame[] getAllGames() {
    return dataController.getAllGames();
  }

  public Play[] getAllPlaysSorted() {
    return dataController.getAllPlaysSorted();
  }

  public Player[] getAllPlayers() {
    return dataController.getAllPlayersSorted();
  }

  public BoardGameSuggestion suggestGames(Player[] array, int maxTime) {
    return logicController.suggestGames(array, maxTime);
  }
}
