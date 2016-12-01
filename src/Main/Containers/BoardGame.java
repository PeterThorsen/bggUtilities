package Main.Containers;

/**
 * Created by Peter on 28/09/16.
 */
public class BoardGame {
  private final int maxPlaytime;
  private final int minPlaytime;
  private final int max;
  private final int minimum;
  private final int id;
  private final String name;
  private final String personalRating;
  private final int numPlays;
  private final String averageRating;
  private double complexity = 0.0;
  private boolean isExpansion;
  private GameCategory[] categories;
  private GameMechanism[] mechanisms;
  private int[] bestWith;
  private int[] recommendedWith;
  private String type; // Type might be null, always check for null

  public BoardGame(String name, int uniqueID, int minPlayers, int maxPlayers, int minPlaytime,
                   int maxPlaytime, String personalRating, int numberOfPlays, String averageRating, String type) {
    this.name = name;
    id = uniqueID;
    minimum = minPlayers;
    max = maxPlayers;
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

  public double getComplexity() {
    return complexity;
  }

  public String getAverageRating() {
    return averageRating;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    BoardGame object = (BoardGame) obj;
    return object.name.equals(name);
  }

  public boolean isExpansion() {
    return isExpansion;
  }

  public GameCategory[] getCategories() {
    return categories;
  }

  public GameMechanism[] getMechanisms() {
    return mechanisms;
  }

  public int[] getBestWith() {
    return bestWith;
  }

  public int[] getRecommendedWith() {
    return recommendedWith;
  }

  public String getType() {
    return type;
  }
}
