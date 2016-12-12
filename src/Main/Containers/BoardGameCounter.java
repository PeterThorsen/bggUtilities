package Main.Containers;

import Main.Containers.Holders.Reason;

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

}
