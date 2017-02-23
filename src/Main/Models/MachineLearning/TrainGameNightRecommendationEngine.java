package Main.Models.MachineLearning;

import Main.Controllers.FacadeController;
import Main.Factories.IStartupFactory;
import Main.Models.Storage.ICollectionBuilder;
import Main.Models.Structure.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class TrainGameNightRecommendationEngine {
  private final FacadeController controller;
  private final Player[] allPlayers;
  private final BoardGameCollection collection;
  private final Plays plays;
  private final MachineLearningGameNightValues values;
  Random random = new Random();

  public static void main(String[] args) {
    new TrainGameNightRecommendationEngine();
  }

  public TrainGameNightRecommendationEngine() {
    IStartupFactory factory = new MachineLearningFactory();
    values = (MachineLearningGameNightValues) factory.getGameNightValues();
    controller = new FacadeController(factory.getCollectionBuilder(), "cwaq", values);
    ICollectionBuilder collectionBuilder = factory.getCollectionBuilder();
    collection = collectionBuilder.getCollection(controller.username);
    allPlayers = collectionBuilder.getPlayers();
    plays = collectionBuilder.getPlays();
    train();
  }

  private void train() {
    String[][] allNames = new String[][]{{"Martin"}, {"Charlotte"}, {"Jesper"},
            {"Martin", "Charlotte"},
            {"Martin", "Charlotte", "Michelle"},
            {"Martin", "Michael", "Emil"},
            {"Martin", "Michael", "Emil", "Signe"},
            {"Charlotte", "Mikkel", "Bolette"},
            {"Charlotte", "Alf", "Lisbeth"},
            {"Charlotte", "Alf", "Lisbeth", "Mikkel"},
            {"Charlotte", "Alf", "Lisbeth", "Mikkel", "Bolette"},
            {"Charlotte", "Alf", "Lisbeth", "Mikkel", "Bolette", "Camilla"},
            {"Martin", "Josefine", "Daniella"},
            {"Marian", "Charlotte", "Poul"},
            {"Marian", "Charlotte", "Poul", "Jesper"},
            {"Marian", "Charlotte", "Jesper"},
            {"Marian", "Charlotte"},
            {"Marian", "Charlotte", "Palle", "Liss", "Jesper", "Poul", "Lillibeth"}
    };
    int[] playTimes = new int[]{10, 15, 15, 15, 20, 30, 30, 30, 40, 45, 45, 45, 60, 60, 70, 80, 90, 90, 100, 120, 120, 140, 150, 180, 180};

    String[] initialUsers = allNames[4];
    Player[] initialPlayers = getPlayersFromNames(initialUsers);
    BoardGameSuggestion initialRecommendation = controller.suggestGames(initialPlayers, playTimes[13]);

    for (int j = 0; j < 100; j++) {
      System.out.println("Starting iteration: " + j);
      String[] users = allNames[random.nextInt(allNames.length)];
      Player[] players = getPlayersFromNames(users);


      int playTime = playTimes[random.nextInt(playTimes.length)];

      ArrayList<BoardGame[]> goodSuggestions = fillRecommendedForMinuteCountAndPlayers(playTime, players);

      outer:
      for (int i = 0; i < 100; i++) {
        BoardGameCounter[] actualSuggestion = controller.suggestGames(players, playTime).suggestedCombination;
        BoardGame[] actualSuggestionAsGames = new BoardGame[actualSuggestion.length];
        for (int k = 0; k < actualSuggestion.length; k++) {
          actualSuggestionAsGames[k] = actualSuggestion[k].game;
        }

        for (BoardGame[] goodSuggestion : goodSuggestions) {
          if (arraysAreEqual(goodSuggestion, actualSuggestionAsGames)) {
            // Increase
            modifyValues(actualSuggestionAsGames, Constants.POSITIVE, players, playTime);
            continue outer;
          }
        }
        // Decrease
        modifyValues(actualSuggestionAsGames, Constants.NEGATIVE, players, playTime);
      }
    }

    System.out.println("Initial recommendation: ");
    for (int i = 0; i < initialRecommendation.suggestedCombination.length; i++) {
      System.out.println(initialRecommendation.suggestedCombination[i].game);
    }


    String[] finalUsers = allNames[4];
    Player[] finalPlayers = getPlayersFromNames(finalUsers);
    BoardGameSuggestion finalRecommendation = controller.suggestGames(finalPlayers, playTimes[13]);
    System.out.println("Final recommendation: ");
    for (int i = 0; i < finalRecommendation.suggestedCombination.length; i++) {
      System.out.println(finalRecommendation.suggestedCombination[i].game);
    }
  }

  private Player[] getPlayersFromNames(String[] users) {
    Player[] players = new Player[users.length];
    int iteration = 0;
    for (Player player : allPlayers) {
      for (String user : users) {
        if (player.name.equals(user)) {
          players[iteration] = player;
          iteration++;
        }
      }
    }
    return players;
  }

  private void modifyValues(BoardGame[] actualSuggestionAsGames, Constants constant, Player[] players, int playTime) {
    // get current score for current games
    BoardGameCounter[] old = controller.getRecommendationCounterForSingleGame(actualSuggestionAsGames, players, playTime);
    double oldScore = 0;
    for (int i = 0; i < old.length; i++) {
      oldScore += old[i].value;
    }

    double modifier = 1.1;
    if(constant.equals(Constants.NEGATIVE)) {
      modifier = 0.9;
    }

    // Modify all variables with either * 0.9 or *1.1 and get score then
    double bestScore = -1;
    double[] bestValues = new double[values.values.length];
    boolean bestValuesChanged = false;
    double[] copyOfActualValues = new double[values.values.length];
    System.arraycopy(values.values, 0, copyOfActualValues, 0, values.values.length);

    for (int i = 0; i < values.values.length; i++) {

      values.values[i] = values.values[i] * modifier;
      BoardGameCounter[] tempCounter = controller.getRecommendationCounterForSingleGame(actualSuggestionAsGames, players, playTime);

      double tempScore = 0;
      for (int j = 0; j < tempCounter.length; j++) {
        tempScore += tempCounter[j].value;
      }

      if(oldScore < tempScore && tempScore > bestScore) {
        bestScore = tempScore;
        System.arraycopy(values.values, 0, bestValues, 0, values.values.length);
        bestValuesChanged = true;
      }
      System.arraycopy(copyOfActualValues, 0, values.values, 0, values.values.length);
    }
    // Finally, replace values array in MachineLearningGameNightValues with the saved array.
    if(bestValuesChanged) System.arraycopy(bestValues, 0, values.values, 0, values.values.length);

  }


  private boolean arraysAreEqual(BoardGame[] goodSuggestion, BoardGame[] actualSuggestionAsGames) {
    if (goodSuggestion.length != actualSuggestionAsGames.length) return false;
    boolean[] filled = new boolean[actualSuggestionAsGames.length];

    outer:
    for (int i = 0; i < goodSuggestion.length; i++) {
      for (int j = 0; j < actualSuggestionAsGames.length; j++) {
        if (!filled[j] && goodSuggestion[i].equals(actualSuggestionAsGames[j])) {
          filled[j] = true;
          continue outer;
        }
      }
      return false;
    }
    return true;
  }

  private ArrayList<BoardGame[]> fillRecommendedForMinuteCountAndPlayers(int playTime, Player[] players) {
    ArrayList<BoardGame[]> combinations = new ArrayList<>();

    File foundFile;
    while (true) {
      StringBuilder sb = new StringBuilder();
      sb.append("PN");
      boolean[] filledPlayers = new boolean[players.length];
      int filledCounter = 0;
      while (true) {
        int position = random.nextInt(players.length);
        if (filledPlayers[position]) continue;
        sb.append("_").append(players[position].name);
        filledPlayers[position] = true;
        filledCounter++;

        if (filledCounter == players.length) break;

      }

      sb.append(".txt");
      File file = new File("PN_Charlotte_Alf_Lisbeth_Mikkel_Bolette.txt");
      if (file.exists() && !file.isDirectory()) {
        foundFile = file;
        break;
      }
    }

    // File has been found now
    BufferedReader reader;
    boolean registerGames = false;
    try {
      reader = new BufferedReader(new FileReader(foundFile));
      String line;
      while ((line = reader.readLine()) != null) {
        if (!registerGames) {
          try {
            int minuteCount = Integer.parseInt(line.split("Playtime: ")[1].split(" minutes")[0]);
            if (minuteCount != playTime) continue;
            registerGames = true;
            continue;
          } catch (Exception e) {
            continue;
          }
        }

        if (line.equals("")) break;
        String[] currentGamesSplitted;
        try {
          currentGamesSplitted = line.split(" \\+ ");
        } catch (Exception e) {
          currentGamesSplitted = new String[]{line};
        }


        BoardGame[] asGames = new BoardGame[currentGamesSplitted.length];
        for (int i = 0; i < currentGamesSplitted.length; i++) {
          asGames[i] = controller.getGame(currentGamesSplitted[i]);
        }
        combinations.add(asGames);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return combinations;
  }


}
