package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Play;
import Main.Network.ICollectionBuilder;
import Main.Network.IConnectionHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 * Will be used for connection to the model (ICollectionBuilder) and the view.
 */
public class DataDisplayController {
  private ArrayList<BoardGame> games;

  public DataDisplayController(ICollectionBuilder collectionBuilder, String username) {
    BoardGameCollection collection = collectionBuilder.getCollection(username);
    games = collection.getGames();
  }

  public String[] getGameNames() {
    String[] names = new String[games.size()];

    for(int i = 0; i<games.size(); i++) {
      names[i] = games.get(i).getName();
    }
    return names;
  }

  public int getNumberOfGames() {
    return games.size();
  }

  public ArrayList<BoardGame> getAllGames() {
    return games;
  }

  public ArrayList<Play> getPlays(int uniqueID) {
    for(BoardGame game : games) {
      if(game.getID() == uniqueID) {
        return game.getPlays();
      }
    }
    return new ArrayList<Play>();
  }

  public ArrayList<Play> getPlays(String name) {
    for(BoardGame game : games) {
      if(game.getName().equals(name)) {
        return game.getPlays();
      }
    }
    return new ArrayList<Play>();
  }
}
