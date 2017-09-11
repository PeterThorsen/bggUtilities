package main.Model.Structure;

/**
 * Created by Peter on 14/11/2016.
 */
public class BoardGameSuggestion {

  public final BoardGameCounter[] suggestedCombination;
  public final BoardGame[] allOptions;

  public BoardGameSuggestion(BoardGameCounter[] suggestedCombination, BoardGame[] allOptions) {

    this.suggestedCombination = suggestedCombination;
    this.allOptions = allOptions;
  }
}
