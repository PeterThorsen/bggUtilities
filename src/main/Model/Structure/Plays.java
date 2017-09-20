package Model.Structure;

import Util.Sorting.InsertionSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Peter on 07/11/2016.
 */
public class Plays {
  private final HashMap<String, Integer> allPlayers = new HashMap<>();
  private final HashMap<String, Integer> nameToIDMap = new HashMap<>();
  private final HashMap<Integer, ArrayList<Play>> allPlays = new HashMap<>();
  private final HashMap<String, HashMap<String, Integer>> playsByPerson = new HashMap<>();

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

  public void addPlay(Play play) {
    String gameName = "";
    try {
      gameName = play.game.name;
    }
    catch (Exception e) {
      System.out.println("ex, play is: " + play.game);
    }
    for (String name : play.playerNames) {

      if (!allPlayers.containsKey(name)) {
        allPlayers.put(name, play.noOfPlays);
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.noOfPlays);
        playsByPerson.put(name, gamePlaysByPlayer);
        continue;
      }
      allPlayers.put(name, allPlayers.get(name) + play.noOfPlays);

      if (!playsByPerson.containsKey(name)) {
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.noOfPlays);
        playsByPerson.put(name, gamePlaysByPlayer);
        continue;
      }

      HashMap<String, Integer> gamePlaysByPlayer = playsByPerson.get(name);
      if (!gamePlaysByPlayer.containsKey(gameName)) {
        gamePlaysByPlayer.put(gameName, play.noOfPlays);
        playsByPerson.put(name, gamePlaysByPlayer);
        continue;
      }
      int noOfPlays = gamePlaysByPlayer.get(gameName);
      noOfPlays += play.noOfPlays;
      gamePlaysByPlayer.put(gameName, noOfPlays);
      playsByPerson.put(name, gamePlaysByPlayer);
    }

    BoardGame game = play.game;
    int id = game.id;
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

  public Play[] getPlays(int uniqueID) {
    if (allPlays.containsKey(uniqueID)) {
      ArrayList<Play> unsortedPlays =  allPlays.get(uniqueID);
      return sortPlays(unsortedPlays);

    }
    return new Play[0];
  }

  public Play[] getPlays(String name) {
    if (nameToIDMap.containsKey(name)) {
      int id = nameToIDMap.get(name);
      ArrayList<Play> unsortedPlays = allPlays.get(id);
      return sortPlays(unsortedPlays);
    }
    return new Play[0];
  }

  public Play[] getAllPlays() {
    ArrayList<Play> unsortedPlays = new ArrayList<>();
    for (int key : allPlays.keySet()) {
      unsortedPlays.addAll(allPlays.get(key));
    }
    return sortPlays(unsortedPlays);
  }

  private Play[] sortPlays(ArrayList<Play> unsortedPlays) {
    Play[] sortedPlays = new Play[unsortedPlays.size()];
    for (int i = 0; i < sortedPlays.length; i++) {
      sortedPlays[i] = unsortedPlays.get(i);
    }

    sortedPlays = InsertionSort.sortPlays(sortedPlays);
    return sortedPlays;
  }


}
