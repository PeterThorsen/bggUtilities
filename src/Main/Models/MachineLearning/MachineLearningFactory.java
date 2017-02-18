package Main.Models.MachineLearning;

import Main.Factories.IStartupFactory;
import Main.Models.Logic.IGameNightValues;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;

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
