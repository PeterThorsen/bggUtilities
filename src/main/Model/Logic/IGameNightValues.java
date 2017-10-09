package Model.Logic;

/**
 * Created by Peter on 31/01/2017.
 */
public interface IGameNightValues {
  double ownerHasPlayedGameLessThanFiveTimes();

  double allPlayersHaveTriedGame();

  double playerRating(double rating);

  double allPlayersHaveNotPlayedGameSinceTimeLimit();

  /**
   *
   * @return The maximum limit of days passed before no more score is added.
   */
  double getLimitOnDaysPassed();

  double allPlayersHaveOnlyPlayedGamesOfCurrentType();

  /**
   * Assuming only one mechanism in all games.
   * @return value.
   */
  double allPlayersHaveOnlyPlayedGamesOfCurrentMechanism();

  double allPlayersHaveOnlyPlayedGamesOfCurrentCategory();

  double ownersPersonalRating(double personalRating);

  double ownersPersonalRatingIsHigherThanAverage(double personalRating, double averageRating);

  double averageRatingIsHigherThanOwnersPersonalRating(double personalRating, double averageRating);

  double canEasilyPlayGameWithinTimeLimit();

  double complexityDifference(double complexity, double difference, double currentComplexity);

  double timeSpentOnGame(double approximationTime);

  double gameBestWithCurrentNumberOfPlayers();
  double gameRecommendedWithCurrentNumberOfPlayers();

  double gameBadWithCurrentNumberOfPlayers();

  double ratingOfSimilarlyComplexGames(double ratingOfSimilarlyComplexGames);

  double comparablePlayersLikesThisGame(double othersAverageRating);

  double thisGameWouldWorkAsSoleGameForGameNight();

  double cantPlayGameWithinTimeLimit();
}
