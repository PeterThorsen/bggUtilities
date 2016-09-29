package Main.Containers;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Peter on 28/09/16.
 */
public class Boardgame {
  private int id;
  private String name;
  public Boardgame(String name, int uniqueID) {
    this.name = name;
    id = uniqueID;
  }

  public String getName() {
    return name;
  }

  public int getID() {
    return id;
  }
}
