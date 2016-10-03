package Main.Containers;

import java.util.ArrayList;

/**
 * Created by Peter on 27/09/16.
 */
public class BoardGameCollection {

  private ArrayList<BoardGame> games;

  public BoardGameCollection(ArrayList<BoardGame> games) {
    this.games = games;
  }
  public ArrayList<BoardGame> getGames() {
    return games;
  }
}
