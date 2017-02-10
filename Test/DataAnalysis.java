import Main.Controllers.FacadeController;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Main.Models.Structure.*;
import Main.Sorting.InsertionSort;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Ignore
public class DataAnalysis {
  private FacadeController facadeController;
  private Play[] allPlays;
  private BoardGame[] allGames;
  private Player[] allPlayers;

  @Before
  public void setUp() {

    ICollectionBuilder collectionBuilder = new CollectionBuilder(new ConnectionHandler());
    String username = "cwaq";
    facadeController = new FacadeController(collectionBuilder, username);
    allGames = facadeController.getAllGames();
    allPlays = facadeController.getAllPlaysSorted();
    allPlayers = facadeController.getAllPlayers();
    System.out.println("Data fetched...");
  }

  @Test
  public void listAllGamesWithLastPlayedDate() {
    HashMap<BoardGame, String> firstDatesOfGames = new HashMap<>();
    for (Play play : allPlays) {
      boolean shouldContinue = false;
      for (String name : play.playerNames) {
        if (name.equals("Charlotte")) {
          shouldContinue = true;
        }
      }
      if (!shouldContinue) continue;
      BoardGame game = play.game;
      if (firstDatesOfGames.containsKey(game)) continue;
      firstDatesOfGames.put(game, play.date);
    }

    for (BoardGame game : firstDatesOfGames.keySet()) {
      System.out.println(game + " : " + firstDatesOfGames.get(game));
    }
  }

  @Test
  public void listGamesComplexitiesAndPlayersPersonalComplexities() {
    for (Player player : allPlayers) {
      System.out.println(player + " : " + player.getMagicComplexity() + " (" + player.getAverageComplexity() + ")");
    }
  }

  @Test
  public void makeRecommendation() {
    //String[] users = new String[]{"Charlotte", "Alf", "Mikkel", "Lisbeth"};
    //String[] users = new String[]{"Charlotte", "Martin", "Michelle", "Emil", "Signe", "Michael"};
    String[] users = new String[]{"Charlotte", "Martin"};

    int timeToPlay = 60;
    Player[] players = new Player[users.length];
    int counter = 0;
    for (Player player : allPlayers) {
      for (String user : users) {
        if (player.name.equals(user)) {
          players[counter] = player;
          counter++;
        }
      }
    }

    BoardGameSuggestion recommendation = facadeController.suggestGames(players, timeToPlay);
    System.out.println("Recommendation is: ");
    for (BoardGameCounter bgCounter : recommendation.suggestedCombination) {
      BoardGame game = bgCounter.game;
      ArrayList<Reason> reasons = bgCounter.reasons;
      Reason[] reasonArray = InsertionSort.sortReasons(reasons);
      System.out.println("---- Game: " + game + " ----");
      for (Reason reason : reasonArray) {
        System.out.println(reason);
      }
    }
  }

