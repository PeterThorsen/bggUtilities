package Model.Structure.Holders;

/**
 * Created by Peter on 06-Dec-16.
 */
public class PlayerNodeInformationHolder {
  public final String playerName;
  public final double rating;
  public final boolean winner;

  public PlayerNodeInformationHolder(String playerName, double rating, boolean winner) {
    this.playerName = playerName;
    this.rating = rating;
    this.winner = winner;
  }

  public int compareTo(PlayerNodeInformationHolder key) {
    return playerName.compareToIgnoreCase(key.playerName);
  }

  @Override
  public boolean equals(Object other) {
    try {
      String otherString = (String) other;
      return playerName.equals(otherString);
    }
    catch (Exception e) {
      PlayerNodeInformationHolder o = (PlayerNodeInformationHolder) other;
      return playerName.equals(o.playerName);
    }
  }

  @Override
  public int hashCode() {
    return playerName.hashCode();
  }
}