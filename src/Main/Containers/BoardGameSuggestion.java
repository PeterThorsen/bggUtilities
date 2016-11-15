package Main.Containers;

/**
 * Created by Peter on 14/11/2016.
 */
public class BoardGameSuggestion {

  public final BoardGame[] suggestedCombination;
  public final BoardGame[] allOptions;

  public BoardGameSuggestion(BoardGame[] suggestedCombination, BoardGame[] allOptions) {

    this.suggestedCombination = suggestedCombination;
    this.allOptions = allOptions;
  }
}
