package Main.Factories;

import Main.Models.Logic.ChosenGameNightValues;
import Main.Models.Logic.IGameNightValues;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;

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
