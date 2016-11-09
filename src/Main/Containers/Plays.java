package Main.Containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Peter on 07/11/2016.
 */
public class Plays {
  private HashMap<String, Integer> allPlayers = new HashMap<String, Integer>();
  private HashMap<String, Integer> nameToIDMap = new HashMap<>();
  private HashMap<Integer, ArrayList<Play>> allPlays = new HashMap<>();
  private HashMap<String, HashMap<String, Integer>> playsByPerson = new HashMap<>();

  public String[] getPlayerNames() {
    String[] names = new String[allPlayers.size()];
    Set<String> keys = allPlayers.keySet();
    Iterator<String> iterator = keys.iterator();

    int i = 0;
    while (iterator.hasNext()) {
      names[i] = iterator.next();
      i++;
    }
    return names;
  }

  public int getNumberOfPlayers() {
    return allPlayers.size();
  }

  public void addPlay(Play play) {
    for (String player : play.getPlayers()) {
      String gameName = play.getGame().getName();

      if (allPlayers.containsKey(player)) {
        allPlayers.put(player, allPlayers.get(player) + 1);

        if(playsByPerson.containsKey(player)) {
          HashMap<String, Integer> gamePlaysByPlayer = playsByPerson.get(player);

          if(gamePlaysByPlayer.containsKey(gameName)) {
            int noOfPlays = gamePlaysByPlayer.get(gameName);
            noOfPlays += play.getQuantity();
            gamePlaysByPlayer.put(gameName, noOfPlays);

          }
          else {
            gamePlaysByPlayer.put(gameName, play.getQuantity());
          }
          playsByPerson.put(player, gamePlaysByPlayer);
        }
        else {
          HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
          gamePlaysByPlayer.put(gameName, play.getQuantity());
          playsByPerson.put(player, gamePlaysByPlayer);
        }
      } else {
        allPlayers.put(player, 1);
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(player, gamePlaysByPlayer);
      }
    }

    BoardGame game = play.getGame();
    int id = game.getID();
    String gameName = game.getName();
    nameToIDMap.put(gameName, id); // For getPlays with name

    if (allPlays.containsKey(id)) {
      ArrayList<Play> plays = allPlays.get(id);
      plays.add(play);
      allPlays.put(id, plays);
    } else {
      ArrayList<Play> plays = new ArrayList<>();
      plays.add(play);
      allPlays.put(id, plays);
    }

  }

  public ArrayList<Play> getPlays(int uniqueID) {
    if (allPlays.containsKey(uniqueID)) {
      return allPlays.get(uniqueID);
    }
    return new ArrayList<>();
  }

  public ArrayList<Play> getPlays(String name) {
    if (nameToIDMap.containsKey(name)) {
      int id = nameToIDMap.get(name);
      return allPlays.get(id);
    }
    return new ArrayList<>();
  }

  public HashMap<String, Integer> getPlayerToPlaysMap() {
    return allPlayers;
  }

  public HashMap<String, String> getMostPlayedGamesByPlayers() {
    HashMap<String, String> maxPlays = new HashMap<>();

    // Go through each player
    for (String player : playsByPerson.keySet()) {

      // Get his game to play map
      HashMap<String, Integer> gamePlaysByPlayer = playsByPerson.get(player);

      int maxGamePlays = 0;
      String maxName = "";
      // Run through the game to play map
      for (String gameName : gamePlaysByPlayer.keySet()) {

        // Compare plays to find max plays
        int currentPlays = gamePlaysByPlayer.get(gameName);
        if (currentPlays > maxGamePlays) {
          maxGamePlays = currentPlays;
          maxName = gameName;
        }
      }
      maxPlays.put(player, maxName);
    }
    return maxPlays;
  }
}
