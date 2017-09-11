package main.Util.Sorting;

import main.Model.Structure.BoardGameCounter;
import main.Model.Structure.Holders.BoardGameIntHolder;
import main.Model.Structure.Holders.StringIntHolder;
import main.Model.Structure.Play;
import main.Model.Structure.Player;
import main.Model.Structure.Reason;

import java.util.ArrayList;

/**
 * Created by Peter on 09-Nov-16.
 */
public class InsertionSort {
  public static String[] sortStrings(String[] sortedPlays) {
    int j;
    String key;
    int i;

    for (j = 1; j < sortedPlays.length; j++)
    {
      key = sortedPlays[j];
      for(i = j - 1; (i >= 0) && (sortedPlays[i].compareToIgnoreCase(key)) > 0; i--)
      {
        sortedPlays[ i+1 ] = sortedPlays[i];
      }
      sortedPlays[ i+1 ] = key;
    }
    return sortedPlays;
  }

  public static Player[] sortPlayers(Player[] players) {
    int j;
    Player key;
    int i;

    for (j = 1; j < players.length; j++)
    {
      key = players[j];
      for(i = j - 1; (i >= 0) && (players[i].name.compareToIgnoreCase(key.name) > 0); i--)
      {
        players[ i+1 ] = players[i];
      }
      players[ i+1 ] = key;
    }
    return players;
  }

  public static Play[] sortPlays(Play[] plays) {
    int j;                     // the number of items sorted so far
    Play key;                // the item to be inserted
    int i;

    for (j = 1; j < plays.length; j++)    // Start with 1 (not 0)
    {
      key = plays[j];
      for(i = j - 1; (i >= 0) && (plays[i].date.compareTo(key.date) < 0); i--)   // Smaller values are moving up
      {
        plays[ i+1 ] = plays[i];
      }
      plays[ i+1 ] = key;    // Put the key in its proper location
    }

    for (Play play : plays) {
      String[] names = play.playerNames;
      names = InsertionSort.sortStrings(names);
      play.playerNames = names;
    }

    return plays;
  }

  public static StringIntHolder[] sortStringIntHolder(StringIntHolder[] holders) {
    int j;
    StringIntHolder key;
    int i;

    for (j = 1; j < holders.length; j++)
    {
      key = holders[j];
      for (i = j - 1; (i >= 0) && (holders[i].num < key.num); i--)
      {
        holders[i + 1] = holders[i];
      }
      holders[i + 1] = key;
    }
    return holders;
  }

  public static Reason[] sortReasons(ArrayList<Reason> arrList) {
    Reason[] arr = new Reason[arrList.size()];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = arrList.get(i);
    }

    int j;
    Reason key;
    int i;

    for (j = 1; j < arr.length; j++)
    {
      key = arr[j];
      for (i = j - 1; (i >= 0) && (arr[i].value < key.value); i--)
      {
        arr[i + 1] = arr[i];
      }
      arr[i + 1] = key;
    }
    return arr;
  }

  public static BoardGameCounter[] sortGamesWithCounter(BoardGameCounter[] gamesWithCounter) {
    int j;
    BoardGameCounter key;
    int i;

    for (j = 1; j < gamesWithCounter.length; j++)
    {
      key = gamesWithCounter[j];
      for(i = j - 1; (i >= 0) && (gamesWithCounter[i].value < key.value); i--)
      {
        gamesWithCounter[ i+1 ] = gamesWithCounter[i];
      }
      gamesWithCounter[ i+1 ] = key;
    }
    return gamesWithCounter;
  }

  public static BoardGameIntHolder[] sortBoardGameIntHolder(BoardGameIntHolder[] holders) {
    int j;
    BoardGameIntHolder key;
    int i;

    for (j = 1; j < holders.length; j++)
    {
      key = holders[j];
      for (i = j - 1; (i >= 0) && (holders[i].num < key.num); i--)
      {
        holders[i + 1] = holders[i];
      }
      holders[i + 1] = key;
    }
    return holders;
  }
}
