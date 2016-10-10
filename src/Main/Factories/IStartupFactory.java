package Main.Factories;

import Main.Models.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public interface IStartupFactory {

  ICollectionBuilder getCollectionBuilder();

}
