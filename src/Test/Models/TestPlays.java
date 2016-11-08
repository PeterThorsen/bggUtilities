package Test.Models;

import Main.Containers.BoardGameCollection;
import Main.Containers.Plays;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Test.Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Peter on 07/11/2016.
 */
public class TestPlays {
  Plays plays;
  BoardGameCollection collection;

  @Before
  public void setup() {
    IConnectionHandler connectionHandler = new ConnectionHandlerStub();
    ICollectionBuilder collectionBuilder = new CollectionBuilder(connectionHandler);
    String username = "cwaq";
    collection = collectionBuilder.getCollection(username);
    plays = collectionBuilder.getPlays();
  }

  @Test
  public void shouldHaveMartinAsPlayer() {

    boolean exists = false;
    for (String player : plays.getPlayerNames()) {
      if(player.equals("Martin")) {
        exists = true;
        break;
      }
    }
    assertTrue(exists);
  }



  ///
/**
  @Test
  public void hivePlayShouldBeOnDate_2016_09_14() {


    BoardGame game = games.get(20);
    ArrayList<Play> plays = game.getPlays();
    boolean correctDateFound = false;

    for (Play play : plays) {
      String date = play.getDate();
      if (date.equals("2016-09-14")) {
        correctDateFound = true;
      }
    }
    assertTrue(correctDateFound);
  }

  @Test
  public void dixitShouldHavePlayOnDate_2016_09_09() {
    BoardGame game = games.get(13);
    ArrayList<Play> plays = game.getPlays();
    boolean correctDateFound = false;

    for (Play play : plays) {
      String date = play.getDate();
      if (date.equals("2016-09-09")) {
        correctDateFound = true;
      }
    }
    assertTrue(correctDateFound);
  }

  @Test
  public void dixitShouldHave2PlaysOnDate_2016_09_09() {
    BoardGame game = games.get(13);
    ArrayList<Play> plays = game.getPlays();
    boolean correctNoOfPlaysFound = false;

    for (Play play : plays) {
      String date = play.getDate();
      if (date.equals("2016-09-09")) {
        if (play.noOfPlays() == 2) {
          correctNoOfPlaysFound = true;
        }
      }
    }
    assertTrue(correctNoOfPlaysFound);
  }

  @Test
  public void dixitShouldHavePlayerNameMartinOnDate_2016_09_09() {
    BoardGame game = games.get(13);
    ArrayList<Play> plays = game.getPlays();
    boolean correctNameFound = false;

    for (Play play : plays) {
      String date = play.getDate();
      if (date.equals("2016-09-09")) {
        String[] players = play.getPlayers();
        for (String player : players) {
          if (player.equals("Martin")) {
            correctNameFound = true;
          }
        }
      }
    }
    assertTrue(correctNameFound);
  } */
}
