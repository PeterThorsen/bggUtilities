package Main.Containers;

import java.util.ArrayList;

/**
 * Created by Peter on 27/09/16.
 */
public class BoardGameCollection {

  private ArrayList<Boardgame> games;

  public BoardGameCollection(ArrayList<Boardgame> games) {
    this.games = games;
  }
  public ArrayList<Boardgame> getGames() {
    return games;
  }
}
