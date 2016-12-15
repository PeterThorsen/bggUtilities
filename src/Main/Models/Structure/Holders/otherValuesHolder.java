package Main.Models.Structure.Holders;

import Main.Models.Structure.GameCategory;
import Main.Models.Structure.GameMechanism;

/**
 * Created by Peter on 06-Dec-16.
 */
public class otherValuesHolder {
  public final double complexity;
  public final GameCategory[] categories;
  public final GameMechanism[] mechanisms;
  public final int[] bestPlayerCount;
  public final int[] recommendedPlayerCount;

  public otherValuesHolder(double complexity, GameCategory[] categories, GameMechanism[] mechanisms, int[] bestPlayerCount, int[] recommendedPlayerCount) {
    this.complexity = complexity;
    this.categories = categories;
    this.mechanisms = mechanisms;
    this.bestPlayerCount = bestPlayerCount;
    this.recommendedPlayerCount = recommendedPlayerCount;
  }
}