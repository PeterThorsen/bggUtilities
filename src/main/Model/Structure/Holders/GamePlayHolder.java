package Model.Structure.Holders;

import Model.Structure.BoardGame;

public class GamePlayHolder {
  public final int plays;
  public final BoardGame game;

  public GamePlayHolder(BoardGame game, int plays) {
    this.game = game;
    this.plays = plays;
  }
}
