package Main.Models.Logic;

import Main.Models.Structure.Reason;
import Main.Models.Structure.*;
import Main.Sorting.InsertionSortGamesWithCounter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Peter on 13-Dec-16.
 */
public class GameNightRecommender {


  public double[] calculateAverageComplexity(Player[] players) {
    double minComplexity = 6.0;
    double maxComplexity = 0.0;
    double averageComplexityGivingAllPlayersEqualWeight = 0;

    for (Player player : players) {
      double currentAverage = player.getAverageComplexity();
      if (player.getMaxComplexity() > maxComplexity) maxComplexity = player.getMaxComplexity();
      if (player.getMinComplexity() < minComplexity) minComplexity = player.getMinComplexity();

      averageComplexityGivingAllPlayersEqualWeight += currentAverage;
    }

    // To direct algorithm towards better recommendations
    averageComplexityGivingAllPlayersEqualWeight = averageComplexityGivingAllPlayersEqualWeight / Double.valueOf(players.length);
    return new double[] {minComplexity, maxComplexity, averageComplexityGivingAllPlayersEqualWeight};
  }

  public BoardGame[] getAllValidBoardgames(Player[] players, int maxTime, BoardGame[] allGames, double minComplexity, double maxComplexity) {
    int numberOfPlayers = players.length + 1; // +1 to include bgg user herself
    ArrayList<BoardGame> allGamesMatchingCriteria = new ArrayList<>();


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
    return allValid;
  }

  public BoardGameCounter[] convertToCounter(BoardGame[] allGames) {
    BoardGameCounter[] gamesWithCounter = new BoardGameCounter[allGames.length];
    for (int i = 0; i < gamesWithCounter.length; i++) {
      gamesWithCounter[i] = new BoardGameCounter(allGames[i]);
    }
    return gamesWithCounter;
  }

  public void calculatePlayerScore(Player[] allPlayers, BoardGameCounter current) {
    int lengthAllPlayers = allPlayers.length;

    // Favor games that the user haven't played much
    if (current.game.getNumberOfPlays() < 5) {
      double value = 25;
      current.value += value;
      current.reasons.add(new Reason("Owner of the game has played the game less than 5 times(25).", value));
    }
    // For all players
    for (Player player : allPlayers) {

      if (player.gameToPlaysMap.containsKey(current.game)) {
        // If players have played the game, recommend it more
        double playedValue = 12.0 / allPlayers.length;
        current.value += playedValue;
        current.reasons.add(new Reason("Player has played the game(" + playedValue + ").", playedValue));


        // For each day passed since last play for each player, favor the game a bit more
        Play[] allPlaysForPlayer = player.allPlays;

        for (int k = 0; k < allPlaysForPlayer.length; k++) {
          if (!allPlaysForPlayer[k].game.equals(current.game)) { // Only match with current game
            continue;
          }

          // Calculating for days since last play for each player
          double dateScore = calculateDateScore(allPlaysForPlayer[k], current.game, lengthAllPlayers);
          current.value += dateScore;
          current.reasons.add(new Reason("Date score: " + dateScore, dateScore));

          break; // Only add scores once
        }

        calculatePersonalRating(player, current, lengthAllPlayers);

      }
      // How well does the complexity, type, mechanisms and categories match
      calculateCombinationScore(current, player, lengthAllPlayers + 1);
    }
  }

  public void calculatePersonalRating(Player player, BoardGameCounter gameCounter, int lengthAllPlayers) {
    double rating = player.getPersonalRating(gameCounter.game);
    if(rating == 0) {
      rating = 5; // default to a rating of 5/10
    }
    if(rating < 5) {
      double value = - 100 / lengthAllPlayers;
      gameCounter.reasons.add(new Reason("Rating less than 5(" + value + ").", value));
      gameCounter.value -= 100 / lengthAllPlayers; // Decrease recommendation massively
    }
    // If 10/10 rating for all players, score a massive 50 points.
    double value = rating * 5 /lengthAllPlayers;
    gameCounter.reasons.add(new Reason("Rating is " + rating + "(" + value + ").", value));
    gameCounter.value += value;
  }

