package Main.Models.Storage;

import Main.Containers.BoardGameCollection;
import Main.Containers.Player;
import Main.Containers.Plays;

/**
 * Created by Peter on 28/09/16.
 */
public interface ICollectionBuilder {
  BoardGameCollection getCollection(String username);

  boolean verifyUser(String username);

  Plays getPlays();

  Player[] getPlayers();
}
