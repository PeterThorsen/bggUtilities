package Model.Logic;

/**
 * Created by Peter on 31/01/2017.
 */
public class ChosenGameNightValues implements IGameNightValues {
  @Override
  public double ownerHasPlayedGameLessThanFiveTimes() {
    return 15;
  }

  @Override
  public double allPlayersHaveTriedGame() {
    return 12;
  }

  @Override
  public double playerRating(double rating) {
    if (rating < 6) {
      return -350 + (rating * 50);
    }
    if(rating < 6.5) {
      return 0;
    }
    double tempValue = rating - 6;
    return tempValue * 8;
  }

  @Override
  public double allPlayersHaveNotPlayedGameSinceTimeLimit() {
    return 20;
  }

  @Override
  public double getLimitOnDaysPassed() {
    return 600;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentType() {
    return 50.0;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentMechanism() {
    return 1000;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentCategory() {
    return 200.0;
  }

  @Override
  public double ownersPersonalRating(double personalRating) {
    if (personalRating < 6) {
      return -350 + (personalRating * 50);
    }
    if (personalRating < 6.5) {
      return 0;
    }
    double tempValue = personalRating - 6;
    return tempValue * 8;
  }

  @Override
  public double ownersPersonalRatingIsHigherThanAverage(double personalRating, double averageRating) {
    return 3 + (personalRating - averageRating) * 3;
  }

  @Override
  public double averageRatingIsHigherThanOwnersPersonalRating(double personalRating, double averageRating) {
    return 0;
  }

  @Override
  public double canEasilyPlayGameWithinTimeLimit() {
    return 5;
  }

  @Override
  public double complexityDifference(double currentGameComplexity, double averageComplexityGivingAllPlayersEqualWeight, double magicComplexity) {

    double differenceMagic = Math.abs(currentGameComplexity - magicComplexity);
    double differenceAverage = Math.abs(currentGameComplexity - averageComplexityGivingAllPlayersEqualWeight);

    double value = 0;
    value += calculateDifference(differenceMagic, currentGameComplexity > magicComplexity);
    value /= 2;
    return value;
  }

  private double calculateDifference(double difference, boolean higherComplexity) {
    double value = 0;

    // First, lower rating if complexity is too different
    if (higherComplexity) {
      if (difference >= 0.8) {
        value += -5;

        if (difference >= 1.0) {
          value += -4;
        }
        if (difference >= 1.2) {
          value += -4;
        }
        if (difference >= 1.4) {
          value += -4;
        }
        if (difference >= 1.5) {
          value += -8;
        }
      }
    } else {
      if (difference >= 1.0) {
        value += -3;
      }
      if (difference >= 1.2) {
        value += -3;
      }
      if (difference >= 1.4) {
        value += -3;
      }
      if (difference >= 1.5) {
        value += -6;
      }
    }
    // If low difference, recommend more!
    if (difference <= 0.8) {
      value += 5;

      if (difference <= 0.5) {
        value += 5;
      }
      if (difference <= 0.4) {
        value += 3;
      }
      if (difference <= 0.3) {
        value += 3;
      }
      if (difference <= 0.2) {
        value += 4;
      }
    }
    return value * 2;
  }

  @Override
  public double timeSpentOnGame(double approximationTime) {
    // Prefer longer games
    int quartersPassed = (int) approximationTime / 15; // Rounding down due to using integers

    // Only add additional rating if actually a longer game
    if (quartersPassed >= 2) {
      return quartersPassed * 5; // An additional five points for every quarter of the game
    }
    return 0;
  }

  @Override
  public double gameBestWithCurrentNumberOfPlayers() {
    return 25;
  }

  @Override
  public double gameRecommendedWithCurrentNumberOfPlayers() {
    return 15;
  }

  @Override
  public double gameBadWithCurrentNumberOfPlayers() {
    return -200;
  }

  @Override
  public double ratingOfSimilarlyComplexGames(double rating) {
    if (rating < 6) {
      return -350 + 50 * rating;
    }
    double tempValue = rating - 6;
    return tempValue * 8;
  }

  @Override
  public double comparablePlayersLikesThisGame(double rating) {
    if (rating < 6) {
      return -350 + 50 * rating;
    }
    double tempValue = rating - 6;
    return tempValue * 8;
  }

  @Override
  public double scoreBasedOnDifferenceToMaxTime(int maxTime, double approximationTime) {
    double difference = maxTime - approximationTime;
    if(difference < 5) return 30;
    if(difference < 10) return 25;
    if(difference < 15) return 20;
    if(difference < 20) return 15;
    if(difference < 25) return 10;
    return 5;
  }

  @Override
  public double cantPlayGameWithinTimeLimit() {
    return -100;
  }
}
