package Model.Structure;

import java.util.ArrayList;

public class GameNight {
  ArrayList<Play> plays = new ArrayList<>();
  private String date;

  public void addPlay(Play currentPlay) {
    plays.add(currentPlay);
  }

  public int getNumberOfPlays() {
    return plays.size();
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDate() {
    return date;
  }
}
