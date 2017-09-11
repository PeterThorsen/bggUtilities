package main.Controller.Factories;

import main.Model.Logic.ChosenGameNightValues;
import main.Model.Logic.IGameNightValues;
import main.Model.Network.ConnectionHandler;
import main.Model.Storage.CollectionBuilder;
import main.Model.Storage.ICollectionBuilder;

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
