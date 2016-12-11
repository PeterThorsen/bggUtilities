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

    for (Player player : array) {
      double currentAverage = player.getAverageComplexity();
      if (player.getMaxComplexity() > maxComplexity) maxComplexity = player.getMaxComplexity();
      if (player.getMinComplexity() < minComplexity) minComplexity = player.getMinComplexity();

      averageComplexityGivingAllPlayersEqualWeight += currentAverage;
    }

    // To direct algorithm towards better recommendations
    averageComplexityGivingAllPlayersEqualWeight = averageComplexityGivingAllPlayersEqualWeight / Double.valueOf(array.length);


    ArrayList<BoardGame> allGamesMatchingCriteria = new ArrayList<>();

    BoardGame[] allGames = facadeController.getAllGames();

    for (BoardGame game : allGames) {

      // Don't include expansions
      if (game.isExpansion()) continue;

      if (numberOfPlayers > game.getMaxPlayers() || numberOfPlayers < game.getMinPlayers()) {
        continue;
      }
      if (maxTime < game.getMinPlaytime()) {
        continue;
      }
      if (minComplexity - 0.50 > game.getComplexity() || maxComplexity + 0.50 < game.getComplexity()) {
        continue;
      }
      // only add games within recommended games
      boolean found = false;
      for (int i = 0; i < game.getBestWith().length; i++) {
        if (game.getBestWith()[i] == numberOfPlayers) {
          found = true;
          break;
        }
      }
      if (!found) {
        for (int i = 0; i < game.getRecommendedWith().length; i++) {
          if (game.getRecommendedWith()[i] == numberOfPlayers) {
            found = true;
            break;
          }
        }
      }
      if (!found) continue;

      allGamesMatchingCriteria.add(game);
    }

    BoardGame[] allValid = new BoardGame[allGamesMatchingCriteria.size()];

    for (int i = 0; i < allValid.length; i++) {
      allValid[i] = allGamesMatchingCriteria.get(i);
    }

    BoardGameCounter[] bestCombination = buildBestCombination(allGamesMatchingCriteria, array, maxTime,
            averageComplexityGivingAllPlayersEqualWeight);

    BoardGameSuggestion suggestedGames = new BoardGameSuggestion(bestCombination, allValid);

    return suggestedGames;
  }

  /**
   * @param allGamesMatchingCriteria
   * @param allPlayers
   * @param maxTime
   * @param averageComplexityGivingAllPlayersEqualWeight
   * @return allOptions and suggested combination
   * <p>
   * Algorithm to suggest best games. Split into three main sections:
   * 1) Have the players played the game before, and when?
   * 2) How similar is the game to other games played by the players before?
   * 3) Absolute factors depending only on the specific game. This section checks the users personal rating,
   * whether it is easily possible to play the game within the given time limit (and whether it takes only a set
   * percentage of the time limit), how complex the game is, giving more points if it is close to the average rating
   * of all players and finally if the game is voted as "best with" the current player number.
   * <p>
   * The first two parts increase dynamically depending on players and games played, while the last will have a
   * hard max.
   */
  private BoardGameCounter[] buildBestCombination(ArrayList<BoardGame> allGamesMatchingCriteria, Player[] allPlayers,
                                                  int maxTime, double averageComplexityGivingAllPlayersEqualWeight) {

    BoardGameCounter[] gamesWithCounter = new BoardGameCounter[allGamesMatchingCriteria.size()];
    for (int i = 0; i < gamesWithCounter.length; i++) {
      gamesWithCounter[i] = new BoardGameCounter(allGamesMatchingCriteria.get(i));
    }

    for (int i = 0; i < gamesWithCounter.length; i++) {
      BoardGameCounter current = gamesWithCounter[i];

      calculatePlayerScore(allPlayers, current);
      calculateAbsoluteFactorsScore(current, maxTime, allPlayers, averageComplexityGivingAllPlayersEqualWeight);
    }
    return calculateSuggestedGames(gamesWithCounter, maxTime);

  }


  private void calculatePlayerScore(Player[] allPlayers, BoardGameCounter current) {
    int lengthAllPlayers = allPlayers.length;

    // Favor games that the user haven't played much
    if (current.game.getNumberOfPlays() < 3) {
      current.value += 10;
    }
    // For all players
    for (Player player : allPlayers) {

      if (player.gameToPlaysMap.containsKey(current.game)) {
        // If players have played the game, recommend it more
        current.value += 12.0 / allPlayers.length;

        // For each day passed since last play for each player, favor the game a bit more
        Play[] allPlaysForPlayer = player.allPlays;

        for (int k = 0; k < allPlaysForPlayer.length; k++) {
          if (!allPlaysForPlayer[k].getGame().equals(current.game)) { // Only match with current game
            continue;
          }

          // Calculating for days since last play for each player
          double dateScore = calculateDateScore(allPlaysForPlayer[k], current.game, lengthAllPlayers);
          current.value += dateScore;
          break; // Only add scores once
        }

        double personalGameRating = calculatePersonalRating(player, current.game, lengthAllPlayers);
        current.value += personalGameRating;
      }
      // How well does the complexity, type, mechanisms and categories match
      calculateCombinationScore(current, player, lengthAllPlayers + 1);
    }
  }

  private double calculatePersonalRating(Player player, BoardGame game, int lengthAllPlayers) {
    double rating = player.getPersonalRating(game);
    if(rating == 0) {
      rating = 5; // default to a rating of 5/10
    }
    // If 10/10 rating for all players, score a massive 50 points.
    return rating * 5 / lengthAllPlayers;
  }

  private double calculateDateScore(Play play, BoardGame game, int lengthAllPlayers) {

    String date = play.getDate();
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

    double absoluteMin = 12.0 / lengthAllPlayers;
    double increaseBy = absoluteMin / 400.0; // If above 400 days since last play, set the limit at 400 days
    return Math.min(absoluteMin, diffInDays * increaseBy);
  }

  private void calculateCombinationScore(BoardGameCounter current, Player player, int numberOfPlayers) {

    ArrayList<BoardGame> listOfMap = new ArrayList<>();

    for (BoardGame key : player.gameToPlaysMap.keySet()) {
      if (key.equals(current.game)) continue;
      listOfMap.add(key);
    }
    BoardGame[] allGames = new BoardGame[listOfMap.size()];

    for (int i = 0; i < allGames.length; i++) {
      allGames[i] = listOfMap.get(i);
    }

    // Don't include expansions
    ArrayList<BoardGame> noExpansions = new ArrayList<>();
    for (int i = 0; i < allGames.length; i++) {
      if (!allGames[i].isExpansion())
        noExpansions.add(allGames[i]);
    }
    allGames = new BoardGame[noExpansions.size()];
    for (int i = 0; i < allGames.length; i++) {
      allGames[i] = noExpansions.get(i);
    }

    BoardGame currentGame = current.game;
    double combinationScore = 0;

    // Favor high complexity
    if((player.getMaxComplexity() > 2.5) && Math.abs(player.getMaxComplexity() - currentGame.getComplexity()) <= 1) {
      combinationScore += 20 / (numberOfPlayers - 1);
    }

    GameMechanism[] currentMechanisms = currentGame.getMechanisms();
    GameCategory[] currentCategories = currentGame.getCategories();

    for (int i = 0; i < allGames.length; i++) {
      BoardGame otherGame = allGames[i];

      GameMechanism[] otherMechanisms = otherGame.getMechanisms();
      GameCategory[] otherCategories = otherGame.getCategories();
      if (currentGame.equals(otherGame)) continue;

      // Up to 6 points based on type match
      if (currentGame.getType().equals(otherGame.getType())) {
        combinationScore += 6.0 / numberOfPlayers / allGames.length;
      }

      // Up to 7 points based on mechanisms match
      for (int j = 0; j < currentMechanisms.length; j++) {
        GameMechanism cMech = currentMechanisms[j];
        for (int k = 0; k < otherMechanisms.length; k++) {
          GameMechanism oMech = otherMechanisms[k];
          if (cMech.equals(oMech)) {
            combinationScore += 7.0 / numberOfPlayers / allGames.length / otherMechanisms.length / currentMechanisms.length;
          }
        }
      }

      // Up to 7 points based on categories match
      for (int j = 0; j < currentCategories.length; j++) {
        GameCategory cCat = currentCategories[j];
        for (int k = 0; k < otherCategories.length; k++) {
          GameCategory oCat = otherCategories[k];
          if (cCat.equals(oCat)) {
            combinationScore += 7.0 / numberOfPlayers / allGames.length / otherCategories.length / currentCategories.length;
          }
        }
      }
    }
    current.value += combinationScore;
  }

  private void calculateAbsoluteFactorsScore(BoardGameCounter current, int maxTime, Player[] allPlayers,
                                             double averageComplexityGivingAllPlayersEqualWeight) {
    BoardGame currentGame = current.game;

    // Personal rating should matter
    String personalRatingString = currentGame.getPersonalRating();
    double personalRating;
    if (personalRatingString.equals("N/A")) {
      personalRating = 5;
    } else {
      personalRating = Double.valueOf(personalRatingString);
    }
    current.value += personalRating * 2;

    // If personal rating is higher than average rating
    String averageRatingString = currentGame.getAverageRating();
    if (!averageRatingString.equals("N/A")) {
      double averageRating = Double.valueOf(averageRatingString);
      if (averageRating < personalRating) {
        current.value += Math.min(5, (personalRating - averageRating) * 3);
      }
    }

    // Can we easily play this game within time limit
    int currentMinTime = currentGame.getMinPlaytime();
    int currentMaxTime = currentGame.getMaxPlaytime();

    double approximationTime;
    if (currentMinTime == currentMaxTime) {
      approximationTime = currentMaxTime;
    } else if (currentGame.getMinPlayers() == currentGame.getMaxPlayers()) {
      approximationTime = currentMinTime;

    } else {
      double difference = currentMaxTime - currentMinTime;
      difference = difference / (currentGame.getMaxPlayers() - currentGame.getMinPlayers());
      approximationTime = currentMinTime + (difference * (allPlayers.length + 1 - currentGame.getMinPlayers()));
    }

    // Add time to approximation if someone has to learn the game
    for (Player player : allPlayers) {
      if (!player.hasPlayed(currentGame)) {
        approximationTime += 10;
        break;
      }
    }
    current.approximateTime = approximationTime;

    if (approximationTime < maxTime) {
      current.value += 20;
    }

    // How fitting is the complexity
    double currentComplexity = currentGame.getComplexity();
    double difference = Math.abs(currentComplexity - averageComplexityGivingAllPlayersEqualWeight);

    // First, lower rating if complexity is too different
    if (difference >= 0.8) {
      current.value -= 5;
      if (difference >= 1.0) {
        current.value -= 2;
      }
      if (difference >= 1.2) {
        current.value -= 2;
      }
      if (difference >= 1.4) {
        current.value -= 2;
      }
      if (difference >= 1.5) {
        current.value -= 4;
      }
    }
    // If low different, recommend more!
    else if (difference <= 0.8) {
      current.value += 5;

      if (difference <= 0.5) {
        current.value += 5;
      }
      if (difference <= 0.4) {
        current.value += 3;
      }
      if (difference <= 0.3) {
        current.value += 3;
      }
      if (difference <= 0.2) {
        current.value += 4;
      }
    }

    // Prefer longer games
    int quartersPassed = (int) approximationTime / 15; // Rounding down due to using integers

    // Only add additional rating if actually a longer game
    if(quartersPassed > 3) {
      current.value += quartersPassed * 5; // An additional five points for every quarter of the game
    }

    // Finally, if the game is best with the player number, recommend it more
    int[] bestWith = currentGame.getBestWith();
    for (int i : bestWith) {
      if (i == allPlayers.length + 1) {
        current.value += 20;
        break;
      }
    }
  }

  private BoardGameCounter[] calculateSuggestedGames(BoardGameCounter[] gamesWithCounter, int maxTime) {
    gamesWithCounter = InsertionSortGamesWithCounter.sort(gamesWithCounter);
/**
    System.out.println("Ratings for each game");
    for (BoardGameCounter counter : gamesWithCounter) {
      System.out.println(counter.game + " : " + counter.value);
    }
 */

    ArrayList<BoardGameCounter> suggestedCombinationList = new ArrayList<>();
    int posOfFirstElement = 0;
    double currentTimeSpent = 0;

    for (int i = 0; i < gamesWithCounter.length; i++) {
      if (maxTime >= gamesWithCounter[i].approximateTime) {
        suggestedCombinationList.add(gamesWithCounter[i]);
        posOfFirstElement = i;
        currentTimeSpent += gamesWithCounter[i].approximateTime;
        break;
      }
    }
    for (int i = posOfFirstElement + 1; i < gamesWithCounter.length; i++) {
      double withinTime = currentTimeSpent + gamesWithCounter[i].approximateTime;
      if (maxTime >= withinTime) {
        suggestedCombinationList.add(gamesWithCounter[i]);
        currentTimeSpent = withinTime;
      }
    }
    BoardGameCounter[] suggestedCombination = new BoardGameCounter[suggestedCombinationList.size()];
    for (int i = 0; i < suggestedCombination.length; i++) {
      suggestedCombination[i] = suggestedCombinationList.get(i);
      //System.out.println("Choose game: " + suggestedCombination[i].game + " : " + suggestedCombination[i].value); // TODO: 07/12/2016
    }
    return suggestedCombination;
  }
}
