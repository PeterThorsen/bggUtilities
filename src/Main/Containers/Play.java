package Main.Containers;

import Main.Containers.Holders.PlayerNodeInformationHolder;

import java.util.HashMap;

/**
 * Created by Peter on 03/10/16.
 */
public class Play {
  public String[] playerNames;
  private final BoardGame game;
  private final String date;
  private final int noOfPlays;
  public final HashMap<String, Double> playerRatings;

  public Play(BoardGame game, String date, String[] playerNames, int noOfPlays,
              HashMap<String, Double> playerRatings) {
    this.game = game;
    this.date = date;
    this.playerNames = playerNames;
    this.noOfPlays = noOfPlays;
    this.playerRatings = playerRatings;
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
