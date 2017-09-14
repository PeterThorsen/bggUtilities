package Models.Structure;

import Model.Network.IConnectionHandler;
import Model.Storage.CollectionBuilder;
import Model.Storage.ICollectionBuilder;
import Model.Structure.BoardGame;
import Model.Structure.BoardGameCollection;
import Model.Structure.Play;
import Model.Structure.Plays;
import Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by Peter on 07/11/2016.
 */
public class TestPlays {
  Plays plays;
  BoardGameCollection collection;
  private BoardGame[] games;

  @Before
  public void setup() {
    IConnectionHandler connectionHandler = new ConnectionHandlerStub();
    ICollectionBuilder collectionBuilder = new CollectionBuilder(connectionHandler);
    String username = "cwaq";
    collection = collectionBuilder.getCollection(username);
    games = collection.getGames();
    plays = collectionBuilder.getPlays();
  }

  @Test
  public void shouldHaveMartinAsPlayer() {

    boolean exists = false;
    for (String player : plays.getPlayerNames()) {
      if (player.equals("Martin")) {
        exists = true;
        break;
      }
    }
    assertTrue(exists);
  }

  @Test
  public void hivePlayShouldBeOnDate_2016_09_14() {
    ArrayList<Play> hivePlays = plays.getPlays("Hive");
    boolean exists = false;
    for (Play play : hivePlays) {
      if (play.date.equals("2016-09-14")) {
        exists = true;
        break;
      }
    }
    assertTrue(exists);

  }

  @Test
  public void dixitShouldHavePlayOnDate_2016_09_09() {
    ArrayList<Play> dixitPlays = plays.getPlays("Dixit Odyssey");
    boolean exists = false;
    for (Play play : dixitPlays) {
      if (play.date.equals("2016-09-09")) {
        exists = true;
        break;
      }
    }
    assertTrue(exists);
  }

  @Test
  public void dixitShouldHave2PlaysOnDate_2016_09_09() {
    ArrayList<Play> dixitPlays = plays.getPlays(92828);
    boolean exists = false;
    for (Play play : dixitPlays) {
      if (play.date.equals("2016-09-09") && play.noOfPlays == 2) {
        exists = true;
        break;

      }
    }
    assertTrue(exists);
  }

  @Test
  public void dixitShouldHavePlayerNameMartinOnDate_2016_09_09() {
    ArrayList<Play> dixitPlays = plays.getPlays(92828);
    boolean exists = false;
    for (Play play : dixitPlays) {
      if (play.date.equals("2016-09-09")) {
        for (String name : play.playerNames) {
          if(name.equals("Martin")) {
            exists = true;
            break;
          }
        }
      }
    }
    assertTrue(exists);
  }
}
