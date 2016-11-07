package Test.Models.StubsAndMocks;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.Play;
import Main.Models.Storage.ICollectionBuilder;

import java.util.ArrayList;

/**
 * Created by Peter on 03/10/16.
 */
public class CollectionBuilderStub implements ICollectionBuilder {
  @Override
  public BoardGameCollection getCollection(String username) {

    // Build games
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978");
    game1.addComplexity(2.3453);

    BoardGame game2 = new BoardGame("Hive",2655,2,2,20,20,String.valueOf(10),1, "7.34394");
    game2.addComplexity(3.6298);
    String[] names = new String[1];
    names[0] = "Martin";
    Play play = new Play("2016-09-14", names, 1);
    game2.addPlay(play);

    ArrayList<BoardGame> games = new ArrayList<>();
    games.add(game1);
    games.add(game2);

    // Add games to collection
    BoardGameCollection collection = new BoardGameCollection(games);
    return collection;
  }

  @Override
  public boolean verifyUser(String username) {
    return false;
  }
}
