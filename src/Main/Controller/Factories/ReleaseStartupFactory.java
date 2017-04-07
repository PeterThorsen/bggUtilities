package Main.Controller.Factories;

import Main.Model.Logic.ChosenGameNightValues;
import Main.Model.Logic.IGameNightValues;
import Main.Model.Network.ConnectionHandler;
import Main.Model.Storage.CollectionBuilder;
import Main.Model.Storage.ICollectionBuilder;

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
