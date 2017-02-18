package Main.Models.MachineLearning;

import Main.Controllers.FacadeController;
import Main.Factories.IStartupFactory;
import Main.Models.Storage.ICollectionBuilder;
import Main.Models.Structure.*;

import java.util.ArrayList;

public class TrainGameNightRecommendationEngine {
  private final FacadeController controller;
  private final Player[] allPlayers;
  private final BoardGameCollection collection;
  private final Plays plays;
  private final MachineLearningGameNightValues values;

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
    String[] users = new String[]{"Martin"};
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
    int playTime = 60;

    ArrayList<BoardGame[]> combinations = fillRecommendedForMinuteCount(playTime);

    for (int i = 0; i < 5; i++) {
      BoardGameCounter[] result = controller.suggestGames(players, playTime).suggestedCombination;

      for (int y = 0; y < 3; y++) {
        for (BoardGame[] combination : combinations) {
          for (BoardGame game : combination) {
            evaluateGame(game, combination.length, result);
          }
          evaluateCombination(combination, result);
        }
      }
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

  private ArrayList<BoardGame[]> fillRecommendedForMinuteCount(int playTime) {
    ArrayList<BoardGame[]> combinations = new ArrayList<>();
    combinations.add(new BoardGame[] {controller.getGame("Agricola") });
    combinations.add(new BoardGame[] {controller.getGame("Camel Up"), controller.getGame("Carcassonne") });
    combinations.add(new BoardGame[] {controller.getGame("Camel Up"), controller.getGame("Crokinole") });
    combinations.add(new BoardGame[] {controller.getGame("Camel Up"), controller.getGame("Hanabi") });
    combinations.add(new BoardGame[] {controller.getGame("Camel Up"), controller.getGame("Jaipur") });
    combinations.add(new BoardGame[] {controller.getGame("Camel Up"), controller.getGame("Tsuro"), controller.getGame("Tsuro") });
    combinations.add(new BoardGame[] {controller.getGame("Carcassonne"), controller.getGame("Hanabi") });
    combinations.add(new BoardGame[] {controller.getGame("Carcassonne"), controller.getGame("Hey, That's My Fish!") });
    combinations.add(new BoardGame[] {controller.getGame("Carcassonne"), controller.getGame("Hive") });
    combinations.add(new BoardGame[] {controller.getGame("Carcassonne"), controller.getGame("Onitama") });
    combinations.add(new BoardGame[] {controller.getGame("Carcassonne"), controller.getGame("Tsuro") });
    combinations.add(new BoardGame[] {controller.getGame("Crokinole"), controller.getGame("Hanabi") });
    combinations.add(new BoardGame[] {controller.getGame("Crokinole"), controller.getGame("Hey, That's My Fish!") });
    combinations.add(new BoardGame[] {controller.getGame("Crokinole"), controller.getGame("Hive") });
    combinations.add(new BoardGame[] {controller.getGame("Crokinole"), controller.getGame("Onitama") });
    combinations.add(new BoardGame[] {controller.getGame("Crokinole"), controller.getGame("Tsuro") });
    combinations.add(new BoardGame[] {controller.getGame("Hanabi"), controller.getGame("Hey, That's My Fish!") });
    combinations.add(new BoardGame[] {controller.getGame("Hanabi"), controller.getGame("Hive") });
    combinations.add(new BoardGame[] {controller.getGame("Hanabi"), controller.getGame("Jaipur") });
    combinations.add(new BoardGame[] {controller.getGame("Hanabi"), controller.getGame("Onitama") });
    combinations.add(new BoardGame[] {controller.getGame("Hey, That's My Fish!"), controller.getGame("Jaipur") });
    combinations.add(new BoardGame[] {controller.getGame("Hey, That's My Fish!"), controller.getGame("Tsuro"), controller.getGame("Tsuro") });
    combinations.add(new BoardGame[] {controller.getGame("Hive"), controller.getGame("Tsuro"), controller.getGame("Tsuro") });
    combinations.add(new BoardGame[] {controller.getGame("Hive"), controller.getGame("Jaipur") });
    combinations.add(new BoardGame[] {controller.getGame("Jaipur"), controller.getGame("Onitama") });
    combinations.add(new BoardGame[] {controller.getGame("Jaipur"), controller.getGame("Tsuro") });
    combinations.add(new BoardGame[] {controller.getGame("Onitama"), controller.getGame("Tsuro"), controller.getGame("Tsuro") });
    return combinations;
  }
}
