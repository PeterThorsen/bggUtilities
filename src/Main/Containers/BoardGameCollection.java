package Main.Containers;

import java.util.ArrayList;

/**
 * Created by Peter on 27/09/16.
 */
public class BoardGameCollection {

  private final ArrayList<BoardGame> games;

  public BoardGameCollection(ArrayList<BoardGame> games) {
    this.games = games;
  }
  public BoardGame[] getGames() {
    BoardGame[] allGames = new BoardGame[games.size()];
    for (int i = 0; i < allGames.length; i++) {
      allGames[i] = games.get(i);
    }
    return allGames;
  }
}
