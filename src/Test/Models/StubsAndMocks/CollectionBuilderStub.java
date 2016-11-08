package Test.Models.StubsAndMocks;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Play;
import Main.Containers.Plays;
import Main.Models.Storage.ICollectionBuilder;

import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 */
public class CollectionBuilderStub implements ICollectionBuilder {

  private final Plays plays;
  private BoardGameCollection collection;

  public CollectionBuilderStub() {
    // Build games
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978");
    game1.addComplexity(2.3453);

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394");
    game2.addComplexity(3.6298);
    
    plays = new Plays();
    String[] names = new String[1];
    names[0] = "Martin";
    Play play = new Play(game1, "2016-09-14", names, 1);
    plays.addPlay(play);

    play = new Play(game2, "2016-09-14", names, 1);
    plays.addPlay(play);

    ArrayList<BoardGame> games = new ArrayList<>();
    games.add(game1);
    games.add(game2);

    // Add games to collection
    collection = new BoardGameCollection(games);
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
}
