package Main.Sorting;

import Main.Models.Structure.BoardGameCounter;

/**
 * Created by Peter on 14/11/2016.
 */
public class InsertionSortGamesWithCounter {
  public static BoardGameCounter[] sort(BoardGameCounter[] gamesWithCounter) {
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
}
