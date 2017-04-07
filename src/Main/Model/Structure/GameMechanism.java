package Main.Model.Structure;

/**
 * Created by Peter on 16/11/2016.
 */
public class GameMechanism {
  public String mechanism;

  public GameMechanism(String mechanism) {
    this.mechanism = mechanism;
  }

  @Override
  public boolean equals(Object other) {
    GameMechanism correctOther = (GameMechanism)other;
    return mechanism.equals(correctOther.mechanism);
  }

  @Override
  public String toString() {
    return mechanism;
  }
}
