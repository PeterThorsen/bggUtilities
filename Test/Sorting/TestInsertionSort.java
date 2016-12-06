package Sorting;

import Main.Containers.*;
import Main.Containers.Holders.StringIntHolder;
import Main.Sorting.InsertionSortGamesWithCounter;
import Main.Sorting.InsertionSortPlayers;
import Main.Sorting.InsertionSortStringIntHolder;
import Main.Sorting.InsertionSortStrings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Peter on 12-Nov-16.
 */
public class TestInsertionSort {

  @Test
  public void sortPlayers_returnOnlyPlayerWhenGivenOne() {
    Player player1 = new Player("Peter", new Play[0]);
    Player[] allPlayers = new Player[1];
    allPlayers[0] = player1;

    allPlayers = InsertionSortPlayers.sort(allPlayers);
    assertEquals("Peter", allPlayers[0].name);
  }

  @Test
  public void sortPlayers_ReturnLowerPlayerLexically() {
    Player player1 = new Player("Peter", new Play[0]);
    Player player2 = new Player("Anna", new Play[0]);
    Player[] allPlayers = new Player[2];
    allPlayers[0] = player1;
    allPlayers[1] = player2;

    allPlayers = InsertionSortPlayers.sort(allPlayers);
    assertEquals("Anna", allPlayers[0].name);
    assertEquals("Peter", allPlayers[1].name);
  }

  @Test
  public void sortPlayers_sortPlayersIgnoringUppercase() {
    Player player1 = new Player("Peter", new Play[0]);
    Player player2 = new Player("anna", new Play[0]);
    Player[] allPlayers = new Player[2];
    allPlayers[0] = player1;
    allPlayers[1] = player2;

    allPlayers = InsertionSortPlayers.sort(allPlayers);
    assertEquals("anna", allPlayers[0].name);
    assertEquals("Peter", allPlayers[1].name);
  }

  @Test
  public void sortPlayers_sortThreePlayersCorrectly() {
    Player player1 = new Player("Peter", new Play[0]);
    Player player2 = new Player("Anna", new Play[0]);
    Player player3 = new Player("Ben", new Play[0]);
    Player[] allPlayers = new Player[3];
    allPlayers[0] = player1;
    allPlayers[1] = player2;
    allPlayers[2] = player3;

    allPlayers = InsertionSortPlayers.sort(allPlayers);
    assertEquals("Anna", allPlayers[0].name);
    assertEquals("Ben", allPlayers[1].name);
    assertEquals("Peter", allPlayers[2].name);
  }

  @Test
  public void sortStrings_returnOnlyStringGivenOne() {
    String str1 = "first string";

    String[] allStrings = new String[1];
    allStrings[0] = str1;

    allStrings = InsertionSortStrings.sort(allStrings);
    assertEquals(str1, allStrings[0]);
  }

  @Test
  public void sortStrings_sortTwoStringsLexicallyCorrect() {
    String str1 = "first string";
    String str2 = "another string";


    String[] allStrings = new String[2];
    allStrings[0] = str1;
    allStrings[1] = str2;

    allStrings = InsertionSortStrings.sort(allStrings);
    assertEquals(str2, allStrings[0]);
    assertEquals(str1, allStrings[1]);
  }

  @Test
  public void sortStrings_sortStringsIgnoringUppercase() {
    String str1 = "First string";
    String str2 = "another string";


    String[] allStrings = new String[2];
    allStrings[0] = str1;
    allStrings[1] = str2;

    allStrings = InsertionSortStrings.sort(allStrings);
    assertEquals(str2, allStrings[0]);
    assertEquals(str1, allStrings[1]);
  }

  @Test
  public void sortStrings_sortThreeStringsCorrectly() {
    String str1 = "first string";
    String str2 = "another string";
    String str3 = "bit";


    String[] allStrings = new String[3];
    allStrings[0] = str1;
    allStrings[1] = str2;
    allStrings[2] = str3;


    allStrings = InsertionSortStrings.sort(allStrings);
    assertEquals(str2, allStrings[0]);
    assertEquals(str3, allStrings[1]);
    assertEquals(str1, allStrings[2]);
  }

  @Test
  public void sortStringIntHolder_returnOnlyHolderGivenOne() {
    StringIntHolder holder1 = new StringIntHolder("a", 1);
    StringIntHolder[] arr = new StringIntHolder[1];
    arr[0] = holder1;

    arr = InsertionSortStringIntHolder.sort(arr);
    assertEquals("a", arr[0].str);
  }

