package Main.Containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Peter on 07/11/2016.
 */
public class Plays {
  HashMap<String, Integer> allPlayers = new HashMap<String, Integer>();
  HashMap<String, Integer> nameToIDMap = new HashMap<>();
  private HashMap<Integer, ArrayList<Play>> allPlays = new HashMap<>();

  public String[] getPlayerNames() {
    String[] names = new String[allPlayers.size()];
    Set<String> keys = allPlayers.keySet();
    Iterator<String> iterator = keys.iterator();

    int i = 0;
    while(iterator.hasNext()) {
      names[i] = iterator.next();
      i++;
    }
    return names;
  }

  public int getNumberOfPlayers() {
    return allPlayers.size();
  }

  public void addPlay(Play play) {
    for (String player: play.getPlayers()) {
      if(allPlayers.containsKey(player)) {
        allPlayers.put(player, allPlayers.get(player) + 1);
      }
      else {
        allPlayers.put(player, 1);
      }
    }

    BoardGame game = play.getGame();
    int id = game.getID();
    String gameName = game.getName();
    nameToIDMap.put(gameName, id); // For getPlays with name

    if(allPlays.containsKey(id)) {
      ArrayList<Play> plays = allPlays.get(id);
      plays.add(play);
      allPlays.put(id, plays);
    }
    else {
      ArrayList<Play> plays = new ArrayList<>();
      plays.add(play);
      allPlays.put(id, plays);
    }

  }

  public ArrayList<Play> getPlays(int uniqueID) {
    if(allPlays.containsKey(uniqueID)) {
      return allPlays.get(uniqueID);
    }
    return new ArrayList<>();
  }

  public ArrayList<Play> getPlays(String name) {
    if(nameToIDMap.containsKey(name)) {
      int id = nameToIDMap.get(name);
      return allPlays.get(id);
    }
    return new ArrayList<>();
  }
}
