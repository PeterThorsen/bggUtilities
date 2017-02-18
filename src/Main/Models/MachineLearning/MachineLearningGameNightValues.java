package Main.Models.MachineLearning;

import Main.Models.Logic.IGameNightValues;


class MachineLearningGameNightValues implements IGameNightValues {

  public static double ownerHasPlayedGameLessThanFiveTimes = 0;
  public int allPlayersHaveTriedGame = 0;
  public double playerRating = 0;
  public int allPlayersHaveNotPlayedGameSinceTimeLimit = 1;
  public double getLimitOnDaysPassed = 0;
  public double allPlayersHaveOnlyPlayedGamesOfCurrentType = 0;
  public double allPlayersHaveOnlyPlayedGamesOfCurrentMechanism = 0;
  public double allPlayersHaveOnlyPlayedGamesOfCurrentCategory = 0;
  public double weightOfOwnersPersonalRating = 0;
  public double ownersPersonalRatingIsHigherThanAverage = 0;
  public double averageRatingIsHigherThanOwnersPersonalRating = 0;
  public double canEasilyPlayGameWithinTimeLimit = 0;
  public double complexityDifference = 0;
  public double timeSpentOnGame = 0;
  public double gameBestWithCurrentNumberOfPlayers = 0;


  @Override
  public double ownerHasPlayedGameLessThanFiveTimes() {
    return ownerHasPlayedGameLessThanFiveTimes;
  }

  @Override
  public int allPlayersHaveTriedGame() {
    return allPlayersHaveTriedGame;
  }

  @Override
  public double playerRating(double rating) {
    return playerRating;
  }

  @Override
  public int allPlayersHaveNotPlayedGameSinceTimeLimit() {
    return allPlayersHaveNotPlayedGameSinceTimeLimit;
  }

  @Override
  public double getLimitOnDaysPassed() {
    return getLimitOnDaysPassed;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentType() {
    return allPlayersHaveOnlyPlayedGamesOfCurrentType;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentMechanism() {
    return allPlayersHaveOnlyPlayedGamesOfCurrentMechanism;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentCategory() {
    return allPlayersHaveOnlyPlayedGamesOfCurrentCategory;
  }

  @Override
  public double weightOfOwnersPersonalRating() {
    return weightOfOwnersPersonalRating;
  }

  @Override
  public double ownersPersonalRatingIsHigherThanAverage(double personalRating, double averageRating) {
    return ownersPersonalRatingIsHigherThanAverage;
  }

  @Override
  public double averageRatingIsHigherThanOwnersPersonalRating(double personalRating, double averageRating) {
    return averageRatingIsHigherThanOwnersPersonalRating;
  }

  @Override
  public double canEasilyPlayGameWithinTimeLimit() {
    return canEasilyPlayGameWithinTimeLimit;
  }

  @Override
  public double complexityDifference(double complexity, double difference, double currentComplexity) {
    return complexityDifference;
  }

  @Override
  public double timeSpentOnGame(double approximationTime) {
    return timeSpentOnGame;
  }

  @Override
  public double gameBestWithCurrentNumberOfPlayers() {
    return gameBestWithCurrentNumberOfPlayers;
  }
}
