package Main.Controllers;

import Main.Models.Logic.GameNightRecommender;
import Main.Models.Structure.BoardGame;
import Main.Models.Structure.BoardGameSuggestion;
import Main.Models.Structure.Play;
import Main.Models.Structure.Player;
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
    logicController = new LogicController(this, new GameNightRecommender());
  }

  public BoardGame[] getAllGames() {
    return dataController.getAllGames();
  }

  public Play[] getAllPlaysSorted() {
    return dataController.getAllPlays();
  }

  public Player[] getAllPlayers() {
    return dataController.getAllPlayers();
  }

  public BoardGameSuggestion suggestGames(Player[] array, int maxTime) {
    return logicController.suggestGamesForGameNight(array, maxTime);
  }
}
