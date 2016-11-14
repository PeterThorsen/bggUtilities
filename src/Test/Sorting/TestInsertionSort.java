package Test.Sorting;

import Main.Containers.Play;
import Main.Containers.Player;
import Main.Containers.StringToIntHolder;
import Main.Sorting.InsertionSortPlayers;
import Main.Sorting.InsertionSortStringAndIntHolder;
import Main.Sorting.InsertionSortStrings;
import org.junit.Test;

import static org.junit.Assert.*;

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
  public void sortStringToIntHolder_returnOnlyHolderGivenOne() {
    StringToIntHolder holder1 = new StringToIntHolder("a", 1);
    StringToIntHolder[] arr = new StringToIntHolder[1];
    arr[0] = holder1;

    arr = InsertionSortStringAndIntHolder.sort(arr);
    assertEquals("a", arr[0].str);
  }

  @Test
  public void sortStringToIntHolder_maxValueShouldBeFirst() {
    StringToIntHolder holder1 = new StringToIntHolder("a", 1);
    StringToIntHolder holder2 = new StringToIntHolder("b", 2);
    StringToIntHolder[] arr = new StringToIntHolder[2];
    arr[0] = holder1;
    arr[1] = holder2;


    arr = InsertionSortStringAndIntHolder.sort(arr);
    assertEquals("b", arr[0].str);
    assertEquals("a", arr[1].str);
  }

  @Test
  public void sortStringToIntHolder_sortThreeEntriesCorrectly() {
    StringToIntHolder holder1 = new StringToIntHolder("a", 1);
    StringToIntHolder holder2 = new StringToIntHolder("b", 2);
    StringToIntHolder holder3 = new StringToIntHolder("c", 10);

    StringToIntHolder[] arr = new StringToIntHolder[3];
    arr[2] = holder1;
    arr[1] = holder2;
    arr[0] = holder3;


    arr = InsertionSortStringAndIntHolder.sort(arr);
    assertEquals("c", arr[0].str);
    assertEquals("b", arr[1].str);
    assertEquals("a", arr[2].str);
  }
}