package Main.Model.MachineLearning;

import Main.Controller.Factories.IStartupFactory;
import Main.Model.Logic.IGameNightValues;
import Main.Model.Storage.CollectionBuilder;
import Main.Model.Storage.ICollectionBuilder;

/**
 * Created by Peter on 13/02/2017.
 */
public class MachineLearningFactory implements IStartupFactory {
  @Override
  public ICollectionBuilder getCollectionBuilder() {
    return new CollectionBuilder(new ConnectionHandlerStub());
  }

  @Override
  public IGameNightValues getGameNightValues() {
    return new MachineLearningGameNightValues();
  }
}
