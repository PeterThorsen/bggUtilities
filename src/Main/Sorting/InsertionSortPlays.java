package Main.Sorting;

import Main.Containers.Holders.PlayerNodeInformationHolder;
import Main.Containers.Play;

/**
 * Created by Peter on 12-Nov-16.
 */
public class InsertionSortPlays {
  public static Play[] sort(Play[] plays) {
    int j;                     // the number of items sorted so far
    Play key;                // the item to be inserted
    int i;

    for (j = 1; j < plays.length; j++)    // Start with 1 (not 0)
    {
      key = plays[j];
      for(i = j - 1; (i >= 0) && (plays[i].getDate().compareTo(key.getDate()) < 0); i--)   // Smaller values are moving up
      {
        plays[ i+1 ] = plays[i];
      }
      plays[ i+1 ] = key;    // Put the key in its proper location
    }

    for (Play play : plays) {
      String[] names = play.playerNames;
      names = InsertionSortStrings.sort(names);
      play.playerNames = names;
    }

    return plays;
  }
}