  @Test
  public void sortStringIntHolder_maxValueShouldBeFirst() {
    StringIntHolder holder1 = new StringIntHolder("a", 1);
    StringIntHolder holder2 = new StringIntHolder("b", 2);
    StringIntHolder[] arr = new StringIntHolder[2];
    arr[0] = holder1;
    arr[1] = holder2;


    arr = InsertionSortStringIntHolder.sort(arr);
    assertEquals("b", arr[0].str);
    assertEquals("a", arr[1].str);
  }

  @Test
  public void sortStringIntHolder_sortThreeEntriesCorrectly() {
    StringIntHolder holder1 = new StringIntHolder("a", 1);
    StringIntHolder holder2 = new StringIntHolder("b", 2);
    StringIntHolder holder3 = new StringIntHolder("c", 10);

    StringIntHolder[] arr = new StringIntHolder[3];
    arr[2] = holder1;
    arr[1] = holder2;
    arr[0] = holder3;


    arr = InsertionSortStringIntHolder.sort(arr);
    assertEquals("c", arr[0].str);
    assertEquals("b", arr[1].str);
    assertEquals("a", arr[2].str);
  }

  @Test
  public void sortGamesWithCounter_ReturnOnlyObjectGivenOne() {
    // Build games
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
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

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");
    GameCategory[] cats2 = new GameCategory[2];
    cats2[0] = new GameCategory("Abstract Strategy");
    cats2[1] = new GameCategory("Animals");

    GameMechanism[] mechs2 = new GameMechanism[2];
    mechs2[0] = new GameMechanism("Grid Movement");
    mechs2[1] = new GameMechanism("Tile Placement");

    int[] bestWith2 = new int[1];
    bestWith[0] = 2;

    game2.addExpandedGameInfo(3.6298, false, cats2, mechs2, bestWith2, new int[0]);

    BoardGameCounter counter = new BoardGameCounter(game1);
    counter.value += 2;

    BoardGameCounter[] arr = new BoardGameCounter[1];
    arr[0] = counter;
    arr = InsertionSortGamesWithCounter.sort(arr);
    assertEquals("Agricola", arr[0].game.getName());
  }

  @Test
  public void sortGamesWithCounter_sortTwoObjectsByValue() {
    // Build games
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
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

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");
    GameCategory[] cats2 = new GameCategory[2];
    cats2[0] = new GameCategory("Abstract Strategy");
    cats2[1] = new GameCategory("Animals");

    GameMechanism[] mechs2 = new GameMechanism[2];
    mechs2[0] = new GameMechanism("Grid Movement");
    mechs2[1] = new GameMechanism("Tile Placement");

    int[] bestWith2 = new int[1];
    bestWith[0] = 2;

    game2.addExpandedGameInfo(3.6298, false, cats2, mechs2, bestWith2, new int[0]);

    BoardGameCounter counter = new BoardGameCounter(game1);
    BoardGameCounter counter2 = new BoardGameCounter(game2);
    counter.value += 2;
    counter2.value += 4;

    BoardGameCounter[] arr = new BoardGameCounter[2];
    arr[0] = counter;
    arr[1] = counter2;
    arr = InsertionSortGamesWithCounter.sort(arr);
    assertEquals("Hive", arr[0].game.getName());
    assertEquals("Agricola", arr[1].game.getName());
  }

  @Test
  public void sortGamesWithCounter_sortThreeObjectsCorrectly() {
    // Build games
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
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

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");
    GameCategory[] cats2 = new GameCategory[2];
    cats2[0] = new GameCategory("Abstract Strategy");
    cats2[1] = new GameCategory("Animals");

    GameMechanism[] mechs2 = new GameMechanism[2];
    mechs2[0] = new GameMechanism("Grid Movement");
    mechs2[1] = new GameMechanism("Tile Placement");

    int[] bestWith2 = new int[1];
    bestWith[0] = 2;

    game2.addExpandedGameInfo(3.6298, false, cats2, mechs2, bestWith2, new int[0]);

    BoardGameCounter counter = new BoardGameCounter(game1);
    BoardGameCounter counter1 = new BoardGameCounter(game1);
    BoardGameCounter counter2 = new BoardGameCounter(game2);
    counter.value += 2;
    counter1.value += 5;
    counter2.value += 4;

    BoardGameCounter[] arr = new BoardGameCounter[3];
    arr[0] = counter;
    arr[1] = counter1;
    arr[2] = counter2;
    arr = InsertionSortGamesWithCounter.sort(arr);
    assertEquals("Agricola", arr[0].game.getName());
    assertEquals(5, arr[0].value, 0); // last is how much the values can be off by
    assertEquals("Hive", arr[1].game.getName());
    assertEquals("Agricola", arr[2].game.getName());
  }
}
