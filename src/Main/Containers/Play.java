package Main.Containers;

/**
 * Created by Peter on 03/10/16.
 */
public class Play {
  public String[] playerNames;
  private final BoardGame game;
  private final String date;
  private final int noOfPlays;

  public Play(BoardGame game, String date, String[] playerNames, int noOfPlays) {
    this.game = game;
    this.date = date;
    this.playerNames = playerNames;
    this.noOfPlays = noOfPlays;
  }

  public String getDate() {
    return date;
  }

  public int getQuantity() {
    return noOfPlays;
  }

  public BoardGame getGame() {
    return game;
  }
}
