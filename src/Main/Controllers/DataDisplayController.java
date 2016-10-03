package Main.Controllers;

import Main.Network.ICollectionBuilder;
import Main.Network.IConnectionHandler;

/**
 * Created by Peter on 03/10/16.
 */
public class DataDisplayController {
  private final ICollectionBuilder collectionBuilder;

  public DataDisplayController(ICollectionBuilder collectionBuilder) {
    this.collectionBuilder = collectionBuilder;
  }
}
