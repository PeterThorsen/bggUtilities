package Main.Containers;

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

  public int getNumberOfPlayers() {
    return allPlayers.size();
  }

  public void addPlay(Play play) {
    String gameName = "";
    try {
      gameName = play.getGame().getName();
    }
    catch (Exception e) {
      System.out.println("ex, play is: " + play.getGame()); // TODO: 09/11/2016 null ved agentkuo, hvorfor findes game ikke
    }
    for (String player : play.getPlayers()) {

      if (!allPlayers.containsKey(player)) {
        allPlayers.put(player, play.getQuantity());
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(player, gamePlaysByPlayer);
        continue;
      }
      allPlayers.put(player, allPlayers.get(player) + play.getQuantity());

      if (!playsByPerson.containsKey(player)) {
        HashMap<String, Integer> gamePlaysByPlayer = new HashMap<>();
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(player, gamePlaysByPlayer);
        continue;
      }

      HashMap<String, Integer> gamePlaysByPlayer = playsByPerson.get(player);
      if (!gamePlaysByPlayer.containsKey(gameName)) {
        gamePlaysByPlayer.put(gameName, play.getQuantity());
        playsByPerson.put(player, gamePlaysByPlayer);
        continue;
      }
      int noOfPlays = gamePlaysByPlayer.get(gameName);
      noOfPlays += play.getQuantity();
      gamePlaysByPlayer.put(gameName, noOfPlays);
      playsByPerson.put(player, gamePlaysByPlayer);
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

  public HashMap<String, Integer> getPlayerToPlaysMap() {
    return allPlayers;
  }

  public HashMap<String, GameNameAndPlayHolder> getMostPlayedGamesByPlayers() {
    HashMap<String, GameNameAndPlayHolder> maxPlays = new HashMap<>();

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
      GameNameAndPlayHolder holder = new GameNameAndPlayHolder(maxName, maxGamePlays);
      maxPlays.put(player, holder);
    }
    return maxPlays;
  }

  public String getLastPlayedGame(String name) {
    int mostRecentYear = 0;
    int mostRecentMonth = 0;
    int mostRecentDay = 0;
    String mostRecentGame = "";

    for (int key : allPlays.keySet()) {
      ArrayList<Play> playsOfSpecificGame = allPlays.get(key);
      for (Play play : playsOfSpecificGame) {

        String[] players = play.getPlayers();
        for (String player : players) {
          if(player.equals(name)) {
            String gameName = play.getGame().getName();
            String date = play.getDate();
            String[] splitDate = date.split("-");
            int year = Integer.valueOf(splitDate[0]);
            int month = Integer.valueOf(splitDate[1]);
            int day = Integer.valueOf(splitDate[2]);

            if(mostRecentYear > year) {
              break;
            }
            if(mostRecentYear < year) {
              mostRecentYear = year;
              mostRecentMonth = month;
              mostRecentDay = day;
              mostRecentGame = gameName;
              break;
            }
            if(mostRecentMonth > month) {
              break;
            }
            if(mostRecentMonth < month) {
              mostRecentYear = year;
              mostRecentMonth = month;
              mostRecentDay = day;
              mostRecentGame = gameName;
              break;
            }
            if(mostRecentDay > day) {
              break;
            }
            if(mostRecentDay < day) {
              mostRecentYear = year;
              mostRecentMonth = month;
              mostRecentDay = day;
              mostRecentGame = gameName;
            }
            break;
          }
        }
      }
    }
    return mostRecentGame;
  }

  public Play[] getAllPlaysSorted() {
    ArrayList<Play> unsortedPlays = new ArrayList<>();
    for (int key : allPlays.keySet()) {
      unsortedPlays.addAll(allPlays.get(key));
    }
    Play[] sortedPlays = new Play[unsortedPlays.size()];
    for (int i = 0; i < sortedPlays.length; i++) {
      sortedPlays[i] = unsortedPlays.get(i);
    }

    sortedPlays = insertionSort(sortedPlays);

    return sortedPlays;
  }

  private Play[] insertionSort(Play[] sortedPlays) {
    int j;                     // the number of items sorted so far
    Play key;                // the item to be inserted
    int i;

    for (j = 1; j < sortedPlays.length; j++)    // Start with 1 (not 0)
    {
      key = sortedPlays[j];
      for(i = j - 1; (i >= 0) && (sortedPlays[i].getDate().compareTo(key.getDate()) < 0); i--)   // Smaller values are moving up
      {
        sortedPlays[ i+1 ] = sortedPlays[i];
      }
      sortedPlays[ i+1 ] = key;    // Put the key in its proper location
    }
    return sortedPlays;
  }


}
