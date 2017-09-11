package main.Model.Logic;

/**
 * Created by Peter on 31/01/2017.
 */
public class ChosenGameNightValues implements IGameNightValues {
  @Override
  public double ownerHasPlayedGameLessThanFiveTimes() {
    return 25;
  }

  @Override
  public double allPlayersHaveTriedGame() {
    return 12;
  }

  @Override
  public double playerRating(double rating) {
    if(rating < 5) {
      return -100 + (rating * 20);
    }
    return -50 + 10 * rating;
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
  public double weightOfOwnersPersonalRating() {
    return 2.0;
  }

  @Override
  public double ownersPersonalRatingIsHigherThanAverage(double personalRating, double averageRating) {
    return 3 + (personalRating - averageRating) * 3;
  }

  @Override
  public double averageRatingIsHigherThanOwnersPersonalRating(double personalRating, double averageRating) {
    return 2 + (averageRating - personalRating) * 2;
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
    value += calculateDifference(differenceAverage);
    value += calculateDifference(differenceMagic) * 1.5;
    value /= 2;
    return value;
  }

  private double calculateDifference(double difference) {
    double value = 0;

    // First, lower rating if complexity is too different
    if (difference >= 0.8) {
      value += -5;

      if (difference >= 1.0) {
        value += -2;
      }
      if (difference >= 1.2) {
        value += -2;
      }
      if (difference >= 1.4) {
        value += -2;
      }
      if (difference >= 1.5) {
        value += -4;
      }
    }
    // If low difference, recommend more!
    else if (difference <= 0.8) {
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
    return value;
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
}
