package Model.Structure;

import java.util.HashMap;

/**
 * Created by Peter on 03/10/16.
 */
public class Play {
  public String[] playerNames;
  public final int id;
  public BoardGame game;
  public final String date;
  public final int noOfPlays;
  private final HashMap<String, Double> playerRatings;
  public String[] winners;

  public Play(int id, BoardGame game, String date, String[] playerNames, int noOfPlays,
              HashMap<String, Double> playerRatings, String[] winners) {
    this.id = id;
    this.game = game;
    this.date = date;
    this.playerNames = playerNames;
    this.noOfPlays = noOfPlays;
    this.playerRatings = playerRatings;
    this.winners = winners;
  }

  public double getRating(String name) {
    if(playerRatings == null || !playerRatings.containsKey(name)) {
      return 0;
    }
    return playerRatings.get(name);
  }
}
