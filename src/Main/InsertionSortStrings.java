package Main;

/**
 * Created by Peter on 09-Nov-16.
 */
public class InsertionSortStrings {
  public static String[] sort(String[] sortedPlays) {
    int j;                     // the number of items sorted so far
    String key;                // the item to be inserted
    int i;

    for (j = 1; j < sortedPlays.length; j++)    // Start with 1 (not 0)
    {
      key = sortedPlays[j];
      for(i = j - 1; (i >= 0) && (sortedPlays[i].compareToIgnoreCase(key)) > 0; i--)   // Smaller values are moving up
      {
        sortedPlays[ i+1 ] = sortedPlays[i];
      }
      sortedPlays[ i+1 ] = key;    // Put the key in its proper location
    }
    return sortedPlays;
  }
}
