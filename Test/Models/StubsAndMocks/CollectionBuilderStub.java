package Models.StubsAndMocks;

import Main.Model.Storage.ICollectionBuilder;
import Main.Model.Structure.*;

import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 */
public class CollectionBuilderStub implements ICollectionBuilder {

  private final Plays plays;
  private final Player[] players = new Player[1];
  private BoardGameCollection collection;

  public CollectionBuilderStub() {
    // Build games
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    GameCategory[] cats = new GameCategory[3];
    cats[0] = new GameCategory("Animals");
    cats[1] = new GameCategory("Economic");
    cats[2] = new GameCategory("Farming");

    GameMechanism[] mechs = new GameMechanism[4];
    mechs[0] = new GameMechanism("Area Enclosure");
    mechs[1] = new GameMechanism("Card Drafting");
    mechs[2] = new GameMechanism("Hand Management");
    mechs[3] = new GameMechanism("Worker Placement");

    int[] bestWith = new int[2];
    bestWith[0] = 3;
    bestWith[1] = 4;

    int[] recommendedWith = new int[2];
    recommendedWith[0] = 1;
    recommendedWith[1] = 2;

    game1.addExpandedGameInfo(2.3453, false, cats, mechs, bestWith, recommendedWith);

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394", "abstracts");
    GameCategory[] cats2 = new GameCategory[2];
    cats2[0] = new GameCategory("Abstract Strategy");
    cats2[1] = new GameCategory("Animals");

    GameMechanism[] mechs2 = new GameMechanism[2];
    mechs2[0] = new GameMechanism("Grid Movement");
    mechs2[1] = new GameMechanism("Tile Placement");

    int[] bestWith2 = new int[1];
    bestWith[0] = 2;

    game2.addExpandedGameInfo(3.6298, false, cats2, mechs2, bestWith2, new int[0]);
    
    plays = new Plays();
    String[] names = new String[1];
    names[0] = "Martin";
    Play play = new Play(game1, "2016-09-14", names, 1, null);
    plays.addPlay(play);

    play = new Play(game2, "2016-09-13", names, 2, null);
    plays.addPlay(play);

    Play[] playForPlayer = new Play[1];
    playForPlayer[0] = play;
    Player player = new Player("Peter", playForPlayer);
    players[0] = player;

    ArrayList<BoardGame> games = new ArrayList<>();
    games.add(game1);
    games.add(game2);

    // Add games to collection
    collection = new BoardGameCollection(asArray(games));
  }

  private BoardGame[] asArray(ArrayList<BoardGame> games) {
    BoardGame[] arr = new BoardGame[games.size()];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = games.get(i);
    }
    return arr;
  }

  @Override
  public BoardGameCollection getCollection(String username) {
    return collection;
  }

  @Override
  public boolean verifyUser(String username) {
    return false;
  }

  public Plays getPlays() {
    return plays;
  }

  @Override
  public Player[] getPlayers() {
    return players;
  }
}
