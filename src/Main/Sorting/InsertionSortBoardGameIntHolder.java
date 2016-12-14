package Main.Sorting;

import Main.Models.Structure.Holders.BoardGameIntHolder;

/**
 * Created by Peter on 13-Nov-16.
 */
public class InsertionSortBoardGameIntHolder {
  public static BoardGameIntHolder[] sort(BoardGameIntHolder[] holders) {
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
