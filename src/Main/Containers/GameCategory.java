package Main.Containers;

/**
 * Created by Peter on 16/11/2016.
 */
public class GameCategory {
  public final String category;

  public GameCategory(String category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object other) {
    GameCategory otherCategory = (GameCategory) other;
    return otherCategory.category.equals(category);
  }
}
