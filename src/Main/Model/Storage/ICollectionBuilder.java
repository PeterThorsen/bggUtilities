package Main.Model.Storage;

import Main.Model.Structure.BoardGameCollection;
import Main.Model.Structure.Player;
import Main.Model.Structure.Plays;

/**
 * Created by Peter on 28/09/16.
 */
public interface ICollectionBuilder {
  BoardGameCollection getCollection(String username);

  boolean verifyUser(String username);

  Plays getPlays();

  Player[] getPlayers();
}
