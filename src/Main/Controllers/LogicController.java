package Main.Controllers;

import Main.Containers.*;
import Main.Sorting.InsertionSortGamesWithCounter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Peter on 14/11/2016.
 */
public class LogicController implements ILogicController {
  private FacadeController facadeController;

  public LogicController(FacadeController facadeController) {
    this.facadeController = facadeController;
  }

  @Override
  public BoardGameSuggestion suggestGames(Player[] array, int maxTime) {

    int numberOfPlayers = array.length + 1; // +1 to include bgg user herself
    double minComplexity = 6.0;
    double maxComplexity = 0.0;

    double averageComplexityGivingAllPlayersEqualWeight = 0;
    double weighedAverageComplexityOfAllPlayers = 0;

    for (Player player : array) {
      double thisComplexity = 0.0;
      double noOfPlays = 0;
      for (Play play : player.allPlays) {
        double gameComplexity = play.getGame().getComplexity();
        thisComplexity += gameComplexity * play.getQuantity();
        noOfPlays += play.getQuantity();

        if (gameComplexity < minComplexity) {
          minComplexity = gameComplexity;
        }
        if (gameComplexity > maxComplexity) {
          maxComplexity = gameComplexity;
        }
      }

      thisComplexity = thisComplexity / noOfPlays;

      averageComplexityGivingAllPlayersEqualWeight += thisComplexity;
      weighedAverageComplexityOfAllPlayers += thisComplexity;
    }

    // To direct algorithm towards better recommendations
    averageComplexityGivingAllPlayersEqualWeight = averageComplexityGivingAllPlayersEqualWeight / Double.valueOf(array.length);
    weighedAverageComplexityOfAllPlayers = weighedAverageComplexityOfAllPlayers / array.length;


    ArrayList<BoardGame> allGamesMatchingCriteria = new ArrayList<>();

    BoardGame[] allGames = facadeController.getAllGames();

    for (BoardGame game : allGames) {
      if (numberOfPlayers > game.getMaxPlayers() || numberOfPlayers < game.getMinPlayers()) {
        continue;
      }
      if (maxTime < game.getMinPlaytime()) {
        continue;
      }
      if (minComplexity - 0.50 > game.getComplexity() || maxComplexity + 0.50 < game.getComplexity()) {
        continue;
      }
      allGamesMatchingCriteria.add(game);
    }

    BoardGame[] allValid = new BoardGame[allGamesMatchingCriteria.size()];

    for (int i = 0; i < allValid.length; i++) {
      allValid[i] = allGamesMatchingCriteria.get(i);
    }

    BoardGame[] bestCombination = buildBestCombination(allGamesMatchingCriteria, array, maxTime,
            averageComplexityGivingAllPlayersEqualWeight, weighedAverageComplexityOfAllPlayers);

    BoardGameSuggestion suggestedGames = new BoardGameSuggestion(bestCombination, allValid);

    return suggestedGames;
  }


