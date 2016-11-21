package Main.Containers;

/**
 * Created by Peter on 09-Nov-16.
 */
public class GameNameAndPlayHolder {
  public final int plays;
  public final BoardGame game;

  public GameNameAndPlayHolder(BoardGame game, int plays) {
    this.game = game;
    this.plays = plays;
  }
}
