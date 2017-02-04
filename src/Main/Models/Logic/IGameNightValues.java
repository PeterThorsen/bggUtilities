package Main.Models.Logic;

/**
 * Created by Peter on 31/01/2017.
 */
public interface IGameNightValues {
  double ownerHasPlayedGameLessThanFiveTimes();

  int allPlayersHaveTriedGame();

  double playerRating(double rating);

  int allPlayersHaveNotPlayedGameSinceTimeLimit();

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

  double weightOfOwnersPersonalRating();

  double ownersPersonalRatingIsHigherThanAverage(double personalRating, double averageRating);

  double averageRatingIsHigherThanOwnersPersonalRating(double personalRating, double averageRating);

  double canEasilyPlayGameWithinTimeLimit();

  double complexityDifference(double complexity, double difference, double currentComplexity);

  double timeSpentOnGame(double approximationTime);

  double gameBestWithCurrentNumberOfPlayers();
}
