package Main.Models.Storage;

import Main.Models.Structure.BoardGameCollection;
import Main.Models.Structure.Player;
import Main.Models.Structure.Plays;

/**
 * Created by Peter on 28/09/16.
 */
public interface ICollectionBuilder {
  BoardGameCollection getCollection(String username);

  boolean verifyUser(String username);

  Plays getPlays();

  Player[] getPlayers();
}
