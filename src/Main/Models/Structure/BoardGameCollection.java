package Main.Models.Structure;

/**
 * Created by Peter on 27/09/16.
 */
public class BoardGameCollection {

  private final BoardGame[] games;

  public BoardGameCollection(BoardGame[] games) {
    this.games = games;
  }
  public BoardGame[] getGames() {
    return games;
  }
}
