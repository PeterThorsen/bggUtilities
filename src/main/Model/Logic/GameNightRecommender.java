package Model.Logic;

import Model.Structure.*;
import Util.Sorting.InsertionSort;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Peter on 13-Dec-16.
 */
public class GameNightRecommender implements IGameNightRecommender {

  private final IGameNightValues gameNightValues;

  public GameNightRecommender(IGameNightValues gameNightValues) {
    this.gameNightValues = gameNightValues;
  }

  @Override
  public BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight) {
    BoardGameCounter[] gamesWithCounter = convertToCounter(allValid);

    for (int i = 0; i < gamesWithCounter.length; i++) {
      BoardGameCounter current = gamesWithCounter[i];

      calculatePlayerScore(players, current);
      calculateAbsoluteFactorsScore(current, maxTime, players, magicComplexity, averageComplexityGivingAllPlayersEqualWeight);
    }
    return calculateSuggestedGames(gamesWithCounter, maxTime);
  }

  @Override
  public BoardGameCounter[] getRecommendationCounterForSingleGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight) {
    return buildBestGameNight(actualSuggestionAsGames, players, maxTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight);
  }


  private BoardGameCounter[] convertToCounter(BoardGame[] allGames) {
    BoardGameCounter[] gamesWithCounter = new BoardGameCounter[allGames.length];
    for (int i = 0; i < gamesWithCounter.length; i++) {
      gamesWithCounter[i] = new BoardGameCounter(allGames[i]);
    }
    return gamesWithCounter;
  }

  private void calculatePlayerScore(Player[] allPlayers, BoardGameCounter current) {
    int lengthAllPlayers = allPlayers.length;

    // Favor games that the user haven't played much
    if (current.game.numPlays < 5) {
      double value = gameNightValues.ownerHasPlayedGameLessThanFiveTimes();
      current.value += value;
      current.reasons.add(new Reason("Owner of the game has played the game less than 5 times.", value));
    }
    // For all players
    for (Player player : allPlayers) {

      if (player.gameToPlaysMap.containsKey(current.game)) {
        // If players have played the game, recommend it more
        double playedValue = gameNightValues.allPlayersHaveTriedGame() / lengthAllPlayers;
        current.value += playedValue;
        current.reasons.add(new Reason(player.name + " has played the game.", playedValue));


        // For each day passed since last play for each player, favor the game a bit more
        Play[] allPlaysForPlayer = player.allPlays;

        for (int k = 0; k < allPlaysForPlayer.length; k++) {
          if (!allPlaysForPlayer[k].game.equals(current.game)) { // Only match with current game
            continue;
          }

          // Calculating for days since last play for each player
          double dateScore = calculateDateScore(allPlaysForPlayer[k], current.game, lengthAllPlayers);
          current.value += dateScore;
          current.reasons.add(new Reason("Date score", dateScore));

          break; // Only add scores once
        }

        calculatePersonalRating(player, current, lengthAllPlayers);

      }
      // How well does the complexity, type, mechanisms and categories match
      calculateCombinationScore(current, player, lengthAllPlayers);
    }
  }

  private void calculatePersonalRating(Player player, BoardGameCounter gameCounter, int lengthAllPlayers) {
    double rating = player.getPersonalRating(gameCounter.game);
    if (rating == 0) {
      rating = 5; // default to a rating of 5/10
    }
    double value = gameNightValues.playerRating(rating) / lengthAllPlayers;
    gameCounter.reasons.add(new Reason("Player rating is " + rating + ".", value));
    gameCounter.value += value;
  }

  private double calculateDateScore(Play play, BoardGame game, int lengthAllPlayers) {

    String date = play.date;
    Date dateFormatted;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    try {
      dateFormatted = df.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
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

    double absoluteMin = gameNightValues.allPlayersHaveNotPlayedGameSinceTimeLimit() / lengthAllPlayers;
    double increaseBy = absoluteMin / gameNightValues.getLimitOnDaysPassed();
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
      if (!allGames[i].isExpansion)
        noExpansions.add(allGames[i]);
    }
    allGames = new BoardGame[noExpansions.size()];
    for (int i = 0; i < allGames.length; i++) {
      allGames[i] = noExpansions.get(i);
    }

    BoardGame currentGame = current.game;

    GameMechanism[] currentMechanisms = currentGame.mechanisms;
    GameCategory[] currentCategories = currentGame.categories;

    for (int i = 0; i < allGames.length; i++) {
      BoardGame otherGame = allGames[i];

      GameMechanism[] otherMechanisms = otherGame.mechanisms;
      GameCategory[] otherCategories = otherGame.categories;
      if (currentGame.equals(otherGame)) continue;

      if (player.getPersonalRating(otherGame) < 6) continue;

      // points based on type match
      if (currentGame.type.equals(otherGame.type)) {
        double value = gameNightValues.allPlayersHaveOnlyPlayedGamesOfCurrentType() / numberOfPlayers / allGames.length;
        current.value += value;
        current.reasons.add(new Reason("Type " + currentGame.type + " matches other game " + otherGame + " for " + player.name + ".", value));
      }

      // points based on mechanisms match
      for (int j = 0; j < currentMechanisms.length; j++) {
        GameMechanism cMech = currentMechanisms[j];
        for (int k = 0; k < otherMechanisms.length; k++) {
          GameMechanism oMech = otherMechanisms[k];
          if (cMech.equals(oMech)) {
            double value = gameNightValues.allPlayersHaveOnlyPlayedGamesOfCurrentMechanism() / numberOfPlayers / allGames.length / otherMechanisms.length / currentMechanisms.length;
            current.value += value;
            current.reasons.add(new Reason("Mechanism " + cMech + " matches other game " + otherGame + " for " + player.name + ".", value));

          }
        }
      }

      // Up to 7 points based on categories match
      for (int j = 0; j < currentCategories.length; j++) {
        GameCategory cCat = currentCategories[j];
        for (int k = 0; k < otherCategories.length; k++) {
          GameCategory oCat = otherCategories[k];
          if (cCat.equals(oCat)) {
            double value = gameNightValues.allPlayersHaveOnlyPlayedGamesOfCurrentCategory() / numberOfPlayers / allGames.length / otherCategories.length / currentCategories.length;
            current.value += value;
            current.reasons.add(new Reason("Category " + cCat + " matches other game " + otherGame + " for " + player.name + ".", value));

          }
        }
      }
    }
  }

  private void calculateAbsoluteFactorsScore(BoardGameCounter current, int maxTime, Player[] allPlayers,
                                             double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight) {
    BoardGame currentGame = current.game;

    // Personal rating should matter
    String personalRatingString = currentGame.personalRating;
    double personalRating;
    if (personalRatingString.equals("N/A")) {
      personalRating = 5;
    } else {
      personalRating = Double.valueOf(personalRatingString);
    }
    double ownersRatingScore = personalRating * gameNightValues.weightOfOwnersPersonalRating();
    current.value += ownersRatingScore;
    current.reasons.add(new Reason("Owners personal rating of " + personalRatingString + "(" + ownersRatingScore + ")", ownersRatingScore));

    String averageRatingString = currentGame.averageRating;
    if (!averageRatingString.equals("N/A")) {
      double averageRating = Double.valueOf(averageRatingString);
      double value;
      if (averageRating < personalRating) {
        value = gameNightValues.ownersPersonalRatingIsHigherThanAverage(personalRating, averageRating);

      } else {
        value = gameNightValues.averageRatingIsHigherThanOwnersPersonalRating(personalRating, averageRating);
      }
      current.value += value;
      current.reasons.add(new Reason("Owners personal rating is higher than average rating.", value));

    }

    // Can we easily play this game within time limit
    int currentMinTime = currentGame.minPlaytime;
    int currentMaxTime = currentGame.maxPlaytime;

    double approximationTime;
    if (currentMinTime == currentMaxTime) {
      approximationTime = currentMaxTime;
    } else if (currentGame.minPlayers == currentGame.maxPlayers) {
      approximationTime = currentMaxTime;

    } else {
      if (currentGame.name.equals("Sequence") || currentGame.name.equals("Fluxx")) currentMinTime += 15;

      double difference = currentMaxTime - currentMinTime;
      difference = difference / (currentGame.maxPlayers - currentGame.minPlayers);
      approximationTime = currentMinTime + (difference * (allPlayers.length + 1 - currentGame.minPlayers));
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
      double value = gameNightValues.canEasilyPlayGameWithinTimeLimit();
      current.value += value;
      current.reasons.add(new Reason("ApproximationTime is lower than maxTime.", value));
    }

    // How fitting is the complexity
    double currentComplexity = currentGame.complexity;
    double value = gameNightValues.complexityDifference(currentComplexity, averageComplexityGivingAllPlayersEqualWeight, magicComplexity);
    current.value += value;
    current.reasons.add(new Reason("Difference in complexity.", value));

    value = gameNightValues.timeSpentOnGame(approximationTime);
    current.value += value;
    current.reasons.add(new Reason("Score based on time spent.", value));


    // Finally, if the game is best with the player number, recommend it more
    int[] bestWith = currentGame.bestWith;
    for (int i : bestWith) {
      if (i == allPlayers.length + 1) {

        value = gameNightValues.gameBestWithCurrentNumberOfPlayers();
        current.value += value;
        current.reasons.add(new Reason("Game is best with your number of players.", value));
        break;
      }
    }
  }

  private BoardGameCounter[] calculateSuggestedGames(BoardGameCounter[] gamesWithCounter, int maxTime) {
    ArrayList<BoardGameCounter> suggestedCombinationList = new ArrayList<>();
    gamesWithCounter = InsertionSort.sortGamesWithCounter(gamesWithCounter);
    int posOfFirstElement = 0;
    double currentTimeSpent = 0;

    for (int i = 0; i < gamesWithCounter.length; i++) {

      // Find first element
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
    }
    return suggestedCombination;
  }

}
