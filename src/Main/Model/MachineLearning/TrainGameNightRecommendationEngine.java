package Main.Model.MachineLearning;

import Main.Controller.FacadeController;
import Main.Controller.Factories.IStartupFactory;
import Main.Model.Storage.ICollectionBuilder;
import Main.Model.Structure.*;

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
  private int positiveCounter = 0;
  private int negativeCounter = 0;

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
    String[][] allNames = new String[][]{
            {"Martin"},
            {"Charlotte"},
            {"Jesper"},
            {"Martin", "Charlotte"},
            {"Martin", "Charlotte", "Michelle"},
            {"Martin", "Michael", "Emil"}, // 5

            {"Martin", "Michael", "Emil", "Signe"},
            {"Charlotte", "Mikkel", "Bolette"},
            {"Charlotte", "Alf", "Lisbeth"},
            {"Charlotte", "Alf", "Lisbeth", "Mikkel"},
            {"Charlotte", "Alf", "Lisbeth", "Mikkel", "Bolette"}, // 10

            {"Charlotte", "Alf", "Lisbeth", "Mikkel", "Bolette", "Camilla"},
            {"Martin", "Josefine", "Daniella"},
            {"Marian", "Charlotte", "Poul"},
            {"Marian", "Charlotte", "Poul", "Jesper"},
            {"Marian", "Charlotte", "Jesper"}, // 15

            {"Marian", "Charlotte"},
            {"Marian", "Charlotte", "Palle", "Liss", "Jesper", "Poul", "Lillibeth"},
            {"Marian", "Charlotte", "Liss", "Lillibeth"}
    };
    int[] playTimes = new int[]{
            10, 15, 15, 20, 20, 30, // 0 to 5
            30, 30, 40, 45, 45, // 6 to 10
            45, 60, 60, 70, 80, // 11 to 15
            90, 90, 100, 120, 120, // 16 to 20
            140, 150, 180, 180}; // 21 to 24

<<<<<<< HEAD:src/Main/Models/MachineLearning/TrainGameNightRecommendationEngine.java
    String[] initialUsers = allNames[18];
    Player[] initialPlayers = getPlayersFromNames(initialUsers);
    int initialPlaytime = playTimes[20];
    BoardGameSuggestion initialRecommendation = controller.suggestGames(initialPlayers, initialPlaytime);

    for (int j = 0; j < 15000; j++) {
=======
    String[] initialUsers = allNames[9];
    Player[] initialPlayers = getPlayersFromNames(initialUsers);
    BoardGameSuggestion initialRecommendation = controller.suggestGames(initialPlayers, playTimes[20]);

    for (int j = 0; j < 100; j++) {
>>>>>>> faa0666789e69a14b0bf9f59f11feeeb1952b12a:src/Main/Model/MachineLearning/TrainGameNightRecommendationEngine.java
      if(j % 100 == 0) System.out.println("Iteration j: " + j);
      String[] users = allNames[random.nextInt(allNames.length)];
      Player[] players = getPlayersFromNames(users);


      int playTime = playTimes[random.nextInt(playTimes.length)];

      ArrayList<BoardGame[]> goodSuggestions = fillRecommendedForMinuteCountAndPlayers(playTime, players);

      outer:
<<<<<<< HEAD:src/Main/Models/MachineLearning/TrainGameNightRecommendationEngine.java
      for (int i = 0; i < 7; i++) {
=======
      for (int i = 0; i < 15; i++) {
>>>>>>> faa0666789e69a14b0bf9f59f11feeeb1952b12a:src/Main/Model/MachineLearning/TrainGameNightRecommendationEngine.java
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

    printResult(initialRecommendation, initialUsers, initialPlaytime);
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
    BoardGameCounter[] old = controller.getBestCombinationForGame(actualSuggestionAsGames, players, playTime);
    double oldScore = 0;
    for (int i = 0; i < old.length; i++) {
      oldScore += old[i].value;
    }

    double modifier = 1.01;
    double oppositeModifier = 0.99;
    if(constant.equals(Constants.NEGATIVE)) {
      modifier = 0.99;
      oppositeModifier = 1.01;
    }

    double bestScore;
    if(constant.equals(Constants.POSITIVE)) {
      bestScore = Double.NEGATIVE_INFINITY;
    }
    else {
      bestScore = Double.POSITIVE_INFINITY;
    }

    double[] bestValues = new double[values.values.length];
    boolean bestValuesChanged = false;
    double[] copyOfActualValues = new double[values.values.length];
    System.arraycopy(values.values, 0, copyOfActualValues, 0, values.values.length);

    for (int i = 0; i < values.values.length; i++) {

      if(values.values[i] > 0) {
        values.values[i] = values.values[i] * modifier;
      }
      else {
        values.values[i] = values.values[i] * oppositeModifier;
      }
      BoardGameCounter[] tempCounter = controller.getBestCombinationForGame(actualSuggestionAsGames, players, playTime);

      double tempScore = 0;
      for (int j = 0; j < tempCounter.length; j++) {
        tempScore += tempCounter[j].value;
      }

      if(oldScore < tempScore && tempScore > bestScore && Constants.POSITIVE.equals(constant)) {
        bestScore = tempScore;
        System.arraycopy(values.values, 0, bestValues, 0, values.values.length);
        bestValuesChanged = true;
        positiveCounter++;
      }
      else if (oldScore > tempScore && tempScore < bestScore && Constants.NEGATIVE.equals(constant)) {
        bestScore = tempScore;
        System.arraycopy(values.values, 0, bestValues, 0, values.values.length);
        bestValuesChanged = true;
        negativeCounter++;
      }
      System.arraycopy(copyOfActualValues, 0, values.values, 0, values.values.length);
    }
    // Finally, replace values array in MachineLearningGameNightValues with the saved array.
    if(bestValuesChanged) {
      bestValues = normalizeValues(bestValues);
      System.arraycopy(bestValues, 0, values.values, 0, values.values.length);
    }

  }

  private double[] normalizeValues(double[] bestValues) {
    double total = 0;
    for (int i = 0; i < bestValues.length; i++) {
      total += bestValues[i];
    }
    for (int i = 0; i < bestValues.length; i++) {
      bestValues[i] = bestValues[i]/total*1000;
    }
    return bestValues;
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

  private void printResult(BoardGameSuggestion initialRecommendation, String[] finalUsers, int playTime) {

    for (int i = 0; i < values.values.length; i++) {
      System.out.println(i + " : " + values.values[i]);
    }
    System.out.println("Pos: " + positiveCounter);
    System.out.println("Neg: " + negativeCounter);
    System.out.println();

    System.out.println("Initial recommendation: ");
    for (int i = 0; i < initialRecommendation.suggestedCombination.length; i++) {
      System.out.println(initialRecommendation.suggestedCombination[i].game);
    }

    Player[] finalPlayers = getPlayersFromNames(finalUsers);
    BoardGameSuggestion finalRecommendation = controller.suggestGames(finalPlayers, playTime);
    System.out.println("Final recommendation: ");
    for (int i = 0; i < finalRecommendation.suggestedCombination.length; i++) {
      System.out.println(finalRecommendation.suggestedCombination[i].game);
    }
  }

}
