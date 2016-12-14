package Main.Sorting;

import Main.Models.Structure.Player;

/**
 * Created by Peter on 12-Nov-16.
 */
public class InsertionSortPlayers {
  public static Player[] sort(Player[] players) {
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
}
