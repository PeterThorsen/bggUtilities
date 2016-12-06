package Containers;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Holders.PlayerNodeInformationHolder;
import Main.Containers.Play;
import Main.Containers.Plays;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
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
      if (play.getDate().equals("2016-09-14")) {
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
      if (play.getDate().equals("2016-09-09")) {
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
      if (play.getDate().equals("2016-09-09") && play.getQuantity() == 2) {
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
      if (play.getDate().equals("2016-09-09")) {
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
