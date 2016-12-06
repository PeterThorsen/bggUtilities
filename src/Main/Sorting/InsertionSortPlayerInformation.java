package Main.Sorting;

import Main.Containers.Holders.PlayerNodeInformationHolder;

/**
 * Created by Peter on 06-Dec-16.
 */
public class InsertionSortPlayerInformation {
  public static PlayerNodeInformationHolder[] sort(PlayerNodeInformationHolder[] playerInformation) {
    int j;
    PlayerNodeInformationHolder key;
    int i;

    for (j = 1; j < playerInformation.length; j++)
    {
      key = playerInformation[j];
      for(i = j - 1; (i >= 0) && (playerInformation[i].compareTo(key)) > 0; i--)
      {
        playerInformation[ i+1 ] = playerInformation[i];
      }
      playerInformation[ i+1 ] = key;
    }
    return playerInformation;
  }
}
