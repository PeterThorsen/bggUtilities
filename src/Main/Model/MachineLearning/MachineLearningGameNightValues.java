package Main.Model.MachineLearning;

import Main.Model.Logic.IGameNightValues;


class MachineLearningGameNightValues implements IGameNightValues {

  double[] values = new double[23];

  public MachineLearningGameNightValues() {
    values[0] = 25;
    values[1] = 12;
    values[2] = 1;
    values[21] = 1;
    values[3] = 20;
    //values[4] = 600; days, set as standard
    values[5] = 50;
    values[6] = 1000;
    values[7] = 200;
    values[8] = 2;
    values[9] = 3;
    values[22] = 3;
    values[10] = 2;
    values[4] = 2;
    values[11] = 5;
    values[12] = 20;
    values[13] = -50;
    values[14] = 0.01;
    values[15] = 4;
    values[16] = 8;
    values[17] = 12;
    values[18] = 16;
    values[19] = 0.33;
    values[20] = 25;
  }


  @Override
  public double ownerHasPlayedGameLessThanFiveTimes() {
    return values[0];
  }

  @Override
  public double allPlayersHaveTriedGame() {
    return values[1];
  }

  @Override
  public double playerRating(double rating) {
    if(rating < 5) {
      return (-100 + rating * 20) * values[2];
    }
    return (-50 + 10 * rating) * values[21];
  }

  @Override
  public double allPlayersHaveNotPlayedGameSinceTimeLimit() {
    return values[3];
  }

  @Override
  public double getLimitOnDaysPassed() {
    return 600;
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentType() {
    return values[5];
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentMechanism() {
    return values[6];
  }

  @Override
  public double allPlayersHaveOnlyPlayedGamesOfCurrentCategory() {
    return values[7];
  }

  @Override
  public double weightOfOwnersPersonalRating() {
    return values[8];
  }

  @Override
  public double ownersPersonalRatingIsHigherThanAverage(double personalRating, double averageRating) {
    return values[22] + (personalRating - averageRating) * values[9];
  }

  @Override
  public double averageRatingIsHigherThanOwnersPersonalRating(double personalRating, double averageRating) {
    return values[4] + (averageRating - personalRating) * values[10];
  }

  @Override
  public double canEasilyPlayGameWithinTimeLimit() {
    return values[11];
  }

  @Override
  public double complexityDifference(double currentGameComplexity, double averageComplexityGivingAllPlayersEqualWeight, double magicComplexity) {
    double differenceMagic = Math.abs(currentGameComplexity - magicComplexity);

    if (differenceMagic == currentGameComplexity) return values[18];

    if (differenceMagic > currentGameComplexity) {
      return values[12];
    }

    if (currentGameComplexity > magicComplexity) {
      if (differenceMagic > 1.5) {
        return values[13];
      }
      if (differenceMagic > 1.2) {
        return values[14];
      }
      if (differenceMagic > 1.0) {
        return values[15];
      }
      if (differenceMagic > 0.8) {
        return values[16];
      }
      if (differenceMagic > 0.6) {
        return values[17];
      }
      return values[18];
    }
    return values[18];
  }

  @Override
  public double timeSpentOnGame(double approximationTime) {
    return approximationTime * values[19];
  }

  @Override
  public double gameBestWithCurrentNumberOfPlayers() {
    return values[20];
  }
}
