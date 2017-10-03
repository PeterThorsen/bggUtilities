package Model.Structure;

import java.util.ArrayList;

/**
 * Created by Peter on 14/11/2016.
 */
public class BoardGameCounter {

  public BoardGame game;
  public double value = 0;
  public double approximateTime = 0;
  public ArrayList<Reason> reasons = new ArrayList<>();

  public BoardGameCounter(BoardGame game) {
    this.game = game;
  }

  public BoardGameCounter(BoardGame boardGame, int value, ArrayList<Reason> reasons) {

    game = boardGame;
    this.value = value;
    this.reasons = reasons;
  }

  @Override
  public String toString() {
    return game.toString();
  }

  @Override
  public boolean equals(Object other) {
    BoardGameCounter counter = (BoardGameCounter) other;
    return counter.toString().equals(toString());
  }
}
