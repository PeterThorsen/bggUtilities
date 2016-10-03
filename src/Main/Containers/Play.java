package Main.Containers;

/**
 * Created by Peter on 03/10/16.
 */
public class Play {
  private final String[] playerNames;
  private final String date;
  private final int noOfPlays;

  public Play(String date, String[] playerNames, int noOfPlays) {
    this.date = date;
    this.playerNames = playerNames;
    this.noOfPlays = noOfPlays;
  }

  public String getDate() {
    return date;
  }

  public String[] getPlayers() {
    return playerNames;
  }

  public int noOfPlays() {
    return noOfPlays;
  }
}
