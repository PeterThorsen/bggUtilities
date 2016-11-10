package Main.Containers;

/**
 * Created by Peter on 09-Nov-16.
 */
public class GameNameAndPlayHolder {
  public final int plays;
  public final String gameName;

  public GameNameAndPlayHolder(String gameName, int plays) {
    this.gameName = gameName;
    this.plays = plays;
  }
}
