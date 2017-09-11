package main.Model.Storage;

import main.Model.Structure.BoardGameCollection;
import main.Model.Structure.Player;
import main.Model.Structure.Plays;

/**
 * Created by Peter on 28/09/16.
 */
public interface ICollectionBuilder {
  BoardGameCollection getCollection(String username);

  boolean verifyUser(String username);

  Plays getPlays();

  Player[] getPlayers();
}