  @Test
  public void generateFavoriteAndNegativesForMachineLearning() {
    String[] users = new String[]{"Martin", "Niklas", "Mikkel Kaysen"};
    Player[] players = new Player[users.length];
    int counter = 0;

    for (Player player : allPlayers) {
      for (String user : users) {
        if (player.name.equals(user)) {
          players[counter] = player;
          counter++;
        }
      }
    }

    BufferedWriter writer = null;
    String fileName = "PN";
    for (String user : users) {
      fileName = fileName.concat("_".concat(user));
    }
    fileName = fileName.concat(".txt");
    try {

      FileWriter fstream = new FileWriter(fileName, true); //true tells to append data.
      writer = new BufferedWriter(fstream);


      BoardGame[] allGamesTemp = facadeController.getAllGames();
      allGamesTemp = removeExpansions(allGamesTemp);
      BoardGameCounter[] allGames = CalculateApproxTimes(allGamesTemp, players);
      int i = 10; //int i = 10;
      while (i <= 120) {
        writer.write("\nPlaytime: " + i + " minutes.\n");
        System.out.println("Current time: " + i);
        ArrayList<String[]> posList = new ArrayList<>();
        ArrayList<String[]> negList = new ArrayList<>();
        for (BoardGameCounter c1 : allGames) {
          double c1Total = 0;
          if (!isPositive(c1Total, c1, i, players)) {
            negList.add(new String[]{c1.toString()});
            continue;
          }
          c1Total += c1.approximateTime;
          if (c1Total + 10 >= i) {
            posList.add(new String[]{c1.toString()});
            continue;
          }

          c1Total += 5; // Time between games.. No rush
          for (BoardGameCounter c2 : allGames) {
            if (!isPositive(c1Total, c2, i, players)) {
              negList.add(new String[]{c1.toString(), c2.toString()});
              continue;
            }

            double c2Total = c1Total + c2.approximateTime;
            if (c2Total + 10 >= i) {
              posList.add(new String[]{c1.toString(), c2.toString()});
              continue;
            }
            c2Total += 5;


            for (BoardGameCounter c3 : allGames) {
              if (!isPositive(c2Total, c3, i, players)) {
                negList.add(new String[]{c1.toString(), c2.toString(), c3.toString()});

                continue;
              }

              double c3Total = c2Total + c3.approximateTime;
              if (c3Total + 10 >= i) {
                posList.add(new String[]{c1.toString(), c2.toString(), c3.toString()});
                continue;
              }
              c3Total += 5;
              for (BoardGameCounter c4 : allGames) {
                if (!isPositive(c3Total, c4, i, players)) {
                  negList.add(new String[]{c1.toString(), c2.toString(), c3.toString(), c4.toString()});
                  continue;
                }
                double c4Total = c3Total + c4.approximateTime;
                if (c4Total + 10 >= i) {
                  posList.add(new String[]{c1.toString(), c2.toString(), c3.toString(), c4.toString()});
                }

              }
            }
          }
        }

        writer.write("\n");
        writer.write("Positives:\n");
        posList = removeDuplicates(posList);

        for (String[] arr : posList) {
          String total = arr[0];
          for (int j = 1; j < arr.length; j++) {
            total = total.concat(" + ".concat(arr[j]));
          }
          writer.write(total + "\n");
        }
        writer.write("\n");
        writer.write("Negatives:\n");
        negList = removeDuplicates(negList);
        for (String[] arr : negList) {
          String total = arr[0];
          for (int j = 1; j < arr.length; j++) {
            total = total.concat(" + ".concat(arr[j]));
          }
          writer.write(total + "\n");
        }
        writer.write("\n");
        i += 5;
      }


    } catch (IOException e) {
      System.err.println("Error: " + e.getMessage());
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private BoardGame[] removeExpansions(BoardGame[] allGamesTemp) {
    ArrayList<BoardGame> list = new ArrayList<>();
    for (BoardGame game : allGamesTemp) {
      if (!game.isExpansion && !game.name.equals("Fluxx") && !game.name.equals("Sequence")) {
        list.add(game);
      }
    }

    BoardGame[] toReturn = new BoardGame[list.size()];
    for (int i = 0; i < list.size(); i++) {
      toReturn[i] = list.get(i);
    }
    return toReturn;
  }

  private boolean isPositive(double totalTime, BoardGameCounter c, int i, Player[] players) {

    if (totalTime + c.approximateTime > i) {
      return false;
    }
    boolean playableWithCurrentNumberOfPlayers = false;
    for (int recommendedWith : c.game.recommendedWith) {
      if (players.length + 1 == recommendedWith) {
        playableWithCurrentNumberOfPlayers = true;
        break;
      }
    }
    for (int bestWith : c.game.bestWith) {
      if (players.length + 1 == bestWith) {
        playableWithCurrentNumberOfPlayers = true;
        break;
      }
    }
    if (!playableWithCurrentNumberOfPlayers) {
      return false;
    }

    for (Player currentPlayer : players) {
      if (currentPlayer.getMagicComplexity() + 0.8 < c.game.complexity) {
        return false;
      }
      if (currentPlayer.getPersonalRating(c.game) < 6 && currentPlayer.getPersonalRating(c.game) != 0) {
        return false;
      }
    }
    return true;
  }

  private BoardGameCounter[] CalculateApproxTimes(BoardGame[] allGamesTemp, Player[] players) {
    BoardGameCounter[] allGames = new BoardGameCounter[allGamesTemp.length];
    int counter = 0;
    for (BoardGame currentGame : allGamesTemp) {
      int currentMinTime = currentGame.minPlaytime;
      int currentMaxTime = currentGame.maxPlaytime;
      double approximationTime;

      if (currentMinTime == currentMaxTime) {
        approximationTime = currentMaxTime;
      } else if (currentGame.minPlayers == currentGame.maxPlayers) {
        approximationTime = currentMinTime;

      } else {
        double difference = currentMaxTime - currentMinTime;
        difference = difference / (currentGame.maxPlayers - currentGame.minPlayers);
        approximationTime = currentMinTime + (difference * (players.length + 1 - currentGame.minPlayers));
      }


      if (currentGame.minPlayers == currentGame.maxPlayers) {
        approximationTime = currentMinTime + (currentMaxTime - currentMinTime) / 2;
      }


      // Add time to approximation if someone has to learn the game
      for (Player player : players) {
        if (!player.hasPlayed(currentGame)) {
          approximationTime += 10;
          break;
        }
      }
      BoardGameCounter currentCounter = new BoardGameCounter(currentGame);
      currentCounter.approximateTime = approximationTime;
      allGames[counter] = currentCounter;
      counter++;
    }
    return allGames;

  }

  private ArrayList<String[]> removeDuplicates(ArrayList<String[]> list) {
    ArrayList<String[]> uniques = new ArrayList<>();
    outer:
    for (String[] current : list) {
      // Check if all substrings are parts of a single unique
      // Otherwise add it
      if (current.length == 1) {
        uniques.add(current);
        continue;
      }

      for (String[] unique : uniques) {
        if (isMatching(current, unique)) continue outer;
      }
      uniques.add(current);


    }
    return uniques;

  }

  private boolean isMatching(String[] current, String[] unique) {
    if (current.length != unique.length) return false;
    int counter = 2;
    try {
      for (int i = 2; i < 4; i++) {

        String s = current[i];
        counter++;
      }
    } catch (ArrayIndexOutOfBoundsException e) {
    }

    String[] modifiedCurrent = new String[counter];
    boolean[] hasBeenFilled = new boolean[counter];

    for (int i = 0; i < counter; i++) {
      modifiedCurrent[i] = current[i];
      hasBeenFilled[i] = false;
    }
    for (int i = 0; i < counter; i++) {
      String u = unique[i];
      for (int j = 0; j < counter; j++) {
        if (u.equals(current[j]) && !hasBeenFilled[j]) {
          hasBeenFilled[j] = true;
        }
      }
    }
    for (boolean bool : hasBeenFilled) {
      if (!bool) return false;
    }
    return true;
  }
}
