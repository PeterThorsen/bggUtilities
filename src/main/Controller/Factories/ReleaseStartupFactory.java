package Controller.Factories;

import Model.Logic.ChosenGameNightValues;
import Model.Logic.IGameNightValues;
import Model.Network.ConnectionHandler;
import Model.Storage.CollectionBuilder;
import Model.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public class ReleaseStartupFactory implements IStartupFactory {

  @Override
  public ICollectionBuilder getCollectionBuilder() {
    return new CollectionBuilder(new ConnectionHandler());
  }

  @Override
  public IGameNightValues getGameNightValues() {
    return new ChosenGameNightValues();
  }
}
