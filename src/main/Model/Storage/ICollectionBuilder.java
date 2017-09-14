package Model.Storage;

import Model.Structure.BoardGameCollection;
import Model.Structure.Player;
import Model.Structure.Plays;

/**
 * Created by Peter on 28/09/16.
 */
public interface ICollectionBuilder {
  BoardGameCollection getCollection(String username);

  boolean verifyUser(String username);

  Plays getPlays();

  Player[] getPlayers();
}
