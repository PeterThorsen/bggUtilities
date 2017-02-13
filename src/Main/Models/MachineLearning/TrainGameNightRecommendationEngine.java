package Main.Models.MachineLearning;

import Main.Factories.IStartupFactory;
import Main.Models.Storage.ICollectionBuilder;

public class TrainGameNightRecommendationEngine {
  private final ICollectionBuilder collectionBuilder;

  public static void main(String[] args) {
    new TrainGameNightRecommendationEngine();
  }

  public TrainGameNightRecommendationEngine() {
    IStartupFactory factory = new MachineLearningFactory();
    collectionBuilder = factory.getCollectionBuilder();
  }
}
