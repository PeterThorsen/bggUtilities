import Main.Controllers.FacadeController;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Main.Models.Structure.*;
import Main.Sorting.InsertionSort;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

@Ignore
public class DataAnalysis {
  private FacadeController facadeController;
  private Play[] allPlays;
  private BoardGame[] allGames;
  private Player[] allPlayers;

  @Before
  public void setUp() {
    ICollectionBuilder collectionBuilder = new CollectionBuilder(new ConnectionHandler());
    String username = "cwaq";
    facadeController = new FacadeController(collectionBuilder, username);
    allGames = facadeController.getAllGames();
    allPlays = facadeController.getAllPlaysSorted();
    allPlayers = facadeController.getAllPlayers();
  }

  @Test
  public void listAllGamesWithLastPlayedDate() {
    HashMap<BoardGame, String> firstDatesOfGames = new HashMap<>();
    for (Play play : allPlays) {
      boolean shouldContinue = false;
      for (String name : play.playerNames) {
        if (name.equals("Charlotte")) {
          shouldContinue = true;
        }
      }
      if (!shouldContinue) continue;
      BoardGame game = play.game;
      if (firstDatesOfGames.containsKey(game)) continue;
      firstDatesOfGames.put(game, play.date);
    }

    for (BoardGame game : firstDatesOfGames.keySet()) {
      System.out.println(game + " : " + firstDatesOfGames.get(game));
    }
  }

  @Test
  public void listGamesComplexitiesAndPlayersPersonalComplexities() {
    for (Player player : allPlayers) {
      System.out.println(player + " : " + player.getMagicComplexity() + " (" + player.getAverageComplexity() + ")");
    }
  }

  @Test
  public void makeRecommendation() {
    //String[] users = new String[]{"Charlotte", "Alf", "Mikkel", "Lisbeth"};
    String[] users = new String[]{"Charlotte", "Martin", "Michelle", "Emil", "Signe", "Michael"};

    int timeToPlay = 120;
    Player[] players = new Player[users.length];
    int counter = 0;
    for (Player player : allPlayers) {
      for (String user : users) {
        if (player.name.equals(user)) {
          players[counter] = player;
          counter++;
        }
      }
    }

    BoardGameSuggestion recommendation = facadeController.suggestGames(players, timeToPlay);
    System.out.println("Recommendation is: ");
    for (BoardGameCounter bgCounter: recommendation.suggestedCombination) {
      BoardGame game = bgCounter.game;
      ArrayList<Reason> reasons = bgCounter.reasons;
      Reason[] reasonArray = InsertionSort.sortReasons(reasons);
      System.out.println("---- Game: " + game + " ----");
      for (Reason reason : reasonArray) {
        System.out.println(reason);
      }
    }
  }

}
