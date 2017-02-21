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

      ArrayList<BoardGame[]> combinations = fillRecommendedForMinuteCountAndPlayers(playTime, players);
      for (BoardGame[] combination : combinations) {
        String output = combination[0].name;
        for (int i = 1; i < combination.length; i++) {
          output = output.concat(" + ").concat(combination[i].name);
        }
        System.out.println(output);
      }
      /*for (int i = 0; i < 5; i++) {
        BoardGameCounter[] result = controller.suggestGames(players, playTime).suggestedCombination;

        for (int y = 0; y < 3; y++) {
          for (BoardGame[] combination : combinations) {
            for (BoardGame game : combination) {
              evaluateGame(game, combination.length, result);
            }
            evaluateCombination(combination, result);
          }
        }
      }*/
    }
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
      while(true) {
        int position = random.nextInt(players.length);
        if(filledPlayers[position]) continue;
        sb.append("_").append(players[position].name);
        filledPlayers[position] = true;
        filledCounter++;

        if(filledCounter == players.length) break;

      }

      sb.append(".txt");
      File file = new File("PN_Charlotte_Alf_Lisbeth_Mikkel_Bolette.txt");
      if(file.exists() && !file.isDirectory()) {
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
      while ((line = reader.readLine()) != null)
      {
        if(!registerGames) {
          try {
            int minuteCount = Integer.parseInt(line.split("Playtime: ")[1].split(" minutes")[0]);
            if (minuteCount != playTime) continue;
            registerGames = true;
            continue;
          } catch (Exception e) {
            continue;
          }
        }

        if(line.equals("")) break;
        String[] currentGamesSplitted;
        try {
          currentGamesSplitted = line.split(" \\+ ");
          System.out.println(currentGamesSplitted[1]);
        }
        catch (Exception e) {
          currentGamesSplitted = new String[] {line};
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
/*


    // Split string to get minute count as int
    try {
      String toSplit = "Playtime: 15 minutes.";
      int minuteCount = Integer.parseInt(toSplit.split("Playtime: ")[1].split(" minutes")[0]);
      System.out.println(minuteCount);
    } catch (Exception e) {

    }
    System.out.println("done");
*/




    /*combinations.add(new BoardGame[]{controller.getGame("Agricola")});
    combinations.add(new BoardGame[]{controller.getGame("Camel Up"), controller.getGame("Carcassonne")});
    combinations.add(new BoardGame[]{controller.getGame("Camel Up"), controller.getGame("Crokinole")});
    combinations.add(new BoardGame[]{controller.getGame("Camel Up"), controller.getGame("Hanabi")});
    combinations.add(new BoardGame[]{controller.getGame("Camel Up"), controller.getGame("Jaipur")});
    combinations.add(new BoardGame[]{controller.getGame("Camel Up"), controller.getGame("Tsuro"), controller.getGame("Tsuro")});
    combinations.add(new BoardGame[]{controller.getGame("Carcassonne"), controller.getGame("Hanabi")});
    combinations.add(new BoardGame[]{controller.getGame("Carcassonne"), controller.getGame("Hey, That's My Fish!")});
    combinations.add(new BoardGame[]{controller.getGame("Carcassonne"), controller.getGame("Hive")});
    combinations.add(new BoardGame[]{controller.getGame("Carcassonne"), controller.getGame("Onitama")});
    combinations.add(new BoardGame[]{controller.getGame("Carcassonne"), controller.getGame("Tsuro")});
    combinations.add(new BoardGame[]{controller.getGame("Crokinole"), controller.getGame("Hanabi")});
    combinations.add(new BoardGame[]{controller.getGame("Crokinole"), controller.getGame("Hey, That's My Fish!")});
    combinations.add(new BoardGame[]{controller.getGame("Crokinole"), controller.getGame("Hive")});
    combinations.add(new BoardGame[]{controller.getGame("Crokinole"), controller.getGame("Onitama")});
    combinations.add(new BoardGame[]{controller.getGame("Crokinole"), controller.getGame("Tsuro")});
    combinations.add(new BoardGame[]{controller.getGame("Hanabi"), controller.getGame("Hey, That's My Fish!")});
    combinations.add(new BoardGame[]{controller.getGame("Hanabi"), controller.getGame("Hive")});
    combinations.add(new BoardGame[]{controller.getGame("Hanabi"), controller.getGame("Jaipur")});
    combinations.add(new BoardGame[]{controller.getGame("Hanabi"), controller.getGame("Onitama")});
    combinations.add(new BoardGame[]{controller.getGame("Hey, That's My Fish!"), controller.getGame("Jaipur")});
    combinations.add(new BoardGame[]{controller.getGame("Hey, That's My Fish!"), controller.getGame("Tsuro"), controller.getGame("Tsuro")});
    combinations.add(new BoardGame[]{controller.getGame("Hive"), controller.getGame("Tsuro"), controller.getGame("Tsuro")});
    combinations.add(new BoardGame[]{controller.getGame("Hive"), controller.getGame("Jaipur")});
    combinations.add(new BoardGame[]{controller.getGame("Jaipur"), controller.getGame("Onitama")});
    combinations.add(new BoardGame[]{controller.getGame("Jaipur"), controller.getGame("Tsuro")});
    combinations.add(new BoardGame[]{controller.getGame("Onitama"), controller.getGame("Tsuro"), controller.getGame("Tsuro")});*/
    //return combinations;
  }


}
