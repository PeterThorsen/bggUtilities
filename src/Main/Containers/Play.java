package Main.Containers;

import Main.Containers.Holders.PlayerNodeInformationHolder;

/**
 * Created by Peter on 03/10/16.
 */
public class Play {
  public PlayerNodeInformationHolder[] playerInformation;
  private final BoardGame game;
  private final String date;
  private final int noOfPlays;

  public Play(BoardGame game, String date, PlayerNodeInformationHolder[] playerInformation, int noOfPlays) {
    this.game = game;
    this.date = date;
    this.playerInformation = playerInformation;
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
