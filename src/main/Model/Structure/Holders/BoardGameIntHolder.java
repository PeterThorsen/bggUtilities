package Model.Structure.Holders;


import Model.Structure.BoardGame;

/**
 * Created by Peter on 13-Nov-16.
 */
public class BoardGameIntHolder {
  public final BoardGame game;
  public final int num;

  public BoardGameIntHolder(BoardGame game, int num) {
    this.game = game;
    this.num = num;
  }
}
