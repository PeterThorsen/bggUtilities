package Containers;

import Main.Containers.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Peter on 10/11/2016.
 */
public class TestPlayer {
  private Player player;
  private String[] playerNames;
  private BoardGame game1;
  private BoardGame game2;
  private BoardGame game3;

  {

  }


  @Before
  public void setup() {
    playerNames = new String[3];
    playerNames[0] = "Michelle";
    playerNames[1] = "Peter";
    playerNames[2] = "Charlotte";
    buildPlayer(new Play[0]);
    game1 = new BoardGame("Agricola", 31260, 1, 5, 30, 150, String.valueOf(8), 0, "8.07978", "strategygames");
    game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");
    game3 = new BoardGame("Camel Up", 153938, 1, 8, 20, 30, String.valueOf(8), 0, "7.118", "familygames");

    GameCategory[] cats = new GameCategory[3];
    cats[0] = new GameCategory("Animals");
    cats[1] = new GameCategory("Economic");
    cats[2] = new GameCategory("Farming");

    GameMechanism[] mechs = new GameMechanism[4];
    mechs[0] = new GameMechanism("Area Enclosure");
    mechs[1] = new GameMechanism("Card Drafting");
    mechs[2] = new GameMechanism("Hand Management");
    mechs[3] = new GameMechanism("Worker Placement");

    int[] bestWith = new int[2];
    bestWith[0] = 3;
    bestWith[1] = 4;

    int[] recommendedWith = new int[2];
    recommendedWith[0] = 1;
    recommendedWith[1] = 2;
    game1.addExpandedGameInfo(2.3453, false, cats, mechs, bestWith, recommendedWith);

    GameCategory[] cats2 = new GameCategory[2];
    cats2[0] = new GameCategory("Abstract Strategy");
    cats2[1] = new GameCategory("Animals");

    GameMechanism[] mechs2 = new GameMechanism[2];
    mechs2[0] = new GameMechanism("Grid Movement");
    mechs2[1] = new GameMechanism("Tile Placement");

    int[] bestWith2 = new int[1];
    bestWith[0] = 2;

    game2.addExpandedGameInfo(3.6298, false, cats2, mechs2, bestWith2, new int[0]);
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
    Play play2 = new Play(game3, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    assertEquals("Agricola", player.getMostPlayedGame().game.getName());
  }

  @Test
  public void shouldHaveMostPlayedGameCamelUpGivenMorePlaysThanOtherGames() {
    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game3, "2016-11-10", playerNames, 2);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    assertEquals("Camel Up", player.getMostPlayedGame().game.getName());
  }

  @Test
  public void shouldBeAbleToGetMapContainingPlaysOfEachGame() {
    Play play1 = new Play(game1, "2016-11-10", playerNames, 1);
    Play play2 = new Play(game3, "2016-11-10", playerNames, 2);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);
    HashMap<BoardGame, Integer> map = player.gameToPlaysMap;
    int valCamelUp = map.get(game3);
    int valAgricola = map.get(game1);
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
    HashMap<String, Integer> map = player.playerNameToPlaysMap;
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
    Play play2 = new Play(game3, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    String gameName = player.getMostRecentGame();
    assertEquals("Camel Up", gameName);
  }

  @Test
  public void hasPlayedAgricolaShouldReturnTrue() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    assertTrue(player.hasPlayed(game1));
  }

  @Test
  public void hasPlayedHiveShouldReturnFalse() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");

    assertFalse(player.hasPlayed(game2));
  }

  @Test
  public void shouldHaveAverageComplexityStoredGivenOnePlay() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    assertEquals(player.getAverageComplexity(), game1.getComplexity(), 0);
  }

  @Test
  public void shouldHaveAverageComplexityStoredGivenTwoPlays() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    double expected = (game1.getComplexity() + game2.getComplexity())/2;

    assertEquals(expected, player.getAverageComplexity(), 0);
  }

  @Test
  public void shouldHaveMaxComplexityStoredGivenOnePlay() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    assertEquals(player.getMaxComplexity(), game1.getComplexity(), 0);
  }

  @Test
  public void shouldHaveMaxComplexityStoredGivenTwoPlays() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    double expected = Math.max(game1.getComplexity(), game2.getComplexity());

    assertEquals(expected, player.getMaxComplexity(), 0);
  }
  @Test
  public void shouldHaveMinComplexityStoredGivenOnePlay() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays);

    assertEquals(game1.getComplexity(), player.getMinComplexity(), 0);
  }

  @Test
  public void shouldHaveMinComplexityStoredGivenTwoPlays() {
    Play play1 = new Play(game1, "2016-11-8", playerNames, 1);
    Play play2 = new Play(game2, "2016-11-10", playerNames, 1);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays);

    double expected = Math.min(game1.getComplexity(), game2.getComplexity());

    assertEquals(expected, player.getMinComplexity(), 0);
  }
}