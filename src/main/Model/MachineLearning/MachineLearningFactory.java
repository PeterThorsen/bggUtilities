package main.Model.MachineLearning;

import main.Controller.Factories.IStartupFactory;
import main.Model.Logic.IGameNightValues;
import main.Model.Storage.CollectionBuilder;
import main.Model.Storage.ICollectionBuilder;

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
