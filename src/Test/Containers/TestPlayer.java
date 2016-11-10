package Test.Containers;

import Main.Containers.BoardGame;
import Main.Containers.Play;
import Main.Containers.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Peter on 10/11/2016.
 */
public class TestPlayer {
  private Player player;
  private String[] playerNames;
  private BoardGame game1 = new BoardGame("Agricola", 31260, 1, 5, 30, 150, String.valueOf(8), 0, "8.07978");
  private BoardGame game2 = new BoardGame("Camel Up", 153938, 1, 8, 20, 30, String.valueOf(8), 0, "7.118");


  @Before
  public void setup() {
    playerNames = new String[3];
    playerNames[0] = "Michelle";
    playerNames[1] = "Peter";
    playerNames[2] = "Charlotte";
    buildPlayer(new Play[0]);
  }

  private void buildPlayer(Play[] plays) {
    String name = "Martin";
    player = new Player(name, plays);
  }

  @Test
  public void shouldHaveName() {
    assertEquals("Martin", player.name);
  }

  @Test
  public void shouldHaveZeroTotalPlays() {
    assertEquals(0, player.totalPlays);
  }

  @Test
  public void shouldHaveTotalPlays1Given1PlayWithQuantity1() {

    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);

    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    assertEquals(1, player.totalPlays);
  }

  @Test
  public void shouldHaveTotalPlays2Given1PlayWithQuantity2() {

    Play play1 = new Play(game1, "2016-11-10", playerNames, 2);

    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    assertEquals(2, player.totalPlays);
  }

  @Test
  public void shouldHaveTotalPlays2Given2PlaysWithQuantity1() {
    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game1, "2016-11-10", playerNames, 1);

    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    assertEquals(2, player.totalPlays);
  }

  @Test
  public void shouldHaveMostPlayedGameAgricolaGivenMorePlaysThanOtherGames() {
    Play play1 = new Play(game1, "2016-11-10", playerNames, 2);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    assertEquals("Agricola", player.getMostPlayedGame().gameName);
  }

  @Test
  public void shouldHaveMostPlayedGameCamelUpGivenMorePlaysThanOtherGames() {
    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 2);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    assertEquals("Camel Up", player.getMostPlayedGame().gameName);
  }

  @Test
  public void shouldBeAbleToGetMapContainingPlaysOfEachGame() {
    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 2);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);
    HashMap<String, Integer> map = player.gameToPlaysMap;
    int valCamelUp = map.get("Camel Up");
    int valAgricola = map.get("Agricola");
    assertEquals(2, valCamelUp);
    assertEquals(1, valAgricola);
  }

  @Test
  public void shouldGetMostCommonFriendPeter() {
    String[] newPlayers = new String[1];
    newPlayers[0] = "Peter";

    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game1, "2016-11-10", newPlayers, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);
    assertEquals("Peter", player.getMostCommonFriend());
  }
  @Test
  public void shouldGetMostCommonFriendMichelle() {
    String[] newPlayers = new String[1];
    newPlayers[0] = "Michelle";

    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game1, "2016-11-10", newPlayers, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);
    assertEquals("Michelle", player.getMostCommonFriend());
  }

  @Test
  public void shouldBeAbleToGetMapContainingPlaysOfEachPlayer() {
    String[] newPlayers = new String[1];
    newPlayers[0] = "Michelle";

    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game1, "2016-11-10", newPlayers, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);
    HashMap<String, Integer> map = player.nameToPlaysMap;
    int valMichelle = map.get("Michelle");
    int valCharlotte = map.get("Charlotte");
    int valPeter = map.get("Peter");
    assertEquals(2, valMichelle);
    assertEquals(1, valCharlotte);
    assertEquals(1, valPeter);
  }

  @Test
  public void shouldShowCorrectMostRecentGame() {

    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    String gameName = player.getMostRecentGame();
    assertEquals("Camel Up", gameName);
  }
}