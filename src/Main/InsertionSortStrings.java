package Main;

/**
 * Created by Peter on 09-Nov-16.
 */
public class InsertionSortStrings {
  public static String[] sort(String[] sortedPlays) {
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
}
