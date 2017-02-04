package Main.Models.Structure;

/**
 * Created by Peter on 12/12/2016.
 */
public class Reason {
  public final String reason;
  public final double value;

  public Reason(String reason, double value) {
    this.reason = reason;
    this.value = value;
  }

  @Override
  public String toString() {
    return reason + " (" + value + ")";
  }
}
