package Main;

import Main.Containers.Player;

/**
 * Created by Peter on 12-Nov-16.
 */
public class InsertionSortPlayers {
  public static Player[] sort(Player[] players) {
    int j;                     // the number of items sorted so far
    Player key;                // the item to be inserted
    int i;

    for (j = 1; j < players.length; j++)    // Start with 1 (not 0)
    {
      key = players[j];
      for(i = j - 1; (i >= 0) && (players[i].name.compareToIgnoreCase(key.name) > 0); i--)   // Smaller values are moving up
      {
        players[ i+1 ] = players[i];
      }
      players[ i+1 ] = key;    // Put the key in its proper location
    }
    return players;
  }
}
