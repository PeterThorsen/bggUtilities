package Main.Models.Logic;

import Main.Models.Structure.BoardGame;
import Main.Models.Structure.BoardGameCounter;
import Main.Models.Structure.Player;

/**
 * Created by Peter on 31/01/2017.
 */
public interface IGameNightRecommender {
  BoardGameCounter[] buildBestGameNight(BoardGame[] allValid, Player[] players, int maxTime, double averageComplexityGivingAllPlayersEqualWeight);
}