  private BoardGame[] buildBestCombination(ArrayList<BoardGame> allGamesMatchingCriteria, Player[] allPlayers,
                                           int maxTime, double averageComplexityGivingAllPlayersEqualWeight,
                                           double weighedAverageComplexityOfAllPlayers) {


    BoardGameCounter[] gamesWithCounter = new BoardGameCounter[allGamesMatchingCriteria.size()];
    for (int i = 0; i < gamesWithCounter.length; i++) {
      gamesWithCounter[i] = new BoardGameCounter(allGamesMatchingCriteria.get(i));
    }

    for (int i = 0; i < gamesWithCounter.length; i++) {
      BoardGame currentGame = gamesWithCounter[i].game;
      int currentGameMinTime = currentGame.getMinPlaytime();
      int currentGameMaxTime = currentGame.getMaxPlaytime();

      if (currentGame.getNumberOfPlays() < 2) {
        gamesWithCounter[i].value += 8;
      }

      // For all players
      for (int j = 0; j < allPlayers.length; j++) {

        if (allPlayers[j].gameToPlaysMap.containsKey(currentGame.getName())) {

          // Should be higher recommended if players have tried the game
          gamesWithCounter[i].value += 5;

          // For each day passed since last play for each player, favor the game a bit more
          Play[] allPlaysForPlayerJ = allPlayers[j].allPlays;
          for (int k = 0; k < allPlaysForPlayerJ.length; k++) {
            if (allPlaysForPlayerJ[k].getGame().equals(currentGame)) {
              String date = allPlaysForPlayerJ[k].getDate();
              Date dateFormatted;
              DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
              try {
                dateFormatted = df.parse(date);
              } catch (ParseException e) {
                dateFormatted = null;
                e.printStackTrace();
              }

              // Calculate days since last play
              Date dateNow = new Date();
              Calendar cal1 = Calendar.getInstance();
              Calendar cal2 = Calendar.getInstance();
              cal1.setTime(dateFormatted);
              cal2.setTime(dateNow);
              long timeInMilisPlayTime = cal1.getTimeInMillis();
              long timeInMilisCurrentTime = cal2.getTimeInMillis();
              long diff = timeInMilisCurrentTime - timeInMilisPlayTime;
              long diffInDays = diff / 1000 / 60 / 60 / 24;
              gamesWithCounter[i].value += Math.min(3, diffInDays * 0.015);
            }
          }
        }
      }

      // Personal rating should matter
      String personalRatingString = currentGame.getPersonalRating();
      double personalRating;
      if (personalRatingString.equals("N/A")) {
        personalRating = 5;
      } else {
        personalRating = Double.valueOf(personalRatingString);
      }
      gamesWithCounter[i].value += personalRating * 3;

      // - if personal rating is higher than average rating
      String averageRatingString = currentGame.getAverageRating();
      if (!averageRatingString.equals("N/A")) {
        double averageRating = Double.valueOf(averageRatingString);
        if (averageRating > personalRating) {
          gamesWithCounter[i].value += (averageRating - personalRating) * 2;
        }
      }

      // How well does the game combine with other valid games? Favor combining two games
      double combinationScore = 0;
      for (int j = 0; j < gamesWithCounter.length; j++) {
        if (i == j) continue;

        int otherMinTime = gamesWithCounter[j].game.getMinPlaytime();
        int otherMaxTime = gamesWithCounter[j].game.getMaxPlaytime();

        if (currentGameMinTime + otherMinTime <= maxTime) {
          combinationScore += 0.1;
        }
        if (currentGameMaxTime + otherMaxTime <= maxTime) {
          combinationScore += 0.2;
        }
      }
      combinationScore = Math.min(combinationScore, 15);
      gamesWithCounter[i].value += combinationScore;

      // If in doubt of whether the game can fit within time limit
      if (currentGameMaxTime > maxTime) {
        gamesWithCounter[i].value -= 5;

        // Some games have playtime increasing linearly by player amount, see Agricola
        if (currentGameMaxTime / allPlayers.length > maxTime) {
          gamesWithCounter[i].value -= 8;
        }
      }

      // Calculating complexity fit
      double currentComplexity = currentGame.getComplexity();

      double difference = Math.abs(currentComplexity - averageComplexityGivingAllPlayersEqualWeight);
      //double differenceWeighed = Math.abs(currentComplexity - weighedAverageComplexityOfAllPlayers);

      if (difference >= 0.8) {
        gamesWithCounter[i].value -= 5;
        if (difference >= 1.0) {
          gamesWithCounter[i].value -= 2;
        }
        if (difference >= 1.2) {
          gamesWithCounter[i].value -= 2;
        }
        if (difference >= 1.4) {
          gamesWithCounter[i].value -= 2;
        }
        if (difference > 1.5) {
          gamesWithCounter[i].value -= 5;
        }
      } else if (difference <= 0.5) {
        gamesWithCounter[i].value += 1;

        if (difference <= 0.4) {
          gamesWithCounter[i].value += 1;
        }
        if (difference <= 0.3) {
          gamesWithCounter[i].value += 1;
        }
        if (difference <= 0.2) {
          gamesWithCounter[i].value += 1;
        }
        if (difference <= 0.1) {
          gamesWithCounter[i].value += 1;
        }
      }

    }
    gamesWithCounter = InsertionSortGamesWithCounter.sort(gamesWithCounter);

    // TODO: 15-Nov-16  REMOVE
    for (int i = 0; i < gamesWithCounter.length; i++) {
      System.out.println(gamesWithCounter[i].game.getName() + ", " + gamesWithCounter[i].value + " <---");
    }

    // If no combination can be found within first 6 games, return highest recommendation
    BoardGame[] suggestedCombination = new BoardGame[1];
    suggestedCombination[0] = gamesWithCounter[0].game;

    int countTo = Math.min(6, gamesWithCounter.length);
    for (int i = 0; i < countTo; i++) {
      ArrayList<BoardGame> currentCombination = new ArrayList<>();
      BoardGame gameI = gamesWithCounter[i].game;
      currentCombination.add(gameI);
      int remainingTime = maxTime - gameI.getMinPlaytime();
      for (int j = i+1; j < countTo; j++) {
        BoardGame gameJ = gamesWithCounter[j].game;
        if (maxTime >= remainingTime + gameJ.getMinPlaytime()) {
          remainingTime += gameJ.getMinPlaytime();
          currentCombination.add(gameJ);
        }
      }
      if (currentCombination.size() > 1) {
        suggestedCombination = new BoardGame[currentCombination.size()];
        for (int j = 0; j < suggestedCombination.length; j++) {
          suggestedCombination[j] = currentCombination.get(j);
        }
        break;
      }
    }
    return suggestedCombination;
  }
}
