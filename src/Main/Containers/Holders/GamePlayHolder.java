package Main.Containers.Holders;

import Main.Containers.BoardGame;

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
