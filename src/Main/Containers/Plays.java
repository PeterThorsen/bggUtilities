package Main.Containers;

import Main.Containers.Holders.PlayerNodeInformationHolder;
import Main.Sorting.InsertionSortPlays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Peter on 07/11/2016.
 */
public class Plays {
  private final HashMap<PlayerNodeInformationHolder, Integer> allPlayers = new HashMap<>();
  private final HashMap<String, Integer> nameToIDMap = new HashMap<>();
  private final HashMap<Integer, ArrayList<Play>> allPlays = new HashMap<>();
  private final HashMap<String, HashMap<String, Integer>> playsByPerson = new HashMap<>();

  public String[] getPlayerNames() {
    String[] names = new String[allPlayers.size()];
    Set<PlayerNodeInformationHolder> keys = allPlayers.keySet();
    Iterator<PlayerNodeInformationHolder> iterator = keys.iterator();

    int i = 0;
    while (iterator.hasNext()) {
      names[i] = iterator.next().playerName;
      i++;
    }
    return names;
  }

  public void addPlay(Play play) {
    String gameName = "";
    try {
      gameName = play.getGame().getName();
    }
    catch (Exception e) {
      System.out.println("ex, play is: " + play.getGame()); // TODO: 09/11/2016 null ved agentkuo, hvorfor findes game ikke
    }
    for (PlayerNodeInformationHolder holder : play.playerInformation) { // TODO: 05/12/2016 change this to playerRatingHolder

      // TODO: 05/12/2016 change below maps to use Player as map.. add ratings 
      if (!allPlayers.containsKey(holder)) {
        allPlayers.put(holder, play.getQuantity());
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(holder.playerName, gamePlaysByPlayer);
        continue;
      }
      allPlayers.put(holder, allPlayers.get(holder) + play.getQuantity());

      if (!playsByPerson.containsKey(holder.playerName)) {
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(holder.playerName, gamePlaysByPlayer);
        continue;
      }

      HashMap<String, Integer> gamePlaysByPlayer = playsByPerson.get(holder.playerName);
      if (!gamePlaysByPlayer.containsKey(gameName)) {
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(holder.playerName, gamePlaysByPlayer);
        continue;
      }
      int noOfPlays = gamePlaysByPlayer.get(gameName);
      noOfPlays += play.getQuantity();
      gamePlaysByPlayer.put(gameName, noOfPlays);
      playsByPerson.put(holder.playerName, gamePlaysByPlayer);
    }

    BoardGame game = play.getGame();
    int id = game.getID();
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

  public Play[] getAllPlays() {
    ArrayList<Play> unsortedPlays = new ArrayList<>();
    for (int key : allPlays.keySet()) {
      unsortedPlays.addAll(allPlays.get(key));
    }
    Play[] sortedPlays = new Play[unsortedPlays.size()];
    for (int i = 0; i < sortedPlays.length; i++) {
      sortedPlays[i] = unsortedPlays.get(i);
    }

    sortedPlays = InsertionSortPlays.sort(sortedPlays);

    return sortedPlays;
  }


}
