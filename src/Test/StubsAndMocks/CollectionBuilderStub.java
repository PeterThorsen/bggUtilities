package Test.StubsAndMocks;

import Main.Containers.BoardGameCollection;
import Main.Network.ICollectionBuilder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Peter on 03/10/16.
 */
public class CollectionBuilderStub implements ICollectionBuilder {
  @Override
  public BoardGameCollection getCollection(String username) {
    BoardGameCollection collection = new BoardGameCollection(null);
    return collection;
  }
}
