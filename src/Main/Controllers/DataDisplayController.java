package Main.Controllers;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Play;
import Main.Models.Storage.ICollectionBuilder;

import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 * Will be used for connection to the model (ICollectionBuilder) and the view.
 */
public class DataDisplayController {
  BoardGameCollection collection;
  public DataDisplayController(ICollectionBuilder collectionBuilder, String username) {
    collection = collectionBuilder.getCollection(username);
  }

  public String[] getGameNames() {
    String[] names = new String[collection.getGames().size()];

    for(int i = 0; i<collection.getGames().size(); i++) {
      names[i] = collection.getGames().get(i).getName();
    }
    return names;
  }

  public int getNumberOfGames() {
    return collection.getGames().size();
  }

  public ArrayList<BoardGame> getAllGames() {
    return collection.getGames();
  }

  public ArrayList<Play> getPlays(int uniqueID) {
    for(BoardGame game : collection.getGames()) {
      if(game.getID() == uniqueID) {
        return game.getPlays();
      }
    }
    return new ArrayList<Play>();
  }

  public ArrayList<Play> getPlays(String name) {
    for(BoardGame game : collection.getGames()) {
      if(game.getName().equals(name)) {
        return game.getPlays();
      }
    }
    return new ArrayList<Play>();
  }

}
