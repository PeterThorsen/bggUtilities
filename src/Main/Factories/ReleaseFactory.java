package Main.Factories;

import Main.Models.Network.ConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public class ReleaseFactory implements IFactory {

  @Override
  public ICollectionBuilder getCollectionBuilder() {
    return new CollectionBuilder(new ConnectionHandler());
  }
}
