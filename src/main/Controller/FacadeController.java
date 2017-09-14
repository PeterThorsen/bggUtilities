package Controller;

import Controller.SubControllers.DataDisplayController;
import Controller.SubControllers.LogicController;
import Model.Logic.GameNightRecommender;
import Model.Logic.IGameNightValues;
import Model.Storage.ICollectionBuilder;
import Model.Structure.*;

/**
 * Created by Peter on 05/10/2016.
 */
public class FacadeController {
  public final String username;
  private final DataDisplayController dataController;
  private final LogicController logicController;

  public FacadeController(ICollectionBuilder collectionBuilder, String username, IGameNightValues gameNightValues) {
    this.username = username;
    dataController = new DataDisplayController(collectionBuilder, username);
    logicController = new LogicController(this, new GameNightRecommender(gameNightValues));
  }

  public BoardGame[] getAllGames() {
    return dataController.getAllGames();
  }

  public BoardGame getGame(String name) { return dataController.getGame(name);}

  public Play[] getAllPlaysSorted() {
    return dataController.getAllPlays();
  }

  public Player[] getAllPlayers() {
    return dataController.getAllPlayers();
  }

  public BoardGameSuggestion suggestGames(Player[] array, int maxTime) {
    return logicController.suggestGamesForGameNight(array, maxTime, getAllGames());
  }

  public BoardGameCounter[] getBestCombinationForGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime) {
    return logicController.getBestCombinationForGame(actualSuggestionAsGames, players, maxTime);
  }
}
