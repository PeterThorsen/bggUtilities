package Main.Models.Storage;

import Main.Containers.BoardGameCollection;

/**
 * Created by Peter on 28/09/16.
 */
public interface ICollectionBuilder {
  BoardGameCollection getCollection(String username);
}