  public double calculateDateScore(Play play, BoardGame game, int lengthAllPlayers) {

    String date = play.date;
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

  public void calculateCombinationScore(BoardGameCounter current, Player player, int numberOfPlayers) {

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

    // Favor high complexity if player likes such games
    if((player.getMaxComplexity() > 2.5) && Math.abs(player.getMaxComplexity() - currentGame.getComplexity()) <= 1) {
      if(player.getAverageRatingOfGamesAboveComplexity(2.5) > 5) {
        double value = 20 / (numberOfPlayers - 1);
        current.value += value;
        current.reasons.add(new Reason("Player has played games of similar high complexity and liked them(" + value + ").", value));
      }
      else {
        double value = - 40 / (numberOfPlayers - 1); // Should try to not force players to play too complex games
        current.value += value;
        current.reasons.add(new Reason("Player has played games of similar high complexity and disliked them(" + value + ").", value));

      }
    }

    GameMechanism[] currentMechanisms = currentGame.getMechanisms();
    GameCategory[] currentCategories = currentGame.getCategories();

    for (int i = 0; i < allGames.length; i++) {
      BoardGame otherGame = allGames[i];

      GameMechanism[] otherMechanisms = otherGame.getMechanisms();
      GameCategory[] otherCategories = otherGame.getCategories();
      if (currentGame.equals(otherGame)) continue;

      if(player.getPersonalRating(otherGame) < 6) continue;

      // Up to 6 points based on type match
      if (currentGame.getType().equals(otherGame.getType())) {
        double value = 6.0 / numberOfPlayers / allGames.length;
        current.value += value;
        current.reasons.add(new Reason("Type matches other game " + otherGame + " for " + player.name + "(" + value + ").", value));
      }

      // Up to 7 points based on mechanisms match
      for (int j = 0; j < currentMechanisms.length; j++) {
        GameMechanism cMech = currentMechanisms[j];
        for (int k = 0; k < otherMechanisms.length; k++) {
          GameMechanism oMech = otherMechanisms[k];
          if (cMech.equals(oMech)) {
            double value = 7.0 / numberOfPlayers / allGames.length / otherMechanisms.length / currentMechanisms.length;
            current.value += value;
            current.reasons.add(new Reason("Mechanisms " + cMech + " matches other game " + otherGame + " for " + player.name + "(" + value + ").", value));

          }
        }
      }

      // Up to 7 points based on categories match
      for (int j = 0; j < currentCategories.length; j++) {
        GameCategory cCat = currentCategories[j];
        for (int k = 0; k < otherCategories.length; k++) {
          GameCategory oCat = otherCategories[k];
          if (cCat.equals(oCat)) {
            double value = 7.0 / numberOfPlayers / allGames.length / otherCategories.length / currentCategories.length;
            current.value += value;
            current.reasons.add(new Reason("Category " + cCat + " matches other game " + otherGame + " for " + player.name + "(" + value + ").", value));

          }
        }
      }
    }
  }

  public void calculateAbsoluteFactorsScore(BoardGameCounter current, int maxTime, Player[] allPlayers,
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
    double ownersRatingScore = personalRating * 2;
    current.value += ownersRatingScore;
    current.reasons.add(new Reason("Owners personal rating of " + personalRatingString + "(" + ownersRatingScore + ")", ownersRatingScore));

    // If personal rating is higher than average rating
    String averageRatingString = currentGame.getAverageRating();
    if (!averageRatingString.equals("N/A")) {
      double averageRating = Double.valueOf(averageRatingString);
      if (averageRating < personalRating) {
        double value = Math.min(5, (personalRating - averageRating) * 3);
        current.value += value;
        current.reasons.add(new Reason("Owners personal rating is higher than average rating(" + value + ")", value));

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

    if (approximationTime <= maxTime) {
      double value = 20;
      current.value += value;
      current.reasons.add(new Reason("ApproximationTime is lower than maxTime(" + value +").", value));
    }

    // How fitting is the complexity
    double currentComplexity = currentGame.getComplexity();
    double difference = Math.abs(currentComplexity - averageComplexityGivingAllPlayersEqualWeight);

    // First, lower rating if complexity is too different
    if (difference >= 0.8) {
      double value = -5;
      current.value += value;
      current.reasons.add(new Reason("Difference in complexity bigger than 0.8(" + value +").", value));

      if (difference >= 1.0) {
        value = -2;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity bigger than 1.0(" + value +").", value));
      }
      if (difference >= 1.2) {
        value = -2;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity bigger than 1.2(" + value +").", value));
      }
      if (difference >= 1.4) {
        value = -2;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity bigger than 1.4(" + value +").", value));
      }
      if (difference >= 1.5) {
        value = -4;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity bigger than 1.5(" + value +").", value));
      }
    }
    // If low difference, recommend more!
    else if (difference <= 0.8) {
      double value = 5;
      current.value += value;
      current.reasons.add(new Reason("Difference in complexity smaller than 0.8(" + value +").",value));

      if (difference <= 0.5) {
        value = 5;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity smaller than 0.8(" + value +").",value));
      }
      if (difference <= 0.4) {
        value = 3;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity smaller than 0.8(" + value +").",value));
      }
      if (difference <= 0.3) {
        value = 3;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity smaller than 0.8(" + value +").",value));
      }
      if (difference <= 0.2) {
        value = 4;
        current.value += value;
        current.reasons.add(new Reason("Difference in complexity smaller than 0.8(" + value +").", value));
      }
    }

    // Prefer longer games
    int quartersPassed = (int) approximationTime / 15; // Rounding down due to using integers

    // Only add additional rating if actually a longer game
    if(quartersPassed > 3) {
      double value = quartersPassed * 5; // An additional five points for every quarter of the game
      current.value += value;
      current.reasons.add(new Reason(quartersPassed + " quarters passed, we prefer long games(" + value +").", value));

    }

    // Finally, if the game is best with the player number, recommend it more
    int[] bestWith = currentGame.getBestWith();
    for (int i : bestWith) {
      if (i == allPlayers.length + 1) {
        double value = 20;
        current.value += value;
        current.reasons.add(new Reason("Game is best with " + i + ", matching player number(" + value +").", value));
        break;
      }
    }
  }

  public BoardGameCounter[] calculateSuggestedGames(BoardGameCounter[] gamesWithCounter, int maxTime) {
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
