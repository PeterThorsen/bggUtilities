package Model.MachineLearning;

import Controller.Factories.IStartupFactory;
import Model.Logic.IGameNightValues;
import Model.Storage.CollectionBuilder;
import Model.Storage.ICollectionBuilder;

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
