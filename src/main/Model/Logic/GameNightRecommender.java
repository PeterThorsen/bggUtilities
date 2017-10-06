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
  public BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight, boolean suggestAllGames, Player[] allPlayersEver) {
    BoardGameCounter[] gamesWithCounter = convertToCounter(allValid);

    for (int i = 0; i < gamesWithCounter.length; i++) {
      BoardGameCounter current = gamesWithCounter[i];

      calculatePlayerScore(players, current, allPlayersEver);
      calculateAbsoluteFactorsScore(current, maxTime, players, magicComplexity, averageComplexityGivingAllPlayersEqualWeight, suggestAllGames, allPlayersEver);
    }
    return calculateSuggestedGames(gamesWithCounter, maxTime, suggestAllGames);
  }

  @Override
  public BoardGameCounter[] getRecommendationCounterForSingleGame(BoardGame[] actualSuggestionAsGames, Player[] players, int maxTime, double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight, Player[] allPlayersEver) {
    return buildBestGameNight(actualSuggestionAsGames, players, maxTime, magicComplexity, averageComplexityGivingAllPlayersEqualWeight, false, allPlayersEver);
  }


  private BoardGameCounter[] convertToCounter(BoardGame[] allGames) {
    BoardGameCounter[] gamesWithCounter = new BoardGameCounter[allGames.length];
    for (int i = 0; i < gamesWithCounter.length; i++) {
      gamesWithCounter[i] = new BoardGameCounter(allGames[i]);
    }
    return gamesWithCounter;
  }

  private void calculatePlayerScore(Player[] allPlayers, BoardGameCounter current, Player[] allPlayersEver) {
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

        for (int k = allPlaysForPlayer.length - 1; k >= 0; k--) {
          if (!allPlaysForPlayer[k].game.equals(current.game)) { // Only match with current game
            continue;
          }

          // Calculating for days since last play for each player
          long daysSinceLastPlay = calculateLastPlayDate(allPlaysForPlayer[k]);
          double dateScore = calculateDateScore(lengthAllPlayers, daysSinceLastPlay);
          current.value += dateScore;
          current.reasons.add(new Reason(player.name + " last played this game " + daysSinceLastPlay + " days ago.", dateScore));

          break; // Only add scores once
        }
      }
      double rating = player.getPersonalRating(current.game);
      if (rating == 0) {
        calculateWhetherThePlayerMightLikeThisGame(player, current, allPlayersEver, allPlayers.length);
      } else {
        calculatePersonalRating(player, current, allPlayers);
      }

      // How well does the complexity, type, mechanisms and categories match
      //calculateCombinationScore(current, player, lengthAllPlayers); todo
    }
  }

  private void calculatePersonalRating(Player player, BoardGameCounter gameCounter, Player[] allPlayers) {
    double rating = player.getPersonalRating(gameCounter.game);
    double value = gameNightValues.playerRating(rating) / allPlayers.length;
    gameCounter.reasons.add(new Reason(player.name + " rated the game " + rating + ".", value));
    gameCounter.value += value;
  }

  private void calculateWhetherThePlayerMightLikeThisGame(Player player, BoardGameCounter gameCounter, Player[] allPlayersEver, int currentPlayersLength) {
    if (player == null) {
      // todo calculate for me
    } else {
      double ratingOfSimilarlyComplexGames = player.getAverageRatingOfGamesBetweenComplexities(gameCounter.game.complexity - 0.5, gameCounter.game.complexity + 0.5);

      boolean hasPlayedSimilarGames = ratingOfSimilarlyComplexGames != 0;


      // How well liked is the game for comparable players?
      double currentPlayerComplexity = player.getMagicComplexity();
      double counter = 0;
      double othersRatings = 0;
      for (Player otherPlayer : allPlayersEver) {
        if (otherPlayer.name.equals(player.name)) {
          continue;
        }
        double otherPersonalRating = otherPlayer.getPersonalRating(gameCounter.game);
        if (otherPersonalRating != 0) {
          double otherComplexity = otherPlayer.getMagicComplexity();
          if (currentPlayerComplexity < otherComplexity + 0.8 && currentPlayerComplexity > otherComplexity - 0.8) {
            counter++;
            othersRatings += otherPersonalRating;
          }
        }
      }

      boolean comparablePlayersHavePlayedThisGame = counter != 0 && othersRatings != 0;

      if (hasPlayedSimilarGames) {
        double value = gameNightValues.ratingOfSimilarlyComplexGames(ratingOfSimilarlyComplexGames);
        value = value / currentPlayersLength;
        if (!comparablePlayersHavePlayedThisGame) value *= 2;
        gameCounter.value += value;
        gameCounter.reasons.add(new Reason(player.name + " rates similarly complex games " + ratingOfSimilarlyComplexGames + "/10 on average.", value));
      }

      if (comparablePlayersHavePlayedThisGame) {
        double value = gameNightValues.comparablePlayersLikesThisGame(othersRatings / counter);
        if (!hasPlayedSimilarGames) value *= 2;
        value = value / currentPlayersLength;
        gameCounter.value += value;
        gameCounter.reasons.add(new Reason("While " + player.name + " hasn't played this game, comparable players rated it " + (othersRatings / counter) + "/10 on average.", value));
      }


    }


  }

  private double calculateDateScore(int lengthAllPlayers, long diffInDays) {

    double absoluteMin = gameNightValues.allPlayersHaveNotPlayedGameSinceTimeLimit() / lengthAllPlayers;
    double increaseBy = absoluteMin / gameNightValues.getLimitOnDaysPassed();
    return Math.min(absoluteMin, diffInDays * increaseBy);
  }

  private long calculateLastPlayDate(Play play) {

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
    return diffInDays;
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
                                             double magicComplexity, double averageComplexityGivingAllPlayersEqualWeight, boolean suggestAllGames, Player[] allPlayersEver) {
    BoardGame currentGame = current.game;

    // Personal rating should matter
    String personalRatingString = currentGame.personalRating;
    double personalRating;
    if (personalRatingString.equals("N/A")) {
      calculateWhetherThePlayerMightLikeThisGame(null, current, allPlayersEver, allPlayers.length);
    } else {
      personalRating = Double.valueOf(personalRatingString);
      double ownersRatingScore = gameNightValues.ownersPersonalRating(personalRating);
      current.value += ownersRatingScore;
      current.reasons.add(new Reason("You rated " + current.game.name + " " + personalRatingString + "/10.", ownersRatingScore));

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

    if (approximationTime <= maxTime && approximationTime >= maxTime - 30) {
      double value = gameNightValues.thisGameWouldWorkAsSoleGameForGameNight();
      current.value += value;
      current.reasons.add(new Reason(current.game.name + " would work well as sole game for your game night requiring approximately " + approximationTime + " minutes.", value));
    }
    else if (approximationTime <= maxTime) {
      double value = gameNightValues.canEasilyPlayGameWithinTimeLimit();
      current.value += value;
      current.reasons.add(new Reason("The game should take approximately " + approximationTime + " minutes.", value));
    }

    // How fitting is the complexity
    double currentComplexity = currentGame.complexity;
    double value = gameNightValues.complexityDifference(currentComplexity, averageComplexityGivingAllPlayersEqualWeight, magicComplexity);
    current.value += value;
    current.reasons.add(new Reason("These players would prefer games around " + magicComplexity + "/5 in complexity. This game has a rating of " + currentComplexity + "/5.", value));

    if (!suggestAllGames) {
      value = gameNightValues.timeSpentOnGame(approximationTime);
      current.value += value;
      current.reasons.add(new Reason("Score based on time spent.", value));
    }


    // Finally, if the game is best with the player number, recommend it more
    int[] bestWith = currentGame.bestWith;
    int isGood = 0;
    for (int i : bestWith) {
      if (i == allPlayers.length + 1) {
        isGood = 1;
        if (i != 2) {

          value = gameNightValues.gameBestWithCurrentNumberOfPlayers();
          current.value += value;
          current.reasons.add(new Reason("Game is best with " + i + " players.", value));
          break;
        }
      }
    }

    if (isGood == 0) {

      for (int i : currentGame.recommendedWith) {
        if (i == allPlayers.length + 1) {
          isGood = 2;
          if (i != 2) {
            value = gameNightValues.gameRecommendedWithCurrentNumberOfPlayers();
            current.value += value;
            current.reasons.add(new Reason("Game is recommended with " + i + " players.", value));
            break;
          }
        }
      }
    }

    if (isGood == 0) {
      value = gameNightValues.gameBadWithCurrentNumberOfPlayers();
      current.value += value;
      current.reasons.add(new Reason("Game is bad with " + (allPlayers.length + 1) + " players.", value));
    }


  }

  private BoardGameCounter[] calculateSuggestedGames(BoardGameCounter[] gamesWithCounter, int maxTime, boolean suggestAllGames) {
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
      if (suggestAllGames || maxTime >= withinTime) {
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
