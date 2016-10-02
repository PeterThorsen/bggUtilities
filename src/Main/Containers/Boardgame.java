package Main.Containers;

/**
 * Created by Peter on 28/09/16.
 */
public class Boardgame {
  private final int maxPlaytime;
  private final int minPlaytime;
  private final int max;
  private final int minimum;
  private final int id;
  private final String name;
  private final String personalRating;
  private final int numPlays;

  public Boardgame(String name, int uniqueID, int minPlayers, int maxPlayers, int minPlaytime,
                   int maxPlaytime, String personalRating, int numberOfPlays) {
    this.name = name;
    id = uniqueID;
    minimum = minPlayers;
    max = maxPlayers;
    this.minPlaytime = minPlaytime;
    this.maxPlaytime = maxPlaytime;
    this.personalRating = personalRating;
    numPlays = numberOfPlays;
  }

  public String getName() {
    return name;
  }

  public int getID() {
    return id;
  }

  public int getMinPlayers() {
    return minimum;
  }

  public int getMaxPlayers() {
    return max;
  }

  public int getMinPlaytime() {
    return minPlaytime;
  }

  public int getMaxPlaytime() {
    return maxPlaytime;
  }

  public String getPersonalRating() {
    return personalRating;
  }

  public int getNumberOfPlays() {
    return numPlays;
  }

  @Override
  public String toString(){
    return name;
  }
}
