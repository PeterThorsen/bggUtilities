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

    for (int j = 0; j < 1; j++) {
      String[] users = allNames[random.nextInt(allNames.length)];

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
      int playTime = playTimes[random.nextInt(playTimes.length)];

      ArrayList<BoardGame[]> goodSuggestions = fillRecommendedForMinuteCountAndPlayers(playTime, players);

      outer:
      for (int i = 0; i < 1000; i++) {
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
  }

  private void modifyValues(BoardGame[] actualSuggestionAsGames, Constants modifier, Player[] players, int playTime) {
    // get current score for current games
    BoardGameCounter[] old = controller.getRecommendationCounterForSingleGame(actualSuggestionAsGames, players, playTime);

    // Modify all variables with either * 0.9 or *1.1 and get score then
    // Always store max/min (depending on modifier) score and it's related values array
    // Finally, replace values array in MachineLearningGameNightValues with the saved array.

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

  private void evaluateCombination(BoardGame[] combination, BoardGameCounter[] result) {

  }

  private void evaluateGame(BoardGame game, int gamesInCombination, BoardGameCounter[] result) {
    // Find score in result and compare it
    // Calculate the score again, but note all factors
    // Set all values to 1 and iterate through each giving it a rating of 1000
    // See which factor would
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
