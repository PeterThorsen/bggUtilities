import Main.Controllers.FacadeController;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Main.Models.Structure.BoardGame;
import Main.Models.Structure.Play;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

@Ignore
public class DataAnalysis {
  private FacadeController facadeController;
  private Play[] allPlays;
  private BoardGame[] allGames;

  @Before
  public void setUp() {
    ICollectionBuilder collectionBuilder = new CollectionBuilder(new ConnectionHandler());
    String username = "cwaq";
    facadeController = new FacadeController(collectionBuilder, username);
    allGames = facadeController.getAllGames();
    allPlays = facadeController.getAllPlaysSorted();
  }

  @Test
  public void listAllGamesWithLastPlayedDate() {
    HashMap<BoardGame, String> firstDatesOfGames = new HashMap<>();
    for (Play play : allPlays) {
      boolean shouldContinue = false;
      for (String name : play.playerNames) {
        if(name.equals("Charlotte")) {
          shouldContinue = true;
        }
      }
      if(!shouldContinue) continue;
      BoardGame game = play.game;
      if(firstDatesOfGames.containsKey(game)) continue;
      firstDatesOfGames.put(game, play.date);
    }

    for (BoardGame game : firstDatesOfGames.keySet()) {
      System.out.println(game + " : " + firstDatesOfGames.get(game));
    }
  }

}
