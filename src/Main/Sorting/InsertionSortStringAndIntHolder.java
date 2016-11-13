package Main.Sorting;

import Main.Containers.StringToIntHolder;

/**
 * Created by Peter on 13-Nov-16.
 */
public class InsertionSortStringAndIntHolder {
  public static StringToIntHolder[] sort(StringToIntHolder[] mostCommonPlayersSorted) {
    int j;
    StringToIntHolder key;
    int i;

    for (j = 1; j < mostCommonPlayersSorted.length; j++)
    {
      key = mostCommonPlayersSorted[j];
      for (i = j - 1; (i >= 0) && (mostCommonPlayersSorted[i].num < key.num); i--)
      {
        mostCommonPlayersSorted[i + 1] = mostCommonPlayersSorted[i];
      }
      mostCommonPlayersSorted[i + 1] = key;
    }
    return mostCommonPlayersSorted;
  }
}
