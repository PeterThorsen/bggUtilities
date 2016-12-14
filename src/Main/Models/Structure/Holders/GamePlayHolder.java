package Main.Models.Structure.Holders;

import Main.Models.Structure.BoardGame;

/**
 * Created by Peter on 09-Nov-16.
 */
public class GamePlayHolder {
  public final int plays;
  public final BoardGame game;

  public GamePlayHolder(BoardGame game, int plays) {
    this.game = game;
    this.plays = plays;
  }
}
