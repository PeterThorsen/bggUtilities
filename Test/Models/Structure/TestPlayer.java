package Models.Structure;

import Main.Models.Structure.*;
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
  private BoardGame gameAgricola;
  private BoardGame gameHive;
  private BoardGame gameCamelUp;
  private HashMap<String, Double> agricolaRatings;
  private HashMap<String, Double> hiveRatings;
  private HashMap<String, Double> camelUpRatings;

  @Before
  public void setup() {
    playerNames = new String[3];
    playerNames[0] = "Michelle";
    playerNames[1] = "Peter";
    playerNames[2] = "Charlotte";
    buildPlayer(new Play[0], "Martin");
    gameAgricola = new BoardGame("Agricola", 31260, 1, 5, 30, 150, String.valueOf(8), 0, "8.07978", "strategygames");
    gameHive = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");
    gameCamelUp = new BoardGame("Camel Up", 153938, 1, 8, 20, 30, String.valueOf(8), 0, "7.118", "familygames");

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
    gameAgricola.addExpandedGameInfo(3.6298, false, cats, mechs, bestWith, recommendedWith);

    GameCategory[] cats2 = new GameCategory[2];
    cats2[0] = new GameCategory("Abstract Strategy");
    cats2[1] = new GameCategory("Animals");

    GameMechanism[] mechs2 = new GameMechanism[2];
    mechs2[0] = new GameMechanism("Grid Movement");
    mechs2[1] = new GameMechanism("Tile Placement");

    int[] bestWith2 = new int[1];
    bestWith[0] = 2;

    gameHive.addExpandedGameInfo(2.3453, false, cats2, mechs2, bestWith2, new int[0]);

    agricolaRatings = new HashMap<>();
    agricolaRatings.put(playerNames[0], 0.0);
    agricolaRatings.put(playerNames[1], 8.0);
    agricolaRatings.put(playerNames[2], 6.0);

    hiveRatings = new HashMap<>();
    hiveRatings.put(playerNames[0], 0.0);
    hiveRatings.put(playerNames[1], 10.0);
    hiveRatings.put(playerNames[2], 4.0);

    camelUpRatings = new HashMap<>();
    camelUpRatings.put(playerNames[0], 0.0);
    camelUpRatings.put(playerNames[1], 8.0);
    camelUpRatings.put(playerNames[2], 7.0);
  }

  private void buildPlayer(Play[] plays, String name) {
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

    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);

    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    assertEquals(1, player.totalPlays);
  }

  @Test
  public void shouldHaveTotalPlays2Given1PlayWithQuantity2() {

    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 2, null);

    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    assertEquals(2, player.totalPlays);
  }

  @Test
  public void shouldHaveTotalPlays2Given2PlaysWithQuantity1() {
    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);
    Play play2 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);

    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    assertEquals(2, player.totalPlays);
  }

  @Test
  public void shouldHaveMostPlayedGameAgricolaGivenMorePlaysThanOtherGames() {
    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 2, null);
    Play play2 = new Play(gameCamelUp, "2016-11-10", playerNames, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    assertEquals("Agricola", player.getMostPlayedGame().game.name);
  }

  @Test
  public void shouldHaveMostPlayedGameCamelUpGivenMorePlaysThanOtherGames() {
    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);
    Play play2 = new Play(gameCamelUp, "2016-11-10", playerNames, 2, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    assertEquals("Camel Up", player.getMostPlayedGame().game.name);
  }

  @Test
  public void shouldBeAbleToGetMapContainingPlaysOfEachGame() {
    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);
    Play play2 = new Play(gameCamelUp, "2016-11-10", playerNames, 2, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");
    HashMap<BoardGame, Integer> map = player.gameToPlaysMap;
    int valCamelUp = map.get(gameCamelUp);
    int valAgricola = map.get(gameAgricola);
    assertEquals(2, valCamelUp);
    assertEquals(1, valAgricola);
  }

  @Test
  public void shouldGetMostCommonFriendPeter() {
    String[] newPlayers = new String[1];
    newPlayers[0] = "Peter";

    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);
    Play play2 = new Play(gameAgricola, "2016-11-10", newPlayers, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");
    assertEquals("Peter", player.getMostCommonFriend());
  }
  @Test
  public void shouldGetMostCommonFriendMichelle() {
    String[] newPlayers = new String[1];
    newPlayers[0] = "Michelle";

    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);
    Play play2 = new Play(gameAgricola, "2016-11-10", newPlayers, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");
    assertEquals("Michelle", player.getMostCommonFriend());
  }

  @Test
  public void shouldBeAbleToGetMapContainingPlaysOfEachPlayer() {
    String[] newPlayers = new String[1];
    newPlayers[0] = "Michelle";

    Play play1 = new Play(gameAgricola, "2016-11-10", playerNames, 1, null);
    Play play2 = new Play(gameAgricola, "2016-11-10", newPlayers, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");
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

    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play play2 = new Play(gameCamelUp, "2016-11-10", playerNames, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    String gameName = player.getMostRecentGame();
    assertEquals("Camel Up", gameName);
  }

  @Test
  public void hasPlayedAgricolaShouldReturnTrue() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    assertTrue(player.hasPlayed(gameAgricola));
  }

  @Test
  public void hasPlayedHiveShouldReturnFalse() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");

    assertFalse(player.hasPlayed(game2));
  }

  @Test
  public void shouldHaveAverageComplexityStoredGivenOnePlay() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    assertEquals(player.getAverageComplexity(), gameAgricola.complexity, 0);
  }

  @Test
  public void shouldHaveAverageComplexityStoredGivenTwoPlays() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play play2 = new Play(gameHive, "2016-11-10", playerNames, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    double expected = (gameAgricola.complexity + gameHive.complexity)/2;

    assertEquals(expected, player.getAverageComplexity(), 0);
  }

  @Test
  public void shouldHaveMaxComplexityStoredGivenOnePlay() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    assertEquals(player.getMaxComplexity(), gameAgricola.complexity, 0);
  }

  @Test
  public void shouldHaveMaxComplexityStoredGivenTwoPlays() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play play2 = new Play(gameHive, "2016-11-10", playerNames, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    double expected = Math.max(gameAgricola.complexity, gameHive.complexity);

    assertEquals(expected, player.getMaxComplexity(), 0);
  }
  @Test
  public void shouldHaveMinComplexityStoredGivenOnePlay() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Martin");

    assertEquals(gameAgricola.complexity, player.getMinComplexity(), 0);
  }

  @Test
  public void shouldHaveMinComplexityStoredGivenTwoPlays() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, null);
    Play play2 = new Play(gameHive, "2016-11-10", playerNames, 1, null);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Martin");

    double expected = Math.min(gameAgricola.complexity, gameHive.complexity);

    assertEquals(expected, player.getMinComplexity(), 0);
  }

  @Test
  public void getAverageRatingOfGamesAboveComplexityShouldWorkWithOneGame() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, agricolaRatings);
    Play[] plays = new Play[1];
    plays[0] = play1;
    buildPlayer(plays, "Peter");

    assertEquals(agricolaRatings.get(player.name), player.getAverageRatingOfGamesAboveComplexity(1), 0);
  }

  @Test
  public void getAverageRatingOfGamesAboveComplexityShouldWorkWithTwoGamesAndMinimalGivenValue() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, agricolaRatings);
    Play play2 = new Play(gameHive, "2016-11-10", playerNames, 1, hiveRatings);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Peter");

    assertEquals((agricolaRatings.get(player.name) + hiveRatings.get(player.name))/2, player.getAverageRatingOfGamesAboveComplexity(1), 0);
  }

  @Test
  public void getAverageRatingOfGamesAboveComplexityShouldWorkWithTwoGamesAndComplexityAbove3() {
    Play play1 = new Play(gameAgricola, "2016-11-8", playerNames, 1, agricolaRatings);
    Play play2 = new Play(gameHive, "2016-11-10", playerNames, 1, hiveRatings);
    Play[] plays = new Play[2];
    plays[0] = play1;
    plays[1] = play2;
    buildPlayer(plays, "Peter");

    assertEquals(agricolaRatings.get(player.name), player.getAverageRatingOfGamesAboveComplexity(3), 0);
  }


}