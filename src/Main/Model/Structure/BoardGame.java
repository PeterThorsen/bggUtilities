package Main.Model.Structure;

/**
 * Created by Peter on 28/09/16.
 */
public class BoardGame {
  public final int maxPlaytime;
  public final int minPlaytime;
  public final int maxPlayers;
  public final int minPlayers;
  public final int id;
  public final String name;
  public final String personalRating;
  public final int numPlays;
  public final String averageRating;
  public double complexity = 0.0;
  public boolean isExpansion;
  public GameCategory[] categories;
  public GameMechanism[] mechanisms;
  public int[] bestWith;
  public int[] recommendedWith;
  public final String type; // Type might be null, always check for null

  public BoardGame(String name, int uniqueID, int minPlayers, int maxPlayers, int minPlaytime,
                   int maxPlaytime, String personalRating, int numberOfPlays, String averageRating, String type) {
    this.name = name;
    id = uniqueID;
    this.minPlayers = minPlayers;
    this.maxPlayers = maxPlayers;
    this.minPlaytime = minPlaytime;
    this.maxPlaytime = maxPlaytime;
    this.personalRating = personalRating;
    numPlays = numberOfPlays;
    this.averageRating = averageRating;
    this.type = type;
  }

  public void addExpandedGameInfo(double complexity, boolean isExpansion, GameCategory[] categories,
                                  GameMechanism[] mechanisms, int[] bestWith, int[] recommendedWith) {
    this.complexity = complexity;
    this.isExpansion = isExpansion;
    this.categories = categories;
    this.mechanisms = mechanisms;
    this.bestWith = bestWith;
    this.recommendedWith = recommendedWith;
  }

  @Override
  public String toString(){
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    BoardGame object = (BoardGame) obj;
    return object.name.equals(name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
