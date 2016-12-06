package Main.Sorting;

import Main.Containers.Holders.StringIntHolder;

/**
 * Created by Peter on 13-Nov-16.
 */
public class InsertionSortStringIntHolder {
  public static StringIntHolder[] sort(StringIntHolder[] mostCommonPlayersSorted) {
    int j;
    StringIntHolder key;
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
